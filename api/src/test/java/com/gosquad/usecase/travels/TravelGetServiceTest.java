
package com.gosquad.usecase.travels;
import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.infrastructure.jwt.JWTInterceptor;
import com.gosquad.usecase.company.CompanyService;

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
    private JWTInterceptor jwtInterceptor;
    private CompanyService companyService;
    private TravelGetServiceImpl travelGetService;

    @BeforeEach
    void setUp() {
        travelService = mock(TravelService.class);
        jwtInterceptor = mock(JWTInterceptor.class);
        companyService = mock(CompanyService.class);
        travelGetService = new TravelGetServiceImpl(travelService, companyService, null, null, jwtInterceptor);
    }

    @Test
    void testGetTravel_success() throws Exception {
        TravelInformationEntity travel = mock(TravelInformationEntity.class);
        when(travelService.getTravelById(1)).thenReturn(travel);
        jakarta.servlet.http.HttpServletRequest request = mock(jakarta.servlet.http.HttpServletRequest.class);
        when(request.getParameter("id")).thenReturn("TRAVEL1");
        when(request.getHeader("Authorization")).thenReturn("Bearer faketoken");
        when(jwtInterceptor.extractTokenInfo("faketoken")).thenReturn(java.util.Map.of("companyCode", "COMPANY1"));
        when(companyService.getCompanyByCode("COMPANY1")).thenReturn(mock(com.gosquad.domain.company.CompanyEntity.class));
        when(companyService.getCompanyByCode("COMPANY1")).thenReturn(new CompanyEntity(1, "COMPANY1", "CompanyName"));

        travelGetService.getTravel(request);

        verify(travelService).getTravelById(1);
    }

    @Test
    void testGetTravel_serviceThrowsException() throws Exception {
        when(travelService.getTravelById(1)).thenThrow(new RuntimeException("Erreur récupération voyage"));
        jakarta.servlet.http.HttpServletRequest request = mock(jakarta.servlet.http.HttpServletRequest.class);
        when(request.getParameter("id")).thenReturn("1");
        when(request.getHeader("Authorization")).thenReturn("Bearer faketoken");
        when(jwtInterceptor.extractTokenInfo("faketoken")).thenReturn(java.util.Map.of("companyCode", "COMPANY1"));
        when(companyService.getCompanyByCode("COMPANY1")).thenReturn(new CompanyEntity(1, "COMPANY1", "CompanyName"));

        assertThrows(RuntimeException.class, () -> travelGetService.getTravel(request));
        verify(travelService).getTravelById(1);
    }
}
