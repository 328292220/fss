package com.zx.front.sysadmin.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class SysSettingVo {
    public String id;//表id

    public String sysName = "文件管理系统";//系统名称

    public String sysLogo = "";//系统logo图标

    public String sysBottomText;//系统底部信息

    public String sysColor;//系统颜色

    public String sysNoticeText;//系统公告

    public String sysApiEncrypt;//API加密 Y/N

    public String sysOpenApiLimiterEncrypt;//OpenAPI限流 Y/N

    public Date createTime;//创建时间

    public Date updateTime;//修改时间

    public String userInitPassword;//用户管理：初始、重置密码
}
