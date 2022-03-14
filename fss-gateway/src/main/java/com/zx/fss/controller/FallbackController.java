package com.zx.fss.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 网关添加降级处理类
 */
@RestController
public class FallbackController {

    @RequestMapping("/defaultFallback")
    public Map<String,Object> defaultFallback(){
        System.out.println("降级操作...");
        Map<String,Object> map = new HashMap<>();
        map.put("code",200);
        map.put("msg","服务超时降级");
        map.put("data",null);
        return map;
    }
}
