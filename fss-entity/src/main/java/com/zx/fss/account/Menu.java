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
 * 菜单表
 * </p>
 *
 * @author guoenhong@crb.cn
 * @since 2022-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("fss_menu")
@ApiModel(value="Menu对象", description="菜单表")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "menu_id", type = IdType.AUTO)
    private Long menuId;

    @ApiModelProperty(value = "菜单编码")
    @TableField("menu_code")
    private String menuCode;

    @ApiModelProperty(value = "菜单名称")
    @TableField("menu_name")
    private String menuName;

    @ApiModelProperty(value = "菜单说明")
    @TableField("menu_desc")
    private String menuDesc;

    @ApiModelProperty(value = "跳转路径")
    @TableField("path")
    private String path;

    @ApiModelProperty(value = "上级菜单ID")
    @TableField("parent_menu_id")
    private Long parentMenuId;

    @ApiModelProperty(value = "前端图标")
    @TableField("icon")
    private String icon;

    @ApiModelProperty(value = "排序")
    @TableField("order_by")
    private Integer orderBy;

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

    @TableField(exist = false)
    private List<Menu> children;


}
