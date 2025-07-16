package com.gosquad.usecase.travels.impl;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.domain.travels.TravelInformationEntity;
import com.gosquad.presentation.DTO.travels.VoyageRequestDTO;
import com.gosquad.usecase.travels.TravelService;
import com.gosquad.usecase.travels.TravelCreationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TravelPostServiceImplTest {

    private TravelService travelService;
    private TravelCreationService travelCreationService;

    private TravelPostServiceImpl travelPostService;

    @BeforeEach
    void setUp() {
        travelService = mock(TravelService.class);
        travelCreationService = mock(TravelCreationService.class);

        travelPostService = new TravelPostServiceImpl(travelService, travelCreationService);
    }

    @Test
    void testAddTravel_success() throws SQLException, ConstraintViolationException {
        TravelInformationEntity travel = new TravelInformationEntity(
                1, "Voyage Paris", "Description du voyage",
                LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 7),
                "Paris, France", 1500.0, 10,
                LocalDateTime.of(2024, 1, 1, 10, 0),
                LocalDateTime.of(2024, 1, 2, 15, 30));

        travelPostService.addTravel(travel);

        verify(travelService).addTravel(travel);
    }

    @Test
    void testAddTravel_travelServiceThrowsSQLException() throws SQLException, ConstraintViolationException {
        TravelInformationEntity travel = new TravelInformationEntity(
                1, "Voyage Paris", "Description",
                LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 7),
                "Paris, France", 1500.0, 10, null, null);

        doThrow(new SQLException("Database error")).when(travelService).addTravel(travel);

        assertThrows(SQLException.class, () -> travelPostService.addTravel(travel));
        verify(travelService).addTravel(travel);
    }

    @Test
    void testAddTravel_travelServiceThrowsConstraintViolationException() throws SQLException, ConstraintViolationException {
        TravelInformationEntity travel = new TravelInformationEntity(
                1, "Voyage Paris", "Description",
                LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 7),
                "Paris, France", 1500.0, 10, null, null);

        doThrow(new ConstraintViolationException("Constraint violation")).when(travelService).addTravel(travel);

        assertThrows(ConstraintViolationException.class, () -> travelPostService.addTravel(travel));
        verify(travelService).addTravel(travel);
    }

    @Test
    void testAddTravel_withNullTravel() throws SQLException, ConstraintViolationException {
        travelPostService.addTravel(null);

        verify(travelService).addTravel(null);
    }

    @Test
    void testCreateTravelFromDTO_success() throws SQLException, ConstraintViolationException {
        VoyageRequestDTO dto = mock(VoyageRequestDTO.class);
        when(dto.getTitle()).thenReturn("Voyage Rome");
        when(dto.getDescription()).thenReturn("Voyage en Italie");
        when(dto.getDestination()).thenReturn("Rome, Italie");
        when(dto.getStartDate()).thenReturn(LocalDate.of(2024, 7, 15));
        when(dto.getEndDate()).thenReturn(LocalDate.of(2024, 7, 22));
        when(dto.getBudget()).thenReturn(2000.0);

        TravelInformationEntity expectedTravel = new TravelInformationEntity(
                1, "Voyage Rome", "Voyage en Italie",
                LocalDate.of(2024, 7, 15), LocalDate.of(2024, 7, 22),
                "Rome, Italie", 2000.0, 10,
                LocalDateTime.of(2024, 1, 1, 10, 0),
                LocalDateTime.of(2024, 1, 2, 15, 30));

        when(travelCreationService.createTravel(dto)).thenReturn(expectedTravel);

        TravelInformationEntity result = travelPostService.createTravelFromDTO(dto);

        assertNotNull(result);
        assertEquals(expectedTravel.getId(), result.getId());
        assertEquals(expectedTravel.getTitle(), result.getTitle());
        assertEquals(expectedTravel.getDescription(), result.getDescription());
        assertEquals(expectedTravel.getStartDate(), result.getStartDate());
        assertEquals(expectedTravel.getEndDate(), result.getEndDate());
        assertEquals(expectedTravel.getDestination(), result.getDestination());
        assertEquals(expectedTravel.getBudget(), result.getBudget());
        assertEquals(expectedTravel.getGroupId(), result.getGroupId());

        verify(travelCreationService).createTravel(dto);
    }

    @Test
    void testCreateTravelFromDTO_creationServiceThrowsRuntimeException() {
        VoyageRequestDTO dto = mock(VoyageRequestDTO.class);
        when(dto.getTitle()).thenReturn("Voyage Test");

        when(travelCreationService.createTravel(dto)).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> travelPostService.createTravelFromDTO(dto));
        verify(travelCreationService).createTravel(dto);
    }

    @Test
    void testCreateTravelFromDTO_creationServiceThrowsIllegalArgumentException() {
        VoyageRequestDTO dto = mock(VoyageRequestDTO.class);
        when(dto.getTitle()).thenReturn("Voyage Test");

        when(travelCreationService.createTravel(dto)).thenThrow(new IllegalArgumentException("Validation error"));

        assertThrows(IllegalArgumentException.class, () -> travelPostService.createTravelFromDTO(dto));
        verify(travelCreationService).createTravel(dto);
    }

    @Test
    void testCreateTravelFromDTO_withNullDTO() throws SQLException, ConstraintViolationException {
        when(travelCreationService.createTravel(null)).thenReturn(null);

        TravelInformationEntity result = travelPostService.createTravelFromDTO(null);

        assertNull(result);
        verify(travelCreationService).createTravel(null);
    }

    @Test
    void testCreateTravelFromDTO_withComplexDTO() throws SQLException, ConstraintViolationException {
        VoyageRequestDTO dto = mock(VoyageRequestDTO.class);
        when(dto.getTitle()).thenReturn("Voyage Complexe");
        when(dto.getDescription()).thenReturn("Un voyage avec plusieurs participants");
        when(dto.getDestination()).thenReturn("Tokyo, Japon");
        when(dto.getStartDate()).thenReturn(LocalDate.of(2024, 9, 1));
        when(dto.getEndDate()).thenReturn(LocalDate.of(2024, 9, 15));
        when(dto.getBudget()).thenReturn(3500.0);
        when(dto.getSelectedGroupId()).thenReturn(20L);
        when(dto.getParticipantIds()).thenReturn(java.util.List.of(1L, 2L, 3L));

        TravelInformationEntity expectedTravel = new TravelInformationEntity(
                2, "Voyage Complexe", "Un voyage avec plusieurs participants",
                LocalDate.of(2024, 9, 1), LocalDate.of(2024, 9, 15),
                "Tokyo, Japon", 3500.0, 20,
                LocalDateTime.of(2024, 2, 1, 14, 0),
                LocalDateTime.of(2024, 2, 1, 14, 30));

        when(travelCreationService.createTravel(dto)).thenReturn(expectedTravel);

        TravelInformationEntity result = travelPostService.createTravelFromDTO(dto);

        assertNotNull(result);
        assertEquals("Voyage Complexe", result.getTitle());
        assertEquals("Un voyage avec plusieurs participants", result.getDescription());
        assertEquals("Tokyo, Japon", result.getDestination());
        assertEquals(LocalDate.of(2024, 9, 1), result.getStartDate());
        assertEquals(LocalDate.of(2024, 9, 15), result.getEndDate());
        assertEquals(3500.0, result.getBudget());
        assertEquals(20, result.getGroupId());

        verify(travelCreationService).createTravel(dto);
    }

    @Test
    void testCreateTravelFromDTO_verifyDTOPassedCorrectly() throws SQLException, ConstraintViolationException {
        VoyageRequestDTO dto = mock(VoyageRequestDTO.class);
        TravelInformationEntity mockTravel = mock(TravelInformationEntity.class);

        when(travelCreationService.createTravel(dto)).thenReturn(mockTravel);

        TravelInformationEntity result = travelPostService.createTravelFromDTO(dto);

        assertEquals(mockTravel, result);

        ArgumentCaptor<VoyageRequestDTO> captor = ArgumentCaptor.forClass(VoyageRequestDTO.class);
        verify(travelCreationService).createTravel(captor.capture());
        
        assertEquals(dto, captor.getValue());
    }


    @Test
    void testAddTravel_verifyTravelPassedCorrectly() throws SQLException, ConstraintViolationException {
        TravelInformationEntity travel = new TravelInformationEntity(
                5, "Voyage Vérification", "Test de vérification",
                LocalDate.of(2024, 10, 1), LocalDate.of(2024, 10, 7),
                "Test Destination", 1200.0, 15, null, null);

        travelPostService.addTravel(travel);

        ArgumentCaptor<TravelInformationEntity> captor = ArgumentCaptor.forClass(TravelInformationEntity.class);
        verify(travelService).addTravel(captor.capture());
        
        TravelInformationEntity capturedTravel = captor.getValue();
        assertEquals(travel.getId(), capturedTravel.getId());
        assertEquals(travel.getTitle(), capturedTravel.getTitle());
        assertEquals(travel.getDescription(), capturedTravel.getDescription());
        assertEquals(travel.getStartDate(), capturedTravel.getStartDate());
        assertEquals(travel.getEndDate(), capturedTravel.getEndDate());
        assertEquals(travel.getDestination(), capturedTravel.getDestination());
        assertEquals(travel.getBudget(), capturedTravel.getBudget());
        assertEquals(travel.getGroupId(), capturedTravel.getGroupId());
    }
}