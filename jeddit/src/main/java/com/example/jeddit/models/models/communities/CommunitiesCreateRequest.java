package com.example.jeddit.models.models.communities;

import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class CommunitiesCreateRequest {

    private String title;
    private String description;
    private String jwttoken;

}
