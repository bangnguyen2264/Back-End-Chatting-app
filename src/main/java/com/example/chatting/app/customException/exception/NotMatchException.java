package com.example.chatting.app.customException.exception;

public class NotMatchException extends RuntimeException{
    public NotMatchException(String message){
        super(message);
    }
}
