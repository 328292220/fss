package com.zx.fss.component;

import com.zx.fss.account.User;
import com.zx.fss.domain.AuthUserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * JWT内容增强器
 * Created by macro on 2020/6/19.
 * 如果你想往JWT中添加自定义信息的话，比如说登录用户的ID，可以自己实现TokenEnhancer接口
 */
@Component
public class JwtTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        AuthUserDetails authUserDetails = (AuthUserDetails) authentication.getPrincipal();
        User user = authUserDetails.getUser();
        Map<String, Object> info = new HashMap<>();
        //把用户ID设置到JWT中
        info.put("userId", user.getUserId());
        info.put("userName", user.getUserName());
        info.put("password", user.getPassword());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        return accessToken;
    }
}
