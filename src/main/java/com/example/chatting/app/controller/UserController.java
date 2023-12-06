package com.example.chatting.app.controller;

import com.example.chatting.app.dto.UserDto;
import com.example.chatting.app.form.ChangeNameForm;
import com.example.chatting.app.form.ChangePasswordForm;
import com.example.chatting.app.model.Image;
import com.example.chatting.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/search")
    public List<UserDto> searchUser(@RequestParam(name = "query") String query) {
        return userService.searchUser(query);
    }

    @GetMapping("/profiles")
    public UserDto getProfileUser(Principal connectedUser) {
        return userService.getProfileUser(connectedUser);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);

    }
    @GetMapping("/avatar")
    public ResponseEntity<byte[]> getAvatar(Principal connectedUser) throws IOException{
        return userService.getAvatar(connectedUser);
    }

    @PostMapping("/avatar")
    public ResponseEntity<String> setAvatar(Principal connectedUser,
                                            @RequestPart("image") MultipartFile file) throws IOException{
        return userService.setAvatar(connectedUser, file);

    }

    @PatchMapping("/change-name")
    public ResponseEntity<String> changeName(@RequestBody ChangeNameForm request, Principal connectedUser) {
        ResponseEntity<String> response = userService.changeName(request, connectedUser);
        return ResponseEntity.ok(response.getBody());
    }

    @PatchMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordForm request, Principal connectedUser) {
        return userService.changePassword(request, connectedUser);
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<String> deleteAccount(Principal connectedUser) {
        return userService.deleteAccount(connectedUser);
    }
}
