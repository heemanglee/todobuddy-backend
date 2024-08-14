package com.todobuddy.backend.scheduler;

import com.todobuddy.backend.repository.MemoRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MemoHardDeleteScheduler {

    private final MemoRepository memoRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // 매일 새벽 0시에 실행
    public void deleteOldMemo() {
        // 휴지통에 저장된지 30일이 지난 메모 삭제
        memoRepository.deleteAllMemoByDeletedTimeBefore(LocalDateTime.now().minusDays(30));
    }
}
