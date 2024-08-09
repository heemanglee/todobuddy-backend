package com.todobuddy.backend.repository;

import com.todobuddy.backend.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsUserByEmailAndDeleted(String email, boolean isDeleted);
}
