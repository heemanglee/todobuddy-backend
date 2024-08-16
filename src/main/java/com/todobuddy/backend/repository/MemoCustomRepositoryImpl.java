package com.todobuddy.backend.repository;

import static com.todobuddy.backend.entity.QMemo.memo;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.todobuddy.backend.dto.GetMemosResponse;
import com.todobuddy.backend.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemoCustomRepositoryImpl implements MemoCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GetMemosResponse> findMemosByYearAndMonth(User user, int year, int month) {
        return queryFactory
            .select(
                Projections.constructor(
                    GetMemosResponse.class,
                    memo.category.categoryName,
                    memo.category.categoryOrder,
                    memo.content,
                    memo.link,
                    memo.memoDeadLine
                )
            )
            .from(memo)
            .where(
                memo.user.eq(user),
                memo.createdDate.year().eq(year),
                memo.createdDate.month().eq(month)
            )
            .orderBy(memo.createdDate.asc())
            .fetch();
    }
}
