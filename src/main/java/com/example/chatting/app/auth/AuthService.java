package com.example.chatting.app.auth;

import com.example.chatting.app.customException.exception.IsPresentException;
import com.example.chatting.app.dto.AuthResponseDto;
import com.example.chatting.app.enums.TokenType;
import com.example.chatting.app.form.SignInForm;
import com.example.chatting.app.form.SignUpForm;
import com.example.chatting.app.model.Token;
import com.example.chatting.app.model.User;
import com.example.chatting.app.repository.TokenRepository;
import com.example.chatting.app.repository.UserRepository;
import com.example.chatting.app.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public ResponseEntity<String> SignUp(SignUpForm request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .dob(request.getDob())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        if (repository.findByEmail(request.getEmail()).isPresent() ||
                repository.findByUsername(request.getUsername()).isPresent()) {
            throw new IsPresentException("Email or Username already registered");
        }
        repository.save(user);
        return ResponseEntity.ok().body("Register complete");
    }

    public AuthResponseDto SignIn(SignInForm signInForm) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInForm.getUsername(),
                        signInForm.getPassword()
                )
        );
        var user = repository.findByUsername(signInForm.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return
                AuthResponseDto.builder()
                        .accessToken(jwtToken)
                        .refreshToken(refreshToken)
                        .build();

    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userName;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userName = jwtService.extractUsername(refreshToken);
        if (userName != null) {
            var user = this.repository.findByUsername(userName)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, (UserDetails) user)) {
                var accessToken = jwtService.generateToken((UserDetails) user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthResponseDto.builder()
                        .accessToken(accessToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }


    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }


}
