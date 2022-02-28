/**
 * 文件名： BaseDTO.java
 * 版权： Copyright 2020-2021 CRB All Rights Reserved.
 * 描述： 基础实体类
 */
package com.zx.fss;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Version;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体类
 *
 * @author guoenhong@crb.cn
 * @date 2021/05/10 12:00
 */
@Getter
@Setter
@ToString
public class BaseEntity implements Serializable {

    public static final String FIELD_OBJECT_VERSION_NUMBER = "objectVersionNumber";
    public static final String FIELD_IS_DELETED = "isDeleted";
    public static final String FIELD_CREATED_BY = "createdBy";
    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_LAST_UPDATED_BY = "lastUpdatedBy";
    public static final String FIELD_LAST_UPDATE_DATE = "lastUpdateDate";

    /*
    * 版本号，用于乐观锁
    */
    @Version
    @ApiModelProperty(value = "版本号")
    @TableField("object_version_number")
    private Long objectVersionNumber;

    /*
     * 逻辑删除，标识未删除字段
     */
    @TableLogic
    @ApiModelProperty(value = "是否未删除(默认为1)")
    @TableField("is_deleted")
    private Integer isDeleted;

    /*
     * 数据库记录字段
     */
    @JsonIgnore
    @ApiModelProperty(value = "创建时间")
    @TableField(value = "creation_date", fill = FieldFill.INSERT)
    private Date creationDate;

    @JsonIgnore
    @ApiModelProperty(value = "创建人")
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private Long createdBy;

    @JsonIgnore
    @ApiModelProperty(value = "最后更新时间")
    @TableField(value = "last_update_date", fill = FieldFill.INSERT_UPDATE)
    private Date lastUpdateDate;

    @JsonIgnore
    @ApiModelProperty(value = "最后更新人")
    @TableField(value = "last_updated_by", fill = FieldFill.INSERT_UPDATE)
    private Long lastUpdatedBy;


}
