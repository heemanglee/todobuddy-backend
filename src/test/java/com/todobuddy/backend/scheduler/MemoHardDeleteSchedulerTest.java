package com.todobuddy.backend.scheduler;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

import com.todobuddy.backend.entity.Category;
import com.todobuddy.backend.entity.Memo;
import com.todobuddy.backend.entity.User;
import com.todobuddy.backend.repository.CategoryRepository;
import com.todobuddy.backend.repository.MemoRepository;
import com.todobuddy.backend.repository.UserRepository;
import com.todobuddy.backend.util.TestUtils;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
@ActiveProfiles("test")
class MemoHardDeleteSchedulerTest {

    @Autowired
    MemoHardDeleteScheduler memoHardDeleteScheduler;

    @MockBean
    MemoRepository memoRepository;
    @MockBean
    CategoryRepository categoryRepository;
    @MockBean
    UserRepository userRepository;

    @Test
    void deleteOldMemo() {
        // given
        User user = TestUtils.createUser("test@test.com", "test", "test");
        Category category = TestUtils.createCategory(user, "test");
        Memo memo = TestUtils.createMemo(user, category, "test", null, null);
        ReflectionTestUtils.setField(memo, "deletedTime", LocalDateTime.now().minusDays(31));

        userRepository.save(user);
        categoryRepository.save(category);
        memoRepository.save(memo);

        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);

        // when
        memoHardDeleteScheduler.deleteOldMemo();

        // then
        verify(memoRepository, Mockito.times(1)).deleteAllMemoByDeletedTimeBefore(
            argThat(argument ->
                argument.isAfter(thirtyDaysAgo.minusSeconds(1)) && argument.isBefore(
                    thirtyDaysAgo.plusSeconds(1))
            ));
    }
}