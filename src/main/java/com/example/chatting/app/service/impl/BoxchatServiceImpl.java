package com.example.chatting.app.service.impl;

import com.example.chatting.app.customException.exception.ForbiddenException;
import com.example.chatting.app.customException.exception.NotFoundException;
import com.example.chatting.app.customException.exception.UnAcceptableException;
import com.example.chatting.app.dto.BoxchatDto;
import com.example.chatting.app.dto.UserDto;
import com.example.chatting.app.form.GroupBoxchatForm;
import com.example.chatting.app.model.Boxchat;
import com.example.chatting.app.model.User;
import com.example.chatting.app.repository.BoxchatRepository;
import com.example.chatting.app.repository.UserRepository;
import com.example.chatting.app.service.BoxchatService;
import com.example.chatting.app.util.BoxchatUtility;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoxchatServiceImpl implements BoxchatService {
    private final UserRepository userRepository;
    private final BoxchatRepository boxchatRepository;
    private final ModelMapper modelMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    public BoxchatDto createPrivateChat(Principal connectedUser, Long userId) {
        User user1 = BoxchatUtility.getUserFromPrincipal(connectedUser);
        User user2 = userRepository.findById(userId).orElseThrow(
                ()-> new NotFoundException("User not found")
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
            for (User user : privateChat.getMembers()) {
                user.getBoxchats().add(privateChat);
                userRepository.save(user);
            }
            return modelMapper.map(privateChat,BoxchatDto.class);
        }
    }

    @Override
    public BoxchatDto createGroupChat(Principal connectedUser, GroupBoxchatForm boxchatForm) {
        User creator = BoxchatUtility.getUserFromPrincipal(connectedUser);
        List<User> members = boxchatForm.getMembersId()
                .stream()
                .map(userId -> userRepository.findById(userId).orElseThrow(
                        () -> new NotFoundException("User not found with ID: " + userId)
                ))
                .collect(Collectors.toList());

        if (members.size() < 2) {
            throw new UnAcceptableException("A group chat must have at least three members.");
        }

        members.add(0, creator);

        Boxchat groupChat = Boxchat.builder()
                .name(boxchatForm.getName())
                .creator(creator.getUsername())
                .members(members)
                .build();

        // Save the group chat to the database
        boxchatRepository.save(groupChat);

        return modelMapper.map(groupChat, BoxchatDto.class);

    }

    @Override
    public List<BoxchatDto> searchBoxchat(String query, Principal connectedUser) {
        User user = BoxchatUtility.getUserFromPrincipal(connectedUser);
        List<Boxchat> userBoxchats = boxchatRepository.searchBoxchat(query);
        List<Boxchat> filteredBoxchats = BoxchatUtility.filterBoxchatsByMember(user,userBoxchats);
        return filteredBoxchats.stream()
                .map(boxchat -> modelMapper.map(boxchat, BoxchatDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<BoxchatDto> getListBoxChat(Principal connectedUser) {
        User user = BoxchatUtility.getUserFromPrincipal(connectedUser);
        List<Boxchat> userBoxchats = boxchatRepository.findByMembersId(user.getId());

        return userBoxchats.stream()
                .map(boxchat -> modelMapper.map(boxchat, BoxchatDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getListMemberOfBoxChat(Long id){
        Boxchat boxchat = boxchatRepository.findById(id).orElseThrow();
        return boxchat.getMembers().stream().map(
                member -> modelMapper.map(member,UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public BoxchatDto getBoxchatById(Principal connectedUser, Long boxchatId) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        Boxchat boxchat = boxchatRepository.findByIdAndMembersId(boxchatId, user.getId())
                .orElseThrow(() -> new ForbiddenException("User is not a member of this chat"));

        return modelMapper.map(boxchat, BoxchatDto.class);
    }


}
