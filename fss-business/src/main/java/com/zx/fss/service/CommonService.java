package com.zx.fss.service;

import com.zx.fss.business.dto.CommonDTO;

import java.util.Map;

public interface CommonService {
    Map<String, Object> query(CommonDTO commonDTO);
}
