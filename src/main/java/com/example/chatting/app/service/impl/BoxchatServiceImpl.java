package com.example.chatting.app.service.impl;

import com.example.chatting.app.customException.exception.ChatNotFoundException;
import com.example.chatting.app.customException.exception.UserNotFoundException;
import com.example.chatting.app.dto.BoxchatDto;
import com.example.chatting.app.dto.UserDto;
import com.example.chatting.app.form.GroupBoxchatForm;
import com.example.chatting.app.model.Boxchat;
import com.example.chatting.app.model.User;
import com.example.chatting.app.repository.BoxchatRepository;
import com.example.chatting.app.repository.UserRepository;
import com.example.chatting.app.service.BoxchatService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        if (existingPrivateChat.isPresent() && existingPrivateChat.get().getMembers().size()==2) {
            return modelMapper.map(existingPrivateChat,BoxchatDto.class);
        } else {
            Boxchat privateChat = Boxchat.builder()
                    .name(user2.getUsername())
                    .creator(user1.getUsername())
                    .members(new ArrayList<>(List.of(user1, user2)))
                    .build();
            boxchatRepository.save(privateChat);
            return modelMapper.map(privateChat,BoxchatDto.class);
        }
    }

    @Override
    public BoxchatDto createGroupChat(Principal connectedUser, GroupBoxchatForm boxchatForm) {
        User creator = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // You might want to perform additional validation or checks here

        // Check if there are exactly three members
        List<User> members = boxchatForm.getMembers()
                .stream().map(member -> modelMapper.map(member,User.class))
                .collect(Collectors.toList());
        if (members == null || members.size() != 2) {
            throw new IllegalArgumentException("A group chat must have exactly three members.");
        }

        Boxchat groupChat = Boxchat.builder()
                .name(boxchatForm.getName())
                .creator(creator.getUsername())
                .members(new ArrayList<>(List.of(creator, members.get(0), members.get(1))))
                .build();

        // Save the group chat to the database
        boxchatRepository.save(groupChat);

        return modelMapper.map(groupChat, BoxchatDto.class);
    }
    public List<UserDto> getListMemberOfBoxChat(Long id){
        Boxchat boxchat = boxchatRepository.findById(id).orElseThrow();
        return boxchat.getMembers().stream().map(
                member -> modelMapper.map(member,UserDto.class))
                .collect(Collectors.toList());
    }
    public BoxchatDto addUser(Long chatId, Long userId) {
        Boxchat existingChat = boxchatRepository.findById(chatId).orElseThrow(
                () -> new ChatNotFoundException("Chat not found")
        );

        User userToAdd = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );

        if (existingChat.getMembers().contains(userToAdd)) {
            throw new IllegalArgumentException("User is already a member of the chat");
        }

        existingChat.getMembers().add(userToAdd);
        boxchatRepository.save(existingChat);


        return modelMapper.map(existingChat, BoxchatDto.class);
    }


}
