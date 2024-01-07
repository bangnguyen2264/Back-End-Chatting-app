package com.example.chatting.app.service.impl;

import com.example.chatting.app.customException.exception.ForbiddenException;
import com.example.chatting.app.customException.exception.NotFoundException;
import com.example.chatting.app.dto.MessageDto;
import com.example.chatting.app.dto.UserDto;
import com.example.chatting.app.form.MessageForm;
import com.example.chatting.app.model.Boxchat;
import com.example.chatting.app.model.Message;
import com.example.chatting.app.model.User;
import com.example.chatting.app.repository.BoxchatRepository;
import com.example.chatting.app.repository.MessageRepository;
import com.example.chatting.app.repository.UserRepository;
import com.example.chatting.app.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final BoxchatRepository boxchatRepository;
    private final ModelMapper modelMapper;
    @Override
    public MessageDto sendMessage(Long boxchatId, MessageForm message, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Boxchat boxchat = boxchatRepository.findById(boxchatId).orElseThrow();
        Message message1 = modelMapper.map(message,Message.class);
        message1.setSender(user.getUsername());
        message1.setBoxchat(boxchat);

        messagingTemplate.convertAndSend("/topic/boxchat/" + boxchatId, message);
        messageRepository.save(message1);
        return modelMapper.map(message1,MessageDto.class);
    }
    @Override
    public void deleteMessage(Long boxchatId, Long messageId,Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        Boxchat boxchat = boxchatRepository.findById(boxchatId)
                .filter(chat -> chat.getMembers().contains(user))
                .orElseThrow(() -> new NotFoundException("User is not a member of the specified box chat"));
        Message message = messageRepository.findById(messageId)
                .filter(msg -> msg.getSender().equals(user.getUsername()) && msg.getBoxchat().equals(boxchat))
                .orElseThrow(() -> new NotFoundException("Message does not belong to the user in the specified box chat"));

        messageRepository.delete(message);
    }

    @Override
    public List<MessageDto> getMessage(Principal connectedUser, Long boxchatId) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        Boxchat boxchat = boxchatRepository.findById(boxchatId)
                .filter(chat -> chat.getMembers().contains(user))
                .orElseThrow(() -> new ForbiddenException("User is not a member of the specified box chat"));
        return boxchat.getMessages()
                .stream()
                .map(msg -> modelMapper.map(msg, MessageDto.class)).collect(Collectors.toList());
    }
    @Override
    public MessageDto addUser(MessageForm message,
                              SimpMessageHeaderAccessor headerAccessorLong,
                              Long chatId,
                              Long userId) {
        Boxchat existingChat = boxchatRepository.findById(chatId).orElseThrow(
                () -> new NotFoundException("Chat not found")
        );

        User userToAdd = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        if (existingChat.getMembers().contains(userToAdd)) {
            throw new IllegalArgumentException("User is already a member of the chat");
        }

        existingChat.getMembers().add(userToAdd);
        boxchatRepository.save(existingChat);
        String notificationMessage = userToAdd.getUsername() + " has joined the chat.";
        messagingTemplate.convertAndSend("/topic/boxchat/" + chatId, notificationMessage);

        return modelMapper.map(notificationMessage, MessageDto.class);
    }


}
