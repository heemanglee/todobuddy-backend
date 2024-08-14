package com.todobuddy.backend.service;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.todobuddy.backend.dto.CreateMemoRequest;
import com.todobuddy.backend.dto.CreateMemoResponse;
import com.todobuddy.backend.entity.Category;
import com.todobuddy.backend.entity.Memo;
import com.todobuddy.backend.entity.User;
import com.todobuddy.backend.exception.category.CategoryNotFoundException;
import com.todobuddy.backend.repository.CategoryRepository;
import com.todobuddy.backend.repository.MemoRepository;
import com.todobuddy.backend.util.TestUtils;
import java.time.LocalDateTime;
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

    @Test
    @DisplayName("링크가 없는 메모를 생성할 수 있다.")
    void createMemoNullableLinkTest() {
        // given
        final String categoryName = "토익";
        final String memoLink = null;
        final String content = "토익 공부하기";
        final LocalDateTime deadLine = LocalDateTime.now();

        User user = TestUtils.createUser("test@test.com", "test", "test");
        Category category = TestUtils.createCategory(user, categoryName);
        when(categoryRepository.existCategory(user, categoryName)).thenReturn(category);

        Memo memo = TestUtils.createMemo(user, category, content, memoLink, deadLine);
        when(memoRepository.save(any())).thenReturn(memo);

        // when
        CreateMemoRequest request = new CreateMemoRequest();
        ReflectionTestUtils.setField(request, "content", content);
        ReflectionTestUtils.setField(request, "categoryName", categoryName);
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
        final String categoryName = "토익";
        final String memoLink = "https://www.todobuddy.com";
        final String content = "토익 공부하기";
        final LocalDateTime deadLine = LocalDateTime.now();

        User user = TestUtils.createUser("test@test.com", "test", "test");
        Category category = TestUtils.createCategory(user, categoryName);
        when(categoryRepository.existCategory(user, categoryName)).thenReturn(category);

        Memo memo = TestUtils.createMemo(user, category, content, memoLink, deadLine);
        when(memoRepository.save(any())).thenReturn(memo);

        // when
        CreateMemoRequest request = new CreateMemoRequest();
        ReflectionTestUtils.setField(request, "content", content);
        ReflectionTestUtils.setField(request, "categoryName", categoryName);
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
        final String categoryName = "토익";
        final String memoLink = "https://www.todobuddy.com";
        final String content = "토익 공부하기";

        User user = TestUtils.createUser("test@test.com", "test", "test");
        when(categoryRepository.existCategory(user, categoryName)).thenReturn(null);

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
        final String categoryName = "토익";
        final String memoLink = "https://www.todobuddy.com";
        final String content = "토익 공부하기";
        final LocalDateTime deadLine = null;

        User user = TestUtils.createUser("test@test.com", "test", "test");
        Category category = TestUtils.createCategory(user, "토익");
        Memo memo = TestUtils.createMemo(user, category, content, memoLink, deadLine);

        when(categoryRepository.existCategory(user, categoryName)).thenReturn(category);
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
        final String categoryName = "토익";
        final String memoLink = "https://www.todobuddy.com";
        final String content = "토익 공부하기";
        final LocalDateTime deadLine = LocalDateTime.now();

        User user = TestUtils.createUser("test@test.com", "test", "test");
        Category category = TestUtils.createCategory(user, "토익");
        Memo memo = TestUtils.createMemo(user, category, content, memoLink, deadLine);

        when(categoryRepository.existCategory(user, categoryName)).thenReturn(category);
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
}
