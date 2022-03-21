package com.zx.fss.controller;


import com.zx.fss.model.FileConvertInfo;
import com.zx.fss.model.Result;
import com.zx.fss.util.ConvertOp;
import com.zx.fss.util.FileAnalysisUtil;
import com.zx.fss.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/service")
public class ServiceController{
    @Autowired
    private FileAnalysisUtil fileAnalysisUtil;

    @PostMapping("/checkFile")
    @ResponseBody
    public Object checkFile(@RequestBody Map<String, Object> params) {
        String file = ConvertOp.convert2String(params.get("file"));
        byte[] decode = Base64.getDecoder().decode(file);
        file = new String(decode);
        Result result = Result.okResult();
        try {
            if (StringUtil.isEmpty(file)) {
                return Result.errorResult().setMsg("未指定预览文件地址");
            }
            String fileGuid = fileAnalysisUtil.readFile(file);
            if (StringUtil.isEmpty(fileGuid)) {
                return Result.errorResult().setMsg("不支持文件的预览类型");
            }
            result.add("fileGuid", fileGuid);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.errorResult().setMsg("文件地址错误");
        }
        return result;
    }


    @PostMapping("/getFileContent")
    @ResponseBody
    public Object getFileContent(@RequestBody Map<String, Object> params) {
        Result result = Result.okResult();
        String fileGuid = ConvertOp.convert2String(params.get("fileGuid"));
        String fileContent = fileAnalysisUtil.getFileContent(fileGuid);
        result.add("fileContent",fileContent);
        return result;
    }

    @PostMapping("/convertFile")
    @ResponseBody
    public Object convertFile(@RequestBody Map<String, Object> params) {
        Result result = Result.okResult();
        String fileGuid = ConvertOp.convert2String(params.get("fileGuid"));
        try {
            FileConvertInfo fileConvertInfo = fileAnalysisUtil.convertFile(fileGuid);
            if (!StringUtil.isEmpty(fileConvertInfo.getErrorInfo())) {
                result = Result.errorResult().setMsg("文件转换失败");
            }
            result.add("obj", fileConvertInfo);
        } catch (Exception e) {
            result = Result.errorResult().setMsg("文件转换失败");
            e.printStackTrace();
        }
        return result;
    }

    @PostMapping("/checkConvertImageFile")
    @ResponseBody
    public Object checkConvertFile(@RequestBody Map<String, Object> params) {
        Result result = Result.okResult();
        String fileGuid = ConvertOp.convert2String(params.get("fileGuid"));
        int pageNum = ConvertOp.convert2Int(params.get("pageNum"));
        int maxFileIndex = 0;
        for (int i=1;i<=pageNum;i++){
            String filePath = "tempfiles/analysis/" + fileGuid + "/" + "split_" + i + ".jpeg";
            File file  = new File(filePath);
            if(file.exists()){
                maxFileIndex = i;
            }else{
                break;
            }
        }
        result.add("maxFileIndex",maxFileIndex);
        return result;
    }

    @PostMapping("/checkConvertExcelHtmlFile")
    @ResponseBody
    public Object checkConvertExcelHtmlFile(@RequestBody Map<String, Object> params) {
        Result result = Result.okResult();
        String fileGuid = ConvertOp.convert2String(params.get("fileGuid"));
        String filePath = "tempfiles/analysis/" + fileGuid + "/" + "convert.html";
        boolean checkConvert = false;
        File file  = new File(filePath);
        if(file.exists()){
            checkConvert = true;
        }
        result.add("checkConvert",checkConvert);
        return result;
    }
}
