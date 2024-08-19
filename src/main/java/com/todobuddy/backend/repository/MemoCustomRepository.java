package com.todobuddy.backend.repository;

import com.todobuddy.backend.dto.GetMemosResponse;
import com.todobuddy.backend.entity.User;
import java.util.List;

public interface MemoCustomRepository {

    List<GetMemosResponse> findMemoByFilter(User user, String memoStatus);
}
