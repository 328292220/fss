package com.zx.fss.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zx.fss.api.Result;
import com.zx.fss.api.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 微服务之网关处理全局异常以及封装统一响应体
 * https://blog.csdn.net/BobZhangfighting/article/details/111210298?utm_medium=distribute.pc_aggpage_search_result.none-task-blog-2~aggregatepage~first_rank_ecpm_v1~rank_v31_ecpm-1-111210298.pc_agg_new_rank&utm_term=%E7%BD%91%E5%85%B3%E7%BB%9F%E4%B8%80%E5%A4%84%E7%90%86404&spm=1000.2123.3001.4430
 */
@Component
@Slf4j
public class JsonResponseWrapperFilter implements ComplexFilter {
    public static final String IS_IGNOREAUTHFILTER = "ingore";

    @Override
    public int getOrder() {
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //操作针对某些路由跳过全局过滤器
        if (exchange.getAttributes().get(IS_IGNOREAUTHFILTER) != null) {
            return chain.filter(exchange);
        }
        //包装响应体
        ServerWebExchange newExchange = exchange.mutate().response(
                ServerHttpResponseDecoratorHelper.build(exchange, (originalBody) -> {
                    String requestUri = exchange.getRequest().getPath().pathWithinApplication().value();
                    MediaType responseMediaType = exchange.getResponse().getHeaders().getContentType();
                    log.info("Request [{}] response content-type is {}", requestUri, responseMediaType);
                    if (MediaType.APPLICATION_JSON.isCompatibleWith(responseMediaType)) {
                        return rewriteBody(exchange, originalBody);
                    } else {
                        return Mono.just(originalBody);
                    }
                })).build();
        return chain.filter(newExchange);
    }

    private Mono<byte[]> rewriteBody(ServerWebExchange exchange, byte[] originalBody) {
        HttpStatus originalResponseStatus = exchange.getResponse().getStatusCode();
        //将状态码统一重置为200，在这里重置才是终极解决办法
        log.debug("Response status code is {} , body is {}", originalResponseStatus, new String(originalBody));
        if (originalResponseStatus == HttpStatus.OK) {
            exchange.getResponse().setStatusCode(HttpStatus.OK);
            if (originalBody == null) {
                log.debug("下游服务响应内容为空，但是http状态码为200，则按照成功的响应体包装返回");
                return makeMono(Result.success());
            } else {
                try {
                    //只能parse出JSONObject、JSONArray、Integer、Boolean等类型，当是一个string但是非json格式则抛出异常
                    Object jsonObject = JSON.parse(originalBody);
                    //如果响应内容已经包含了errcode字段，则表示下游的响应体本身已经是统一结果体了，无需再包装
                    if ((jsonObject instanceof JSONObject) && ((JSONObject) jsonObject).containsKey("errcode")) {
                        log.debug("服务响应体已经是统一结果体，无需包装");
                        return Mono.just(originalBody);
                    } else {
                        //网关不用再包一层
                        //return makeMono(Result.success(jsonObject));
                        return Mono.just(originalBody);
                    }
                } catch (Exception e) {
                    log.error("解析下游响应体异常", e);
                    return makeMono(Result.success(originalBody));
                }
            }
        } else {
            //如果不是401和403异常则重置为200状态码
            if (!ArrayUtils.contains(new int[]{401, 403}, originalResponseStatus.value())) {
                exchange.getResponse().setStatusCode(HttpStatus.OK);
            }

            //响应异常的报文
            if (originalBody == null) {
                return Mono.just(JSON.toJSONBytes(Result.fail(ResultCode.FAILURE),
                        SerializerFeature.WriteMapNullValue));
            } else {
                try {
                    //只能parse出JSONObject、JSONArray、Integer、Boolean等类型，当是一个string但是非json格式则抛出异常
                    Object jsonObject = JSON.parse(originalBody);
                    //如果响应内容已经包含了errcode字段，则表示下游的响应体本身已经是统一结果体了
                    if ((jsonObject instanceof JSONObject)) {
                        JSONObject jo = ((JSONObject) jsonObject);
                        if (jo.containsKey("errcode")) {
                            return Mono.just(originalBody);
                        } else if (jo.containsKey("status") && jo.containsKey("errorCode")) {
                            int errorCode = jo.getIntValue("errorCode");
                            String message = jo.getString("message");
                            return Mono.just(JSON.toJSONBytes(Result.fail(errorCode, message)
                                    , SerializerFeature.WriteMapNullValue));
                        } else if ("404".equals(jo.getString("status"))) {
                            //下游返回了404
                            return Mono.just(JSON.toJSONBytes(Result.fail(ResultCode.NOT_FOUND),
                                    SerializerFeature.WriteMapNullValue));
                        } else if ("405".equals(jo.getString("status"))) {
                            //下游返回了405
                            return Mono.just(JSON.toJSONBytes(Result.fail(ResultCode.NOT_ALLOW),
                                    SerializerFeature.WriteMapNullValue));
                        } else if ("415".equals(jo.getString("status"))) {
                            //下游返回了415
                            return Mono.just(JSON.toJSONBytes(Result.fail(ResultCode.ERROR_CONTENT_TYPE)));
                        } else {
                            //下游返回的包体是一个jsonobject，并不是规范的错误包体
                            return Mono.just(JSON.toJSONBytes(Result.fail(ResultCode.ERROR_RESPONSE_BODY),
                                    SerializerFeature.WriteMapNullValue));
                        }
                    } else {
                        //不是一个jsonobject，可能是一个jsonarray
                        return Mono.just(JSON.toJSONBytes(Result.fail(ResultCode.ERROR_RESPONSE_BODY,originalBody),
                                SerializerFeature.WriteMapNullValue));
                    }
                } catch (Exception e) {
                    log.error("解析下游响应体异常", e);
                    return Mono.just(JSON.toJSONBytes(Result.fail(ResultCode.ERROR_RESPONSE_BODY,originalBody
                    ), SerializerFeature.WriteMapNullValue));
                }
            }
        }
    }

    private Mono<byte[]> makeMono(Result<?> result) {
        return Mono.just(JSON.toJSONBytes(result, SerializerFeature.WriteMapNullValue));
    }


}