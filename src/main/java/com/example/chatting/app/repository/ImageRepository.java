package com.example.chatting.app.repository;

import com.example.chatting.app.model.Image;
import com.example.chatting.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByUser(User user);
}
