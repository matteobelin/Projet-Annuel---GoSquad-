package com.gosquad.infrastructure.persistence.categories;

import com.gosquad.core.exceptions.NotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface CategoryRepository {
    List<CategoryModel> getAllByCompanyId(int companyId) throws Exception;
    CategoryModel getById(int id) throws NotFoundException, SQLException;
    List<CategoryModel> findByIds(List<Integer> ids) throws SQLException;
    CategoryModel getCategoryByNameAndCompanyId(String name, int companyId) throws SQLException, NotFoundException;
    void createCategory(CategoryModel category) throws SQLException;
    void updateCategory(CategoryModel category) throws SQLException;
    void deleteCategory(int id) throws SQLException;
}
