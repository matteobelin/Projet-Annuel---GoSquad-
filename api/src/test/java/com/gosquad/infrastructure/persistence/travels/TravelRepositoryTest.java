package com.gosquad.infrastructure.persistence.travels;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.travels.impl.TravelRepositoryImpl;
import com.gosquad.infrastructure.persistence.utils.TestDatabaseHelper;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TravelRepositoryTest
{

    private TravelRepositoryImpl repository;

    @BeforeEach
    public void setup() throws SQLException
    {
        TestDatabaseHelper.setupTestDatabase();
        repository = new TravelRepositoryImpl();
    }

    @AfterEach
    public void tearDown() throws SQLException
    {
        TestDatabaseHelper.tearDown();
    }

    @Test
    public void testGetById_Success() throws SQLException, NotFoundException
    {
        TravelModel travel = repository.getById(1);
        assertNotNull(travel);
        assertEquals("Voyage Paris", travel.getTitle());
    }

    @Test
    public void testGetById_NotFound()
    {
        assertThrows(NotFoundException.class, () -> repository.getById(9999));
    }

    @Test
    public void testGetAllByCompanyId_Success() throws Exception
    {
        List<TravelModel> travels = repository.getAllByCompanyId(1);
        assertNotNull(travels);
        assertFalse(travels.isEmpty());
    }

    @Test
    public void testCreateTravel_Success() throws SQLException, ConstraintViolationException, NotFoundException
    {
        TravelModel newTravel = new TravelModel(
            null,
            "Week‑end Berlin",
            "Berlin",
            Date.valueOf("2026-03-15"),
            Date.valueOf("2026-03-20"),
            900.00,
            "Découverte de Berlin",
            1,
            1
        );

        repository.createTravel(newTravel);

        TravelModel inserted = repository.getTravelByTitleAndCompanyId("Week‑end Berlin", 1);
        assertNotNull(inserted);
        assertEquals("Berlin", inserted.getDestination());
    }

    @Test
    public void testUpdateTravel_Success() throws SQLException, ConstraintViolationException, NotFoundException
    {
        TravelModel travel = repository.getTravelByTitleAndCompanyId("Trip New York", 1);
        travel.setTitle("Trip New York Modifié");

        repository.updateTravel(travel);

        TravelModel updated = repository.getTravelByTitleAndCompanyId("Trip New York Modifié", 1);
        assertEquals("Trip New York Modifié", updated.getTitle());
    }

    @Test
    public void testDeleteTravel_Success() throws SQLException, NotFoundException
    {
        TravelModel travel = repository.getTravelByTitleAndCompanyId("Séjour Londres", 1);
        repository.deleteTravel(travel.getId());

        assertThrows(NotFoundException.class, () -> repository.getById(travel.getId()));
    }
}
