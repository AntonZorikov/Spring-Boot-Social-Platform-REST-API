package com.example.jeddit.models.models.users;

import lombok.Getter;

@Getter
public class UserChangePasswordRequest {
    private String jwttoken;
    private String oldPassword;
    private String newPassword;

    @Override
    public String toString() {
        return "UserChangePasswordRequest{" +
                "JWTToken='" + jwttoken + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
