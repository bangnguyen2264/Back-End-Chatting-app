package com.example.chatting.app.controller;

import com.example.chatting.app.dto.BoxchatDto;
import com.example.chatting.app.dto.MessageDto;
import com.example.chatting.app.form.GroupBoxchatForm;
import com.example.chatting.app.form.MessageForm;
import com.example.chatting.app.service.impl.BoxchatServiceImpl;
import com.example.chatting.app.service.impl.MessageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/api/v1/boxchat")
@RequiredArgsConstructor
public class ChatCotroller {
    private final MessageServiceImpl messageService;
    private final BoxchatServiceImpl boxchatService;
    @MessageMapping("/chat/{boxchatId}")
    public MessageDto sendMessage(@PathVariable Long boxchatId, @Payload MessageForm message, Principal connectedUser) {
        return messageService.sendMessage(boxchatId, message,connectedUser);
    }
    @PostMapping("/withuser/{userId}")
    public BoxchatDto createPrivateChat(
            Principal connectedUser,
            @PathVariable Long userId) {
        return  boxchatService.createPrivateChat(connectedUser, userId);

    }

    @PostMapping("/group")
    public BoxchatDto createGroupChat(
            Principal connectedUser,
            @RequestBody GroupBoxchatForm boxchatForm) {
        return boxchatService.createGroupChat(connectedUser, boxchatForm);

    }

}
