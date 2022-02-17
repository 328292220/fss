/**
 * 文件名： GatewayException.java
 * 版权： Copyright 2020-2021 CRB All Rights Reserved.
 * 描述： 认证异常类
 */
package com.zx.fss.exception;

/**
 * 认证异常类
 *
 * @author guoenhong@crb.cn
 * @date 2021/05/08 12:00
 */

public class AuthException extends BaseRuntimeException {

    public static final String ERROR_CODE = "RPTS_AUTH";

    /**
     * 当前服务不可用，请稍后再试!
     */
    public static final String MSG_FALLBACK_ERROR = "rpts.auth.fallback_error";

    /**
     * 鉴权失败,token为空！
     */
    public static final String MSG_TOKEN_NULL = "rpts.auth.token_null";

    /**
     * 鉴权失败,token失效或已过期！
     */
    public static final String MSG_TOKEN_EXPIRED = "rpts.auth.token_expired";

    /**
     * 无法获取用户信息，请检查用户名是否正确！
     */
    public static final String MSG_USER_NOT_FOUND = "rpts.auth.user_not_found";

    /**
     * 用户密码错误，请重新输入再试！
     */
    public static final String MSG_PASSWORD_ERROR = "rpts.auth.password_error";

    /**
     * 用户服务错误，接口调用失败！
     */
    public static final String MSG_USER_SERVICE_ERROR = "用户服务错误，接口调用失败";

    /**
     * 角色服务错误，接口调用失败！
     */
    public static final String MSG_ROLE_SERVICE_ERROR = "rpts.auth.role_service_error";

    /**
     * RSA加解密错误，接口调用失败！
     */
    public static final String MSG_RSA_ERROR = "RSA加解密错误，接口调用失败！";

    public AuthException(int code, String message, Object[] parameters) {
        super(code, message, parameters);
    }

    public AuthException(String message, Object[] parameters) {
        super(500, message, parameters);
    }

    public AuthException(int code, String message) {
        super(code, message, null);
    }

    public AuthException(String message) {
        super(500, message, null);
    }
}
