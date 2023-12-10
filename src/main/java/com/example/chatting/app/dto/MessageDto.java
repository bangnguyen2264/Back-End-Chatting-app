package com.example.chatting.app.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class MessageDto {
    private String sender;
    private String content;
    private LocalDateTime createTime;
    private boolean isRead ;

}
