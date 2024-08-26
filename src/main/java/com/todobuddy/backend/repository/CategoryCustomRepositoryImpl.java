package com.todobuddy.backend.repository;

import static com.todobuddy.backend.entity.QCategory.category;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.todobuddy.backend.dto.GetCategoriesResponse;
import com.todobuddy.backend.entity.Category;
import com.todobuddy.backend.entity.QUser;
import com.todobuddy.backend.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CategoryCustomRepositoryImpl implements CategoryCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GetCategoriesResponse> getCategories(User user) {
        return queryFactory
            .select(Projections.constructor(
                GetCategoriesResponse.class,
                category.id,
                category.categoryName,
                category.categoryOrderId
            ))
            .from(category)
            .join(category.user, QUser.user)
            .where(QUser.user.eq(user))
            .orderBy(category.categoryOrderId.asc())
            .fetch();
    }

    @Override
    public Category existCategory(User user, Long categoryId) {
        return queryFactory
            .selectFrom(category)
            .where(category.user.eq(user), category.id.eq(categoryId))
            .fetchOne();
    }
}
