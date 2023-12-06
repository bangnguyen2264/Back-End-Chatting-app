package com.example.chatting.app.service.impl;

import com.example.chatting.app.customException.exception.UserNotFoundException;
import com.example.chatting.app.dto.UserDto;
import com.example.chatting.app.form.ChangeNameForm;
import com.example.chatting.app.form.ChangePasswordForm;
import com.example.chatting.app.model.Image;
import com.example.chatting.app.model.User;
import com.example.chatting.app.repository.ImageRepository;
import com.example.chatting.app.repository.UserRepository;
import com.example.chatting.app.service.UserService;
import com.example.chatting.app.util.ImageUtility;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@CrossOrigin()
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelmapper;

    @Override
    public List<UserDto> searchUser(String query) {
        return userRepository.searchUser(query)
                .stream()
                .map(user -> modelmapper.map(user,UserDto.class)).collect(Collectors.toList());
    }

    @Override
    public UserDto getProfileUser(Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return modelmapper.map(user,UserDto.class);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->  new UserNotFoundException("User not exist"));
        return modelmapper.map(user,UserDto.class) ;
    }

    @Override
    public ResponseEntity<byte[]> getAvatar(Principal connectedUser) throws IOException {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        final Optional<Image> dbImage = imageRepository.findByUser(user);
        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(dbImage.get().getType()))
                .body(ImageUtility.decompressImage(dbImage.get().getData()));
    }

    @Override
    public ResponseEntity<String> setAvatar(Principal connectedUser,
                                            MultipartFile file) throws IOException {
        imageRepository.save(Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .data(ImageUtility.compressImage(file.getBytes())).build());
        return ResponseEntity.ok("Avatar updated successfully");
    }

    @Override
    public ResponseEntity<String> changeName(ChangeNameForm request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        user.setFirstname(request.getFirtsname());
        user.setLastname(request.getLastname());
        userRepository.save(user);
        return ResponseEntity.ok().body("Change name complete");
    }

    @Override
    public ResponseEntity<String> changePassword(ChangePasswordForm request, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        userRepository.save(user);
        return ResponseEntity.ok().body("Change password complete");
    }

    @Override
        public ResponseEntity<String> deleteAccount(Principal connectedUser) {
        return null;
    }
}