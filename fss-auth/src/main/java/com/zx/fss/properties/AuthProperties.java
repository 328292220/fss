package com.zx.fss.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 认证白名单配置
 * Created by macro on 2020/6/17.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Component
@ConfigurationProperties(prefix="auth")
@RefreshScope
public class AuthProperties {
    private Boolean isOpenCheckCode;

    public Boolean getIsOpenCheckCode() {
        return isOpenCheckCode;
    }

    public void setIsOpenCheckCode(Boolean isOpenCheckCode) {
        this.isOpenCheckCode = isOpenCheckCode;
    }
}
