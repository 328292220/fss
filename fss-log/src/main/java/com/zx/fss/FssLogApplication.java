package com.zx.fss;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class FssLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(FssLogApplication.class, args);
        log.info("日志服务启动......");
        log.info("日志服务启动......");
        log.info("日志服务启动......");
    }

}
