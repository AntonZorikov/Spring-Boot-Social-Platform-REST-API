package com.example.jeddit.models.models.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSignInRequest {
    private String login;
    private String password;

    public UserSignInRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
