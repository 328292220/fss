/**
 * 文件名： OpenFeignConfiguration.java
 * 版权： Copyright 2020-2021 CRB All Rights Reserved.
 * 描述： OpenFeign配置类
 */
package com.zx.fss.feign.config;

import feign.Logger;
import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenFeign配置类
 *
 * @author guoenhong@crb.cn
 * @date 2021/05/24 12:00
 */
@Configuration
public class OpenFeignConfiguration {

    /**
     * 自定义拦截器
     * @return
     */
    @Bean
    public OpenFeignRequestInterceptor openFeignRequestInterceptor() {
        return new OpenFeignRequestInterceptor();
    }

    /**
     * 超时时间配置
     */
     @Bean
     public Request.Options options() {
     return new Request.Options(5000, 30000);
     }


    @Bean
    Logger.Level feignLoggerLevel() {
        //这里记录所有，根据实际情况选择合适的日志level
        return Logger.Level.FULL;
    }
}
