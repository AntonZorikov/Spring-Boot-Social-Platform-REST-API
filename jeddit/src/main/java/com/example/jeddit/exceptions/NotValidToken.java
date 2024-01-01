package com.example.jeddit.exceptions;

public class NotValidToken extends Exception {
    public NotValidToken(String message) {
        super(message);
    }

    public NotValidToken() {
    }
}
