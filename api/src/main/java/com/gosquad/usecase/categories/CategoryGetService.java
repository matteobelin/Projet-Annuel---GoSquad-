package com.gosquad.usecase.categories;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.presentation.DTO.categories.CategoryResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.util.List;

public interface CategoryGetService {
    List<CategoryResponseDTO> getAllCategories(HttpServletRequest request)throws Exception;
    CategoryResponseDTO getCategoryById(HttpServletRequest request)throws NotFoundException, SQLException;
    CategoryResponseDTO getCategoryByNameAndCompanyId(HttpServletRequest request) throws Exception;

}
