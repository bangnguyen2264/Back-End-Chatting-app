package com.example.chatting.app.form;

import com.example.chatting.app.dto.UserDto;
import com.example.chatting.app.model.User;
import lombok.Data;

import java.util.List;

@Data
public class GroupBoxchatForm {
    private String name;
    private List<UserDto> members;
}
