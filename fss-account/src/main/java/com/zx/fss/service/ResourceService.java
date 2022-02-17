package com.zx.fss.service;

import com.zx.fss.account.Resource;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 资源表 服务类
 * </p>
 *
 * @author guoenhong@crb.cn
 * @since 2022-02-15
 */
public interface ResourceService extends IService<Resource> {

    Map<String, Object> getResourceRolesMap();

}
