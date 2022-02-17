package com.zx.front.sysadmin.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SysUserVo implements Serializable {
    private String userId;//用户id

    private String loginName;//登录名

    private String userName;//用户名称


}
