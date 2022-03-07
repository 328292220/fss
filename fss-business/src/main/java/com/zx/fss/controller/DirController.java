package com.zx.fss.controller;


import com.zx.fss.account.User;
import com.zx.fss.api.RequestParameter;
import com.zx.fss.api.Result;
import com.zx.fss.business.Dir;
import com.zx.fss.service.DirService;
import com.zx.fss.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 文件夹 前端控制器
 * </p>
 *
 * @author guoenhong@crb.cn
 * @since 2022-03-01
 */
@Slf4j
@RestController
@RequestMapping("/dir")
@AllArgsConstructor
public class DirController {

    DirService dirService;

    @RequestMapping("/add")
    public Result add(@RequestBody RequestParameter<Dir> parameter){
        if(parameter.getData().getName() == null){
            return Result.fail("目录名称[name]不能为空");
        }
        dirService.add(parameter.getData());
        return Result.success();
    }
    @RequestMapping("/del")
    public Result del(@RequestBody RequestParameter<Dir> parameter){
        if(parameter.getData().getDirId() == null){
            return Result.fail("目录ID[dirId]不能为空");
        }
        dirService.del(parameter.getData().getDirId());
        return Result.success();
    }

    /**
     * 目前只支持修改名称
     * @param parameter
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody RequestParameter<Dir> parameter){
        if(parameter.getData().getDirId() == null){
            return Result.fail("目录ID[dirId]不能为空");
        }
        if(parameter.getData().getName() == null){
            return Result.fail("名称[name]不能为空");
        }
        return dirService.updateDir(parameter.getData());
    }


}
