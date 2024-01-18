package com.example.jeddit.models.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JWTTokenRequest {

    private String jwttoken;

    public JWTTokenRequest(String jwttoken) {
        this.jwttoken = jwttoken;
    }
}
