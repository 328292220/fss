package com.zx.front.sysadmin.controller;

import com.zx.front.sysadmin.entity.SysUserVo;
import com.zx.front.sysadmin.util.SysSettingUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class IndexController {

    /**
     * 跳转登录页面
     */
    @GetMapping("/")
    public String toLogin(){

        return "redirect:login";
    }

    /**
     * 跳转登录页面
     */
    @GetMapping("login")
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView("login");
        //系统信息
        modelAndView.addObject("sys", SysSettingUtil.getSysSetting());
        return modelAndView;
    }
    /**
     * 跳转登录页面
     */
    @GetMapping("index")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("index");
        //系统信息
        modelAndView.addObject("sys", SysSettingUtil.getSysSetting());

//        //登录用户
//        SysUserVo sysUserVo = sysUserService.findByLoginName(SecurityUtil.getLoginUser().getUsername()).getData();
//        sysUserVo.setPassword(null);//隐藏部分属性
//        modelAndView.addObject( "loginUser", sysUserVo);
//
//        //登录用户系统菜单
//        List<SysMenuVo> menuVoList = sysUserMenuService.findByUserId(sysUserVo.getUserId()).getData();
//        modelAndView.addObject("menuList",menuVoList);
//
//        //登录用户快捷菜单
//        List<SysShortcutMenuVo> shortcutMenuVoList= sysShortcutMenuService.findByUserId(sysUserVo.getUserId()).getData();
//        modelAndView.addObject("shortcutMenuList",shortcutMenuVoList);
        return modelAndView;
    }
}
