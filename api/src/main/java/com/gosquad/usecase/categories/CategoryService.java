package com.gosquad.usecase.categories;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.categories.CategoryEntity;

import java.sql.SQLException;
import java.util.List;

public interface CategoryService {
    List<CategoryEntity> getAllCategoryByCompanyId(int companyId) throws Exception;
    CategoryEntity getCategoryById(int id) throws NotFoundException, SQLException;
    CategoryEntity getCategoryByNameAndCompanyId(String name, int companyId)throws SQLException, NotFoundException;
    List<CategoryEntity> findByIds(List<Integer> ids) throws SQLException;
    void createCategory(CategoryEntity category) throws SQLException;
    void updateCategory(CategoryEntity category) throws SQLException;
    void deleteCategory(int id) throws SQLException;
}
