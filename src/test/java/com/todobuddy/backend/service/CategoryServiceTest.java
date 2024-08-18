package com.todobuddy.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.todobuddy.backend.dto.CreateCategoryRequest;
import com.todobuddy.backend.dto.CreateCategoryResponse;
import com.todobuddy.backend.dto.GetCategoriesResponse;
import com.todobuddy.backend.dto.UpdateCategoryRequest;
import com.todobuddy.backend.dto.UpdateCategoryResponse;
import com.todobuddy.backend.entity.Category;
import com.todobuddy.backend.entity.User;
import com.todobuddy.backend.exception.category.MaxCategoriesExceededException;
import com.todobuddy.backend.repository.CategoryRepository;
import com.todobuddy.backend.util.TestUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        String categoryName = "토익";
        Category category = TestUtils.createCategory(user, categoryName);

        when(categoryRepository.countByUser(user)).thenReturn(2L);
        when(categoryRepository.save(any())).thenReturn(category);


        // then
        CreateCategoryRequest request = new CreateCategoryRequest();
        ReflectionTestUtils.setField(request, "categoryName", categoryName);
        ReflectionTestUtils.setField(request, "categoryOrder", 1);

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
        assertThrows(MaxCategoriesExceededException.class,
            () -> categoryService.createCategory(user, request));
    }

    @Test
    @DisplayName("사용자가 등록한 모든 카테고리를 조회할 수 있다.")
    void getCategoriesTest() {
        // given
        User user = TestUtils.createUser("test@test.com", "test", "test");

        List<Category> list = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Category category = TestUtils.createCategory(user, "category" + i);
            ReflectionTestUtils.setField(category, "categoryOrder", i);
            list.add(category);
        }

        List<GetCategoriesResponse> categories = list.stream()
            .map(c -> new GetCategoriesResponse(c.getId(), c.getCategoryName(),
                c.getCategoryOrder()))
            .toList();

        when(categoryRepository.getCategories(user)).thenReturn(categories);

        // when
        List<GetCategoriesResponse> response = categoryService.getCategories(user);

        // then
        assertThat(response.size()).isEqualTo(3);
        assertThat(response.get(0).getCategoryName()).isEqualTo("category1");
        assertThat(response.get(0).getCategoryOrder()).isEqualTo(1);

        assertThat(response.get(1).getCategoryName()).isEqualTo("category2");
        assertThat(response.get(1).getCategoryOrder()).isEqualTo(2);

        assertThat(response.get(2).getCategoryName()).isEqualTo("category3");
        assertThat(response.get(2).getCategoryOrder()).isEqualTo(3);
    }

    @Test
    @DisplayName("카테고리 이름을 성공적으로 수정할 수 있다.")
    void successUpdateCategoryNameTest() {
        // given
        User user = TestUtils.createUser("test@test.com", "test", "test");
        Category createCategory = TestUtils.createCategory(user, "category1");

        when(categoryRepository.findById(any())).thenReturn(Optional.of(createCategory));

        // when
        UpdateCategoryRequest request = new UpdateCategoryRequest();
        ReflectionTestUtils.setField(request, "categoryName", "category2");
        UpdateCategoryResponse response = categoryService.updateCategory(createCategory.getId(),
            request);

        // then
        assertThat(response.getCategoryName()).isEqualTo("category2");
    }

    @Test
    @DisplayName("사용자가 작성한 카테고리를 삭제할 수 있다.")
    void deleteCategorySuccessTest() {
        // given
        User user = TestUtils.createUser("test@test.com", "test", "test");
        Category createCategory = TestUtils.createCategory(user, "category1");

        when(categoryRepository.findById(any())).thenReturn(Optional.of(createCategory));

        // when
        categoryService.deleteCategory(createCategory.getId());
        ReflectionTestUtils.setField(createCategory, "deleted", true);

        // then
        assertThat(categoryRepository.getCategories(user).size()).isEqualTo(0);
        assertThat(createCategory.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("등록된 카테고리가 3개 미만인 경우 카테고리를 생성할 수 있다.")
    void createNewCategoryTest() {
        // given
        User user = TestUtils.createUser("test@test.com", "test", "test");

        when(categoryRepository.countByUser(user)).thenReturn(2L);

        Category category = TestUtils.createCategory(user, "토익");
        ReflectionTestUtils.setField(category, "categoryOrder", 1);
        when(categoryRepository.save(any())).thenReturn(category);

        // when
        CreateCategoryRequest request = new CreateCategoryRequest();
        ReflectionTestUtils.setField(request, "categoryName", "토익");
        ReflectionTestUtils.setField(request, "categoryOrder", 1);

        CreateCategoryResponse response = categoryService.createCategory(user, request);

        // then
        assertThat(response.getCategoryName()).isEqualTo("토익");
        assertThat(response.getCategoryOrder()).isEqualTo(1);
    }

    @Test
    @DisplayName("등록된 카테고리가 3개인 경우 카테고리를 생성할 수 없다.")
    void createNewCategoryFailTest() {
        // given
        User user = TestUtils.createUser("test@test.com", "test", "test");

        when(categoryRepository.countByUser(user)).thenReturn(3L);

        // when
        CreateCategoryRequest request = new CreateCategoryRequest();
        ReflectionTestUtils.setField(request, "categoryName", "토익");
        ReflectionTestUtils.setField(request, "categoryOrder", 4);

        // then
        assertThrows(MaxCategoriesExceededException.class,
            () -> categoryService.createCategory(user, request));
    }

}