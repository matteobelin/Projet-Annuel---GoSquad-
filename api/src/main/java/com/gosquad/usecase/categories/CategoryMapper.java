package com.gosquad.usecase.categories;


import com.gosquad.domain.categories.CategoryEntity;
import com.gosquad.infrastructure.persistence.categories.CategoryModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryMapper {

    public CategoryEntity modelToEntity(CategoryModel categoryModel) {
        return new CategoryEntity(
                categoryModel.getId(),
                categoryModel.getName(),
                categoryModel.getCompanyId()
        );
    }

    public CategoryModel entityToModel(CategoryEntity categoryEntity) {
        return new CategoryModel(
                categoryEntity.getId(),
                categoryEntity.getName(),
                categoryEntity.getCompanyId()
        );
    }

    public List<CategoryEntity> modelToEntity(List<CategoryModel> categoryModels) {
        return categoryModels.stream()
                .map(this::modelToEntity)
                .toList();
    }

}
