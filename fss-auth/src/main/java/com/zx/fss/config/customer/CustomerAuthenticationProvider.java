package com.zx.fss.config.customer;

import com.zx.fss.constant.MessageConstant;
import com.zx.fss.exception.AuthException;
import com.zx.fss.utils.RSAEncrypt;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.KeyPair;


/**
 * @className: CustomerAuthenticationProvider
 * @description: 自定义密码登录Provider
 * @author: pengju.ma
 * @date: 2021/6/21 14:01
 **/
@Slf4j

public class CustomerAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    private PasswordEncoder passwordEncoder;

    private KeyPair keyPair;

    @ConditionalOnBean({UserDetailsService.class})
    @Autowired
    @Lazy
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    @ConditionalOnBean({PasswordEncoder.class})
    @Autowired
    @Lazy
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    @ConditionalOnBean({KeyPair.class})
    @Autowired
    @Lazy
    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }




    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("This is Customer Password Authentication Provider Process ...");
        String grantType = getRequest().getParameter("grant_type");
        //代码逻辑处理处会循环调用provider 直到遇到能处理的provider类，典型的责任链模式
        if(!"my_password".equalsIgnoreCase(grantType)){
            return null;
        }
        Object principal = authentication.getPrincipal();
        String account = (String) principal;
        String privateKey = Base64.encodeBase64String(keyPair.getPrivate().getEncoded());
        String password = null;
        String inPassword = (String) authentication.getCredentials();
        try {
            //解密请求密码（前端用户用JWT公钥和RSA加密）
            password = RSAEncrypt.decrypt(inPassword, privateKey);
        } catch (Exception e) {
            log.error("使用RSA解密传入参数出错啦{}", e.getLocalizedMessage());
            throw new AuthException(AuthException.MSG_RSA_ERROR);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(account);
        //校验密码
        // passwordEncoder使用的是BCryptPasswordEncoder对象，所以数据库用户表密码也要用这个对象加密存储
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
        }
        //返回自定义的token
        return new CustomerAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomerAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public static HttpServletRequest getRequest(){
        ServletRequestAttributes ra= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert ra != null;
        return ra.getRequest();
    }


}
