package com.example.chatting.app.auth;

import com.example.chatting.app.dto.AuthResponseDto;
import com.example.chatting.app.form.SignInForm;
import com.example.chatting.app.form.SignUpForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/sign-up")
    public ResponseEntity<String> SignUp(
            @Valid @RequestBody SignUpForm request
    ) {
        return service.SignUp(request);
    }
    @PostMapping("/sign-in")
    public AuthResponseDto SignIn(
            @Valid @RequestBody SignInForm request
    ) {
        return service.SignIn(request);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }


}
