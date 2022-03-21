package com.zx.fss.domain;

import lombok.Data;

@Data
public class LoginDTO {
    String grant_type;
    String client_id;
    String client_secret;
    String username;
    String password;
    String code;
    String uuid;
}
