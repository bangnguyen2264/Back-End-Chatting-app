package com.example.chatting.app.customException.exception;

public class UnAcceptableException extends RuntimeException{
    public UnAcceptableException(String message){
        super(message);
    }
}
