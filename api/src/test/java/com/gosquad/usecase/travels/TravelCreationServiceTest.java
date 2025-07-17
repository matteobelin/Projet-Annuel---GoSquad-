package com.gosquad.usecase.travels;

import com.gosquad.domain.travels.TravelInformationEntity;
import com.gosquad.presentation.DTO.travels.VoyageRequestDTO;
import com.gosquad.usecase.travels.impl.TravelCreationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TravelCreationServiceTest {

    private TravelService travelService;
    private TravelCreationServiceImpl travelCreationService;

    private com.gosquad.usecase.groups.GroupService groupService;
    private com.gosquad.usecase.customergroup.CustomerGroupService customerGroupService;
    private com.gosquad.usecase.company.CompanyService companyService;
    private com.gosquad.infrastructure.jwt.JWTInterceptor jwtInterceptor;

    @BeforeEach
    void setUp() {
        travelService = mock(TravelService.class);
        groupService = mock(com.gosquad.usecase.groups.GroupService.class);
        customerGroupService = mock(com.gosquad.usecase.customergroup.CustomerGroupService.class);
        companyService = mock(com.gosquad.usecase.company.CompanyService.class);
        jwtInterceptor = mock(com.gosquad.infrastructure.jwt.JWTInterceptor.class);
        travelCreationService = new TravelCreationServiceImpl(travelService, groupService, customerGroupService, companyService, jwtInterceptor);
    }

    @Test
    void testCreateTravel_success() throws Exception {
        VoyageRequestDTO voyageRequest = mock(VoyageRequestDTO.class);
        doNothing().when(travelService).addTravel(any(TravelInformationEntity.class));

        travelCreationService.createTravel(null, voyageRequest);

        verify(travelService).addTravel(any(TravelInformationEntity.class));
    }

    @Test
    void testCreateTravel_serviceThrowsException() throws Exception {
        VoyageRequestDTO voyageRequest = mock(VoyageRequestDTO.class);
        doThrow(new RuntimeException("Erreur création voyage")).when(travelService).addTravel(any(TravelInformationEntity.class));

        assertThrows(RuntimeException.class, () -> travelCreationService.createTravel(null, voyageRequest));
        verify(travelService).addTravel(any(TravelInformationEntity.class));
    }
}
