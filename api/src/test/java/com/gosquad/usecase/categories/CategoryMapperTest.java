package com.gosquad.usecase.categories;

import com.gosquad.domain.categories.CategoryEntity;
import com.gosquad.infrastructure.persistence.categories.CategoryModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryMapperTest {

    private final CategoryMapper mapper = new CategoryMapper();

    @Test
    void testModelToEntity() {
        CategoryModel model = new CategoryModel(1, "Sports", 10);

        CategoryEntity entity = mapper.modelToEntity(model);

        assertNotNull(entity);
        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getName(), entity.getName());
        assertEquals(model.getCompanyId(), entity.getCompanyId());
    }

    @Test
    void testEntityToModel() {
        CategoryEntity entity = new CategoryEntity(2, "Music", 20);

        CategoryModel model = mapper.entityToModel(entity);

        assertNotNull(model);
        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getName(), model.getName());
        assertEquals(entity.getCompanyId(), model.getCompanyId());
    }

    @Test
    void testModelToEntityList() {
        List<CategoryModel> models = List.of(
                new CategoryModel(1, "Sports", 10),
                new CategoryModel(2, "Music", 20),
                new CategoryModel(3, "Tech", 30)
        );

        List<CategoryEntity> entities = mapper.modelToEntity(models);

        assertNotNull(entities);
        assertEquals(models.size(), entities.size());

        for (int i = 0; i < models.size(); i++) {
            assertEquals(models.get(i).getId(), entities.get(i).getId());
            assertEquals(models.get(i).getName(), entities.get(i).getName());
            assertEquals(models.get(i).getCompanyId(), entities.get(i).getCompanyId());
        }
    }
}
