package com.example.jeddit.models.models.auth;

import lombok.Getter;

@Getter
public class UserRegistrationRequest {
    private String login;
    private String password;
    private String email;
}
