package com.zx.fss.account;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zx.fss.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author guoenhong@crb.cn
 * @since 2022-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("fss_user")
@ApiModel(value="User对象", description="")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @ApiModelProperty(value = "用户名")
    @TableField("user_name")
    private String userName;

    @ApiModelProperty(value = "密码")
    @TableField("password")
    private String password;

    @TableField("nick_name")
    private String nickName;

    @ApiModelProperty(value = "性别（1男 2女）")
    @TableField("gender")
    private Integer gender;

    @ApiModelProperty(value = "邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty(value = "启用标识")
    @TableField("enabled_flag")
    private String enabledFlag;

    @ApiModelProperty(value = "RSA密码")
    @TableField("rsa_password")
    private String rsaPassword;
	
	/**
     * 系统权限标识组
     */
    @ApiModelProperty("系统权限标识组")
    @TableField(exist = false)
    private List<String> resources;

    /**
     * 系统角色标识组
     */
    @ApiModelProperty(value = "系统角色标识组")
    @TableField(exist = false)
    private List<String> roleIds;


}
