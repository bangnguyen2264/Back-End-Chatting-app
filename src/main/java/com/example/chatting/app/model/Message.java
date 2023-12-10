package com.example.chatting.app.model;

import com.example.chatting.app.enums.MessageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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
    @CreationTimestamp
    private LocalDateTime createTime;
    private boolean isRead = false;
    @Enumerated(EnumType.STRING)
    private MessageType type;

}
