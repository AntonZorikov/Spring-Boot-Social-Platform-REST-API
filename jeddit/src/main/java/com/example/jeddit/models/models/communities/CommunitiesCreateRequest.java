package com.example.jeddit.models.models.communities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommunitiesCreateRequest {
    private String title;
    private String description;
    private String jwttoken;

    public CommunitiesCreateRequest(String title, String description, String jwttoken) {
        this.title = title;
        this.description = description;
        this.jwttoken = jwttoken;
    }
}
