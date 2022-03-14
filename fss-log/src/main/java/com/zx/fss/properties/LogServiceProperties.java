package com.zx.fss.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 认证白名单配置
 * Created by macro on 2020/6/17.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Component
@ConfigurationProperties(prefix="log-service")
public class LogServiceProperties {
    private int defaultLastLineSize;
    private int maxLogLineSize;
    private String rootPath;//日志根目录
    private String  groupId;
    private List<String> serverList;
    private List<String> levelList;
}
