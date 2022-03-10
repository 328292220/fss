package com.zx.fss;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@MapperScan("com.zx.fss.mapper")
@EnableTransactionManagement
public class FssAccountApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(FssAccountApplication.class, args);
        log.info("用户服务启动......");
        log.info("用户服务启动......");
        log.info("用户服务启动......");
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(this.getClass());
    }
}
