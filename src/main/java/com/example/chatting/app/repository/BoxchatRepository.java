package com.example.chatting.app.repository;

import com.example.chatting.app.dto.BoxchatDto;
import com.example.chatting.app.model.Boxchat;
import com.example.chatting.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoxchatRepository extends JpaRepository<Boxchat, Long> {
    @Query("SELECT b FROM Boxchat b " +
            "WHERE SIZE(b.members) = 2 " +
            "AND :user1 MEMBER OF b.members " +
            "AND :user2 MEMBER OF b.members")
    Optional<Boxchat> findPrivateChat(@Param("user1") User user1, @Param("user2") User user2);
}
