package com.todobuddy.backend.repository;

import com.todobuddy.backend.dto.GetCategoriesResponse;
import com.todobuddy.backend.entity.Category;
import com.todobuddy.backend.entity.User;
import java.util.List;

public interface CategoryCustomRepository {

    List<GetCategoriesResponse> getCategories(User user);

    Category existCategory(User user, Long categoryId);
}
