package com.gosquad.infrastructure.persistence.company;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.company.impl.CompanyRepositoryImpl;
import com.gosquad.infrastructure.persistence.utils.TestDatabaseHelper;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyRepositoryTest {

    private CompanyRepositoryImpl repository;

    @BeforeAll
    public void setup() throws SQLException {
        TestDatabaseHelper.setupTestDatabase();

        repository = new CompanyRepositoryImpl();
    }

    @AfterAll
    public void tearDown() throws SQLException {
        TestDatabaseHelper.tearDown();
    }

    @Test
    public void testGetById_Success() throws SQLException, NotFoundException {
        CompanyModel company = repository.getById(1);

        assertNotNull(company, "La company avec id=1 doit exister");
        assertEquals(1, company.getId(), "L'ID doit être 1");
        assertEquals("GOSQUAD", company.getCode(), "Le code doit être GOSQUAD");
        assertEquals("Entreprise Gosquad", company.getName(), "Le nom doit être 'Entreprise Gosquad'");
    }

    @Test
    public void testGetById_NotFound() {
        assertThrows(NotFoundException.class, () -> {
            repository.getById(9999);
        }, "Doit lever une NotFoundException pour un ID inexistant");
    }

    @Test
    public void testGetById_NegativeId() {
        assertThrows(NotFoundException.class, () -> {
            repository.getById(-1);
        }, "Doit lever une NotFoundException pour un ID négatif");
    }

    @Test
    public void testGetById_ZeroId() {
        assertThrows(NotFoundException.class, () -> {
            repository.getById(0);
        }, "Doit lever une NotFoundException pour l'ID 0");
    }
}