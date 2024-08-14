package com.todobuddy.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Memo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_id")
    private Long id;

    @Column(name = "memo_content")
    private String content; // 메모 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 메모 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category; // 메모 카테고리

    @Column(name = "memo_link")
    private String link; // 메모 링크

    @Column(name = "memo_deadline")
    private LocalDateTime memoDeadLine; // 메모 마감일

    @Builder
    public Memo(String content, User user, Category category, String link, LocalDateTime memoDeadLine)  {
        this.content = content;
        this.user = user;
        this.category = category;
        this.link = link;
        this.memoDeadLine = memoDeadLine;
    }

}
