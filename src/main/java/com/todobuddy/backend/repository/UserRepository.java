package com.todobuddy.backend.repository;

import com.todobuddy.backend.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.deleted = 1 WHERE u.id = :userId")
    void deleteByIdInQuery(Long userId);

}
