/**
 * 文件名： GatewayException.java
 * 版权： Copyright 2020-2021 CRB All Rights Reserved.
 * 描述： 认证异常类
 */
package com.zx.fss.feign.exception;

import lombok.Data;

/**
 * 认证异常类
 *
 * @author guoenhong@crb.cn
 * @date 2021/05/08 12:00
 */
@Data
public class ClientException extends RuntimeException {

    private String code;

    private String message;

    private Object[] parameters;

    public ClientException(String code, String message, Object[] parameters) {
        super(message);
        this.code = code;
        this.message = message;
        this.parameters = parameters;
    }


}
