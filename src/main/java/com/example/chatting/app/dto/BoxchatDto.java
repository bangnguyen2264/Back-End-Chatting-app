package com.example.chatting.app.dto;

import com.example.chatting.app.model.Message;
import com.example.chatting.app.model.User;
import lombok.Data;

import java.util.List;
@Data
public class BoxchatDto {
    private String name;
    private List<Message> messages;

}
