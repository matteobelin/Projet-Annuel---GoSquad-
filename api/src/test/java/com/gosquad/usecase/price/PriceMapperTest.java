package com.gosquad.usecase.price;

import com.gosquad.domain.price.PriceEntity;
import com.gosquad.infrastructure.persistence.price.PriceModel;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PriceMapperTest {

    private final PriceMapper mapper = new PriceMapper();

    @Test
    void testModelToEntity() {
        PriceModel model = new PriceModel(
                1,
                new BigDecimal("100.00"),
                new BigDecimal("0.20"),
                new BigDecimal("20.00"),
                new BigDecimal("120.00")
        );

        PriceEntity entity = mapper.modelToEntity(model);

        assertNotNull(entity);
        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getNetPrice(), entity.getNetPrice());
        assertEquals(model.getVatRate(), entity.getVatRate());
        assertEquals(model.getVatAmount(), entity.getVatAmount());
        assertEquals(model.getGrossPrice(), entity.getGrossPrice());
    }

    @Test
    void testEntityToModel() {
        PriceEntity entity = new PriceEntity(
                2,
                new BigDecimal("200.00"),
                new BigDecimal("0.10"),
                new BigDecimal("20.00"),
                new BigDecimal("220.00")
        );

        PriceModel model = mapper.entityToModel(entity);

        assertNotNull(model);
        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getNetPrice(), model.getNetPrice());
        assertEquals(entity.getVatRate(), model.getVatRate());
        assertEquals(entity.getVatAmount(), model.getVatAmount());
        assertEquals(entity.getGrossPrice(), model.getGrossPrice());
    }
}
