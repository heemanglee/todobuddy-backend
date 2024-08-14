package com.todobuddy.backend.repository;

import com.todobuddy.backend.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<Memo, Long> {

}
