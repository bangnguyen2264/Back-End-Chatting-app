package com.example.chatting.app.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpForm {
    @NotEmpty(message = "The first name must not be empty")
    @Size(min = 2, message = "The first name must have at least 2 characters")
    private String firstname;
    @NotEmpty(message = "The last name must not be empty")
    @Size(min = 2, message = "The last name must have at least 2 characters")
    private String lastname;
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Past(message = "Invalid date of birth ")
    private LocalDate dob;
    @NotEmpty(message = "The last name must not be empty")
    @Size(min = 2, message = "The  username must have at least 2 characters")
    private String username;
    @NotEmpty(message = "The email must not be empty")
    @Email(message = "Invalid email address")
    private String email;
    @NotEmpty(message = "The password must not be empty")
    @Size(min = 8, message = "The password must have at least 8 characters")
    private String password;

}
