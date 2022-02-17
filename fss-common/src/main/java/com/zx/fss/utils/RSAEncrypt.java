/**
 *
 */
package com.zx.fss.utils;

/**
 * @description: (用一句话描述该文件做什么)
 * @author: lifang109
 */

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAEncrypt {

    private static final String REPLACE_TARGET = "[+]";
    private static final String REPLACE_CODE = "@";

    public static String private_key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALgY6O7xTdvghBOOpuaaHyFtHiAd0myd+9lAFL+TwJaIVnQNEfxtbJlLyZUISXZWzVRO5yxKQvEkW7eclOKYuEnveUFg2c4GeAduvoJF9WOGAkKqpoi8fnlZCYf+/ehIoZkvJybn2PoOrTK8AKsiAMe681zVlNW9ST66dOie+Ic9AgMBAAECgYEAruOjLZN5Zr+EWVv0fC/AES4rJ1GNXHg9ECcLTvAbTiYZE0hzZL3DJGJWD0l/GHffug73GCE4NQNYSjbrTdTNhHdx/GcqSJnqW8sNdhMMCGFMe4YbWkQbidgg2OqJwvggbK/iMlePA2POKWhPQlBTXpsxiFhxpZMJ5U6LLsPzVrUCQQD3wlzobGh9cjDkCqNW4AMW50x6IGKmANCCSmjg+0GppOU8ywJCQe1JMCc/9wm45fkkpU2Y9Vn47ZV+xL/O7sjDAkEAvjh5XkkSzcxAsiWHvF8d8JivvilXuWprnE9l7QgAniBJpVELWPwWLADCUPR48o+splDfuXNiPODzFZojoIZv/wJBAIGyIjadQVmh5EwL8ZNxssxTimpH97wUoxIBZ8OWY7otkr7vOqB6qH9ukK1gYZuD75cqXWOb1FyF6oMw9YOa5kcCQD8/ieN2yqDRHGXfor1Ypxzbt/uzxW5qWzOTFuH0ejgZpkMoqmxBDaI98l52m0yTxyVZ2LnSCsURSuJQ6S2z5cMCQCwHI9aflBZHkjfd2JlpjMzWY7E+py+YYqIeXvTJoRuc4CHKpzHPX3cE520fc9L7N6cTa2iM0Kz+pSRtwuh0JwI=";
    public static String publick_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC9UcbGHLrLGbX1HA1BP8jZzMkXdC2yNLmmwfKq2IJnJ5oG9gKKApkIvzDr9EBVNWh4UKYW+uXoIyRUg3iKjS/d+lu16AJ/Yz9z5TJM3KM6AH5/kMXj1XycazZk8PsgikNsEyIOp0q5DvDgOq0vDqtmh5IXiRWc1B6GWvLxj+BnAwIDAQAB";

    private static Map<Integer, String> keyMap = new HashMap<Integer, String>();  //用于封装随机产生的公钥与私钥

    public static void main(String[] args) throws Exception {
//        //生成公钥和私钥
//        genKeyPair();
//        //加密字符串
//        String message = "df723820";
//        System.out.println("随机生成的公钥为:" + keyMap.get(0));
//        System.out.println("随机生成的私钥为:" + keyMap.get(1));
//        String messageEn = encrypt(message, keyMap.get(0));
//        System.out.println(message + "\t加密后的字符串为:" + messageEn);
//        String messageDe = decrypt(messageEn, keyMap.get(1));
//        System.out.println("还原后的字符串为:" + messageDe);


        String key = "vVHGxhy6yxm19RwNQT_I2czJF3QtsjS5psHyqtiCZyeaBvYCigKZCL8w6_RAVTVoeFCmFvrl6CMkVIN4io0v3fpbtegCf2M_c-UyTNyjOgB-f5DF49V8nGs2ZPD7IIpDbBMiDqdKuQ7w4DqtLw6rZoeSF4kVnNQehlry8Y_gZwM";

        String s = Base64.encodeBase64String(key.getBytes("utf-8"));
        System.out.println(s);

        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCEU3520gUkFWqiNc6KoNcEPo9l3kZxPhhzNfyML/5723WWlwuqeF83Mo/W4fpbQfsc6J4iOQPp0YMu9QGF7K6gu4OtHNSFUbu6QSnItzzSL86w0gG3DZqGmY519TAuOrV4yVHDw8wRAEJDeSQr+h4yjAXXL6y3XuDkvQpltElY5QIDAQAB";
        publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC9UcbGHLrLGbX1HA1BP8jZzMkXdC2yNLmmwfKq2IJnJ5oG9gKKApkIvzDr9EBVNWh4UKYW+uXoIyRUg3iKjS/d+lu16AJ/Yz9z5TJM3KM6AH5/kMXj1XycazZk8PsgikNsEyIOp0q5DvDgOq0vDqtmh5IXiRWc1B6GWvLxj+BnAwIDAQAB";
        String encrypt = encrypt("123456", publicKey);
        System.out.println(encrypt);

        String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAL1RxsYcussZtfUcDUE/yNnMyRd0LbI0uabB8qrYgmcnmgb2AooCmQi/MOv0QFU1aHhQphb65egjJFSDeIqNL936W7XoAn9jP3PlMkzcozoAfn+QxePVfJxrNmTw+yCKQ2wTIg6nSrkO8OA6rS8Oq2aHkheJFZzUHoZa8vGP4GcDAgMBAAECgYEAqKdEK9MGOM6TwWKXEEyRUi3T6tftqXa8+KiI1NgB+4FU/uSDyIyd5iPkoh66fO4SPjSicZBb5dwAjmV8qfky7MVYljwrpWd90ipOZ7a42hf1C4e0B0eDxqCz6QJNBksxMrK9E0e0x+UohozzsDGpJMqSIDLNNa76vshTLOnJxdECQQDnLkkq2KslIxVlKBSrfwpl+eH1RZydAQWAhYaM9NLBYf97/q7LvwpSTRu/Y84LcQTv4P3T9fGwTi7oclAHMevbAkEA0aT7zLnH4ZObiDuQ/j56boLdLO9YooRFV/Dv1SqcsXsc/QH6vESwfJfKFnOsqaYSI/nt4IKIBVm5V8XOHn2t+QJAM728Soar52fuxfEujyOj5CcFV6v43Xr5DEbWUn4JHZcdY1chMCDV4o23hCKGXzEwda/Zn7MQIPGetg+/sZyOuQJBALKS5iks1qW/JeyVUJxAe+lGbz+nYMiOAsM16BSbh31mcPrShNF5cRuv7+PQBHM9kpYyff2PMm3cT5QEWd5/D4ECQQCFmHuwH4xdAPPEskNeTF7mlDnfUiyQkA/vxTxw405aoDsC38hVzqZNWbp9RVSLGXlbil5bMgHnq12CsYAC8N9P";
        privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAL1RxsYcussZtfUcDUE/yNnMyRd0LbI0uabB8qrYgmcnmgb2AooCmQi/MOv0QFU1aHhQphb65egjJFSDeIqNL936W7XoAn9jP3PlMkzcozoAfn+QxePVfJxrNmTw+yCKQ2wTIg6nSrkO8OA6rS8Oq2aHkheJFZzUHoZa8vGP4GcDAgMBAAECgYEAqKdEK9MGOM6TwWKXEEyRUi3T6tftqXa8+KiI1NgB+4FU/uSDyIyd5iPkoh66fO4SPjSicZBb5dwAjmV8qfky7MVYljwrpWd90ipOZ7a42hf1C4e0B0eDxqCz6QJNBksxMrK9E0e0x+UohozzsDGpJMqSIDLNNa76vshTLOnJxdECQQDnLkkq2KslIxVlKBSrfwpl+eH1RZydAQWAhYaM9NLBYf97/q7LvwpSTRu/Y84LcQTv4P3T9fGwTi7oclAHMevbAkEA0aT7zLnH4ZObiDuQ/j56boLdLO9YooRFV/Dv1SqcsXsc/QH6vESwfJfKFnOsqaYSI/nt4IKIBVm5V8XOHn2t+QJAM728Soar52fuxfEujyOj5CcFV6v43Xr5DEbWUn4JHZcdY1chMCDV4o23hCKGXzEwda/Zn7MQIPGetg+/sZyOuQJBALKS5iks1qW/JeyVUJxAe+lGbz+nYMiOAsM16BSbh31mcPrShNF5cRuv7+PQBHM9kpYyff2PMm3cT5QEWd5/D4ECQQCFmHuwH4xdAPPEskNeTF7mlDnfUiyQkA/vxTxw405aoDsC38hVzqZNWbp9RVSLGXlbil5bMgHnq12CsYAC8N9P";
        String decrypt = decrypt(encrypt, privateKey);
        System.out.println(decrypt);

    }

    /**
     * 随机生成密钥对
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        keyMap.put(0, publicKeyString);  //0表示公钥
        keyMap.put(1, privateKeyString);  //1表示私钥
    }

    /**
     * RSA公钥加密
     *
     * @param str
     *            加密字符串
     * @param publicKey
     *            公钥
     * @return 密文
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		return Base64.encodeBase64String(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8))).replaceAll(REPLACE_TARGET, REPLACE_CODE);
	}

    /**
     * RSA私钥解密
     *
     * @param str
     *            加密字符串
     * @param privateKey
     *            私钥
     * @return 铭文
     * @throws Exception
     *             解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception {
		str = str.replaceAll(REPLACE_CODE, REPLACE_TARGET);
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes(StandardCharsets.UTF_8));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(inputByte));
    }

}
