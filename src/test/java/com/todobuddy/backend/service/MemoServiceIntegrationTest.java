package com.todobuddy.backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.todobuddy.backend.dto.GetMemosResponse;
import com.todobuddy.backend.entity.Category;
import com.todobuddy.backend.entity.Memo;
import com.todobuddy.backend.entity.User;
import com.todobuddy.backend.repository.CategoryRepository;
import com.todobuddy.backend.repository.MemoRepository;
import com.todobuddy.backend.repository.UserRepository;
import com.todobuddy.backend.util.TestUtils;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class MemoServiceIntegrationTest {

    @Autowired
    MemoRepository memoRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    MemoService memoService;
    @Autowired
    private UserRepository userRepository;

    User user1;
    User user2;
    Category category1;
    Category category2;
    Category category3;

    @BeforeEach
    void setUp() {
        user1 = TestUtils.createUser("test@test.com", "test", "test");
        user2 = TestUtils.createUser("test2@test.com", "test2", "test2");
        category1 = TestUtils.createCategory(user1, "토익");
        category2 = TestUtils.createCategory(user1, "운동");
        category3 = TestUtils.createCategory(user2, "게임");

        userRepository.save(user1);
        userRepository.save(user2);
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);
    }

    @Test
    @DisplayName("사용자가 작성한 모든 메모를 조회할 수 있다.")
    void getMemosTest() {
        // given
        ReflectionTestUtils.setField(category1, "categoryOrderId", 1);
        ReflectionTestUtils.setField(category2, "categoryOrderId", 2);
        ReflectionTestUtils.setField(category3, "categoryOrderId", 3);

        Memo memo1 = TestUtils.createMemo(user1, category1, "토익 공부", null, null);
        Memo memo2 = TestUtils.createMemo(user1, category1, "토익 강의", "https://www.todobuddy.com",
            null);
        Memo memo3 = TestUtils.createMemo(user1, category2, "헬스장 가기", null, LocalDateTime.now());
        Memo memo4 = TestUtils.createMemo(user2, category3, "게임하기", null, null);

        memoRepository.save(memo1);
        memoRepository.save(memo2);
        memoRepository.save(memo3);
        memoRepository.save(memo4);

        // when
        List<GetMemosResponse> result = memoService.getMemoAll(user1, null);

        // then
        assertThat(result.size()).isEqualTo(3);

        assertThat(result.get(0).getMemoContent()).isEqualTo(memo1.getContent());
        assertThat(result.get(0).getMemoLink()).isEqualTo(memo1.getLink());
        assertThat(result.get(0).getMemoDeadLine()).isEqualTo(memo1.getMemoDeadLine());
        assertThat(result.get(0).getCategoryId()).isEqualTo(memo1.getCategory().getId());
        assertThat(result.get(0).getMemoStatus()).isEqualTo(memo1.getMemoStatus());
        assertThat(result.get(0).getCategoryOrderId()).isEqualTo(memo1.getCategory().getCategoryOrderId());

        assertThat(result.get(1).getMemoContent()).isEqualTo(memo2.getContent());
        assertThat(result.get(1).getMemoLink()).isEqualTo(memo2.getLink());
        assertThat(result.get(1).getMemoDeadLine()).isEqualTo(memo2.getMemoDeadLine());
        assertThat(result.get(1).getCategoryId()).isEqualTo(memo2.getCategory().getId());
        assertThat(result.get(1).getMemoStatus()).isEqualTo(memo2.getMemoStatus());
        assertThat(result.get(1).getCategoryOrderId()).isEqualTo(memo2.getCategory().getCategoryOrderId());

        assertThat(result.get(2).getMemoContent()).isEqualTo(memo3.getContent());
        assertThat(result.get(2).getMemoLink()).isEqualTo(memo3.getLink());
        assertThat(result.get(2).getMemoDeadLine().withNano(0)).isEqualTo(memo3.getMemoDeadLine().withNano(0));
        assertThat(result.get(2).getCategoryId()).isEqualTo(memo3.getCategory().getId());
        assertThat(result.get(2).getMemoStatus()).isEqualTo(memo3.getMemoStatus());
        assertThat(result.get(2).getCategoryOrderId()).isEqualTo(memo3.getCategory().getCategoryOrderId());

        assertThat(result).extracting("memoContent").doesNotContain(memo4.getContent());
        assertThat(result).extracting("memoLink").doesNotContain(memo4.getContent());
        assertThat(result).extracting("memoDeadLine").doesNotContain(memo4.getContent());
    }
}
