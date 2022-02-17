package com.zx.fss;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.zx.fss.mapper")
@EnableTransactionManagement
public class FssAccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(FssAccountApplication.class, args);
    }

}
