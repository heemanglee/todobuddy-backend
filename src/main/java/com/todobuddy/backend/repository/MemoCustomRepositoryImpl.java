package com.todobuddy.backend.repository;

import static com.todobuddy.backend.entity.QMemo.memo;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.todobuddy.backend.dto.GetMemosResponse;
import com.todobuddy.backend.entity.MemoStatus;
import com.todobuddy.backend.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemoCustomRepositoryImpl implements MemoCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GetMemosResponse> findMemoByFilter(User user, String memoStatus) {
        return queryFactory
            .select(
                Projections.constructor(
                    GetMemosResponse.class,
                    memo.category.id,
                    memo.content,
                    memo.link,
                    memo.memoDeadLine,
                    memo.createdDate,
                    memo.memoStatus
                )
            )
            .from(memo)
            .join(memo.category)
            .where(memo.user.eq(user), eqMemoStatus(memoStatus))
            .orderBy(memo.createdDate.asc())
            .fetch();
    }

    private BooleanExpression eqMemoStatus(String memoStatus)  {
        if(memoStatus == null) {
            return null;
        }
        return memo.memoStatus.eq(MemoStatus.valueOf(memoStatus));
    }
}
