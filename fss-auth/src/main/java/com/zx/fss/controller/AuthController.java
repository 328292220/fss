package com.zx.fss.controller;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.zx.fss.api.Result;
import com.zx.fss.constant.AuthConstant;
import com.zx.fss.domain.LoginDTO;
import com.zx.fss.domain.Oauth2TokenDto;
import com.zx.fss.properties.AuthProperties;
import com.zx.fss.service.IVerificationCodeService;
import com.zx.fss.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
@Api(value = "认证", tags = "认证登录相关接口")
public class AuthController {

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @Autowired
    private IVerificationCodeService verificationCodeService;

    @Autowired
    AuthProperties authProperties;

    @Autowired
    private TokenStore jdbcTokenStore;

    @Autowired
    RedisUtil redisUtil;

    @Resource
    private ConsumerTokenServices consumerTokenServices;

    /**
     * Oauth2登录认证
     */
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    @ApiOperation(value = "获取或刷新token",httpMethod = "POST")
    public Result<Oauth2TokenDto> postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        String verificationCode = parameters.get(AuthConstant.VERIFICATION_CODE);
        String verificationUuid = parameters.get(AuthConstant.VERIFICATION_UUID);
        String checkResult = verificationCodeService.checkVerificationCode(verificationCode, verificationUuid);
        if (StringUtils.isNotBlank(checkResult) && authProperties.getIsOpenCheckCode()) {
            //如果校验验证码失败,直接返回
            return Result.fail(checkResult);
        }
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        Oauth2TokenDto oauth2TokenDto = Oauth2TokenDto.builder()
                .token(oAuth2AccessToken.getValue())
                .refreshToken(oAuth2AccessToken.getRefreshToken().getValue())
                .expiresIn(oAuth2AccessToken.getExpiresIn())
                .tokenHead("Bearer ").build();
        //将token存入redis,退出时删除
        //网关每次需要查询redis判断是否有该值，没有则说明已经退出
        redisUtil.set(oAuth2AccessToken.getValue(),"none_null",60*60*24);

        return Result.success(oauth2TokenDto);
    }
    /**
     * Oauth2登录认证
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "获取登录验证码",httpMethod = "GET")
    public Result<Map<String, Object>> postAccessToken() throws IOException {
        return Result.success(verificationCodeService.generateVerificationCode());
    }

    /**
     * 删除token
     */
    @GetMapping(value = "/loginOut/{token}")
    @ApiOperation(value = "退出登录",httpMethod = "GET")
    public Result<Oauth2TokenDto> loginOut(@PathVariable String token ) throws HttpRequestMethodNotSupportedException {
        token = token.replace("Bearer ", "");
        //通过clientId获取数据库已经有得token集合，如果等于传入得token，那么就删除
//        Collection<OAuth2AccessToken> tokens = jdbcTokenStore.findTokensByClientId(clientId);
//        tokens.forEach(token->jdbcTokenStore.removeAccessToken(token));
        //根据token删除
        boolean b = consumerTokenServices.revokeToken(token);
        if(b){
            //删除redis存储得token
            redisUtil.del(token);
            return Result.success("退出成功");
        }
        return Result.success("退出失败");
    }

    @RequestMapping("/verificationCode")
    @ApiOperation(value = "获取登录验证码",httpMethod = "GET")
    public Result<Map<String, Object>> generateVerificationCode() throws IOException {
        return Result.success(verificationCodeService.generateVerificationCode());
    }
}
