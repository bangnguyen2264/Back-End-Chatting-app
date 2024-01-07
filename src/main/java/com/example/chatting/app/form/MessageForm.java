package com.example.chatting.app.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MessageForm {
    @NotEmpty(message = "Content must required")
    private String content;
}
