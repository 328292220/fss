package com.zx.fss.service.impl;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.zx.fss.constant.AuthConstant;
import com.zx.fss.service.IVerificationCodeService;
import com.zx.fss.utils.RedisUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @className: VerificationCodeServiceImpl
 * @description: 验证码实现类
 * @author: pengju.ma
 * @date: 2022/1/12 15:44
 **/
@Service
@AllArgsConstructor
public class VerificationCodeServiceImpl implements IVerificationCodeService {

    private final DefaultKaptcha defaultKaptcha;

    private final RedisUtil redisUtil;

    @Override
    public Map<String, Object> generateVerificationCode() throws IOException {
        Map<String, Object> map = new HashMap<>(16);

        // 生成验证文字
        String text = defaultKaptcha.createText();
        // 生成图片验证码
        ByteArrayOutputStream outputStream = null;
        BufferedImage bufferedImage = defaultKaptcha.createImage(text);
        outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", outputStream);

        map.put("img", Base64.getEncoder().encodeToString(outputStream.toByteArray()));
        // 生成对应验证码的随机uuid
        String uuid = UUID.randomUUID().toString();
        redisUtil.set(uuid, text, AuthConstant.VERIFICATION_CODE_TIME);

        map.put("uuid", uuid);
        return map;
    }

    @Override
    public String checkVerificationCode(String verificationCode, String verificationUuid) {
        if (Objects.isNull(verificationCode)) {
            return "请输入验证码";
        }
        Object cacheCode = redisUtil.get(verificationUuid);
        if (Objects.isNull(cacheCode)) {
            return "验证码已失效,请重新获取";
        } else {
            String verifyErrorKey = String.format(AuthConstant.VERIFY_ERROR_TIME, verificationUuid);
            if (verificationCode.equals(cacheCode)) {
                // 验证码匹配,移除redis中的数据
                redisUtil.del(verificationUuid, verifyErrorKey);
                return null;
            } else {
                // 验证码错误,最多可输入5次错误
                Object verifyErrorTime = redisUtil.get(verifyErrorKey);
                if (Objects.isNull(verifyErrorTime)) {
                    redisUtil.set(verifyErrorKey, 1, AuthConstant.VERIFICATION_CODE_TIME);
                    return "验证码错误,剩余重试次数：4";
                } else {
                    int errorTime = (int) verifyErrorTime;
                    if (errorTime > AuthConstant.VERIFICATION_MAX_TIME) {
                        // 超过
                        redisUtil.del(verificationUuid, verifyErrorKey);
                        return "验证码超过最大重试次数,请重新获取";
                    } else {
                        redisUtil.incr(verifyErrorKey, 1);
                        int leftRetryTime = AuthConstant.VERIFICATION_MAX_TIME - errorTime;
                        return "验证码错误,剩余重试次数：" + leftRetryTime;
                    }
                }
            }
        }
    }

}
