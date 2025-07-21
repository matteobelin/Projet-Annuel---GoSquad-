package com.gosquad.usecase.categories.impl;

import com.gosquad.presentation.DTO.categories.CategoryRequestDTO;
import com.gosquad.usecase.categories.CategoryDeleteService;
import com.gosquad.usecase.categories.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class CategoryDeleteImpl implements CategoryDeleteService {

    private final CategoryService categoryService;

    public CategoryDeleteImpl(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public void deleteCategory(HttpServletRequest request, CategoryRequestDTO dto) throws Exception {
        categoryService.deleteCategory(dto.id());
    }
}
