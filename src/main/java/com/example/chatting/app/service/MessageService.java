package com.example.chatting.app.service;

import com.example.chatting.app.dto.MessageDto;
import com.example.chatting.app.form.MessageForm;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.security.Principal;
import java.util.List;

public interface MessageService {
    MessageDto sendMessage(Long boxchatId, MessageForm message, Principal connectedUser);
    void deleteMessage(Long boxchatId, Long messageId,Principal connectedUser);
    List<MessageDto> getMessage(Principal connectedUser, Long boxchatId);
    public MessageDto addUser(MessageForm message,
                              SimpMessageHeaderAccessor headerAccessorLong,
                              Long chatId,
                              Long userId);
}
