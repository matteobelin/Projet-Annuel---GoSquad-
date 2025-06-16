package com.gosquad.usecase.countries;

import static org.junit.jupiter.api.Assertions.*;

import com.gosquad.domain.countries.CountryEntity;
import com.gosquad.infrastructure.persistence.countries.CountryModel;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class CountryMapperTest {

    private final CountryMapper mapper = new CountryMapper();

    @Test
    void testModelToEntity_single() {
        // Arrange
        CountryModel model = new CountryModel(1, "FR", "France");

        // Act
        CountryEntity entity = mapper.modelToEntity(model);

        // Assert
        assertNotNull(entity);
        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getIsoCode(), entity.getIsoCode());
        assertEquals(model.getCountryName(), entity.getCountryName());
    }

    @Test
    void testEntityToModel_single() {
        // Arrange
        CountryEntity entity = new CountryEntity(2, "US", "United States");

        // Act
        CountryModel model = mapper.entityToModel(entity);

        // Assert
        assertNotNull(model);
        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getIsoCode(), model.getIsoCode());
        assertEquals(entity.getCountryName(), model.getCountryName());
    }

    @Test
    void testModelToEntity_list() {
        // Arrange
        List<CountryModel> models = Arrays.asList(
                new CountryModel(1, "FR", "France"),
                new CountryModel(2, "US", "United States")
        );

        // Act
        List<CountryEntity> entities = models.stream()
                .map(mapper::modelToEntity)
                .toList();

        // Assert
        assertEquals(models.size(), entities.size());
        for (int i = 0; i < models.size(); i++) {
            assertEquals(models.get(i).getId(), entities.get(i).getId());
            assertEquals(models.get(i).getIsoCode(), entities.get(i).getIsoCode());
            assertEquals(models.get(i).getCountryName(), entities.get(i).getCountryName());
        }
    }

    @Test
    void testEntityToModel_list() {
        // Arrange
        List<CountryEntity> entities = Arrays.asList(
                new CountryEntity(1, "FR", "France"),
                new CountryEntity(2, "US", "United States")
        );

        // Act
        List<CountryModel> models = entities.stream()
                .map(mapper::entityToModel)
                .toList();

        // Assert
        assertEquals(entities.size(), models.size());
        for (int i = 0; i < entities.size(); i++) {
            assertEquals(entities.get(i).getId(), models.get(i).getId());
            assertEquals(entities.get(i).getIsoCode(), models.get(i).getIsoCode());
            assertEquals(entities.get(i).getCountryName(), models.get(i).getCountryName());
        }
    }
}
