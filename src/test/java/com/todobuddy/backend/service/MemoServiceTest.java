package com.todobuddy.backend.service;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.todobuddy.backend.dto.CreateMemoRequest;
import com.todobuddy.backend.dto.CreateMemoResponse;
import com.todobuddy.backend.dto.UpdateMemoRequest;
import com.todobuddy.backend.dto.UpdateMemoResponse;
import com.todobuddy.backend.entity.Category;
import com.todobuddy.backend.entity.Memo;
import com.todobuddy.backend.entity.User;
import com.todobuddy.backend.exception.category.CategoryNotFoundException;
import com.todobuddy.backend.repository.CategoryRepository;
import com.todobuddy.backend.repository.MemoRepository;
import com.todobuddy.backend.util.TestUtils;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class MemoServiceTest {

    @Mock
    MemoRepository memoRepository;
    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    MemoService memoService;

    private User user;
    private Category category;

    @BeforeEach
    void setUp() {
        user = TestUtils.createUser("test@test.com", "test", "test");
        category = TestUtils.createCategory(user, "토익");
    }

    @Test
    @DisplayName("링크가 없는 메모를 생성할 수 있다.")
    void createMemoNullableLinkTest() {
        // given
        final String memoLink = null;
        final String content = "토익 공부하기";
        final LocalDateTime deadLine = LocalDateTime.now();

        when(categoryRepository.existCategory(user, category.getCategoryName())).thenReturn(
            category);

        Memo memo = TestUtils.createMemo(user, category, content, memoLink, deadLine);
        when(memoRepository.save(any())).thenReturn(memo);

        // when
        CreateMemoRequest request = new CreateMemoRequest();
        ReflectionTestUtils.setField(request, "content", content);
        ReflectionTestUtils.setField(request, "categoryName", category.getCategoryName());
        ReflectionTestUtils.setField(request, "memoLink", memoLink);

        CreateMemoResponse result = memoService.createMemo(user, request);

        // then
        assertThat(result.getMemoId()).isEqualTo(memo.getId());
        assertThat(result.getMemoLink()).isNull();
        assertThat(result.getMemoContent()).isEqualTo(content);
        assertThat(result.getMemoDeadLine()).isEqualTo(deadLine);
    }

    @Test
    @DisplayName("링크가 있는 메모를 생성할 수 있다.")
    void createMemoContainLinkTest() {
        // given
        final String memoLink = "https://www.todobuddy.com";
        final String content = "토익 공부하기";
        final LocalDateTime deadLine = LocalDateTime.now();

        when(categoryRepository.existCategory(user, category.getCategoryName())).thenReturn(
            category);

        Memo memo = TestUtils.createMemo(user, category, content, memoLink, deadLine);
        when(memoRepository.save(any())).thenReturn(memo);

        // when
        CreateMemoRequest request = new CreateMemoRequest();
        ReflectionTestUtils.setField(request, "content", content);
        ReflectionTestUtils.setField(request, "categoryName", category.getCategoryName());
        ReflectionTestUtils.setField(request, "memoLink", memoLink);

        CreateMemoResponse result = memoService.createMemo(user, request);

        // then
        assertThat(result.getMemoId()).isEqualTo(memo.getId());
        assertThat(result.getMemoLink()).isEqualTo(memoLink);
        assertThat(result.getMemoContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("사용자가 등록한 카테고리 목록에 없는 카테고리로 메모를 생성할 경우 예외가 발생한다.")
    void exceptionCreateMemoTest() {
        // given
        final String memoLink = "https://www.todobuddy.com";
        final String content = "토익 공부하기";

        when(categoryRepository.existCategory(user, category.getCategoryName())).thenReturn(null);

        // when
        CreateMemoRequest request = new CreateMemoRequest();
        ReflectionTestUtils.setField(request, "content", content);
        ReflectionTestUtils.setField(request, "categoryName", "토익");
        ReflectionTestUtils.setField(request, "memoLink", memoLink);

        // then
        assertThrows(CategoryNotFoundException.class,
            () -> memoService.createMemo(user, request));
    }

    @Test
    @DisplayName("마감 일정이 없는 메모를 생성할 수 있다.")
    void createMemoNullableDeadLineTest() {
        // given
        final String memoLink = "https://www.todobuddy.com";
        final String content = "토익 공부하기";
        final LocalDateTime deadLine = null;

        Memo memo = TestUtils.createMemo(user, category, content, memoLink, deadLine);

        when(categoryRepository.existCategory(user, category.getCategoryName())).thenReturn(
            category);
        when(memoRepository.save(any())).thenReturn(memo);

        // when
        CreateMemoRequest request = new CreateMemoRequest();
        ReflectionTestUtils.setField(request, "content", content);
        ReflectionTestUtils.setField(request, "categoryName", "토익");
        ReflectionTestUtils.setField(request, "memoLink", memoLink);
        ReflectionTestUtils.setField(request, "memoDeadLine", deadLine);

        CreateMemoResponse result = memoService.createMemo(user, request);

        // then
        assertThat(result.getMemoId()).isEqualTo(memo.getId());
        assertThat(result.getMemoLink()).isEqualTo(memoLink);
        assertThat(result.getMemoContent()).isEqualTo(content);
        assertThat(result.getMemoDeadLine()).isNull();
    }

    @Test
    @DisplayName("마감 일정이 존재하는 메모를 생성할 수 있다.")
    void createMemoContainDeadLineTest() {
        // given
        final String memoLink = "https://www.todobuddy.com";
        final String content = "토익 공부하기";
        final LocalDateTime deadLine = LocalDateTime.now();

        Memo memo = TestUtils.createMemo(user, category, content, memoLink, deadLine);

        when(categoryRepository.existCategory(user, category.getCategoryName())).thenReturn(
            category);
        when(memoRepository.save(any())).thenReturn(memo);

        // when
        CreateMemoRequest request = new CreateMemoRequest();
        ReflectionTestUtils.setField(request, "content", content);
        ReflectionTestUtils.setField(request, "categoryName", "토익");
        ReflectionTestUtils.setField(request, "memoLink", memoLink);
        ReflectionTestUtils.setField(request, "memoDeadLine", deadLine);

        CreateMemoResponse result = memoService.createMemo(user, request);

        // then
        assertThat(result.getMemoId()).isEqualTo(memo.getId());
        assertThat(result.getMemoLink()).isEqualTo(memoLink);
        assertThat(result.getMemoContent()).isEqualTo(content);
        assertThat(result.getMemoDeadLine()).isNotNull();
        assertThat(result.getMemoDeadLine()).isEqualTo(deadLine);
    }

    @Test
    @DisplayName("메모를 수정할 수 있다.")
    void updateMemoTest() {
        // given
        final String memoLink = "https://www.todobuddy.com";
        final String content = "토익 공부하기";
        final LocalDateTime deadLine = LocalDateTime.now();
        Memo memo = TestUtils.createMemo(user, category, content, memoLink, deadLine);
        when(memoRepository.findById(any())).thenReturn(Optional.of(memo));

        final String otherCategoryName = "운동";
        Category otherCategory = TestUtils.createCategory(user, otherCategoryName);
        when(categoryRepository.existCategory(user, otherCategoryName)).thenReturn(otherCategory);

        final LocalDateTime newDeadLine = LocalDateTime.now().plusHours(5);

        // then
        UpdateMemoRequest request = new UpdateMemoRequest();
        ReflectionTestUtils.setField(request, "memoContent", "운동하기");
        ReflectionTestUtils.setField(request, "categoryName", "운동");
        ReflectionTestUtils.setField(request, "memoLink", "https://www.change.com");
        ReflectionTestUtils.setField(request, "memoDeadLine", newDeadLine);

        UpdateMemoResponse result = memoService.updateMemo(user, memo.getId(), request);

        // then
        assertThat(result.getMemoDeadLine()).isEqualTo(newDeadLine);
        assertThat(result.getMemoDeadLine()).isNotEqualTo(deadLine);
        assertThat(result.getMemoLink()).isEqualTo("https://www.change.com");
        assertThat(result.getMemoLink()).isNotEqualTo(memoLink);
        assertThat(result.getCategoryName()).isEqualTo(otherCategoryName);
        assertThat(result.getCategoryName()).isNotEqualTo(category.getCategoryName());
        assertThat(result.getMemoContent()).isEqualTo(memo.getContent());
        assertThat(result.getMemoContent()).isNotEqualTo(content);
    }

}
