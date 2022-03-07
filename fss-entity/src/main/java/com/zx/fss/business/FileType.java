package com.zx.fss.business;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 文件类型
 * </p>
 *
 * @author guoenhong@crb.cn
 * @since 2022-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("fss_file_type")
@ApiModel(value="FileType对象", description="文件类型")
public class FileType implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "类型编号")
    @TableId(value = "type_id", type = IdType.AUTO)
    private Long typeId;

    @ApiModelProperty(value = "类型名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "类型状态")
    @TableField("status")
    private String status;


}
