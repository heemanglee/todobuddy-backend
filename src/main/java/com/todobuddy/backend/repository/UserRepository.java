package com.todobuddy.backend.repository;

import com.todobuddy.backend.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT u.* FROM users u WHERE u.email = :email", nativeQuery = true)
    Optional<User> findUserByIdInQuery(String email);

}
