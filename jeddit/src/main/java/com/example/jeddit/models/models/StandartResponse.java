package com.example.jeddit.models.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StandartResponse {

    private boolean error;
    private String message;

    public StandartResponse(String message) {
        this.message = message;
    }

    public StandartResponse(boolean error, String message) {
        this.error = error;
        this.message = message;
    }
}
