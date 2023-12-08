package com.example.chatting.app.model;

import com.example.chatting.app.enums.MessageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue
    private Long id;
    private String sender;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boxchat_id")
    private Boxchat boxchat;
    private boolean isRead = false;
    @Enumerated(EnumType.STRING)
    private MessageType type;

}
