package com.example.chatting.app.service;

import com.example.chatting.app.dto.BoxchatDto;
import com.example.chatting.app.dto.MessageDto;
import com.example.chatting.app.dto.UserDto;
import com.example.chatting.app.form.GroupBoxchatForm;
import com.example.chatting.app.form.MessageForm;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.security.Principal;
import java.util.List;

public interface BoxchatService {
    BoxchatDto createPrivateChat(Principal connectedUser, Long userId);
    BoxchatDto createGroupChat(Principal connectedUser, GroupBoxchatForm boxchatForm);
    List<BoxchatDto> searchBoxchat(String query,Principal connectedUser);
    List<BoxchatDto> getListBoxChat(Principal connectedUser);
    List<UserDto> getListMemberOfBoxChat(Long id);
    BoxchatDto getBoxchatById(Principal connectedUser, Long boxchatId);

}
