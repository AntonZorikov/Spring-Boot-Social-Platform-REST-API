package com.example.jeddit.models.models.posts;

import lombok.Getter;

@Getter
public class PostCreateRequest {

    private String jwttoken;
    private String title;
    private String text;
    private String communityTitle;

}
