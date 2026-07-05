package com.inv.invmaster001.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    public String accessToken;
    public String refreshToken;

    public LoginResponse(String a, String r) {
        this.accessToken = a;
        this.refreshToken = r;
    }
}
