package com.example.jeddit.models.models.vote;

import com.example.jeddit.models.entitys.Vote;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteGetResponse {
    private long user_id;
    private long post_id;
    private long value;

    public VoteGetResponse(Vote vote) {
        this.user_id = vote.getUser().getId();
        this.post_id = vote.getPost().getId();
        this.value = vote.getValue();
    }
}
