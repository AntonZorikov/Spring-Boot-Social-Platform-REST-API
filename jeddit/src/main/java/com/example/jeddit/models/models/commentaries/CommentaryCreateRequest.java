package com.example.jeddit.models.models.commentaries;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
public class CommentaryCreateRequest {

    private String jwttoken;
    private String text;

}
