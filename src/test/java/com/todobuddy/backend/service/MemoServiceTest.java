package com.todobuddy.backend.service;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.todobuddy.backend.dto.CreateMemoRequest;
import com.todobuddy.backend.dto.CreateMemoResponse;
import com.todobuddy.backend.dto.UpdateMemoRequest;
import com.todobuddy.backend.dto.UpdateMemoResponse;
import com.todobuddy.backend.dto.UpdateMemoStatusRequest;
import com.todobuddy.backend.entity.Category;
import com.todobuddy.backend.entity.Memo;
import com.todobuddy.backend.entity.MemoStatus;
import com.todobuddy.backend.entity.User;
import com.todobuddy.backend.exception.category.CategoryNotFoundException;
import com.todobuddy.backend.exception.memo.MemoAuthorMismatchException;
import com.todobuddy.backend.exception.memo.MemoStatusUnchangedException;
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
        ReflectionTestUtils.setField(user, "id", 1L);
        category = TestUtils.createCategory(user, "토익");
        ReflectionTestUtils.setField(category, "id", 1L);
    }

    @Test
    @DisplayName("링크가 없는 메모를 생성할 수 있다.")
    void createMemoNullableLinkTest() {
        // given
        final String memoLink = null;
        final String content = "토익 공부하기";
        final LocalDateTime deadLine = LocalDateTime.now();

        when(categoryRepository.existCategory(user, category.getId())).thenReturn(
            category);

        Memo memo = TestUtils.createMemo(user, category, content, memoLink, deadLine);
        when(memoRepository.save(any())).thenReturn(memo);

        // when
        CreateMemoRequest request = new CreateMemoRequest();
        ReflectionTestUtils.setField(request, "memoContent", content);
        ReflectionTestUtils.setField(request, "categoryId", category.getId());
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

        when(categoryRepository.existCategory(user, category.getId())).thenReturn(
            category);

        Memo memo = TestUtils.createMemo(user, category, content, memoLink, deadLine);
        when(memoRepository.save(any())).thenReturn(memo);

        // when
        CreateMemoRequest request = new CreateMemoRequest();
        ReflectionTestUtils.setField(request, "memoContent", content);
        ReflectionTestUtils.setField(request, "categoryId", category.getId());
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

        when(categoryRepository.existCategory(user, category.getId())).thenReturn(null);

        // when
        CreateMemoRequest request = new CreateMemoRequest();
        ReflectionTestUtils.setField(request, "memoContent", content);
        ReflectionTestUtils.setField(request, "categoryId", 1L);
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

        when(categoryRepository.existCategory(user, category.getId())).thenReturn(
            category);
        when(memoRepository.save(any())).thenReturn(memo);

        // when
        CreateMemoRequest request = new CreateMemoRequest();
        ReflectionTestUtils.setField(request, "memoContent", content);
        ReflectionTestUtils.setField(request, "categoryId", 1L);
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

        when(categoryRepository.existCategory(user, category.getId())).thenReturn(
            category);
        when(memoRepository.save(any())).thenReturn(memo);

        // when
        CreateMemoRequest request = new CreateMemoRequest();
        ReflectionTestUtils.setField(request, "memoContent", content);
        ReflectionTestUtils.setField(request, "categoryId", 1L);
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
    @DisplayName("메모의 상태(미완료/완료)를 변경할 수 있다.")
    void updateMemoStatusTest() {
        // given
        Memo memo = TestUtils.createMemo(user, category, "토익", null, null);
        when(memoRepository.findById(any())).thenReturn(Optional.of(memo));

        // when
        UpdateMemoStatusRequest request = new UpdateMemoStatusRequest();
        ReflectionTestUtils.setField(request, "memoStatus", MemoStatus.COMPLETED);

        memoService.updateMemoStatus(user, memo.getId(), request);

        // then
        assertThat(memo.getMemoStatus()).isEqualTo(MemoStatus.COMPLETED);
    }

    @Test
    @DisplayName("동일한 메모 상태로 변경할 수 없다.")
    void exceptionUpdateSameMemoStats() {
        // given
        Memo memo = TestUtils.createMemo(user, category, "토익", null, null);
        when(memoRepository.findById(any())).thenReturn(Optional.of(memo));

        // when
        UpdateMemoStatusRequest request = new UpdateMemoStatusRequest();
        ReflectionTestUtils.setField(request, "memoStatus", MemoStatus.NOT_COMPLETED);

        // then
        assertThrows(MemoStatusUnchangedException.class,
            () -> memoService.updateMemoStatus(user, memo.getId(), request));
    }

    @Test
    @DisplayName("작성한 메모를 삭제할 수 있다.")
    void deleteMemoTest() {
        // given
        Memo memo = TestUtils.createMemo(user, category, "토익", null, null);
        when(memoRepository.findMemoByIdInQuery(any())).thenReturn(Optional.of(memo));

        // when
        memoService.deleteMemo(user, memo.getId());

        // then
        assertThat(memoRepository.findById(memo.getId())).isEmpty();
        verify(memoRepository).deleteById(any());
    }

    @Test
    @DisplayName("메모 작성자와 메모 작성자가 동일하지 않을 경우 예외가 발생한다.")
    void deleteMemoExceptionTest() {
        // given
        User otherUser = TestUtils.createUser("aaa@aaa.com", "aaa", "aaa");
        ReflectionTestUtils.setField(otherUser, "id", 2L); // 다른 ID 설정
        Memo memo = TestUtils.createMemo(otherUser, category, "토익", null, null);
        ReflectionTestUtils.setField(memo, "id", 1L); // Ensure memo ID is set
        when(memoRepository.findMemoByIdInQuery(memo.getId())).thenReturn(Optional.of(memo));

        // when & then
        assertThrows(MemoAuthorMismatchException.class,
            () -> memoService.deleteMemo(user, memo.getId()));
    }

    @Test
    @DisplayName("메모를 수정할 수 있다.")
    void updateMemoTest() {
        // given
        Memo memo = TestUtils.createMemo(user, category, "test", null, null);
        when(memoRepository.findById(any())).thenReturn(Optional.of(memo));
        when(categoryRepository.existCategory(user, category.getId())).thenReturn(category);

        // when
        UpdateMemoRequest request = new UpdateMemoRequest();
        ReflectionTestUtils.setField(request, "memoContent", "update test");
        ReflectionTestUtils.setField(request, "categoryId", category.getId());
        ReflectionTestUtils.setField(request, "memoLink", "https://www.todobuddy.com");
        ReflectionTestUtils.setField(request, "memoDeadLine", null);

        UpdateMemoResponse response = memoService.updateMemo(user, memo.getId(), request);

        // then
        assertThat(response.getMemoContent()).isEqualTo("update test");
        assertThat(response.getMemoLink()).isEqualTo("https://www.todobuddy.com");
        assertThat(response.getMemoDeadLine()).isNull();
        assertThat(response.getCategoryId()).isEqualTo(category.getId());
    }
}

