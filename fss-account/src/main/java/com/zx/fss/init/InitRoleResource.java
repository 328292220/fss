package com.zx.fss.init;

import com.zx.fss.constant.RedisConstant;
import com.zx.fss.service.ResourceService;
import com.zx.fss.utils.RedisUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 初始化的时候把资源与角色匹配关系缓存到Redis中，方便网关服务进行鉴权的时候获取
 */
@Component
@AllArgsConstructor
@Slf4j
public class InitRoleResource implements CommandLineRunner {
    ResourceService resourceService;
    RedisUtil redisUtil;
    @Override
    public void run(String... args) throws Exception {
        //先清空缓存
        redisUtil.del(RedisConstant.RESOURCE_ROLES_MAP);
        Map<String, Object> resourceRolesMap = resourceService.getResourceRolesMap();
        redisUtil.hmset(RedisConstant.RESOURCE_ROLES_MAP, resourceRolesMap);

    }
}
