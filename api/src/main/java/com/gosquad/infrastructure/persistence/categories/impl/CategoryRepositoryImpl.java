package com.gosquad.infrastructure.persistence.categories.impl;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.Repository;
import com.gosquad.infrastructure.persistence.categories.CategoryModel;
import com.gosquad.infrastructure.persistence.categories.CategoryRepository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


@org.springframework.stereotype.Repository
public class CategoryRepositoryImpl extends Repository<CategoryModel> implements CategoryRepository {

    public static final String TABLE_NAME = "category";

    public CategoryRepositoryImpl() throws SQLException {
        super(TABLE_NAME);
    }


    @Override
    protected CategoryModel mapResultSetToEntity(java.sql.ResultSet rs) throws SQLException {
        return new CategoryModel(
                rs.getInt("id"),
                rs.getString("name")
        );
    }

    public CategoryModel getCategoryByName(String name) throws SQLException, NotFoundException {
        return findBy("name",name);
    }

    public void createCategory(CategoryModel category) throws SQLException {
        Map<String, Object> value = new HashMap<>();
        value.put("name", category.getName());
        category.setId(insert(value));
    }

    public void updateCategory(CategoryModel category) throws SQLException {
        Map<String, Object> value = new HashMap<>();
        value.put("name", category.getName());
        updateBy("id", category.getId(), value);
    }
    public void deleteCategory(int id) throws SQLException {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", id);
        deleteBy(conditions);
    }
}
