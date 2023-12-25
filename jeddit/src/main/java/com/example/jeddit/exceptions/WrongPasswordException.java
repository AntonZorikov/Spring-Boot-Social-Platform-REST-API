package com.example.jeddit.exceptions;

public class WrongPasswordException extends Exception{
    public WrongPasswordException(String message) {
        super(message);
    }
}
