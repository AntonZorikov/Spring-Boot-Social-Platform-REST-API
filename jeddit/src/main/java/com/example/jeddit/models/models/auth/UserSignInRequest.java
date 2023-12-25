package com.example.jeddit.models.models.auth;

import lombok.Getter;

@Getter
public class UserSignInRequest {
    private String login;
    private String password;
}
