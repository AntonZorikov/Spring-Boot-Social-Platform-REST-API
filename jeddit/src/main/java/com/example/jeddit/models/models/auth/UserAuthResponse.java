package com.example.jeddit.models.models.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserAuthResponse {
    private String JWTToken;

    public UserAuthResponse(String JWTToken) {
        this.JWTToken = JWTToken;
    }
}
