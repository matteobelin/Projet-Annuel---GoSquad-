package com.gosquad.infrastructure.persistence.cities;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.cities.impl.CityRepositoryImpl;
import com.gosquad.infrastructure.persistence.utils.TestDatabaseHelper;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CityRepositoryTest {

    private CityRepositoryImpl repository;

    @BeforeEach
    public void setup() throws SQLException {
        TestDatabaseHelper.setupTestDatabase();
        repository = new CityRepositoryImpl();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        TestDatabaseHelper.tearDown();
    }

    @Test
    public void testGetById_Success() throws SQLException, NotFoundException {
        CityModel city = repository.getById(1);
        assertNotNull(city, "La ville avec id=1 doit exister");
        assertEquals("Nantes", city.getCityName());
        assertEquals("44200", city.getPostalCode());
        assertEquals(1, city.getCountryId());
    }

    @Test
    public void testGetById_NotFound() {
        assertThrows(NotFoundException.class, () -> repository.getById(9999));
    }

    @Test
    public void testGetByNameByPostalCodeByCountry_Success() throws SQLException, ConstraintViolationException, NotFoundException {
        CityModel city = repository.getByNameByPostalCodeByCountry("Paris", "75001", 1);
        assertNotNull(city, "La ville avec ces infos doit exister");
        assertEquals(4, city.getId());
    }

    @Test
    public void testGetByNameByPostalCodeByCountry_NotFound() {
        assertThrows(NotFoundException.class, () -> repository.getByNameByPostalCodeByCountry("Ville Inexistante", "99999", 99));
    }

    @Test
    public void testAddCity_Success() throws SQLException, NotFoundException {
        CityModel newCity = new CityModel(null, "Lyon", "69000", 1);

        repository.addCity(newCity);

        CityModel added = repository.getByNameByPostalCodeByCountry("Lyon", "69000", 1);
        assertNotNull(added, "La ville ajoutée doit exister");
        assertEquals("Lyon", added.getCityName());
        assertEquals("69000", added.getPostalCode());
    }

    @Test
    public void testUpdateCity_Success() throws SQLException, ConstraintViolationException, NotFoundException {
        CityModel city = repository.getById(1);
        city.setCityName("Paris Modifie");

        repository.updateCity(city);

        CityModel updated = repository.getById(1);
        assertEquals("Paris Modifie", updated.getCityName(), "La ville doit avoir été mise à jour");
    }

    @Test
    public void testUpdateCity_ConstraintViolation() {
        CityModel invalidCity = new CityModel(1, null, "75000", 1); // nom null = violation

        assertThrows(ConstraintViolationException.class, () -> repository.updateCity(invalidCity));
    }
}
