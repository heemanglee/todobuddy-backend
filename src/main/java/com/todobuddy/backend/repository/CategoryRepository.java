package com.todobuddy.backend.repository;

import com.todobuddy.backend.entity.Category;
import com.todobuddy.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryCustomRepository {

    Long countByUser(User user);
}
