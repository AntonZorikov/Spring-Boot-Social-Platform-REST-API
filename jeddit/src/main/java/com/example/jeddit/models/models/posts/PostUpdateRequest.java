package com.example.jeddit.models.models.posts;

import lombok.Getter;

@Getter
public class PostUpdateRequest {

    private String jwttoken;
    private String text;

}
