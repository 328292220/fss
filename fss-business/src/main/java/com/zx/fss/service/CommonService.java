package com.zx.fss.service;

import com.zx.fss.api.Result;
import com.zx.fss.business.dto.CommonDTO;
import com.zx.fss.business.dto.DeleteDTO;

import java.util.Map;

public interface CommonService {
    Map<String, Object> query(CommonDTO commonDTO);

    Result download(Map<String, Object> data);

    void del(DeleteDTO data);
}
