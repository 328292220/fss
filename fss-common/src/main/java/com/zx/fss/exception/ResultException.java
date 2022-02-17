package com.zx.fss.exception;

import com.zx.fss.api.ResultCode;
import lombok.Getter;

@Getter
public class ResultException extends Exception {

    /**
     * 业务异常信息
     */
    ResultCode resultCode;

    public ResultException() {
        this(ResultCode.ERROR);
    }

    public ResultException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.resultCode = resultCode;
    }
}

