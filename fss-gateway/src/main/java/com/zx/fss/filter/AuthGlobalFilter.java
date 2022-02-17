package com.zx.fss.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.nimbusds.jose.JWSObject;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Map;

/**
 * 将登录用户的JWT转化成用户信息的全局过滤器
 * Created by macro on 2020/6/17.
 * 这里我们还需要实现一个全局过滤器AuthGlobalFilter，当
 * 鉴权通过后将JWT令牌中的用户信息解析出来，然后存入请求的Header中，
 * 这样后续服务就不需要解析JWT令牌了，可以直接从请求的Header中获取到用户信息
 */
@Component
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private static Logger LOGGER = LoggerFactory.getLogger(AuthGlobalFilter.class);
    private static String CACHED_REQUEST_BODY_OBJECT_KEY = "不知道";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (StrUtil.isEmpty(token)) {
            return chain.filter(exchange);
        }
        try {
            //从token中解析用户信息并设置到Header中去
            String realToken = token.replace("Bearer ", "");
            JWSObject jwsObject = JWSObject.parse(realToken);
            String userStr = jwsObject.getPayload().toString();
            LOGGER.info("AuthGlobalFilter.filter() user:{}",userStr);
            ServerHttpRequest request = exchange.getRequest().mutate().header("user", userStr).build();
            exchange = exchange.mutate().request(request).build();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //log(exchange);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private void log(ServerWebExchange exchange){
        long start = System.currentTimeMillis();

        StringBuilder reqMsg = new StringBuilder();
        StringBuilder resMsg = new StringBuilder();
        // 获取请求信息
        ServerHttpRequest request = exchange.getRequest();
        InetSocketAddress address = request.getRemoteAddress();
        String method = request.getMethodValue();
        URI uri = request.getURI();
        HttpHeaders headers = request.getHeaders();
        // 获取请求body
        Object cachedRequestBodyObject = exchange.getAttributeOrDefault(CACHED_REQUEST_BODY_OBJECT_KEY, null);
        byte[] body = (byte[]) cachedRequestBodyObject;
        String params = new String(body);
        // 获取请求query
        Map queryMap = request.getQueryParams();
        String query = JSON.toJSONString(queryMap);

        // 拼接请求日志
//        reqMsg.append(REQUEST_PREFIX);
        reqMsg.append("\n header=").append(headers);
        reqMsg.append("\n query=").append(query);
        reqMsg.append("\n params=").append(params);
        reqMsg.append("\n address=").append(address.getHostName()).append(address.getPort());
        reqMsg.append("\n method=").append(method);
        reqMsg.append("\n url=").append(uri.getPath());
//        reqMsg.append(REQUEST_TAIL);
        log.info(reqMsg.toString()); // 打印入参日志

        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory bufferFactory = response.bufferFactory();
//        resMsg.append(RESPONSE_PREFIX);
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(fluxBody.map(dataBuffer -> {
                        byte[] content = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(content);
                        String responseResult = new String(content, Charset.forName("UTF-8"));
                        resMsg.append("\n status=").append(this.getStatusCode());
                        resMsg.append("\n header=").append(this.getHeaders());
                        resMsg.append("\n responseResult=").append(responseResult);
//                        resMsg.append(RESPONSE_TAIL);

                        // 计算请求时间
                        long end = System.currentTimeMillis();
                        long time = end - start;
                        resMsg.append("耗时ms:").append(time);
                        log.info(resMsg.toString()); // 打印结果日志
                        return bufferFactory.wrap(content);
                    }));
                }
                return super.writeWith(body);
            }
        };

    }
}
