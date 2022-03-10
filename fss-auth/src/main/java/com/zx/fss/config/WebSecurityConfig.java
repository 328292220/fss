package com.zx.fss.config;

import com.zx.fss.config.customer.CustomerAuthenticationSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SpringSecurity配置
 * Created by macro on 2020/6/19.
 * 配置Spring Security，允许获取公钥接口的访问
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    IgnoreUrlsProperties ignoreUrlsProperties;

    @Lazy
    @Autowired
    private CustomerAuthenticationSecurityConfig customerAuthenticationSecurityConfig;

    /**
     * 配置SpringSecurity相关信息
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
//                .antMatchers("/rsa/publicKey","/oauth/verificationCode","/common/download/**").permitAll()
                .antMatchers(ignoreUrlsProperties.getUrls()).permitAll()
                .anyRequest().authenticated()
                .and()
                //这里添加一个配置，本项目目的是为了添加一个自定义provider
                .apply(customerAuthenticationSecurityConfig);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        //也可以在这里添加一个自定义provider
        //auth.authenticationProvider("自定义一个provider");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
