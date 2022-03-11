package com.zx.fss;

import com.zx.fss.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FssAccountApplicationTests {
    @Autowired
    RedisUtil redisUtil;
    @Test
    void contextLoads() {
        redisUtil.set("a.b.A",1);
        redisUtil.set("a.b.*",3);
        redisUtil.set("a.b.B",2);
        Object o = redisUtil.get("a.b.*");
        System.out.println(o);
    }

}
