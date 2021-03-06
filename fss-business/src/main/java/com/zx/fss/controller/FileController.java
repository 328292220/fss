package com.zx.fss.controller;


import com.zx.fss.account.User;
import com.zx.fss.api.RequestParameter;
import com.zx.fss.api.Result;
import com.zx.fss.constant.SpecialSymbolsUtil;
import com.zx.fss.exception.ResultException;
import com.zx.fss.service.FileService;
import com.zx.fss.utils.LoginUserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * <p>
 * 文件信息 前端控制器
 * </p>
 *
 * @author guoenhong@crb.cn
 * @since 2022-03-01
 */
@Slf4j
@RestController
@RequestMapping("/file")
@AllArgsConstructor
@Api(value = "文件", tags = "文件相关接口")
public class FileController {

    FileService fileService;

    @PostMapping("/upload")
    @ResponseBody
    @ApiOperation(value = "上传文件",httpMethod = "POST")
    public Result upload(@RequestParam("files")MultipartFile[] files,
                         @RequestParam(required = false) Long  parentId) throws ResultException {
        if (files.length == 0) {
            return Result.fail("上传失败，请选择文件");
        }
        fileService.upload(parentId,files);
        return Result.success("上传成功");
    }



}
