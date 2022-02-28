package com.zx.fss.service;

import java.io.IOException;
import java.util.Map;

/**
 * @className: IVerificationCodeService
 * @description: TODO 类描述
 * @author: pengju.ma
 * @date: 2022/1/12 15:43
 **/
public interface IVerificationCodeService {

    /**
     * @description: 生成验证码图片
     * @author: pengju.ma
     * @date: 2022/1/12 15:57
     * @param:
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     **/
    Map<String, Object> generateVerificationCode() throws IOException;

    /**
     * 检验验证码
     *
     * @param verificationCode 验证码
     * @param verificationUuid uuid
     * @return
     */
    String checkVerificationCode(String verificationCode, String verificationUuid);

}
