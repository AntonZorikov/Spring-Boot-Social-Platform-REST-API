package com.example.jeddit.models.models.users;

import lombok.Getter;

@Getter
public class UserChangePasswordRequest {

    private String jwttoken;
    private String oldPassword;
    private String newPassword;

}
