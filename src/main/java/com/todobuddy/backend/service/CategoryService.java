package com.todobuddy.backend.service;

import com.todobuddy.backend.dto.CreateCategoryRequest;
import com.todobuddy.backend.dto.CreateCategoryResponse;
import com.todobuddy.backend.dto.GetCategoriesResponse;
import com.todobuddy.backend.dto.UpdateCategoryRequest;
import com.todobuddy.backend.dto.UpdateCategoryResponse;
import com.todobuddy.backend.entity.Category;
import com.todobuddy.backend.entity.User;
import com.todobuddy.backend.exception.category.CategoryErrorCode;
import com.todobuddy.backend.exception.category.CategoryNotFoundException;
import com.todobuddy.backend.exception.category.MaxCategoriesExceededException;
import com.todobuddy.backend.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // 카테고리 등록
    @Transactional
    public CreateCategoryResponse createCategory(User user, CreateCategoryRequest request) {

        // 사용자는 최대 3개의 카테고리를 등록할 수 있다.
        Long categoryCount = categoryRepository.countByUser(user);
        if (categoryCount >= Category.MAX_COUNT) { // 등록된 카테고리가 3개 이상이면 더 이상 등록할 수 없다.
            throw new MaxCategoriesExceededException(CategoryErrorCode.MAX_CATEGORIES_EXCEEDED);
        }

        // 카테고리 등록
        Category createCategory = createCategory(user, request.getCategoryName(),
            request.getCategoryOrderId());
        Category savedCategory = categoryRepository.save(createCategory);

        return new CreateCategoryResponse(savedCategory.getCategoryName(), savedCategory.getCategoryOrderId());
    }

    // 사용자가 등록한 모든 카테고리 조회
    @Transactional(readOnly = true)
    public List<GetCategoriesResponse> getCategories(User user) {
        return categoryRepository.getCategories(user);
    }

    // 카테고리 삭제
    @Transactional
    public void deleteCategory(Long categoryId) {
        Category findCategory = findCategoryById(categoryId);
        categoryRepository.delete(findCategory);
    }

    // 카테고리 이름 수정
    @Transactional
    public UpdateCategoryResponse updateCategory(Long categoryId, UpdateCategoryRequest request) {
        Category findCategory = findCategoryById(categoryId);
        findCategory.updateCategoryName(request.getCategoryName());

        return new UpdateCategoryResponse(findCategory.getId(), findCategory.getCategoryName());
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException(CategoryErrorCode.CATEGORY_NOT_FOUND));
    }

    private static Category createCategory(User user, String categoryName, int categoryOrder) {
        return Category.builder()
            .categoryName(categoryName)
            .user(user)
            .categoryOrderId(categoryOrder)
            .build();
    }

}
