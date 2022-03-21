package com.zx.fss.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * swagger的配置类
 *
 * @author lc
 */
//@Component
//@Primary
public class GatewaySwaggerResourcesProvider implements SwaggerResourcesProvider {

    /**
     * swagger3默认的url后缀 v3时默认不会拼接路由前缀 请求子服务时会缺少buseUrl
     */
    public static final String SWAGGER2URL = "/v2/api-docs";
    /**
     * 网关路由
     */
    private final RouteLocator routeLocator;

    /**
     * 网关应用名称
     */
    @Value("${spring.application.name}")
    private String self;

    @Autowired
    public GatewaySwaggerResourcesProvider(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    /**
     * 对于gateway来说这块比较重要 让swagger能找到对应的服务
     *
     * @return
     */
    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routeHosts = new ArrayList<>();
        // 获取所有可用的host：serviceId
        routeLocator.getRoutes().filter(route -> route.getUri().getHost() != null)
                .filter(route -> !self.equals(route.getUri().getHost()))
                // 解决不会拼接路由里面的 pattern 打开swagger时无显示问题
                .subscribe(route -> {
                    System.out.println(route.getId());
                    System.out.println(route.getUri());
                    //网上的代码
                    //routeHosts.add(getData(route.getPredicate().toString())+"/"+route.getUri().getHost());
                    //自己的
                    if(route.getUri().getHost().contains(route.getId())){
                        routeHosts.add(route.getId());
                    }
                });

        // 记录已经添加过的server
        Set<String> dealed = new HashSet<>();
        routeHosts.forEach(instance -> {
            // 拼接url
            String url = "/" + instance.toLowerCase() + SWAGGER2URL;
            if (!dealed.contains(url)) {
                dealed.add(url);
                SwaggerResource swaggerResource = new SwaggerResource();
                swaggerResource.setUrl(url);
                swaggerResource.setName(instance);
                swaggerResource.setSwaggerVersion("3.0.n");
                resources.add(swaggerResource);
            }
        });
        return resources;
    }

    /**
     * 获取配置的路由name
     * @param data
     * @return
     */
    public static String getData(String data) {
        List<String> list = new ArrayList<>();
        Pattern p = Pattern.compile("(\\[[^\\]]*\\])");
        Matcher m = p.matcher(data);
        while (m.find()) {
            list.add(m.group().substring(1, m.group().length() - 1));
        }
        if (!CollectionUtils.isEmpty(list)) {
            String s = list.get(0);
            return s.substring(s.indexOf("/") + 1, s.lastIndexOf("/"));
        }
        return null;
    }
}
