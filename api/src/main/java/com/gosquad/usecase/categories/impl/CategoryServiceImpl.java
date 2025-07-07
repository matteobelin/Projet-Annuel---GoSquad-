package com.gosquad.usecase.categories.impl;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.categories.CategoryEntity;
import com.gosquad.infrastructure.persistence.categories.CategoryModel;
import com.gosquad.infrastructure.persistence.categories.CategoryRepository;
import com.gosquad.usecase.categories.CategoryMapper;
import com.gosquad.usecase.categories.CategoryService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryEntity> getAllCategoryByCompanyId(int companyId) throws Exception{
        List<CategoryModel> categoryModel = categoryRepository.getAllByCompanyId(companyId);
        return categoryMapper.modelToEntity(categoryModel);

    }

    public CategoryEntity getCategoryById(int id) throws NotFoundException, SQLException{
        CategoryModel categoryModel = categoryRepository.getById(id);
        return categoryMapper.modelToEntity(categoryModel);
    }

    public CategoryEntity getCategoryByNameAndCompanyId(String name, int companyId) throws SQLException, NotFoundException{
        CategoryModel categoryModel = categoryRepository.getCategoryByNameAndCompanyId(name, companyId);
        return categoryMapper.modelToEntity(categoryModel);
    }

    public void createCategory(CategoryEntity category) throws SQLException{
        CategoryModel categoryModel = categoryMapper.entityToModel(category);
        categoryRepository.createCategory(categoryModel);
    }

    public void updateCategory(CategoryEntity category) throws SQLException{
        CategoryModel categoryModel = categoryMapper.entityToModel(category);
        categoryRepository.updateCategory(categoryModel);
    }

    public void deleteCategory(int id) throws SQLException{
        categoryRepository.deleteCategory(id);
    }
}
