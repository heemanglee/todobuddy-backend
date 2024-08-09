package com.todobuddy.backend.repository;

import com.todobuddy.backend.entity.Category;
import com.todobuddy.backend.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryCustomRepository {

    Long countByUser(User user);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Category c SET c.deleted = 1 WHERE c.id IN :ids")
    void deleteAllByIdInQuery(@Param("ids") List<Long> ids);

    List<Category> findByUser(User user);
}
