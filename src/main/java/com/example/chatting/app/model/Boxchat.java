package com.example.chatting.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "boxchat")
public class Boxchat {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String creator;
    @CreationTimestamp
    private LocalDate createTime;
    @UpdateTimestamp
    private LocalDate updateTime;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "boxchatmember",
            joinColumns = @JoinColumn(name = "boxchat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> members;
    @OneToMany (mappedBy = "boxchat")
    private List<Message> messages = new ArrayList<>();

}
