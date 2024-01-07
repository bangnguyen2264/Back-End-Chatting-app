package com.example.chatting.app.controller;

import com.example.chatting.app.dto.BoxchatDto;
import com.example.chatting.app.dto.MessageDto;
import com.example.chatting.app.form.GroupBoxchatForm;
import com.example.chatting.app.form.MessageForm;
import com.example.chatting.app.service.impl.BoxchatServiceImpl;
import com.example.chatting.app.service.impl.MessageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MessageCotroller {
    private final MessageServiceImpl messageService;

    @MessageMapping("/chat.sendMessage/{boxchatId}")
    @SendTo("/topic/boxchat/{boxchatId}")
    public MessageDto sendMessage(@DestinationVariable Long boxchatId, @Payload MessageForm message, Principal connectedUser) {
        return messageService.sendMessage(boxchatId, message, connectedUser);
    }
    @MessageMapping("/chat.addUser/{boxchatId}/{userId}")
    @SendTo("/topic/boxchat/{boxchatId}")
    public MessageDto addUser(@Payload MessageForm message,
                              SimpMessageHeaderAccessor headerAccessorLong,
                              @DestinationVariable Long chatId,
                              @DestinationVariable Long userId){
        return messageService.addUser(message,headerAccessorLong,chatId,userId);
    }


}
