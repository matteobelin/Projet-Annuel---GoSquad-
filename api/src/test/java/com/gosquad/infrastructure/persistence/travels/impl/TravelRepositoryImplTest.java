
package com.gosquad.infrastructure.persistence.travels.impl;
import org.springframework.test.context.ActiveProfiles;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.travels.TravelModel;
import com.gosquad.infrastructure.persistence.travels.TravelRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TravelRepositoryImplTest {

    @Autowired
    private TravelRepository travelRepository;

    @Test
    @Order(1)
    void testAddAndGetTravel() throws SQLException, ConstraintViolationException, NotFoundException {
        TravelModel travel = new TravelModel(null, "Test Voyage", "Paris", new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis()), 1000.0, "desc", 1, 1);
        travelRepository.addTravel(travel);
        assertNotNull(travel.getId());
        TravelModel fetched = travelRepository.getById(travel.getId());
        assertEquals("Test Voyage", fetched.getTitle());
        assertEquals("Paris", fetched.getDestination());
    }

    @Test
    @Order(2)
    void testUpdateTravel() throws SQLException, ConstraintViolationException, NotFoundException {
        TravelModel travel = new TravelModel(null, "Update Voyage", "Lyon", new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis()), 500.0, "desc", 1, 1);
        travelRepository.addTravel(travel);
        travel.setTitle("Updated Title");
        travel.setDestination("Marseille");
        travelRepository.updateTravel(travel);
        TravelModel updated = travelRepository.getById(travel.getId());
        assertEquals("Updated Title", updated.getTitle());
        assertEquals("Marseille", updated.getDestination());
    }

    @Test
    @Order(3)
    void testDeleteTravel() throws SQLException, ConstraintViolationException, NotFoundException {
        TravelModel travel = new TravelModel(null, "Delete Voyage", "Nice", new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis()), 200.0, "desc", 1, 1);
        travelRepository.addTravel(travel);
        int id = travel.getId();
        travelRepository.deleteTravel(id);
        assertThrows(NotFoundException.class, () -> travelRepository.getById(id));
    }

    @Test
    @Order(4)
    void testGetAllTravels() throws ConstraintViolationException, SQLException {
        List<TravelModel> travels = travelRepository.getAllTravels();
        assertNotNull(travels);
        assertTrue(travels.size() >= 0);
    }
}
