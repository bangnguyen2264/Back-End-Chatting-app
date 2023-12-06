package com.example.chatting.app.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePasswordForm {
    @NotEmpty(message = "The password must not be empty")
    @Size(min = 8, message = "The password must have at least 8 characters")
    private String currentPassword;
    @NotEmpty(message = "The password must not be empty")
    @Size(min = 8, message = "The password must have at least 8 characters")
    private String newPassword;
    @NotEmpty(message = "The password must not be empty")
    @Size(min = 8, message = "The password must have at least 8 characters")
    private String confirmationPassword;
}
