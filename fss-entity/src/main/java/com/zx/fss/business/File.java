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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文件信息
 * </p>
 *
 * @author guoenhong@crb.cn
 * @since 2022-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("fss_file")
@ApiModel(value="File对象", description="文件信息")
public class File extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件编号")
    @TableId(value = "file_id", type = IdType.AUTO)
    private Long fileId;

    @ApiModelProperty(value = "类型编号")
    @TableField("type_id")
    private Long typeId;

    @ApiModelProperty(value = "文件夹编号")
    @TableField("dir_id")
    private Long dirId;

    @ApiModelProperty(value = "用户编号")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "文件名")
    @TableField("file_name")
    private String fileName;

    @ApiModelProperty(value = "文件扩展名(后缀)")
    @TableField("ext_name")
    private String extName;

    @ApiModelProperty(value = "文件大小(单位kb)")
    @TableField("file_size")
    private Long fileSize;

    @ApiModelProperty(value = "文件真实存储路径")
    @TableField("real_path")
    private String realPath;

    @ApiModelProperty(value = "二进制文件数据")
    @TableField(exist = false)
    private String streamData;


}
