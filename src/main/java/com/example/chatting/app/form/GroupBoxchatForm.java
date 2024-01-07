package com.example.chatting.app.form;

import com.example.chatting.app.dto.UserDto;
import com.example.chatting.app.model.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Lombok;

import java.util.List;

@Data
public class GroupBoxchatForm {
    @NotEmpty(message = "Name must required")
    private String name;
    private List<Long> membersId;
}
