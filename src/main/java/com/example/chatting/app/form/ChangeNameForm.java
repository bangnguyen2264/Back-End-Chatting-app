package com.example.chatting.app.form;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ChangeNameForm {
    @NotEmpty(message = "First name must not be empty ")
    private String firtsname;
    @NotEmpty(message = "Last name must not be empty ")
    private String lastname;
}
