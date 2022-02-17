package com.zx.fss.authorization;

import cn.hutool.core.convert.Convert;
import com.zx.fss.config.IgnoreUrlsConfig;
import com.zx.fss.constant.AuthConstant;
import com.zx.fss.constant.RedisConstant;
import com.zx.fss.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 鉴权管理器，用于判断是否有资源的访问权限
 * Created by macro on 2020/6/19.
 * 在WebFluxSecurity中自定义鉴权操作需要实现ReactiveAuthorizationManager接口
 */
@Component
@Slf4j
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        //访问路径
        String path = request.getURI().getPath();
        PathMatcher pathMatcher = new AntPathMatcher();
        // 对应跨域的预检请求直接放行
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return Mono.just(new AuthorizationDecision(true));
        }

        //白名单路径直接放行
        List<String> ignoreUrls = ignoreUrlsConfig.getUrls();
        for (String ignoreUrl : ignoreUrls) {
            if (pathMatcher.match(ignoreUrl, path)) {
                return Mono.just(new AuthorizationDecision(true));
            }
        }

        // token为空拒绝访问
        String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(token)) {
            return Mono.just(new AuthorizationDecision(false));
        }

        // 指定服务请求直接放行,可以加多个，根据需要添加
        if (pathMatcher.match("/common/**", path) || pathMatcher.match("/common/**", path)) {
            return Mono.just(new AuthorizationDecision(true));
        }

        // 请求路径匹配到的资源需要的角色权限集合authorities
        //从Redis中获取当前路径可访问角色列表
        URI uri = authorizationContext.getExchange().getRequest().getURI();
//        Object obj = redisTemplate.opsForHash().get(RedisConstant.RESOURCE_ROLES_MAP, uri.getPath());
        Object obj = redisUtil.hget(RedisConstant.RESOURCE_ROLES_MAP, uri.getPath());
        List<String> roles = Convert.toList(String.class,obj);
        List<String> authorities = roles.stream().map(i -> i = AuthConstant.AUTHORITY_PREFIX + i).collect(Collectors.toList());
        //认证通过且角色匹配的用户可访问当前路径
        Mono<Authentication> filter = mono.filter(one->{
            return one.isAuthenticated();
        });
        Flux<? extends GrantedAuthority> flux = filter.flatMapIterable(one->{
            return one.getAuthorities();
        });
        Flux<String> map = flux.map(one->{
            return one.getAuthority();
        });
        Mono<Boolean> role_admin = map.any(roleId -> {
            log.info("roleId=====:" + roleId);
            log.info("roleId=====:" + roleId);
            log.info("roleId=====:" + roleId);
            if (roleId.equals("ROLE_ADMIN")) {
                return true;
            }
            return authorities.contains(roleId);
        });
        Mono<AuthorizationDecision> map1 = role_admin.map(AuthorizationDecision::new);
        Mono<AuthorizationDecision> authorizationDecisionMono = map1.defaultIfEmpty(new AuthorizationDecision(false));
        return authorizationDecisionMono;

//        return mono
//                .filter(Authentication::isAuthenticated)
//                .flatMapIterable(Authentication::getAuthorities)
//                .map(GrantedAuthority::getAuthority)
////                .any(authorities::contains)
//                .any(roleId -> {
//                    log.info("roleId=====:"+roleId);
//                    log.info("roleId=====:"+roleId);
//                    log.info("roleId=====:"+roleId);
//                    if (roleId.equals("ROLE_ADMIN")) {
//                        return true;
//                    }
//                    return authorities.contains(roleId);
//                })
//                .map(AuthorizationDecision::new)
//                .defaultIfEmpty(new AuthorizationDecision(false));
    }

}
