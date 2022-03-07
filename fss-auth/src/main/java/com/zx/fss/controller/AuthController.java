package com.zx.fss.controller;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.zx.fss.api.Result;
import com.zx.fss.constant.AuthConstant;
import com.zx.fss.domain.Oauth2TokenDto;
import com.zx.fss.service.IVerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Map;

/**
 * 自定义Oauth2获取令牌接口
 * Created by macro on 2020/7/17.
 */
@RestController
@RequestMapping("/oauth")
public class AuthController {

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @Autowired
    private IVerificationCodeService verificationCodeService;

//    @Autowired
//    private JdbcTokenStore jdbcTokenStore;

    /**
     * Oauth2登录认证
     */
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public Result<Oauth2TokenDto> postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        String verificationCode = parameters.get(AuthConstant.VERIFICATION_CODE);
        String verificationUuid = parameters.get(AuthConstant.VERIFICATION_UUID);
        String checkResult = verificationCodeService.checkVerificationCode(verificationCode, verificationUuid);
        if (StringUtils.isNotBlank(checkResult)) {
            // 如果校验验证码失败,直接返回
            //todo
            //return Result.fail(checkResult);
        }
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        Oauth2TokenDto oauth2TokenDto = Oauth2TokenDto.builder()
                .token(oAuth2AccessToken.getValue())
                .refreshToken(oAuth2AccessToken.getRefreshToken().getValue())
                .expiresIn(oAuth2AccessToken.getExpiresIn())
                .tokenHead("Bearer ").build();

        return Result.success(oauth2TokenDto);
    }

    /**
     * 删除token
     */
//    @GetMapping(value = "/loginOut/{clientId}")
//    public Result<Oauth2TokenDto> loginOut(@PathVariable String clientId ) throws HttpRequestMethodNotSupportedException {
//        Collection<OAuth2AccessToken> tokens = jdbcTokenStore.findTokensByClientId(clientId);
//        tokens.forEach(token->jdbcTokenStore.removeAccessToken(token));
//        return Result.success("退出成功");
//    }

    @RequestMapping("/verificationCode")
    public Result<Map<String, Object>> generateVerificationCode() throws IOException {
        return Result.success(verificationCodeService.generateVerificationCode());
    }
}
