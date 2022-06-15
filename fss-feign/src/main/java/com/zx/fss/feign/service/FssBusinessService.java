package com.zx.fss.feign.service;

import com.zx.fss.api.RequestParameter;
import com.zx.fss.api.Result;
import com.zx.fss.business.Dir;
import com.zx.fss.feign.config.OpenFeignConfiguration;
import com.zx.fss.feign.service.impl.AccountServiceHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "fss-business", fallback = AccountServiceHystrix.class,configuration = OpenFeignConfiguration.class)
public interface FssBusinessService {
    @RequestMapping("/business/dir/add")
    Result add(@RequestBody RequestParameter<Dir> parameter);
}
