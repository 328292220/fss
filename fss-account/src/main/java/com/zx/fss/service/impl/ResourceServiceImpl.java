package com.zx.fss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.fss.account.Resource;
import com.zx.fss.account.Role;
import com.zx.fss.account.RoleResource;
import com.zx.fss.account.User;
import com.zx.fss.mapper.ResourceMapper;
import com.zx.fss.service.ResourceService;
import com.zx.fss.service.RoleResourceService;
import com.zx.fss.service.RoleService;
import com.zx.fss.utils.LoginUserHolder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author guoenhong@crb.cn
 * @since 2022-02-15
 */
@Service
@AllArgsConstructor
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    private RoleResourceService roleResourceService;

    private RoleService roleService;

    @Override
    public Map<String, Object> getResourceRolesMap() {
        Map<String, Object> resultMap = new TreeMap<>();

        //获取所以资源
        LambdaQueryWrapper<Resource> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Resource::getIsDeleted, 0L);
        wrapper.eq(Resource::getEnabledFlag, "Y");
        List<Resource> resourceList = this.list(wrapper);
        //获取拥有资源权限的角设集合
        resourceList.stream().forEach(resource -> {
            //获取角色资源中间表集合
            LambdaQueryWrapper<RoleResource> rrWrapper = new LambdaQueryWrapper<>();
            rrWrapper.eq(RoleResource::getResourceId, resource.getResourceId());
            List<RoleResource> roleResourceList = roleResourceService.list(rrWrapper);
            roleResourceList.stream().forEach(roleResource -> {
                //获取角色
                LambdaQueryWrapper<Role> rWrapper = new LambdaQueryWrapper<>();
                rWrapper.eq(Role::getRoleId, roleResource.getRoleId());
                List<Role> roleList = roleService.list(rWrapper);
                List<String> roles = roleList.stream().map(one -> one.getEname()).collect(Collectors.toList());
                if(roles.size() > 0){
                    resultMap.put(resource.getResourceUrl(),roles);
                }
            });

        });

        return resultMap;
    }

    @Override
    public Map<String, Object> menus() {
        User user = LoginUserHolder.getCurrentUser(User.class);
        //通过用户获取该用户拥有的菜单资源
        List<Resource> resourceList = baseMapper.getResourcesByUserId(user.getUserId());

        return null;
    }
}
