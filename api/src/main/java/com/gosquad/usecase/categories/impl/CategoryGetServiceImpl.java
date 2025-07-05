package com.gosquad.usecase.categories.impl;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.categories.CategoryEntity;
import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.presentation.DTO.categories.CategoryResponseDTO;
import com.gosquad.usecase.categories.CategoryGetService;
import com.gosquad.usecase.categories.CategoryService;
import com.gosquad.usecase.company.utils.GetCompany;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class CategoryGetServiceImpl implements CategoryGetService {

    private final GetCompany getCompany;
    private final CategoryService categoryService;

    public CategoryGetServiceImpl(CategoryService categoryService, GetCompany getCompany) {
        this.categoryService = categoryService;
        this.getCompany = getCompany;
    }


    public List<CategoryResponseDTO> getAllCategories(HttpServletRequest request) throws Exception {
            CompanyEntity company = getCompany.getCompanyFromRequest(request);
            List<CategoryEntity> category = categoryService.getAllCategoryByCompanyId(company.getId());
            return category.stream()
                    .map(cat -> new CategoryResponseDTO(cat.getId(), cat.getName()))
                    .toList();

    }
    public CategoryResponseDTO getCategoryById(HttpServletRequest request) throws NotFoundException, SQLException {
            CategoryEntity category = categoryService.getCategoryById(Integer.parseInt(request.getParameter("id")));
            return new CategoryResponseDTO(category.getId(), category.getName());

    }

    public CategoryResponseDTO getCategoryByNameAndCompanyId(HttpServletRequest request)throws Exception{
            String name = request.getParameter("name");
            CompanyEntity company = getCompany.getCompanyFromRequest(request);
            CategoryEntity category = categoryService.getCategoryByNameAndCompanyId(name, company.getId());
            return new CategoryResponseDTO(category.getId(), category.getName());
    }


}
