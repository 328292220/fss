package com.zx.fss.business;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zx.fss.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.Tolerate;

/**
 * <p>
 * 文件夹
 * </p>
 *
 * @author guoenhong@crb.cn
 * @since 2022-03-01
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("fss_dir")
@ApiModel(value="Dir对象", description="文件夹")
public class Dir extends BaseEntity {

    @Tolerate
    public Dir(){

    }

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件夹编号")
    @TableId(value = "dir_id", type = IdType.AUTO)
    private Long dirId;

    @ApiModelProperty(value = "文件夹名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "父文件夹")
    @TableField("parent_id")
    private Long parentId;

    @ApiModelProperty(value = "创建人")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "文件夹地址")
    @TableField("path")
    private String path;




}
