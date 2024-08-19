package com.todobuddy.backend.service;

import com.todobuddy.backend.dto.CreateMemoRequest;
import com.todobuddy.backend.dto.CreateMemoResponse;
import com.todobuddy.backend.dto.GetMemosRequest;
import com.todobuddy.backend.dto.GetMemosResponse;
import com.todobuddy.backend.dto.UpdateMemoRequest;
import com.todobuddy.backend.dto.UpdateMemoResponse;
import com.todobuddy.backend.dto.UpdateMemoStatusRequest;
import com.todobuddy.backend.dto.UpdateMemoStatusResponse;
import com.todobuddy.backend.entity.Category;
import com.todobuddy.backend.entity.Memo;
import com.todobuddy.backend.entity.MemoStatus;
import com.todobuddy.backend.entity.User;
import com.todobuddy.backend.exception.category.CategoryErrorCode;
import com.todobuddy.backend.exception.category.CategoryNotFoundException;
import com.todobuddy.backend.exception.memo.MemoAuthorMismatchException;
import com.todobuddy.backend.exception.memo.MemoErrorCode;
import com.todobuddy.backend.exception.memo.MemoNotFoundException;
import com.todobuddy.backend.exception.memo.MemoStatusUnchangedException;
import com.todobuddy.backend.repository.CategoryRepository;
import com.todobuddy.backend.repository.MemoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemoService {

    private final MemoRepository memoRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public CreateMemoResponse createMemo(User user, CreateMemoRequest request) {
        Category findCategory = validationExistCategory(user,
            request.getCategoryName());// 사용자가 등록한 카테고리 존재 여부 확인

        Memo memo = Memo.builder()
            .category(findCategory)
            .content(request.getMemoContent())
            .user(user)
            .link(request.getMemoLink())
            .memoDeadLine(request.getMemoDeadLine())
            .build();
        Memo savedMemo = memoRepository.save(memo);

        return new CreateMemoResponse(savedMemo.getId(), savedMemo.getContent(),
            savedMemo.getLink(), savedMemo.getMemoDeadLine());
    }

    @Transactional
    public UpdateMemoResponse updateMemo(User user, Long memoId, UpdateMemoRequest request) {
        Memo findMemo = findMemoById(memoId);
        Category findCategory = validationExistCategory(user, request.getCategoryName());

        findMemo.updateMemo(request.getMemoContent(), findCategory, request.getMemoLink(),
            request.getMemoDeadLine());

        return new UpdateMemoResponse(findMemo.getMemoDeadLine(), findMemo.getContent(),
            findMemo.getLink(), findCategory.getCategoryName());
    }

    @Transactional
    public UpdateMemoStatusResponse updateMemoStatus(User user, Long memoId,
        UpdateMemoStatusRequest request) {
        Memo findMemo = findMemoById(memoId);
        validationMemoAuthor(user, findMemo); // 메모 작성자가 동일한지 검증한다.
        validationMemoStatus(request.getMemoStatus(), findMemo); // 현재 메모 상태와 요청 메모의 상태가 동일하면 안된다.

        MemoStatus memoStatus = request.getMemoStatus();
        findMemo.updateMemoStatus(memoStatus);

        return new UpdateMemoStatusResponse(findMemo.getId(), memoStatus.name());
    }

    @Transactional
    public void deleteMemo(User user, Long memoId)  {
        Memo findMemo = findMemoByIdInQuery(memoId);
        validationMemoAuthor(user, findMemo); // 메모 작성자가 동일한지 검증한다.

        memoRepository.deleteById(memoId);
    }

    @Transactional
    public List<GetMemosResponse> getMemoAll(User user, GetMemosRequest request) {
        return memoRepository.findMemosByYearAndMonth(user,
            request.getYear(), request.getMonth());
    }

    private Memo findMemoByIdInQuery(Long memoId) {
        return memoRepository.findMemoByIdInQuery(memoId)
            .orElseThrow(() -> new MemoNotFoundException(MemoErrorCode.MEMO_NOT_FOUND));
    }

    private static void validationMemoStatus(MemoStatus memoStatus, Memo findMemo) {
        if(findMemo.getMemoStatus().equals(memoStatus)){
            throw new MemoStatusUnchangedException(MemoErrorCode.MEMO_STATUS_UNCHANGED);
        }
    }

    private static void validationMemoAuthor(User user, Memo findMemo) {
        if (!findMemo.getUser().getId().equals(user.getId())) {
            throw new MemoAuthorMismatchException(MemoErrorCode.MEMO_AUTHOR_MISMATCH);
        }
    }

    private Memo findMemoById(Long memoId) {
        return memoRepository.findById(memoId)
            .orElseThrow(() -> new MemoNotFoundException(MemoErrorCode.MEMO_NOT_FOUND));
    }

    private Category validationExistCategory(User user, String categoryName) {
        Category findCategory = categoryRepository.existCategory(user, categoryName);
        if (findCategory == null) {
            throw new CategoryNotFoundException(CategoryErrorCode.CATEGORY_NOT_FOUND);
        }
        return findCategory;
    }
}
