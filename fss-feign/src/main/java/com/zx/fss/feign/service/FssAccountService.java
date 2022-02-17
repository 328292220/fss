/**
 * 文件名： BaseService.java
 * 版权： Copyright 2020-2021 CRB All Rights Reserved.
 * 描述： 基础服务Feign
 */
package com.zx.fss.feign.service;

import com.zx.fss.api.Result;
import com.zx.fss.feign.config.OpenFeignConfiguration;
import com.zx.fss.feign.service.impl.AccountServiceHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 基础服务Feign
 *
 * @author guoenhong@crb.cn
 * @date 2021/05/10 12:00
 */
@FeignClient(value = "fss-account", fallback = AccountServiceHystrix.class,configuration = OpenFeignConfiguration.class)
public interface FssAccountService {

    @RequestMapping("account/user/getUserByUserName")
    Result getUserByUserName(@RequestParam("userName") String userName);

    @RequestMapping("account/resource/getResourceRolesMap")
    Result getResourceRolesMap();

}
