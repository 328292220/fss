package com.zx.fss.controller;


import com.zx.fss.account.Menu;
import com.zx.fss.api.Result;
import com.zx.fss.service.MenuService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author guoenhong@crb.cn
 * @since 2022-02-15
 */
@RestController
@RequestMapping("/menu")
@AllArgsConstructor
public class MenuController {
    private MenuService menuService;
    @RequestMapping("/getMenu")
    Result getMenu(){
        Menu menu = menuService.getMenu();
        return Result.data(menu);
    }
}
