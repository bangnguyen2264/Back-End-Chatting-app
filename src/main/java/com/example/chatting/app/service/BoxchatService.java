package com.example.chatting.app.service;

import com.example.chatting.app.dto.BoxchatDto;
import com.example.chatting.app.form.BoxchatForm;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface BoxchatService {
    BoxchatDto createPrivateChat(Principal connectedUser, Long userId);
    BoxchatDto createGroupChat(Principal connectedUser, BoxchatForm boxchatForm);

}
