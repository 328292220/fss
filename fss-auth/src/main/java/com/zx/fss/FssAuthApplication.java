package com.zx.fss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FssAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(FssAuthApplication.class, args);
    }

}
