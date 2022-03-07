package com.zx.fss.exception;

import com.zx.fss.api.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局处理Oauth2抛出的异常
 * Created by macro on 2020/7/17.
 */
//@ControllerAdvice
//@Slf4j
//public class CommonExceptionHandler {
//
////    @ResponseBody
////    @ExceptionHandler(value = NullPointerException.class)
////    public Result NullPointerException(NullPointerException e) {
////        log.error("CommonExceptionHandler异常:{}",e);
////        return Result.fail(e.getMessage());
////    }
//    @ResponseBody
//    @ExceptionHandler(value = RuntimeException.class)
//    public Result runtimeException(RuntimeException e) {
//        log.error("CommonExceptionHandler异常:{}",e);
//        return Result.fail(e.getMessage());
//    }
//    @ResponseBody
//    @ExceptionHandler(value = Exception.class)
//    public Result Exception(Exception e) {
//        log.error("CommonExceptionHandler异常:{}",e);
//        return Result.fail(e.getMessage());
//    }
//    @ResponseBody
//    @ExceptionHandler(value = Throwable.class)
//    public Result Throwable(Throwable e) {
//        log.error("CommonExceptionHandler异常:{}",e);
//        return Result.fail(e.getMessage());
//    }
//}
