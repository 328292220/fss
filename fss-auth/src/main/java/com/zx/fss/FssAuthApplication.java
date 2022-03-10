package com.zx.fss;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@Slf4j
@SpringBootApplication
@EnableFeignClients
public class FssAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(FssAuthApplication.class, args);
        log.info("认证服务启动.......");
        log.info("认证服务启动.......");
        log.info("认证服务启动.......");
    }

}
