package com.zx.fss;

import com.alibaba.fastjson.JSON;
import com.zx.fss.account.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


class FssBusinessApplicationTests {

    @Test
    void contextLoads() {
        String s = "{\"userId\":21}";
        User user = JSON.parseObject(s, User.class);
        System.out.println(user);
    }

}
