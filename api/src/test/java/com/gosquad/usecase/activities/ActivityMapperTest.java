package com.gosquad.usecase.activities;

import com.gosquad.domain.activities.ActivityEntity;
import com.gosquad.infrastructure.persistence.activities.ActivityModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ActivityMapperTest {

    private final ActivityMapper mapper = new ActivityMapper();

    @Test
    void testModelToEntity() {
        ActivityModel model = new ActivityModel(
                1,
                "Activity Name",
                "Description",
                10,
                20,
                30,
                40
        );

        ActivityEntity entity = mapper.modelToEntity(model);

        assertNotNull(entity);
        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getName(), entity.getName());
        assertEquals(model.getDescription(), entity.getDescription());
        assertEquals(model.getAddressId(), entity.getAddressId());
        assertEquals(model.getPriceId(), entity.getPriceId());
        assertEquals(model.getCategoryId(), entity.getCategoryId());
        assertEquals(model.getCompanyId(), entity.getCompanyId());
    }

    @Test
    void testEntityToModel() {
        ActivityEntity entity = new ActivityEntity(
                2,
                "Another Activity",
                "Another Description",
                11,
                21,
                31,
                41
        );

        ActivityModel model = mapper.entityToModel(entity);

        assertNotNull(model);
        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getName(), model.getName());
        assertEquals(entity.getDescription(), model.getDescription());
        assertEquals(entity.getAddressId(), model.getAddressId());
        assertEquals(entity.getPriceId(), model.getPriceId());
        assertEquals(entity.getCategoryId(), model.getCategoryId());
        assertEquals(entity.getCompanyId(), model.getCompanyId());
    }

    @Test
    void testModelToEntityList() {
        List<ActivityModel> models = List.of(
                new ActivityModel(1, "Name1", "Desc1", 10, 20, 30, 40),
                new ActivityModel(2, "Name2", "Desc2", 11, 21, 31, 41)
        );

        List<ActivityEntity> entities = mapper.modelToEntity(models);

        assertNotNull(entities);
        assertEquals(models.size(), entities.size());

        for (int i = 0; i < models.size(); i++) {
            ActivityModel m = models.get(i);
            ActivityEntity e = entities.get(i);

            assertEquals(m.getId(), e.getId());
            assertEquals(m.getName(), e.getName());
            assertEquals(m.getDescription(), e.getDescription());
            assertEquals(m.getAddressId(), e.getAddressId());
            assertEquals(m.getPriceId(), e.getPriceId());
            assertEquals(m.getCategoryId(), e.getCategoryId());
            assertEquals(m.getCompanyId(), e.getCompanyId());
        }
    }
}
