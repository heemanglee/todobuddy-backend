package com.todobuddy.backend.entity;

import com.todobuddy.backend.common.BooleanToIntegerConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE category SET deleted = 1 WHERE category_id = ?")
@SQLRestriction("deleted = 0")
public class Category extends BaseEntity{

    public static final int MAX_COUNT = 3; // 사용자가 등록할 수 있는 최대 개수

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name")
    private String categoryName; // 카테고리 이름

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 카테고리를 등록한 사용자

    @Builder
    public Category(String categoryName, User user, int categoryOrder) {
        this.categoryName = categoryName;
        this.user = user;
        this.categoryOrder = categoryOrder;
    }

    @Column(name = "category_order")
    private int categoryOrder; // 카테고리 순서

    @Convert(converter = BooleanToIntegerConverter.class)
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    public void updateCategoryName(String categoryName)  {
        this.categoryName = categoryName;
    }

}
