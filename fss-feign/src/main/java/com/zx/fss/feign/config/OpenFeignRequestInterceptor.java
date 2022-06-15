/**
 * 文件名： OpenFeignRequestInterceptor.java
 * 版权： Copyright 2020-2021 CRB All Rights Reserved.
 * 描述： OpenFeign请求拦截器
 */
package com.zx.fss.feign.config;

import com.zx.fss.account.User;
import feign.RequestInterceptor;
import feign.RequestTemplate;


/**
 * OpenFeign请求拦截器
 *
 * @author guoenhong@crb.cn
 * @date 2021/05/24 12:00
 */
public class OpenFeignRequestInterceptor implements RequestInterceptor {
    /**
     * 请求头：REQUEST_FROM
     */
    public static final String REQUEST_HEADER_REQUEST_FROM = "REQUEST_FROM";

    /**
     * 请求头值：OPEN_FEIGN
     */
    public static final String HEADER_VALUE_OPEN_FEIGN = "OPEN_FEIGN";

    public OpenFeignRequestInterceptor() {

    }

    @Override
    public void apply(RequestTemplate template) {
        template.header(REQUEST_HEADER_REQUEST_FROM, HEADER_VALUE_OPEN_FEIGN);
    }
}