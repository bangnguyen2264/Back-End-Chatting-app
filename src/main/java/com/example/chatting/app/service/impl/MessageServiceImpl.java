package com.example.chatting.app.service.impl;

import com.example.chatting.app.model.Message;
import com.example.chatting.app.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final SimpMessagingTemplate messagingTemplate;

    public Message sendMessage(Message message) {
        // Process the message if needed
        return message;
    }

    public Message addUser(Message chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());

        messagingTemplate.convertAndSend("/topic/public", chatMessage);

        return chatMessage;
    }
}
