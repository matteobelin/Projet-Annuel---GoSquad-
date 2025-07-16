package com.gosquad.usecase.travels;

import com.gosquad.domain.travels.TravelInformationEntity;
import com.gosquad.infrastructure.persistence.travels.TravelModel;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TravelMapperTest {

    private final TravelMapper mapper = new TravelMapper();

    private TravelModel createSampleModel() {
        return new TravelModel(
                1,
                "Voyage à Paris",
                "Paris, France",
                Date.valueOf("2024-06-01"),
                Date.valueOf("2024-06-07"),
                1500.0,
                "Un magnifique voyage à Paris avec visite des monuments",
                10,
                1
        );
    }

    private TravelInformationEntity createSampleEntity() {
        return new TravelInformationEntity(
                1,
                "Voyage à Paris",
                "Un magnifique voyage à Paris avec visite des monuments",
                LocalDate.of(2024, 6, 1),
                LocalDate.of(2024, 6, 7),
                "Paris, France",
                1500.0,
                10,
                null,
                null
        );
    }

    @Test
    void testModelToEntity() {
        TravelModel model = createSampleModel();
        TravelInformationEntity entity = mapper.modelToEntity(model);

        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getTitle(), entity.getTitle());
        assertEquals(model.getDescription(), entity.getDescription());
        assertEquals(model.getStartDate().toLocalDate(), entity.getStartDate());
        assertEquals(model.getEndDate().toLocalDate(), entity.getEndDate());
        assertEquals(model.getDestination(), entity.getDestination());
        assertEquals(model.getBudget(), entity.getBudget());
        assertEquals(model.getGroupId(), entity.getGroupId());
        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());
    }

    @Test
    void testEntityToModel() {
        TravelInformationEntity entity = createSampleEntity();
        TravelModel model = mapper.entityToModel(entity);

        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getTitle(), model.getTitle());
        assertEquals(entity.getDescription(), model.getDescription());
        assertEquals(Date.valueOf(entity.getStartDate()), model.getStartDate());
        assertEquals(Date.valueOf(entity.getEndDate()), model.getEndDate());
        assertEquals(entity.getDestination(), model.getDestination());
        assertEquals(entity.getBudget(), model.getBudget());
        assertEquals(entity.getGroupId(), model.getGroupId());
        assertNull(model.getCompanyId());
    }

    @Test
    void testModelListToEntityList() {
        TravelModel model1 = createSampleModel();
        TravelModel model2 = new TravelModel(
                2,
                "Voyage à Rome",
                "Rome, Italie",
                Date.valueOf("2024-07-15"),
                Date.valueOf("2024-07-22"),
                2000.0,
                "Découverte de la Rome antique",
                11,
                1
        );

        List<TravelModel> models = List.of(model1, model2);

        List<TravelInformationEntity> entities = mapper.modelToEntity(models);

        assertEquals(models.size(), entities.size());

        for (int i = 0; i < models.size(); i++) {
            assertEquals(models.get(i).getId(), entities.get(i).getId());
            assertEquals(models.get(i).getTitle(), entities.get(i).getTitle());
            assertEquals(models.get(i).getDescription(), entities.get(i).getDescription());
            assertEquals(models.get(i).getStartDate().toLocalDate(), entities.get(i).getStartDate());
            assertEquals(models.get(i).getEndDate().toLocalDate(), entities.get(i).getEndDate());
            assertEquals(models.get(i).getDestination(), entities.get(i).getDestination());
            assertEquals(models.get(i).getBudget(), entities.get(i).getBudget());
            assertEquals(models.get(i).getGroupId(), entities.get(i).getGroupId());
        }
    }

    @Test
    void testEntityListToModelList() {
        TravelInformationEntity entity1 = createSampleEntity();
        TravelInformationEntity entity2 = new TravelInformationEntity(
                2,
                "Voyage à Rome",
                "Découverte de la Rome antique",
                LocalDate.of(2024, 7, 15),
                LocalDate.of(2024, 7, 22),
                "Rome, Italie",
                2000.0,
                11,
                null,
                null
        );

        List<TravelInformationEntity> entities = List.of(entity1, entity2);

        List<TravelModel> models = mapper.entityToModel(entities);

        assertEquals(entities.size(), models.size());

        for (int i = 0; i < entities.size(); i++) {
            assertEquals(entities.get(i).getId(), models.get(i).getId());
            assertEquals(entities.get(i).getTitle(), models.get(i).getTitle());
            assertEquals(entities.get(i).getDescription(), models.get(i).getDescription());
            assertEquals(Date.valueOf(entities.get(i).getStartDate()), models.get(i).getStartDate());
            assertEquals(Date.valueOf(entities.get(i).getEndDate()), models.get(i).getEndDate());
            assertEquals(entities.get(i).getDestination(), models.get(i).getDestination());
            assertEquals(entities.get(i).getBudget(), models.get(i).getBudget());
            assertEquals(entities.get(i).getGroupId(), models.get(i).getGroupId());
        }
    }

    @Test
    void testModelToEntity_withNullModel() {
        TravelInformationEntity entity = mapper.modelToEntity((TravelModel) null);
        assertNull(entity);
    }

    @Test
    void testEntityToModel_withNullEntity() {
        TravelModel model = mapper.entityToModel((TravelInformationEntity) null);
        assertNull(model);
    }

    @Test
    void testModelListToEntityList_withNullList() {
        List<TravelInformationEntity> entities = mapper.modelToEntity((List<TravelModel>) null);
        assertNull(entities);
    }

    @Test
    void testEntityListToModelList_withNullList() {
        List<TravelModel> models = mapper.entityToModel((List<TravelInformationEntity>) null);
        assertNull(models);
    }

    @Test
    void testModelToEntity_withNullDates() {
        TravelModel model = new TravelModel(
                1,
                "Voyage sans dates",
                "Destination inconnue",
                null,
                null,
                1000.0,
                "Description",
                10,
                1
        );

        TravelInformationEntity entity = mapper.modelToEntity(model);

        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getTitle(), entity.getTitle());
        assertNull(entity.getStartDate());
        assertNull(entity.getEndDate());
    }

    @Test
    void testEntityToModel_withNullDates() {
        TravelInformationEntity entity = new TravelInformationEntity(
                1,
                "Voyage sans dates",
                "Description",
                null,
                null,
                "Destination inconnue",
                1000.0,
                10,
                null,
                null
        );

        TravelModel model = mapper.entityToModel(entity);

        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getTitle(), model.getTitle());
        assertNull(model.getStartDate());
        assertNull(model.getEndDate());
    }
}