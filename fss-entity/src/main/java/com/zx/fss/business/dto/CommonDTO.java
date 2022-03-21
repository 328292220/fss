package com.zx.fss.business.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CommonDTO {
//    @ApiModelProperty(value = "当前目录ID")
//    private Long dirId;
    @ApiModelProperty(value = "父目录ID,不传或null,表示查询根目录下数据")
    private Long parentId;
    @ApiModelProperty(value = "排序字段:暂时无用，默认根据修改时间排序")
    private String orderByName;
    @ApiModelProperty(value = "排序方式：DESC | ASC")
    private String orderByType;
}
