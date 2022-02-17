package com.zx.fss.account;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

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
@TableName("fss_role")
@ApiModel(value="Role对象", description="")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

    @ApiModelProperty(value = "角色名字")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "角色名字")
    @TableField("ename")
    private String ename;

    @ApiModelProperty(value = "描述")
    @TableField("description")
    private String description;

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


}
