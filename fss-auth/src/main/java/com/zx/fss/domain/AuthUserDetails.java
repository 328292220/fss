package com.zx.fss.domain;

import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class AuthUserDetails extends User {
    com.zx.fss.account.User user;
    public AuthUserDetails(com.zx.fss.account.User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getUserName(), user.getPassword(), authorities);
        this.user = user;
    }
}
