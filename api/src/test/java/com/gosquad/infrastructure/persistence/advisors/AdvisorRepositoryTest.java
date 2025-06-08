package com.gosquad.infrastructure.persistence.advisors;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.advisors.impl.AdvisorRepositoryImpl;
import com.gosquad.infrastructure.persistence.utils.TestDatabaseHelper;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdvisorRepositoryTest {

    private AdvisorRepositoryImpl repository;

    @BeforeAll
    public void setup() throws SQLException {
        TestDatabaseHelper.setupTestDatabase();
        repository = new AdvisorRepositoryImpl();
    }

    @AfterAll
    public void tearDown() throws SQLException {
        TestDatabaseHelper.tearDown();
    }

    @Test
    public void testGetAll() throws SQLException {
        List<AdvisorModel> advisors = repository.getAll();
        assertFalse(advisors.isEmpty(), "La liste des advisors ne doit pas être vide");
        assertEquals(3, advisors.size(), "Doit contenir 3 advisors");
    }

    @Test
    public void testGetById_Success() throws SQLException, NotFoundException {
        AdvisorModel advisor = repository.getById(1);
        assertNotNull(advisor, "L'advisor avec id=1 doit exister");
        assertEquals("gedeon.mutikanga@gosquad.com", advisor.getEmail());
        assertEquals("Gedeon", advisor.getFirstname());
        assertEquals("Mutikanga", advisor.getLastname());
    }

    @Test
    public void testGetById_NotFound() {
        assertThrows(NotFoundException.class, () -> repository.getById(9999));
    }

    @Test
    public void testGetByEmail_Success() throws SQLException, ConstraintViolationException {
        AdvisorModel advisor = repository.getByEmail("gedeon.mutikanga@gosquad.com");
        assertNotNull(advisor, "L'advisor avec email gedeon.mutikanga@gosquad.com doit exister");
        assertEquals(1, advisor.getId());
    }

    @Test
    public void testGetByEmail_NotFound() {
        assertThrows(ConstraintViolationException.class, () -> repository.getByEmail("notfound@example.com"));
    }
}