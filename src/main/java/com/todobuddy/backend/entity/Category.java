package com.todobuddy.backend.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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
    public Category(String categoryName, User user, int categoryOrderId) {
        this.categoryName = categoryName;
        this.user = user;
        this.categoryOrderId = categoryOrderId;
    }

    @Column(name = "category_order_id")
    private int categoryOrderId; // 카테고리 순서

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    List<Memo> memos = new ArrayList<>();

    public void updateCategoryName(String categoryName)  {
        this.categoryName = categoryName;
    }

}
