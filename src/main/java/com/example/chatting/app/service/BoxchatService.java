package com.example.chatting.app.service;

import com.example.chatting.app.dto.BoxchatDto;
import com.example.chatting.app.form.GroupBoxchatForm;

import java.security.Principal;

public interface BoxchatService {
    BoxchatDto createPrivateChat(Principal connectedUser, Long userId);
    BoxchatDto createGroupChat(Principal connectedUser, GroupBoxchatForm boxchatForm);

}
