package com.zx.fss.controller;

import com.google.common.collect.Maps;
import com.zx.fss.api.RequestParameter;
import com.zx.fss.api.Result;
import com.zx.fss.business.Dir;
import com.zx.fss.business.dto.CommonDTO;
import com.zx.fss.business.dto.DeleteDTO;
import com.zx.fss.exception.ResultException;
import com.zx.fss.service.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/common")
@AllArgsConstructor
@Api(value = "公共", tags = "公共接口")
public class CommonController {
    CommonService commonService;

    /**
     *
     * @param parameter
     * @return
     */
    @RequestMapping(value = "/query",method = RequestMethod.POST)
    @ApiOperation(value = "获取文件和文件夹",httpMethod = "POST")
    public Result query(@RequestBody RequestParameter<CommonDTO> parameter){
        Map map = commonService.query(parameter.getData());
        return Result.success(map);
    }

    @RequestMapping("/del")
    @ApiOperation(value = "删除目录或文件",httpMethod = "POST")
    public Result del(@RequestBody RequestParameter<DeleteDTO> parameter){
        DeleteDTO data = parameter.getData();
        if(data == null || (CollectionUtils.isEmpty(data.getDirIds()) && CollectionUtils.isEmpty(data.getFileIds()))){
            return Result.fail("请选择需要删除的目录或文件");
        }
        commonService.del(parameter.getData());
        return Result.success();
    }

    @RequestMapping("/download/{type}/{id}")
    @ResponseBody
    @ApiOperation(value = "下载文件",httpMethod = "GET")
    public Result download(@PathVariable String type,@PathVariable Integer id) throws ResultException {
        if(id == null || StringUtils.isBlank(type)){
            return Result.fail("id和type必传");
        }
        Map<String, Object> data = Maps.newHashMap();
        data.put("type",type);
        data.put("id",id);
        Result download = commonService.download(data);
        return download;
    }
}
