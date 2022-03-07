package com.zx.fss.business.dto;

import lombok.Data;

@Data
public class CommonDTO {
    //当前目录ID
    private Long dirId;
    //父目录ID
    private Long parentId;
    //排序字段
    private String orderByName;
    //排序方式
    private String orderByType;
}
