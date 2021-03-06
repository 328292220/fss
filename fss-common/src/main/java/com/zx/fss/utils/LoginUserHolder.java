package com.zx.fss.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 获取登录用户信息
 * Created by macro on 2020/6/17.
 */
@Slf4j
public class LoginUserHolder {
    static ThreadLocal<HeaderMapRequestWrapper> threadLocal = new ThreadLocal();
    public static void setCurrentUser(String userJsonStr){
        //从Header中获取用户信息
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(request);
        requestWrapper.addHeader("user", userJsonStr);
        threadLocal.set(requestWrapper);
    }
    public static void clearCurrentUser(){
        threadLocal.set(null);
    }

    public static <T> T getCurrentUser(Class<T> tClass){
        //从Header中获取用户信息
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String userStr = request.getHeader("user");
        if(StringUtils.isEmpty(userStr)){
            String user = threadLocal.get().getHeader("user");
            if(user != null){
                T t = JSON.parseObject(user, tClass);
                return t;
            }
            return null;
        }
        T user = JSON.parseObject(userStr, tClass);
        return user;
    }
    public static <T> T getCurrentUser(){
        //从Header中获取用户信息
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String userStr = request.getHeader("user");
        if(StringUtils.isEmpty(userStr)){
            return null;
        }
        Class clazz = null;
        try {
//            clazz = ClassLoader.getSystemClassLoader().loadClass("com.zx.fss.account.User");
            clazz = Class.forName("com.zx.fss.account.User");
        } catch (ClassNotFoundException e) {
            log.error("反射获取类class报错:{}",e);
        }
        T user = (T)JSON.parseObject(userStr, clazz);
        return user;
    }


}
