package com.example.jeddit.models.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentaryCreateRequest {

    private String jwttoken;
    private String text;

}
