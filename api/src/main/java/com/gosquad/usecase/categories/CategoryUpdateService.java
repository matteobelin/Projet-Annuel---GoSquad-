package com.gosquad.usecase.categories;

import com.gosquad.presentation.DTO.categories.CategoryRequestDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface CategoryUpdateService {
        void updateCategory(HttpServletRequest request, CategoryRequestDTO dto) throws Exception;
}
