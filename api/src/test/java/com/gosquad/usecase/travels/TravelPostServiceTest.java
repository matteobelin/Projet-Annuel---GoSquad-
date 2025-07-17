package com.gosquad.usecase.travels;

import com.gosquad.domain.travels.TravelInformationEntity;
import com.gosquad.presentation.DTO.travels.VoyageRequestDTO;
import com.gosquad.usecase.travels.TravelService;
import com.gosquad.usecase.travels.TravelCreationService;
import com.gosquad.usecase.travels.impl.TravelPostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TravelPostServiceTest {

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
    void testAddTravel_success() throws SQLException, com.gosquad.core.exceptions.ConstraintViolationException {
        TravelInformationEntity travel = new TravelInformationEntity(
                1, "Voyage Paris", "Description du voyage",
                LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 7),
                "Paris, France", 1500.0, 10,
                LocalDateTime.of(2024, 1, 1, 10, 0),
                LocalDateTime.of(2024, 1, 2, 15, 30), 1);

        travelPostService.addTravel(travel);
        verify(travelService).addTravel(travel);
    }

    @Test
    void testCreateTravelFromDTO_success() throws SQLException, com.gosquad.core.exceptions.ConstraintViolationException {
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
                LocalDateTime.of(2024, 1, 2, 15, 30), 1);

        jakarta.servlet.http.HttpServletRequest request = mock(jakarta.servlet.http.HttpServletRequest.class);
        when(travelCreationService.createTravel(request, dto)).thenReturn(expectedTravel);

        TravelInformationEntity result = travelPostService.createTravelFromDTO(request, dto);

        assertNotNull(result);
        assertEquals(expectedTravel.getId(), result.getId());
        assertEquals(expectedTravel.getTitle(), result.getTitle());
        assertEquals(expectedTravel.getDescription(), result.getDescription());
        assertEquals(expectedTravel.getStartDate(), result.getStartDate());
        assertEquals(expectedTravel.getEndDate(), result.getEndDate());
        assertEquals(expectedTravel.getDestination(), result.getDestination());
        assertEquals(expectedTravel.getBudget(), result.getBudget());
        assertEquals(expectedTravel.getGroupId(), result.getGroupId());
        verify(travelCreationService).createTravel(any(jakarta.servlet.http.HttpServletRequest.class), eq(dto));
    }
}
