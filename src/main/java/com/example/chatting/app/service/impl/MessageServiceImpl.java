package com.example.chatting.app.service.impl;

import com.example.chatting.app.dto.MessageDto;
import com.example.chatting.app.form.MessageForm;
import com.example.chatting.app.model.Boxchat;
import com.example.chatting.app.model.Message;
import com.example.chatting.app.model.User;
import com.example.chatting.app.repository.BoxchatRepository;
import com.example.chatting.app.repository.MessageRepository;
import com.example.chatting.app.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;
    private final BoxchatRepository boxchatRepository;
    private final ModelMapper modelMapper;

    public MessageDto sendMessage(Long boxchatId, MessageForm message, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Boxchat boxchat = boxchatRepository.findById(boxchatId).orElseThrow();

        Message message1 = new Message();
        message1.setSender(user.getUsername());
        message1.setContent(message.getContent());
        message1.setBoxchat(boxchat);


        messagingTemplate.convertAndSend("/topic/boxchat/" + boxchatId, message);
        messageRepository.save(message1);
        return modelMapper.map(message1,MessageDto.class);
    }


}
