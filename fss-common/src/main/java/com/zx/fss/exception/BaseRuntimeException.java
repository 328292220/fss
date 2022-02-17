/**
 * 文件名： BaseRuntimeException.java
 * 版权： Copyright 2020-2021 CRB All Rights Reserved.
 * 描述： 基础运行时异常类
 */
package com.zx.fss.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * 基础运行时异常类
 *
 * @author guoenhong@crb.cn
 * @date 2021/05/10 12:00
 */
@Slf4j
public abstract class BaseRuntimeException extends RuntimeException{

    private int code;

    private String message;

    private Object[] parameters;

    protected BaseRuntimeException(int code, String message, Object[] parameters) {
        super(message);
        this.code = code;
        this.message = message;
        this.parameters = parameters;
    }
}
