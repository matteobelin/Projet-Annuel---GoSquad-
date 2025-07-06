package com.gosquad.usecase.activities_customers;

import com.gosquad.domain.activities_customers.ActivityCustomerEntity;
import com.gosquad.infrastructure.persistence.activities_customers.ActivityCustomerModel;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ActivityCustomerMapperTest {

    private final ActivityCustomerMapper mapper = new ActivityCustomerMapper();

    @Test
    void testModelToEntity() {
        ActivityCustomerModel model = new ActivityCustomerModel(
                1, // activityId
                7, // customerId
                true, // participation
                LocalDateTime.of(2025, 7, 6, 10, 0),
                LocalDateTime.of(2025, 7, 6, 12, 0),
                1 // groupId
        );

        ActivityCustomerEntity entity = mapper.modelToEntity(model);

        assertNotNull(entity);
        assertEquals(model.getActivityId(), entity.getActivityId());
        assertEquals(model.getCustomerId(), entity.getCustomerId());
        assertEquals(model.getParticipation(), entity.getParticipation());
        assertEquals(model.getStartDate(), entity.getStartDate());
        assertEquals(model.getEndDate(), entity.getEndDate());
        assertEquals(model.getGroupId(), entity.getGroupId());
    }

    @Test
    void testEntityToModel() {
        ActivityCustomerEntity entity = new ActivityCustomerEntity(
                2,
                8,
                false,
                LocalDateTime.of(2025, 7, 7, 14, 0),
                LocalDateTime.of(2025, 7, 7, 16, 0),
                2
        );

        ActivityCustomerModel model = mapper.entityToModel(entity);

        assertNotNull(model);
        assertEquals(entity.getActivityId(), model.getActivityId());
        assertEquals(entity.getCustomerId(), model.getCustomerId());
        assertEquals(entity.getParticipation(), model.getParticipation());
        assertEquals(entity.getStartDate(), model.getStartDate());
        assertEquals(entity.getEndDate(), model.getEndDate());
        assertEquals(entity.getGroupId(), model.getGroupId());
    }

    @Test
    void testModelToEntityList() {
        List<ActivityCustomerModel> models = List.of(
                new ActivityCustomerModel(1, 7, true, LocalDateTime.of(2025,7,6,10,0), LocalDateTime.of(2025,7,6,12,0), 1),
                new ActivityCustomerModel(2, 8, false, LocalDateTime.of(2025,7,7,14,0), LocalDateTime.of(2025,7,7,16,0), 2)
        );

        List<ActivityCustomerEntity> entities = mapper.modelToEntity(models);

        assertNotNull(entities);
        assertEquals(models.size(), entities.size());

        for (int i = 0; i < models.size(); i++) {
            ActivityCustomerModel m = models.get(i);
            ActivityCustomerEntity e = entities.get(i);
            assertEquals(m.getActivityId(), e.getActivityId());
            assertEquals(m.getCustomerId(), e.getCustomerId());
            assertEquals(m.getParticipation(), e.getParticipation());
            assertEquals(m.getStartDate(), e.getStartDate());
            assertEquals(m.getEndDate(), e.getEndDate());
            assertEquals(m.getGroupId(), e.getGroupId());
        }
    }
}
