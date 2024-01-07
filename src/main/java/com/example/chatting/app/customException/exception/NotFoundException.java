package com.example.chatting.app.customException.exception;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String message){
        super(message);
    }
}
