package com.zx.fss.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zx.fss.account.User;
import com.zx.fss.api.Result;
import com.zx.fss.holder.LoginUserHolder;
import com.zx.fss.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 获取登录用户信息接口
 * Created by macro on 2020/6/19.
 */
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController{

    private LoginUserHolder loginUserHolder;

    private UserService userService;

    @RequestMapping("/currentUser")
    public Result<User> currentUser() {
        User currentUser = loginUserHolder.getCurrentUser();
        return Result.success(currentUser);
    }

    @RequestMapping("/getUserByUserName")
    public Result<User> getUserByUserName(String userName) {
//        User user = new User();
//        user.setUserId(1111L);
//        user.setUserName(userName);
//        user.setPassword("$2a$10$kyB0xHJQE57P9.uelRx1AOMPgZhIZYv4prqxOATaTAjY8CEOGyd3q");
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,userName);
        User user = userService.getOne(wrapper);
        if (user == null){
            return Result.fail();
        }
        //获取角色
        List<String> roleIds = userService.getRolesByUserId(user.getUserId());
        user.setRoleIds(roleIds);
        return Result.success(user);
    }

    @RequestMapping("/registerUser")
    public Result<User> registerUser(@RequestBody User user) throws Exception {
        if(StringUtils.isEmpty(user.getUserName()) || StringUtils.isEmpty(user.getPassword())){
            return Result.fail("用户名、密码不能为空");
        }
        userService.registerUser(user);
        return Result.success(user);
    }

}
