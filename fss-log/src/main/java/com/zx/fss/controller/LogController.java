package com.zx.fss.controller;

import com.zx.fss.api.Result;
import com.zx.fss.constant.SpecialSymbolsUtil;
import com.zx.fss.properties.LogServiceProperties;
import com.zx.fss.properties.LogNacosProperties;
import com.zx.fss.utils.YamlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.websocket.server.PathParam;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
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

    @RequestMapping("delete/{applicationName}/{logLevel}")
    @ResponseBody
    public boolean delete(@PathVariable("logLevel") String  logLevel,
                       @PathVariable("applicationName") String  applicationName){
        FileWriter fileWriter = null;
        try{
            //日志路径
            String filePath = logServiceProperties.getRootPath()
                    + SpecialSymbolsUtil.separator
                    + applicationName
                    + SpecialSymbolsUtil.separator
                    +"log_"+logLevel.toLowerCase()+".log";
            File file = new File(filePath);
            if (file.exists()){
                fileWriter = new FileWriter(file);
                fileWriter.write("");
                fileWriter.flush();

                log.info("日志删除成功！");
            }
            return true;
        }catch (Exception e){
            log.error("日志删除失败！",e.getMessage());
        }finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;

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

    private Result updateNacos( Map<String,Object> map){
        String dataId = "fss-log.yml";
        boolean update = YamlUtil.builder()
                .logNacosProperties(logNacosProperties)
                .dataId(dataId)
                .groupId(logServiceProperties.getGroupId())
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
