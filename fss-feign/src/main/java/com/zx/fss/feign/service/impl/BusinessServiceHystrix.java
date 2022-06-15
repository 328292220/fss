/**
 * 文件名： BaseService.java
 * 版权： Copyright 2020-2021 CRB All Rights Reserved.
 * 描述： 基础服务Feign熔断类
 */
package com.zx.fss.feign.service.impl;

import com.zx.fss.api.RequestParameter;
import com.zx.fss.api.Result;
import com.zx.fss.api.ResultCode;
import com.zx.fss.business.Dir;
import com.zx.fss.feign.service.FssBusinessService;
import org.springframework.stereotype.Component;

/**
 * 基础服务Feign熔断类
 *
 * @author guoenhong@crb.cn
 * @date 2021/05/10 12:00
 */
@Component
public class BusinessServiceHystrix implements FssBusinessService {


    @Override
    public Result add(RequestParameter<Dir> parameter) {
        return Result.fail(ResultCode.SERVICE_UNAVAILABLE);
    }
}
