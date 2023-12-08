package com.example.chatting.app.service.impl;

import com.example.chatting.app.customException.exception.UserNotFoundException;
import com.example.chatting.app.dto.BoxchatDto;
import com.example.chatting.app.form.BoxchatForm;
import com.example.chatting.app.model.Boxchat;
import com.example.chatting.app.model.User;
import com.example.chatting.app.repository.BoxchatRepository;
import com.example.chatting.app.repository.UserRepository;
import com.example.chatting.app.service.BoxchatService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoxchatServiceImpl implements BoxchatService {
    private final UserRepository userRepository;
    private final BoxchatRepository boxchatRepository;
    private final ModelMapper modelMapper;
    @Override
    public BoxchatDto createPrivateChat(Principal connectedUser, Long userId) {
        User user1 = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        User user2 = userRepository.findById(userId).orElseThrow(
                ()-> new UserNotFoundException("User not found")
        );

        Optional<Boxchat> existingPrivateChat = boxchatRepository.findPrivateChat(user1, user2);

        if (existingPrivateChat.isPresent()) {
            return modelMapper.map(existingPrivateChat,BoxchatDto.class);
        } else {
            Boxchat privateChat = Boxchat.builder()
                    .name(user2.getUsername())
                    .creator(user1.getUsername())
                    .createTime(LocalDate.now())
                    .updateTime(LocalDate.now())
                    .members(new ArrayList<>(List.of(user1, user2)))
                    .build();
            boxchatRepository.save(privateChat);
            return modelMapper.map(privateChat,BoxchatDto.class);
        }
    }

    @Override
    public BoxchatDto createGroupChat(Principal connectedUser, BoxchatForm boxchatForm) {
        return null;
    }

}
