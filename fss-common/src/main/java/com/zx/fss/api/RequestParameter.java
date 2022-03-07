package com.zx.fss.api;

import com.zx.fss.utils.LoginUserHolder;
import lombok.Data;

import javax.validation.Valid;

/**
 * @author 张新
 * @title: RequestParameter
 * @projectName rpts-recon
 * @description: 
 * @date 2021/6/29 10:33
 */
@Data
public class RequestParameter<T> {
    //入参对象
    @Valid
    private T data;

    public Object user = null;
    public <T>T getUser(Class<T> tClass){
        return LoginUserHolder.getCurrentUser(tClass);
    }
}
