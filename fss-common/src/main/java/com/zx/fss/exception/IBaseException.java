/**
 * 文件名： IBaseException.java
 * 版权： Copyright 2020-2021 CRB All Rights Reserved.
 * 描述： 基础异常类
 */
package com.zx.fss.exception;

/**
 * 基础异常类
 *
 * @author guoenhong@crb.cn
 * @date 2021/05/10 12:00
 */
public interface IBaseException {

    String getCode();

    String getMessage();

    Object[] getParameters();

    void setCode(String code);

    void setMessage(String message);

    void setParameters(Object[] parameters);

}
