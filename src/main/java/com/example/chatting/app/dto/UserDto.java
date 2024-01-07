package com.example.chatting.app.dto;

import com.example.chatting.app.model.Image;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
@Data
public class UserDto {
    private Integer id;
    private String firstname;
    private String lastname;
    private Image avatar;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;
    private Integer age;
    private String username;
}
