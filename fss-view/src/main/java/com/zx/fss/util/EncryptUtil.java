package com.zx.fss.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtil {
    public static String encryptByMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //生成md5加密算法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(str.getBytes());
        byte b[] = md5.digest();
        int i;
        StringBuffer buf = new StringBuffer("");
        for (int j = 0; j < b.length; j++) {
            i = b[j];
            if (i < 0)
                i += 256;
            if (i < 16)
                buf.append("0");
            buf.append(Integer.toHexString(i));
        }
        String md5_32 = buf.toString();		//32位加密   与mysql的MD5函数结果一致。
//        String md5_16 = buf.toString().substring(8, 24);	//16位加密
        return md5_32;
    }
}
