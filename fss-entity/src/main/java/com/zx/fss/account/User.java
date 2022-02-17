package com.zx.fss.account;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class User implements Serializable {

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

    @ApiModelProperty(value = "创建时间")
    @TableField("creation_date")
    private Date creationDate;

    @ApiModelProperty(value = "创建人")
    @TableField("created_by")
    private Integer createdBy;

    @ApiModelProperty(value = "最后更新时间")
    @TableField("last_update_date")
    private Date lastUpdateDate;

    @ApiModelProperty(value = "最后更新人")
    @TableField("last_updated_by")
    private Integer lastUpdatedBy;

    @ApiModelProperty(value = "是否未删除")
    @TableField("is_deleted")
    private Integer isDeleted;

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
