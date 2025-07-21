package com.gosquad.usecase.categories.impl;

import com.gosquad.domain.categories.CategoryEntity;
import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.presentation.DTO.categories.CategoryRequestDTO;
import com.gosquad.usecase.categories.CategoryService;
import com.gosquad.usecase.categories.CategoryUpdateService;
import com.gosquad.usecase.company.utils.GetCompany;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class CategoryUpdateServiceImpl implements CategoryUpdateService {

    private final GetCompany getCompany;
    private final CategoryService categoryService;

    public CategoryUpdateServiceImpl(CategoryService categoryService, GetCompany getCompany) {
        this.categoryService = categoryService;
        this.getCompany = getCompany;
    }

    public void updateCategory(HttpServletRequest request, CategoryRequestDTO dto) throws Exception {
        CompanyEntity company = getCompany.getCompanyFromRequest(request);
        CategoryEntity existingCategory = null;



        try {
            existingCategory = categoryService.getCategoryByNameAndCompanyId(dto.newName(), company.getId());

        } catch (Exception e) {
            if (!e.getMessage().contains("No record found matching conditions")) {
                throw e;
            }
        }

        if (existingCategory != null) {
            throw new Exception("Category with this name already exists for this company.");
        }

        CategoryEntity category = new CategoryEntity(
                dto.id(),
                dto.newName(),
                company.getId()
        );

        categoryService.updateCategory(category);
    }

}
