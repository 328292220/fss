package com.zx.fss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zx.fss.account.Role;
import com.zx.fss.account.User;
import com.zx.fss.account.UserRole;
import com.zx.fss.api.RequestParameter;
import com.zx.fss.api.Result;
import com.zx.fss.api.ResultCode;
import com.zx.fss.business.Dir;
import com.zx.fss.exception.BaseRuntimeException;
import com.zx.fss.exception.ResultException;
import com.zx.fss.feign.service.FssAccountService;
import com.zx.fss.feign.service.FssBusinessService;
import com.zx.fss.mapper.UserMapper;
import com.zx.fss.service.RoleService;
import com.zx.fss.service.UserRoleService;
import com.zx.fss.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.fss.utils.BCryptUtil;
import com.zx.fss.utils.RSAEncrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author guoenhong@crb.cn
 * @since 2022-02-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    UserRoleService userRoleService;

    @Autowired
    RoleService roleService;

    @Autowired
    FssBusinessService fssBusinessService;

    @Autowired
    FssAccountService fssAccountService;

    @Override
    public List<String> getRolesByUserId(Long userId) {
        return baseMapper.getRolesByUserId(userId);
    }

    @Override
    @Transactional
    public void registerUser(User user) throws Exception {
        //校验用户是否存在
        User one = this.getOne(new LambdaQueryWrapper<User>().eq(User::getUserName, user.getUserName()).eq(User::getIsDeleted,0));
        if(one != null){
            throw new ResultException(ResultCode.USER_IS_EXIST);
        }

        String password = user.getPassword();
        user.setPassword(BCryptUtil.encode(password));
        user.setRsaPassword(RSAEncrypt.encrypt(password,RSAEncrypt.publick_key));
        this.save(user);
        List<String> roleIds = user.getRoleIds();
        //默认普通用户
        if(CollectionUtils.isEmpty(roleIds)){
            //获取普通角色ID
            LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
            Role role = roleService.getOne(wrapper.eq(Role::getEname, "USER"));
            UserRole userRole = new UserRole();
            userRole.setRoleId(role.getRoleId());
            userRole.setUserId(user.getUserId());
            userRoleService.save(userRole);
        }

        //远程调用创建默认目录
        RequestParameter<Dir> parameter = new RequestParameter<>();
        Dir dir = new Dir();
        dir.setUserId(user.getUserId());
        dir.setName("默认文件夹");
        parameter.setData(dir);
        Result result = fssBusinessService.add(parameter);
        if (result.getCode() != 200){
            throw new RuntimeException("远程调用创建默认目录失败："+result.getMsg());
        }
    }
}
