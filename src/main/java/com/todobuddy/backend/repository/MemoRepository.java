package com.todobuddy.backend.repository;

import com.todobuddy.backend.entity.Memo;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemoRepository extends JpaRepository<Memo, Long>, MemoCustomRepository {

    @Query(value = "SELECT m.* FROM memo m WHERE m.memo_id = :memoId", nativeQuery = true)
    Optional<Memo> findMemoByIdInQuery(Long memoId);

    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM memo m WHERE m.deleted_time <= :date", nativeQuery = true)
    void deleteAllMemoByDeletedTimeBefore(LocalDateTime date);
}
