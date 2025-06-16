package com.gosquad.usecase.addresses;

import static org.junit.jupiter.api.Assertions.*;

import com.gosquad.domain.addresses.AddressEntity;
import com.gosquad.infrastructure.persistence.addresses.AddressModel;
import org.junit.jupiter.api.Test;

class AddressMapperTest {

    private final AddressMapper mapper = new AddressMapper();

    @Test
    void testModelToEntity() {
        // Arrange
        AddressModel model = new AddressModel(1, "123 Rue de la Paix", 10);

        // Act
        AddressEntity entity = mapper.modelToEntity(model);

        // Assert
        assertNotNull(entity);
        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getAddressLine(), entity.getAddressLine());
        assertEquals(model.getCityId(), entity.getCityId());
    }

    @Test
    void testEntityToModel() {
        // Arrange
        AddressEntity entity = new AddressEntity(2, "456 Avenue des Champs", 20);

        // Act
        AddressModel model = mapper.entityToModel(entity);

        // Assert
        assertNotNull(model);
        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getAddressLine(), model.getAddressLine());
        assertEquals(entity.getCityId(), model.getCityId());
    }
}
