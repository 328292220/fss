package com.zx.fss;

import com.zx.fss.constant.RedisConstant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDemo {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Test
    public void test(){
        String fss_secret = passwordEncoder.encode("fss_secret");
        System.out.println(fss_secret);
        fss_secret = passwordEncoder.encode("123456");
        System.out.println(fss_secret);
        boolean matches = passwordEncoder.matches("123456",
                "$2a$10$LaApID8TP9nfpus/Rxrjqeoc5SNlG76o5p2xZbgr1b8v7eBt8quWO");
        System.out.println(matches);

    }

    @Test
    public void testRedis(){
        Boolean delete = redisTemplate.delete(RedisConstant.RESOURCE_ROLES_MAP);
        System.out.println(delete);
    }
}
