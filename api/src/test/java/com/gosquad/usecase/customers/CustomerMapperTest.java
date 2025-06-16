package com.gosquad.usecase.customers;

import com.gosquad.domain.customers.CustomerEntity;
import com.gosquad.infrastructure.persistence.customers.CustomerModel;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerMapperTest {

    private final CustomerMapper mapper = new CustomerMapper();

    private CustomerModel createSampleModel() {
        return new CustomerModel(
                1,
                "Jean",
                "Dupont",
                "jean.dupont@example.com",
                "0123456789",
                new Date(System.currentTimeMillis()),
                "ID123456",
                new Date(System.currentTimeMillis()),
                "http://example.com/idcard.jpg",
                "P1234567",
                new Date(System.currentTimeMillis()),
                "http://example.com/passport.jpg",
                10,
                20,
                30,
                40
        );
    }

    private CustomerEntity createSampleEntity() {
        return new CustomerEntity(
                1,
                "Jean",
                "Dupont",
                "jean.dupont@example.com",
                "0123456789",
                new Date(System.currentTimeMillis()),
                "ID123456",
                new Date(System.currentTimeMillis()),
                "http://example.com/idcard.jpg",
                "P1234567",
                new Date(System.currentTimeMillis()),
                "http://example.com/passport.jpg",
                10,
                20,
                30,
                40
        );
    }

    @Test
    void testModelToEntity() {
        CustomerModel model = createSampleModel();
        CustomerEntity entity = mapper.modelToEntity(model);

        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getFirstname(), entity.getFirstname());
        assertEquals(model.getLastname(), entity.getLastname());
        assertEquals(model.getEmail(), entity.getEmail());
        assertEquals(model.getPhoneNumber(), entity.getPhoneNumber());
        assertEquals(model.getBirthDate(), entity.getBirthDate());
        assertEquals(model.getIdCardNumber(), entity.getIdCardNumber());
        assertEquals(model.getIdCardExpirationDate(), entity.getIdCardExpirationDate());
        assertEquals(model.getIdCardCopyUrl(), entity.getIdCardCopyUrl());
        assertEquals(model.getPassportNumber(), entity.getPassportNumber());
        assertEquals(model.getPassportExpirationDate(), entity.getPassportExpirationDate());
        assertEquals(model.getPassportCopyUrl(), entity.getPassportCopyUrl());
        assertEquals(model.getCountryId(), entity.getCountryId());
        assertEquals(model.getAddressId(), entity.getAddressId());
        assertEquals(model.getBillingAddressId(), entity.getBillingAddressId());
        assertEquals(model.getCompanyId(), entity.getCompanyId());
    }

    @Test
    void testEntityToModel() {
        CustomerEntity entity = createSampleEntity();
        CustomerModel model = mapper.entityToModel(entity);

        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getFirstname(), model.getFirstname());
        assertEquals(entity.getLastname(), model.getLastname());
        assertEquals(entity.getEmail(), model.getEmail());
        assertEquals(entity.getPhoneNumber(), model.getPhoneNumber());
        assertEquals(entity.getBirthDate(), model.getBirthDate());
        assertEquals(entity.getIdCardNumber(), model.getIdCardNumber());
        assertEquals(entity.getIdCardExpirationDate(), model.getIdCardExpirationDate());
        assertEquals(entity.getIdCardCopyUrl(), model.getIdCardCopyUrl());
        assertEquals(entity.getPassportNumber(), model.getPassportNumber());
        assertEquals(entity.getPassportExpirationDate(), model.getPassportExpirationDate());
        assertEquals(entity.getPassportCopyUrl(), model.getPassportCopyUrl());
        assertEquals(entity.getCountryId(), model.getCountryId());
        assertEquals(entity.getAddressId(), model.getAddressId());
        assertEquals(entity.getBillingAddressId(), model.getBillingAddressId());
        assertEquals(entity.getCompanyId(), model.getCompanyId());
    }

    @Test
    void testModelListToEntityList() {
        CustomerModel model1 = createSampleModel();
        CustomerModel model2 = new CustomerModel(
                2,
                "Marie",
                "Curie",
                "marie.curie@example.com",
                "0987654321",
                new Date(System.currentTimeMillis()),
                "ID654321",
                new Date(System.currentTimeMillis()),
                "http://example.com/idcard2.jpg",
                "P7654321",
                new Date(System.currentTimeMillis()),
                "http://example.com/passport2.jpg",
                11,
                21,
                31,
                41
        );

        List<CustomerModel> models = List.of(model1, model2);

        List<CustomerEntity> entities = mapper.modelToEntity(models);

        assertEquals(models.size(), entities.size());

        for (int i = 0; i < models.size(); i++) {
            assertEquals(models.get(i).getId(), entities.get(i).getId());
            assertEquals(models.get(i).getFirstname(), entities.get(i).getFirstname());
            assertEquals(models.get(i).getLastname(), entities.get(i).getLastname());
            assertEquals(models.get(i).getEmail(), entities.get(i).getEmail());
            assertEquals(models.get(i).getPhoneNumber(), entities.get(i).getPhoneNumber());
            assertEquals(models.get(i).getBirthDate(), entities.get(i).getBirthDate());
            assertEquals(models.get(i).getIdCardNumber(), entities.get(i).getIdCardNumber());
            assertEquals(models.get(i).getIdCardExpirationDate(), entities.get(i).getIdCardExpirationDate());
            assertEquals(models.get(i).getIdCardCopyUrl(), entities.get(i).getIdCardCopyUrl());
            assertEquals(models.get(i).getPassportNumber(), entities.get(i).getPassportNumber());
            assertEquals(models.get(i).getPassportExpirationDate(), entities.get(i).getPassportExpirationDate());
            assertEquals(models.get(i).getPassportCopyUrl(), entities.get(i).getPassportCopyUrl());
            assertEquals(models.get(i).getCountryId(), entities.get(i).getCountryId());
            assertEquals(models.get(i).getAddressId(), entities.get(i).getAddressId());
            assertEquals(models.get(i).getBillingAddressId(), entities.get(i).getBillingAddressId());
            assertEquals(models.get(i).getCompanyId(), entities.get(i).getCompanyId());
        }
    }
}
