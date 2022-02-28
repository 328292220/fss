package com.zx.fss.service.impl;

import com.zx.fss.api.Result;
import com.zx.fss.api.ResultCode;
import com.zx.fss.constant.RedisConstant;
import com.zx.fss.exception.AuthException;
import com.zx.fss.feign.service.FssAccountService;
import com.zx.fss.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

/**
 * 资源与角色匹配关系管理业务类
 * Created by macro on 2020/6/19.
 * 初始化的时候把资源与角色匹配关系缓存到Redis中，方便网关服务进行鉴权的时候获取
 */
//@Service 初始化数据放到account服务中了
public class ResourceServiceImpl {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    FssAccountService accountService;
    //改到account服务中
    //@PostConstruct
    public void initData() {
        //先清空缓存
        redisUtil.del(RedisConstant.RESOURCE_ROLES_MAP);

        //feign调用获取登录用户及角色
        Result result = accountService.getResourceRolesMap();
        if( ResultCode.SERVICE_UNAVAILABLE.getCode() ==  result.getCode()){
            throw new AuthException(result.getCode(),AuthException.MSG_USER_SERVICE_ERROR);
        }
        if(result.getData() != null && ((Map)result.getData()).size() > 0){
            Map data = (Map)result.getData();
            redisUtil.hmset(RedisConstant.RESOURCE_ROLES_MAP, data);
        }

//        Map<String, Object> resourceRolesMap = new TreeMap<>();
//        resourceRolesMap.put("/account/hello", CollUtil.toList("ADMIN"));
//        resourceRolesMap.put("/account/user/currentUser", CollUtil.toList("ADMIN", "TEST"));
//        redisUtil.hmset(RedisConstant.RESOURCE_ROLES_MAP, resourceRolesMap);
    }
}
