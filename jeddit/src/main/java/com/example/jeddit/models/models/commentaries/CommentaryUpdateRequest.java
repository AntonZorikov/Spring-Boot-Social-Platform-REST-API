package com.example.jeddit.models.models.commentaries;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentaryUpdateRequest {

    private String jwttoken;
    private String text;

    public CommentaryUpdateRequest(String jwttoken, String text) {
        this.jwttoken = jwttoken;
        this.text = text;
    }
}
