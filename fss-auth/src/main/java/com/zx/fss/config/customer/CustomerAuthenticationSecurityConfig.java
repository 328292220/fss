package com.zx.fss.config.customer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
@Slf4j
@Component
@AllArgsConstructor
public class CustomerAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final KeyPair keyPair;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        log.info("This is CustomerAuthenticationSecurityConfig configure");
        CustomerAuthenticationProvider provider = new CustomerAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        provider.setKeyPair(keyPair);
        //添加自定义provider
        http.authenticationProvider(provider);
    }
}
