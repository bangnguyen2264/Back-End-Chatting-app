package com.example.chatting.app.repository;

import com.example.chatting.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);

    Optional<Object> findByEmail(String email);

    @Query("SELECT p FROM User p " +
            "WHERE p.firstname like concat('%',:query,'%') " +
            "or p.lastname like concat('%',:query,'%')" +
            "or p.username like concat('%',:query,'%')"
    )
    List<User> searchUser(String query);
}
