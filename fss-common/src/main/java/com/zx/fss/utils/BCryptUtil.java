package com.zx.fss.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BCryptUtil {
    private static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 加密
     * @return
     */
    public static String encode(String password) {
        if(StringUtils.isEmpty(password)){
            password = "123456";
        }
        return passwordEncoder().encode(password);
    }

}
