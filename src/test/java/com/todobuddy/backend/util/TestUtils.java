package com.todobuddy.backend.util;

import com.todobuddy.backend.entity.Category;
import com.todobuddy.backend.entity.Memo;
import com.todobuddy.backend.entity.User;

public class TestUtils {

    public static User createUser(String email, String password, String nickName) {
        return User.builder()
            .email(email)
            .password(password)
            .nickName(nickName)
            .build();
    }

    public static Category createCategory(User user, String categoryName) {
        return Category.builder()
            .categoryName(categoryName)
            .user(user)
            .build();
    }

    public static Memo createMemo(User user, Category category, String content, String link) {
        return Memo.builder()
            .content(content)
            .user(user)
            .category(category)
            .link(link)
            .build();
    }
}
