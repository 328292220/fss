package com.zx.fss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.fss.account.Menu;
import com.zx.fss.account.User;
import com.zx.fss.mapper.MenuMapper;
import com.zx.fss.service.MenuService;
import com.zx.fss.utils.LoginUserHolder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author guoenhong@crb.cn
 * @since 2022-02-15
 */
@Service
@AllArgsConstructor
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public Menu getMenu() {
        User user = LoginUserHolder.getCurrentUser(User.class);
        //通过用户获取该用户拥有的菜单
        Menu menu = baseMapper.getMenusByUserId(user.getUserId());

        return menu;
    }
}
