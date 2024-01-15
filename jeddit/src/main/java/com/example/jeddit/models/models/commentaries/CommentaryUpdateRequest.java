package com.example.jeddit.models.models.commentaries;

import lombok.Getter;

@Getter
public class CommentaryUpdateRequest {

    private String jwttoken;
    private String text;

}
