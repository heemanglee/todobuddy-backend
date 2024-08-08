package com.todobuddy.backend.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.todobuddy.backend.dto.CreateCategoryRequest;
import com.todobuddy.backend.entity.Category;
import com.todobuddy.backend.entity.User;
import com.todobuddy.backend.exception.category.MaxCategoriesExceededException;
import com.todobuddy.backend.repository.CategoryRepository;
import com.todobuddy.backend.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;
    @InjectMocks
    CategoryService categoryService;

    @Test
    @DisplayName("사용자가 등록한 카테고리가 3개 미만인 경우, 추가로 카테고리를 등록할 수 있다.")
    void successCreateCategoryTest() {
        // given
        User user = TestUtils.createUser("test@test.com", "test", "test");

        when(categoryRepository.countByUser(user)).thenReturn(2L);

        String categoryName = "토익";

        // then
        CreateCategoryRequest request = new CreateCategoryRequest();
        ReflectionTestUtils.setField(request, "categoryName", categoryName);

        categoryService.createCategory(user, request);

        // then
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("사용자가 등록한 카테고리가 3개인경우, 추가로 카테고리를 등록할 경우 예외가 발생한다.")
    void failCreateCategoryTest() {
        // given
        User user = TestUtils.createUser("test@test.com", "test", "test");

        when(categoryRepository.countByUser(user)).thenReturn(3L);

        String categoryName = "토익";

        // when
        CreateCategoryRequest request = new CreateCategoryRequest();
        ReflectionTestUtils.setField(request, "categoryName", categoryName);

        // then
        Assertions.assertThrows(MaxCategoriesExceededException.class,
            () -> categoryService.createCategory(user, request));
    }
}