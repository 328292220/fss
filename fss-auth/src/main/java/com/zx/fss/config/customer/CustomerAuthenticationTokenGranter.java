package com.zx.fss.config.customer;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 自定义的密码授权模式
 */
public class CustomerAuthenticationTokenGranter extends AbstractTokenGranter {
    private final AuthenticationManager authenticationManager;
    public CustomerAuthenticationTokenGranter(AuthenticationManager authenticationManager,
                                              AuthorizationServerTokenServices tokenServices,
                                              ClientDetailsService clientDetailsService,
                                              OAuth2RequestFactory requestFactory,
                                              String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        String account = parameters.get("username");
        String password = parameters.get("password");
        //自定义授权token
        Authentication userAuth = new CustomerAuthenticationToken(account, password);
        ((AbstractAuthenticationToken)userAuth).setDetails(parameters);

        try {
            //对自定义的参数校验
            userAuth = authenticationManager.authenticate(userAuth);
        }catch (Exception e) {
            throw new InvalidGrantException("验证出错: " + e.getMessage());
        }

        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException("Could not authenticate account: " + account);
        }
        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}
