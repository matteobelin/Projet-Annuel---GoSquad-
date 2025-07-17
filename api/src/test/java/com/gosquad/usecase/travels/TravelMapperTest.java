package com.gosquad.usecase.travels;

import com.gosquad.domain.travels.TravelInformationEntity;
import com.gosquad.infrastructure.persistence.travels.TravelModel;
import com.gosquad.usecase.travels.TravelMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

class TravelMapperTest {

    private final TravelMapper mapper = new TravelMapper();

    private TravelModel createSampleModel() {
        return new TravelModel(1, "Voyage Test", "Paris", new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis()), 1500.0, "Description", 10, 1);
    }

    private TravelInformationEntity createSampleEntity() {
        return new TravelInformationEntity(
            1,
            "Voyage Test",
            "Description",
            java.time.LocalDate.of(2024, 6, 1),
            java.time.LocalDate.of(2024, 6, 7),
            "Paris",
            1500.0,
            10,
            java.time.LocalDateTime.now(),
            java.time.LocalDateTime.now(),
            1
        );
    }

    @Test
    void testModelToEntity() {
        TravelModel model = createSampleModel();
        TravelInformationEntity entity = mapper.modelToEntity(model);

        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getTitle(), entity.getTitle());
        assertEquals(model.getDescription(), entity.getDescription());
        assertEquals(model.getStartDate(), entity.getStartDate());
        assertEquals(model.getEndDate(), entity.getEndDate());
        assertEquals(model.getDestination(), entity.getDestination());
        assertEquals(model.getBudget(), entity.getBudget());
        assertEquals(model.getGroupId(), entity.getGroupId());
        assertEquals(model.getCompanyId(), entity.getCompanyId());
    }

    @Test
    void testEntityToModel() {
        TravelInformationEntity entity = createSampleEntity();
        TravelModel model = mapper.entityToModel(entity);

        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getTitle(), model.getTitle());
        assertEquals(entity.getDescription(), model.getDescription());
        assertEquals(entity.getStartDate(), model.getStartDate());
        assertEquals(entity.getEndDate(), model.getEndDate());
        assertEquals(entity.getDestination(), model.getDestination());
        assertEquals(entity.getBudget(), model.getBudget());
        assertEquals(entity.getGroupId(), model.getGroupId());
        assertEquals(entity.getCompanyId(), model.getCompanyId());
    }

    @Test
    void testModelListToEntityList() {
        TravelModel model1 = createSampleModel();
        TravelModel model2 = new TravelModel(2, "Voyage Deux", "Rome", new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis()), 2000.0, "Desc2", 20, 2);
        List<TravelModel> models = List.of(model1, model2);

        List<TravelInformationEntity> entities = mapper.modelToEntity(models);
        assertEquals(models.size(), entities.size());
        for (int i = 0; i < models.size(); i++) {
            assertEquals(models.get(i).getId(), entities.get(i).getId());
            assertEquals(models.get(i).getTitle(), entities.get(i).getTitle());
            assertEquals(models.get(i).getDescription(), entities.get(i).getDescription());
            assertEquals(models.get(i).getStartDate(), entities.get(i).getStartDate());
            assertEquals(models.get(i).getEndDate(), entities.get(i).getEndDate());
            assertEquals(models.get(i).getDestination(), entities.get(i).getDestination());
            assertEquals(models.get(i).getBudget(), entities.get(i).getBudget());
            assertEquals(models.get(i).getGroupId(), entities.get(i).getGroupId());
            assertEquals(models.get(i).getCompanyId(), entities.get(i).getCompanyId());
        }
    }
}
