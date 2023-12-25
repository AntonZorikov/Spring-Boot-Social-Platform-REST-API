package com.example.jeddit.models.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StandardResponse {

    private boolean success;
    private ErrorModel error;
    private String message;

    public StandardResponse(String message) {
        this.message = message;
    }

    public StandardResponse(boolean success, ErrorModel error, String message) {
        this.success = success;
        this.error = error;
        this.message = message;
    }

    public StandardResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
