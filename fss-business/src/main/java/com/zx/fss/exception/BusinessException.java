package com.zx.fss.exception;

public class BusinessException extends BaseRuntimeException{

    public BusinessException(int code, String message, Object[] parameters) {
        super(code, message, parameters);
    }
}
