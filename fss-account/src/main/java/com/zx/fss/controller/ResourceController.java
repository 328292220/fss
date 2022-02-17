package com.zx.fss.controller;


import com.zx.fss.api.Result;
import com.zx.fss.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 资源表 前端控制器
 * </p>
 *
 * @author guoenhong@crb.cn
 * @since 2022-02-15
 */
@RestController
@RequestMapping("/resource")
public class ResourceController {

    @Autowired
    ResourceService resourceService;

    @RequestMapping("/getResourceRolesMap")
    Result getResourceRolesMap(){
        Map<String,Object> map = resourceService.getResourceRolesMap();
        return Result.data(map);
    }

}
