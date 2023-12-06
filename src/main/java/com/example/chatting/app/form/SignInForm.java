package com.example.chatting.app.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignInForm {
    @NotEmpty(message = "username must not be empty")
    private String username;
    @NotEmpty(message = "The password must not be empty")
    @Size(min = 8, message = "The password must have at least 8 characters")
    private String password;
}