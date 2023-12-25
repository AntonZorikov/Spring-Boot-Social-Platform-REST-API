package com.example.jeddit.models.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorModel {
    private int code;
    private String message;
    private String details;

    public ErrorModel(int code, String message, String details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }
}
