package com.todobuddy.backend.repository;

import com.todobuddy.backend.entity.Memo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    @Query(value = "SELECT m.* FROM memo m WHERE m.memo_id = :memoId", nativeQuery = true)
    Optional<Memo> findMemoByIdInQuery(Long memoId);
}
