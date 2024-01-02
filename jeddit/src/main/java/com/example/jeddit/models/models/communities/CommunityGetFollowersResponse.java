package com.example.jeddit.models.models.communities;

import com.example.jeddit.models.entitys.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CommunityGetFollowersResponse {
    private String community;
    private List<User> users;

    public CommunityGetFollowersResponse(String community, List<User> users) {
        this.community = community;
        this.users = users;
    }
}
