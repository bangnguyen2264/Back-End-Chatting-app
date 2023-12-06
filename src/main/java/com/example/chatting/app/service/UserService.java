package com.example.chatting.app.service;

import com.example.chatting.app.dto.UserDto;
import com.example.chatting.app.form.ChangeNameForm;
import com.example.chatting.app.form.ChangePasswordForm;
import com.example.chatting.app.model.Image;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface UserService {
    List<UserDto> searchUser(String query);
    UserDto getProfileUser(Principal connectedUser);
    UserDto getUserById(Long id);
    ResponseEntity<byte[]> getAvatar(Principal connectedUser) throws IOException;
    ResponseEntity<String> setAvatar(Principal connectedUser, MultipartFile file) throws IOException;
    ResponseEntity<String> changePassword(ChangePasswordForm request, Principal connectedUser);
    ResponseEntity<String> changeName(ChangeNameForm request, Principal connectedUser);
    ResponseEntity<String> deleteAccount(Principal connectedUser);
}
