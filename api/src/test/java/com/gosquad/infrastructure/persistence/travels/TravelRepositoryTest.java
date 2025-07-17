package com.gosquad.infrastructure.persistence.travels;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.travels.impl.TravelRepositoryImpl;
import com.gosquad.infrastructure.persistence.utils.TestDatabaseHelper;
import com.gosquad.infrastructure.persistence.travels.TravelModel;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TravelRepositoryTest {

    private TravelRepositoryImpl repository;

    @BeforeEach
    public void setup() throws SQLException {
        TestDatabaseHelper.setupTestDatabase();
        repository = new TravelRepositoryImpl();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        TestDatabaseHelper.tearDown();
    }

    @Test
    public void testGetById_Success() throws SQLException, NotFoundException {
        TravelModel travel = repository.getById(1);
        assertNotNull(travel, "Le voyage avec id=1 doit exister");
        assertEquals("Voyage Paris", travel.getTitle());
        assertEquals("Paris", travel.getDestination());
    }

    @Test
    public void testGetById_NotFound() {
        assertThrows(NotFoundException.class, () -> repository.getById(9999));
    }

    @Test
    public void testGetAllByCompanyId_Success() throws Exception {
        List<TravelModel> travels = repository.getAllByCompanyId(1);
        assertNotNull(travels, "La liste des voyages ne doit pas être nulle");
        assertFalse(travels.isEmpty(), "Il doit y avoir au moins un voyage pour companyId=1");
        // Vérifie que tous les voyages ont bien companyId=1
        for (TravelModel travel : travels) {
            assertEquals(1, travel.getCompanyId());
        }
    }

    @Test
    public void testCreateTravel_Success() throws SQLException, NotFoundException {
        TravelModel newTravel = new TravelModel(null, "Voyage Test", "Test Destination",
                Date.valueOf(LocalDate.of(2025, 7, 17)), Date.valueOf(LocalDate.of(2025, 7, 20)),
                1000.0, "Description Test", 1, 1);
        repository.createTravel(newTravel);
        // Vérifie que le voyage a bien été créé
        TravelModel created = repository.getTravelByTitleAndCompanyId("Voyage Test", 1);
        assertNotNull(created);
        assertEquals("Voyage Test", created.getTitle());
        assertEquals("Test Destination", created.getDestination());
        assertEquals(1, created.getCompanyId());
    }

    // ...existing code...

    @Test
    public void testUpdateTravelTitle_Success() throws SQLException, NotFoundException {
        TravelModel travel = repository.getById(1);
        travel.setTitle("Voyage Modifié");

        repository.updateTravel(travel);

        TravelModel updated = repository.getById(1);
        assertEquals("Voyage Modifié", updated.getTitle(), "Le titre du voyage doit avoir été mis à jour");
    }

}
