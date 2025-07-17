
package com.gosquad.usecase.travels;

import com.gosquad.domain.travels.TravelInformationEntity;
import com.gosquad.presentation.DTO.travels.GetTravelResponseDTO;
import com.gosquad.usecase.travels.TravelService;
import com.gosquad.usecase.travels.impl.TravelGetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TravelGetServiceTest {

    private TravelService travelService;
    private TravelGetServiceImpl travelGetService;

    @BeforeEach
    void setUp() {
        travelService = mock(TravelService.class);
        travelGetService = new TravelGetServiceImpl(travelService, null, null, null, null);
    }

    @Test
    void testGetTravel_success() throws Exception {
        TravelInformationEntity travel = mock(TravelInformationEntity.class);
        when(travelService.getTravelById(1)).thenReturn(travel);
        jakarta.servlet.http.HttpServletRequest request = mock(jakarta.servlet.http.HttpServletRequest.class);

        travelGetService.getTravel(request);

        verify(travelService).getTravelById(1);
    }

    @Test
    void testGetTravel_serviceThrowsException() throws Exception {
        when(travelService.getTravelById(1)).thenThrow(new RuntimeException("Erreur récupération voyage"));
        jakarta.servlet.http.HttpServletRequest request = mock(jakarta.servlet.http.HttpServletRequest.class);

        assertThrows(RuntimeException.class, () -> travelGetService.getTravel(request));
        verify(travelService).getTravelById(1);
    }
}
