package com.zx.fss.controller;

import com.google.common.collect.Maps;
import com.zx.fss.api.RequestParameter;
import com.zx.fss.api.Result;
import com.zx.fss.business.Dir;
import com.zx.fss.business.dto.CommonDTO;
import com.zx.fss.exception.ResultException;
import com.zx.fss.service.CommonService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/common")
@AllArgsConstructor
public class CommonController {
    CommonService commonService;

    /**
     *
     * @param parameter
     * @return
     */
    @RequestMapping("/query")
    public Result query(@RequestBody RequestParameter<CommonDTO> parameter){
        Map map = commonService.query(parameter.getData());
        return Result.success(map);
    }

    @RequestMapping("/download/{type}/{id}")
    @ResponseBody
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
