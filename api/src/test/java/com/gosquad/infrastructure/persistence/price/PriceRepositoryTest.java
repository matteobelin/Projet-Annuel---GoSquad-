package com.gosquad.infrastructure.persistence.price;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.price.impl.PriceRepositoryImpl;
import com.gosquad.infrastructure.persistence.utils.TestDatabaseHelper;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PriceRepositoryTest {

    private PriceRepositoryImpl repository;

    @BeforeAll
    public void setup() throws SQLException {
        TestDatabaseHelper.setupTestDatabase();
        repository = new PriceRepositoryImpl();
    }

    @AfterAll
    public void tearDown() throws SQLException {
        TestDatabaseHelper.tearDown();
    }

    @Test
    public void testGetById_Success() throws Exception {
        PriceModel price = repository.getById(1);
        assertNotNull(price);
        assertTrue(price.getNetPrice().compareTo(BigDecimal.valueOf(25.00)) == 0);
        assertTrue(price.getVatRate().compareTo(BigDecimal.valueOf(20.00)) == 0);
        assertTrue(price.getVatAmount().compareTo(BigDecimal.valueOf(5.00)) == 0);
        assertTrue(price.getGrossPrice().compareTo(BigDecimal.valueOf(30.00)) == 0);
    }

    @Test
    public void testGetById_NotFound() {
        assertThrows(NotFoundException.class, () -> repository.getById(9999));
    }

    @Test
    public void testCreatePrice() throws Exception {
        PriceModel newPrice = new PriceModel(
                null,
                BigDecimal.valueOf(100.00),
                BigDecimal.valueOf(10.00),
                null,
                null
        );

        repository.createPrice(newPrice);
        assertNotNull(newPrice.getId());

        PriceModel created = repository.getById(newPrice.getId());
        assertTrue(created.getNetPrice().compareTo(BigDecimal.valueOf(100.00)) == 0);
        assertTrue(created.getVatRate().compareTo(BigDecimal.valueOf(10.00)) == 0);
        assertTrue(created.getVatAmount().compareTo(BigDecimal.valueOf(10.00)) == 0);
        assertTrue(created.getGrossPrice().compareTo(BigDecimal.valueOf(110.00)) == 0);
    }

    @Test
    public void testUpdatePrice() throws Exception {
        PriceModel price = repository.getById(2);
        price.setNetPrice(BigDecimal.valueOf(50.00));
        price.setVatRate(BigDecimal.valueOf(5.00));
        repository.updatePrice(price);

        PriceModel updated = repository.getById(2);
        assertTrue(updated.getNetPrice().compareTo(BigDecimal.valueOf(50.00)) == 0);
        assertTrue(updated.getVatRate().compareTo(BigDecimal.valueOf(5.00)) == 0);
        assertTrue(updated.getVatAmount().compareTo(BigDecimal.valueOf(2.50)) == 0);
        assertTrue(updated.getGrossPrice().compareTo(BigDecimal.valueOf(52.50)) == 0);
    }

    @Test
    public void testDeletePrice() throws Exception {
        PriceModel toDelete = new PriceModel(2, BigDecimal.valueOf(50.00), BigDecimal.valueOf(5.00), null, null);
        repository.createPrice(toDelete);

        int idToDelete = toDelete.getId();
        repository.deletePrice(idToDelete);

        assertThrows(NotFoundException.class, () -> repository.getById(idToDelete));
    }
}
