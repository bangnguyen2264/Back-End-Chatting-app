package com.example.chatting.app.controller;

import com.example.chatting.app.dto.BoxchatDto;
import com.example.chatting.app.dto.MessageDto;
import com.example.chatting.app.dto.UserDto;
import com.example.chatting.app.form.GroupBoxchatForm;
import com.example.chatting.app.form.MessageForm;
import com.example.chatting.app.service.impl.BoxchatServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/v1/boxchat")
@RequiredArgsConstructor
public class BoxchatController {
    private final BoxchatServiceImpl boxchatService;
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
    @GetMapping("/search")
    public List<BoxchatDto> searchBoxchat(@RequestParam(name = "query")String query, Principal connectedUser){
        return boxchatService.searchBoxchat(query,connectedUser);
    }
    @GetMapping
    public List<BoxchatDto> getListBoxChat(Principal connectedUser){
        return boxchatService.getListBoxChat(connectedUser);
    }
    @GetMapping("{boxchatId}/members")
    public List<UserDto> getListMemberOfBoxChat(@PathVariable(name = "boxchatId") Long id){
        return boxchatService.getListMemberOfBoxChat(id);
    }
    @GetMapping("{boxchatId}")
    public BoxchatDto getBoxchatById(Principal connectedUser,@PathVariable(name = "boxchatId") Long boxchatId){
        return getBoxchatById(connectedUser,boxchatId);
    }

}
