package com.zx.fss.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.zx.fss.account.User;
import com.zx.fss.api.Result;
import com.zx.fss.api.ResultCode;
import com.zx.fss.constant.MessageConstant;
import com.zx.fss.domain.AuthUserDetails;
import com.zx.fss.domain.UserDTO;
import com.zx.fss.exception.AuthException;
import com.zx.fss.feign.service.FssAccountService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户管理业务类
 * Created by macro on 2020/6/19.
 */
@Service
public class UserServiceImpl implements UserDetailsService {

    @Resource
    FssAccountService accountService;

    private List<UserDTO> userList;

    private PasswordEncoder passwordEncoder = new PasswordEncoder() {
        @Override
        public String encode(CharSequence charSequence) {
            return null;
        }

        @Override
        public boolean matches(CharSequence charSequence, String s) {
            return false;
        }
    };

    @PostConstruct
    public void initData() {
        String password = passwordEncoder.encode("123456");
        userList = new ArrayList<>();
        userList.add(new UserDTO(1L,"macro", password,1, CollUtil.toList("ADMIN")));
        userList.add(new UserDTO(2L,"andy", password,1, CollUtil.toList("TEST")));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //测试使用
//        List<UserDTO> findUserList = userList.stream().filter(item -> item.getUsername().equals(username)).collect(Collectors.toList());
//        if (CollUtil.isEmpty(findUserList)) {
//            throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
//        }


        //feign调用获取登录用户及角色
        Result result = accountService.getUserByUserName(username);
        if( ResultCode.FAILURE.getCode() ==  result.getCode()){
            throw new AuthException(result.getCode(),result.getMsg());
        }
        if( ResultCode.SERVICE_UNAVAILABLE.getCode() ==  result.getCode()){
            throw new AuthException(result.getCode(),AuthException.MSG_USER_SERVICE_ERROR);
        }
        //用户校验
        User user = JSON.parseObject(JSON.toJSONStringWithDateFormat(result.getData(), JSON.DEFFAULT_DATE_FORMAT),User.class);
        if(user == null){
            throw new UsernameNotFoundException(MessageConstant.USERNAME_ERROR);
        }

        //角色信息
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if(user.getRoleIds() !=null){
            user.getRoleIds().forEach(item->authorities.add(new SimpleGrantedAuthority(item)));
        }

        return new AuthUserDetails(user,authorities);
    }

}
