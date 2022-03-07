package com.zx.fss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zx.fss.account.Role;
import com.zx.fss.account.User;
import com.zx.fss.account.UserRole;
import com.zx.fss.api.ResultCode;
import com.zx.fss.exception.ResultException;
import com.zx.fss.mapper.UserMapper;

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

}
