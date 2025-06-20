package com.gosquad.usecase.cities;

import static org.junit.jupiter.api.Assertions.*;

import com.gosquad.domain.cities.CityEntity;
import com.gosquad.infrastructure.persistence.cities.CityModel;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class CityMapperTest {

    private final CityMapper mapper = new CityMapper();

    @Test
    void testModelToEntity_single() {
        // Arrange
        CityModel model = new CityModel(1, "Paris", "75001", 10);

        // Act
        CityEntity entity = mapper.modelToEntity(model);

        // Assert
        assertNotNull(entity);
        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getCityName(), entity.getCityName());
        assertEquals(model.getPostalCode(), entity.getPostalCode());
        assertEquals(model.getCountryId(), entity.getCountryId());
    }

    @Test
    void testEntityToModel_single() {
        // Arrange
        CityEntity entity = new CityEntity(2, "Lyon", "69001", 11);

        // Act
        CityModel model = mapper.entityToModel(entity);

        // Assert
        assertNotNull(model);
        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getCityName(), model.getCityName());
        assertEquals(entity.getPostalCode(), model.getPostalCode());
        assertEquals(entity.getCountryId(), model.getCountryId());
    }

    @Test
    void testModelToEntity_list() {
        // Arrange
        List<CityModel> models = Arrays.asList(
                new CityModel(1, "Paris", "75001", 10),
                new CityModel(2, "Lyon", "69001", 11)
        );

        // Act
        List<CityEntity> entities = models.stream()
                .map(mapper::modelToEntity)
                .toList();

        // Assert
        assertEquals(models.size(), entities.size());
        for (int i = 0; i < models.size(); i++) {
            assertEquals(models.get(i).getId(), entities.get(i).getId());
            assertEquals(models.get(i).getCityName(), entities.get(i).getCityName());
            assertEquals(models.get(i).getPostalCode(), entities.get(i).getPostalCode());
            assertEquals(models.get(i).getCountryId(), entities.get(i).getCountryId());
        }
    }

    @Test
    void testEntityToModel_list() {
        // Arrange
        List<CityEntity> entities = Arrays.asList(
                new CityEntity(1, "Paris", "75001", 10),
                new CityEntity(2, "Lyon", "69001", 11)
        );

        // Act
        List<CityModel> models = entities.stream()
                .map(mapper::entityToModel)
                .toList();

        // Assert
        assertEquals(entities.size(), models.size());
        for (int i = 0; i < entities.size(); i++) {
            assertEquals(entities.get(i).getId(), models.get(i).getId());
            assertEquals(entities.get(i).getCityName(), models.get(i).getCityName());
            assertEquals(entities.get(i).getPostalCode(), models.get(i).getPostalCode());
            assertEquals(entities.get(i).getCountryId(), models.get(i).getCountryId());
        }
    }
}
