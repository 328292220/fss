package com.zx.fss.controller;

import com.zx.fss.api.RequestParameter;
import com.zx.fss.api.Result;
import com.zx.fss.business.Dir;
import com.zx.fss.business.dto.CommonDTO;
import com.zx.fss.service.CommonService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
