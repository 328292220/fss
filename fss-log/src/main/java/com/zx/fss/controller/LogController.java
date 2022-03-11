package com.zx.fss.controller;

import com.zx.fss.api.Result;
import com.zx.fss.properties.LogServiceProperties;
import com.zx.fss.properties.LogNacosProperties;
import com.zx.fss.utils.YamlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping
public class LogController {
    @Autowired
    LogServiceProperties logServiceProperties;

    @Autowired
    LogNacosProperties logNacosProperties;

    @RequestMapping("/")
    public String tolog(){
        return "redirect:log";
    }

    @RequestMapping("log")
    public ModelAndView log(){
        ModelAndView modelAndView = new ModelAndView("log");
        //系统信息
        modelAndView.addObject("serverList", logServiceProperties.getServerList());
        modelAndView.addObject("levelList", logServiceProperties.getLevelList());
        return modelAndView;
    }


    @RequestMapping("/updateNacos")
    @ResponseBody
    public Result updateNacos(@RequestParam(defaultValue = "500") int defaultLastLineSizem,
                              @RequestParam(defaultValue = "1000") int maxLogLineSize){
        Map<String,Object> map = new HashMap<>();
        map.put("log-service.defaultLastLineSize",defaultLastLineSizem);
        map.put("log-service.maxLogLineSize",maxLogLineSize);
        return updateNacos(map);
    }

    private static Result updateNacos( Map<String,Object> map){
        String dataId = "fss-log.yml";
        String groupId = "dev";
        boolean update = YamlUtil.builder()
                .logNacosProperties(null)
                .dataId(dataId)
                .groupId(groupId)
                .build()
                .update(map);
        if(update){
            return Result.success();
        }
        return Result.fail("失败");
    }

//    public static void main(String[] args) {
//        Map<String,Object> map = new HashMap<>();
//        map.put("log-service.defaultLastLineSize",500);
//        map.put("log-service.maxLogLineSize",1000);
//        updateNacos(map);
//    }

}
