package com.gosquad.infrastructure.persistence.countries;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.countries.impl.CountryRepositoryImpl;
import com.gosquad.infrastructure.persistence.utils.TestDatabaseHelper;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CountryRepositoryTest {

    private CountryRepositoryImpl repository;

    @BeforeEach
    public void setup() throws SQLException {
        TestDatabaseHelper.setupTestDatabase();
        repository = new CountryRepositoryImpl();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        TestDatabaseHelper.tearDown();
    }

    @Test
    public void testGetById_Success() throws SQLException, NotFoundException {
        CountryModel country = repository.getById(1);
        assertNotNull(country, "Le pays avec id=1 doit exister");
        assertEquals("France", country.getCountryName());
        assertEquals("FR", country.getIsoCode());
    }

    @Test
    public void testGetById_NotFound() {
        assertThrows(NotFoundException.class, () -> repository.getById(9999));
    }

    @Test
    public void testGetByIsoCode_Success() throws SQLException, ConstraintViolationException, NotFoundException {
        CountryModel country = repository.getByIsoCode("FR");
        assertNotNull(country, "Le pays avec l'ISO code 'FR' doit exister");
        assertEquals(1, country.getId());
        assertEquals("France", country.getCountryName());
    }

    @Test
    public void testGetByIsoCode_NotFound() {
        assertThrows(NotFoundException.class, () -> repository.getByIsoCode("XX"));
    }

    @Test
    public void testAddCountry_Success() throws SQLException, NotFoundException {
        CountryModel newCountry = new CountryModel(null, "DE", "Allemagne");

        repository.addCountry(newCountry);

        CountryModel added = repository.getByIsoCode("DE");
        assertNotNull(added, "Le pays ajouté doit exister");
        assertEquals("Allemagne", added.getCountryName());
        assertEquals("DE", added.getIsoCode());
    }
}
