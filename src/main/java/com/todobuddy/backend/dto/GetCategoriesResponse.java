package com.todobuddy.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetCategoriesResponse {

    private Long categoryId;
    private String categoryName;
}
