package com.zx.fss.exception;

import com.zx.fss.api.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局处理Oauth2抛出的异常
 * Created by macro on 2020/7/17.
 */
@ControllerAdvice
@Slf4j
public class Oauth2ExceptionHandler {
    @ResponseBody
    @ExceptionHandler(value = OAuth2Exception.class)
    public Result handleOauth2(OAuth2Exception e) {
        log.error("Oauth2ExceptionHandler.OAuth2Exception异常:{}",e);
        return Result.fail(e.getMessage());
    }
    @ResponseBody
    @ExceptionHandler(value = RuntimeException.class)
    public Result runtimeException(RuntimeException e) {
        log.error("Oauth2ExceptionHandlerRuntimeException异常:{}",e);
        return Result.fail(e.getMessage());
    }
}
