package com.todobuddy.backend.service;

import com.todobuddy.backend.dto.CreateMemoRequest;
import com.todobuddy.backend.dto.CreateMemoResponse;
import com.todobuddy.backend.entity.Category;
import com.todobuddy.backend.entity.Memo;
import com.todobuddy.backend.entity.User;
import com.todobuddy.backend.exception.category.CategoryErrorCode;
import com.todobuddy.backend.exception.category.CategoryNotFoundException;
import com.todobuddy.backend.repository.CategoryRepository;
import com.todobuddy.backend.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public CreateMemoResponse createMemo(User user, CreateMemoRequest request) {
        Category findCategory = validationExistCategory(user,
            request.getCategoryName());// 사용자가 등록한 카테고리 존재 여부 확인

        Memo memo = Memo.builder()
            .category(findCategory)
            .content(request.getContent())
            .user(user)
            .link(request.getMemoLink())
            .build();
        Memo savedMemo = memoRepository.save(memo);

        return new CreateMemoResponse(savedMemo.getId(), savedMemo.getContent(),
            savedMemo.getLink());
    }

    private Category validationExistCategory(User user, String categoryName) {
        Category findCategory = categoryRepository.existCategory(user, categoryName);
        if (findCategory == null) {
            throw new CategoryNotFoundException(CategoryErrorCode.CATEGORY_NOT_FOUND);
        }
        return findCategory;
    }
}
