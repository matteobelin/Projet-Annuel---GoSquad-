package com.gosquad.usecase.categories;

import com.gosquad.presentation.DTO.categories.CategoryRequestDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface CategoryDeleteService {

    void deleteCategory(HttpServletRequest request, CategoryRequestDTO dto) throws Exception;
}
