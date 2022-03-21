package com.zx.fss.business.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DeleteDTO {
    @ApiModelProperty(value = "需要删除的目录ID集合")
    private List<Long> dirIds;
    @ApiModelProperty(value = "需要删除的文件ID集合")
    private List<Long> fileIds;
}
