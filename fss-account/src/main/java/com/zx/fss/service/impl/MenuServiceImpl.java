package com.zx.fss.service.impl;

import com.zx.fss.account.Menu;
import com.zx.fss.account.Resource;
import com.zx.fss.account.User;
import com.zx.fss.holder.LoginUserHolder;
import com.zx.fss.mapper.MenuMapper;
import com.zx.fss.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    private LoginUserHolder loginUserHolder;

    @Override
    public Menu getMenu() {
        User user = loginUserHolder.getCurrentUser();
        //通过用户获取该用户拥有的菜单
        Menu menu = baseMapper.getMenusByUserId(user.getUserId());

        return menu;
    }
}
