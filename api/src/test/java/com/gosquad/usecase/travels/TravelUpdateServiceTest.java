package com.gosquad.usecase.travels;

import com.gosquad.domain.travels.TravelInformationEntity;
import com.gosquad.usecase.travels.TravelService;
import com.gosquad.usecase.travels.impl.TravelUpdateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TravelUpdateServiceTest {

    private com.gosquad.infrastructure.persistence.travels.TravelRepository travelRepository;
    private com.gosquad.usecase.travels.TravelMapper travelMapper;
    private TravelService travelService;
    private com.gosquad.usecase.travels.impl.TravelUpdateServiceImpl travelUpdateService;

    @BeforeEach
    void setUp() {
        travelRepository = mock(com.gosquad.infrastructure.persistence.travels.TravelRepository.class);
        travelMapper = mock(com.gosquad.usecase.travels.TravelMapper.class);
        travelService = mock(TravelService.class);
        travelUpdateService = new com.gosquad.usecase.travels.impl.TravelUpdateServiceImpl(travelRepository, travelMapper, travelService);
    }

    @Test
    void testUpdateTravel_success() throws SQLException, com.gosquad.core.exceptions.ConstraintViolationException {
        TravelInformationEntity travel = new TravelInformationEntity(
                1, "Voyage Paris", "Description du voyage",
                LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 7),
                "Paris, France", 1500.0, 10,
                LocalDateTime.of(2024, 1, 1, 10, 0),
                LocalDateTime.of(2024, 1, 2, 15, 30), 1);

        doNothing().when(travelService).updateTravel(travel);
        travelUpdateService.updateTravel(travel);
        verify(travelService).updateTravel(travel);
    }
}
