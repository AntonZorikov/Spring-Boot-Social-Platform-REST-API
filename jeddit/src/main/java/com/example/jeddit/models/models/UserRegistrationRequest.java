package com.example.jeddit.models.models;

import lombok.Getter;

@Getter
public class UserRegistrationRequest {
    private String login;
    private String password;
    private String email;
}
