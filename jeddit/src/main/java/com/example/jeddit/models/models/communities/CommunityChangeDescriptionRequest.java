package com.example.jeddit.models.models.communities;

import lombok.Getter;

@Getter
public class CommunityChangeDescriptionRequest {

    private String jwttoken;
    private String newDescription;

}
