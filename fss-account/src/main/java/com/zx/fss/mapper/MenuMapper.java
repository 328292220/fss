package com.zx.fss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zx.fss.account.Menu;

import java.util.Map;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author guoenhong@crb.cn
 * @since 2022-02-15
 */
public interface MenuMapper extends BaseMapper<Menu> {

    Menu getMenusByUserId(Long userId);
}
