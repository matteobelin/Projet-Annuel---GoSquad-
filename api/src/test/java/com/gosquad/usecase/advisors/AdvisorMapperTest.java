package com.gosquad.usecase.advisors;

import static org.junit.jupiter.api.Assertions.*;

import com.gosquad.domain.advisors.AdvisorEntity;
import com.gosquad.infrastructure.persistence.advisors.AdvisorModel;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Arrays;

class AdvisorMapperTest {

    private final AdvisorMapper mapper = new AdvisorMapper();

    @Test
    void testModelToEntity_single() {
        // Arrange
        AdvisorModel model = new AdvisorModel(1, "John", "Doe", "john@example.com", "1234567890", 10, "pass", "ROLE_USER");

        // Act
        AdvisorEntity entity = mapper.modelToEntity(model);


        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getFirstname(), entity.getFirstname());
        assertEquals(model.getLastname(), entity.getLastname());
        assertEquals(model.getEmail(), entity.getEmail());
        assertEquals(model.getPhone(), entity.getPhone());
        assertEquals(model.getRole(), entity.getRole());
    }

    @Test
    void testModelToEntity_list() {
        // Arrange
        AdvisorModel model1 = new AdvisorModel(1, "John", "Doe", "john@example.com", "1234567890", 10, "pass", "ROLE_USER");
        AdvisorModel model2 = new AdvisorModel(2, "Jane", "Smith", "jane@example.com", "0987654321", 11, "pass2", "ROLE_ADMIN");

        List<AdvisorModel> modelList = Arrays.asList(model1, model2);

        // Act
        List<AdvisorEntity> entityList = mapper.modelToEntity(modelList);

        // Assert
        assertEquals(modelList.size(), entityList.size());

        for (int i = 0; i < modelList.size(); i++) {
            assertEquals(modelList.get(i).getId(), entityList.get(i).getId());
            assertEquals(modelList.get(i).getFirstname(), entityList.get(i).getFirstname());
            assertEquals(modelList.get(i).getLastname(), entityList.get(i).getLastname());
            assertEquals(modelList.get(i).getEmail(), entityList.get(i).getEmail());
            assertEquals(modelList.get(i).getPhone(), entityList.get(i).getPhone());
            assertEquals(modelList.get(i).getRole(), entityList.get(i).getRole());
        }
    }
}
