package com.gosquad.usecase.categories.impl;

import com.gosquad.domain.categories.CategoryEntity;
import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.presentation.DTO.categories.CategoryRequestDTO;
import com.gosquad.usecase.categories.CategoryPostService;
import com.gosquad.usecase.categories.CategoryService;
import com.gosquad.usecase.company.utils.GetCompany;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;



@Service
public class CategoryPostServiceImpl implements CategoryPostService {


    private final CategoryService categoryService;
    private final GetCompany getCompany;

    public CategoryPostServiceImpl(CategoryService categoryService, GetCompany getCompany) {
        this.categoryService = categoryService;
        this.getCompany = getCompany;
    }

    public void createCategory(HttpServletRequest request, CategoryRequestDTO dto) throws Exception {
        CompanyEntity company = getCompany.getCompanyFromRequest(request);
        CategoryEntity existingCategory = null;

        try {
            existingCategory = categoryService.getCategoryByNameAndCompanyId(dto.name(), company.getId());
        } catch (Exception e) {
            if (!e.getMessage().contains("No record found matching conditions")) {
                throw e;
            }
        }
        if (existingCategory != null) {
            throw new Exception("Category with this name already exists for this company.");
        }

        CategoryEntity category = new CategoryEntity(null, dto.name(), company.getId());
        categoryService.createCategory(category);
    }


}
