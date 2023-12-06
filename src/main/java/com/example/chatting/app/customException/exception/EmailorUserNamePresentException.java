package com.example.chatting.app.customException.exception;

public class EmailorUserNamePresentException extends RuntimeException {
    public EmailorUserNamePresentException(String message) {
        super(message);
    }
}
