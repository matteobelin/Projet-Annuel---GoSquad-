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
        jakarta.servlet.http.HttpServletRequest request = mock(jakarta.servlet.http.HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer faketoken");
        when(jwtInterceptor.extractTokenInfo(anyString())).thenReturn(java.util.Map.of("companyCode", "GOSQUAD"));
        when(companyService.getCompanyByCode(anyString())).thenReturn(new com.gosquad.domain.company.CompanyEntity(1, "GOSQUAD", "Entreprise Gosquad"));
        when(voyageRequest.getTitle()).thenReturn("Voyage Test");
        when(voyageRequest.getDestination()).thenReturn("Paris");
        when(voyageRequest.getStartDate()).thenReturn(java.time.LocalDate.now());
        when(voyageRequest.getEndDate()).thenReturn(java.time.LocalDate.now().plusDays(1));
        doNothing().when(travelService).addTravel(any(TravelInformationEntity.class));

        travelCreationService.createTravel(request, voyageRequest);

        verify(travelService).addTravel(any(TravelInformationEntity.class));
    }

    @Test
    void testCreateTravel_serviceThrowsException() throws Exception {
        VoyageRequestDTO voyageRequest = mock(VoyageRequestDTO.class);
        jakarta.servlet.http.HttpServletRequest request = mock(jakarta.servlet.http.HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer faketoken");
        when(jwtInterceptor.extractTokenInfo(anyString())).thenReturn(java.util.Map.of("companyCode", "GOSQUAD"));
        when(companyService.getCompanyByCode(anyString())).thenReturn(new com.gosquad.domain.company.CompanyEntity(1, "GOSQUAD", "Entreprise Gosquad"));
        when(voyageRequest.getTitle()).thenReturn("Voyage Test");
        when(voyageRequest.getDestination()).thenReturn("Paris");
        when(voyageRequest.getStartDate()).thenReturn(java.time.LocalDate.now());
        when(voyageRequest.getEndDate()).thenReturn(java.time.LocalDate.now().plusDays(1));
        doThrow(new RuntimeException("Erreur création voyage")).when(travelService).addTravel(any(TravelInformationEntity.class));

        assertThrows(RuntimeException.class, () -> travelCreationService.createTravel(request, voyageRequest));
        verify(travelService).addTravel(any(TravelInformationEntity.class));
    }
}
