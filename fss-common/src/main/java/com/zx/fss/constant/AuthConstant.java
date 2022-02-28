package com.zx.fss.constant;

/**
 * Created by macro on 2020/6/19.
 */
public class AuthConstant {

    public static final String AUTHORITY_PREFIX = "ROLE_";

    public static final String AUTHORITY_CLAIM_NAME = "authorities";

    /**
     * 验证码有效期
     */
    public static final Integer VERIFICATION_CODE_TIME = 60;

    /**
     * 错误次数
     */
    public static final String VERIFY_ERROR_TIME = "VERIFY_ERROR_TIME:%s";

    /**
     * 最大允许重试次数
     */
    public static final Integer VERIFICATION_MAX_TIME = 5;

    public static final String VERIFICATION_CODE = "code";

    public static final String VERIFICATION_UUID = "uuid";

}
