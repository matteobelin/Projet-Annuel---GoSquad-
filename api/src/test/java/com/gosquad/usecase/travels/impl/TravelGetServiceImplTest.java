package com.gosquad.usecase.travels.impl;

import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.domain.travels.TravelInformationEntity;
import com.gosquad.domain.groups.GroupEntity;
import com.gosquad.domain.customers.CustomerEntity;
import com.gosquad.infrastructure.jwt.JWTInterceptor;
import com.gosquad.presentation.DTO.travels.GetAllTravelsResponseDTO;
import com.gosquad.presentation.DTO.travels.GetTravelResponseDTO;
import com.gosquad.usecase.company.CompanyService;
import com.gosquad.usecase.travels.TravelService;
import com.gosquad.usecase.groups.GroupService;
import com.gosquad.usecase.customers.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TravelGetServiceImplTest {

    private TravelService travelService;
    private CompanyService companyService;
    private GroupService groupService;
    private CustomerService customerService;
    private JWTInterceptor jwtInterceptor;

    private TravelGetServiceImpl travelGetService;

    @BeforeEach
    void setUp() {
        travelService = mock(TravelService.class);
        companyService = mock(CompanyService.class);
        groupService = mock(GroupService.class);
        customerService = mock(CustomerService.class);
        jwtInterceptor = mock(JWTInterceptor.class);

        travelGetService = new TravelGetServiceImpl(
                travelService, companyService, groupService, customerService, jwtInterceptor);
    }

    @Test
    void testGetAllTravels() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        when(jwtInterceptor.extractTokenInfo("token123")).thenReturn(Map.of("companyCode", "COMP"));

        TravelInformationEntity travel1 = new TravelInformationEntity(
                1, "Voyage Paris", "Description Paris", 
                LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 7),
                "Paris, France", 1500.0, 10, null, null);
        
        TravelInformationEntity travel2 = new TravelInformationEntity(
                2, "Voyage Rome", "Description Rome",
                LocalDate.of(2024, 7, 15), LocalDate.of(2024, 7, 22),
                "Rome, Italie", 2000.0, 11, null, null);

        when(travelService.getAllTravels()).thenReturn(List.of(travel1, travel2));

        List<GetAllTravelsResponseDTO> result = travelGetService.getAllTravels(request);

        assertEquals(2, result.size());
        
        GetAllTravelsResponseDTO dto1 = result.get(0);
        assertEquals("TRAVEL1", dto1.uniqueTravelId());
        assertEquals("Voyage Paris", dto1.title());
        assertEquals("Paris, France", dto1.destination());
        assertEquals(LocalDate.of(2024, 6, 1), dto1.startDate());
        assertEquals(LocalDate.of(2024, 6, 7), dto1.endDate());
        assertEquals(1500.0, dto1.budget());

        GetAllTravelsResponseDTO dto2 = result.get(1);
        assertEquals("TRAVEL2", dto2.uniqueTravelId());
        assertEquals("Voyage Rome", dto2.title());
        assertEquals("Rome, Italie", dto2.destination());
        assertEquals(LocalDate.of(2024, 7, 15), dto2.startDate());
        assertEquals(LocalDate.of(2024, 7, 22), dto2.endDate());
        assertEquals(2000.0, dto2.budget());
    }

    @Test
    void testGetTravel_withGroupAndParticipants() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        when(request.getParameter("id")).thenReturn("TRAVEL1");
        when(jwtInterceptor.extractTokenInfo("token123")).thenReturn(Map.of("companyCode", "COMP"));

        TravelInformationEntity travel = new TravelInformationEntity(
                1, "Voyage Paris", "Description détaillée du voyage à Paris",
                LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 7),
                "Paris, France", 1500.0, 10,
                LocalDateTime.of(2024, 1, 1, 10, 0),
                LocalDateTime.of(2024, 1, 2, 15, 30));

        when(travelService.getTravelById(1)).thenReturn(travel);

        GroupEntity group = new GroupEntity(10, "Groupe Paris");
        group.setVisible(true);
        group.setCreatedAt(LocalDateTime.of(2024, 1, 1, 9, 0));
        group.setUpdatedAt(LocalDateTime.of(2024, 1, 1, 9, 30));
        when(groupService.getGroupById(10)).thenReturn(group);

        CustomerEntity customer1 = new CustomerEntity(1, "John", "Doe", "john@example.com", "+33612345678", null, null, null, null, null, null, null, 1, 1, 1, 1);
        CustomerEntity customer2 = new CustomerEntity(2, "Jane", "Smith", "jane@example.com", "+33687654321", null, null, null, null, null, null, null, 1, 1, 1, 1);
        when(customerService.getCustomersByGroupId(10)).thenReturn(List.of(customer1, customer2));

        GetTravelResponseDTO result = travelGetService.getTravel(request);

        assertEquals("TRAVEL1", result.uniqueTravelId());
        assertEquals("Voyage Paris", result.title());
        assertEquals("Description détaillée du voyage à Paris", result.description());
        assertEquals(LocalDate.of(2024, 6, 1), result.startDate());
        assertEquals(LocalDate.of(2024, 6, 7), result.endDate());
        assertEquals("Paris, France", result.destination());
        assertEquals(1500.0, result.budget());
        assertEquals(10, result.groupId());
        assertEquals("COMP", result.companyCode());

        assertEquals(1, result.groups().size());
        assertEquals(10, result.groups().get(0).id());
        assertEquals("Groupe Paris", result.groups().get(0).name());

        assertEquals(2, result.participants().size());
        assertEquals("COMP1", result.participants().get(0).uniqueCustomerId());
        assertEquals("John", result.participants().get(0).firstName());
        assertEquals("COMP2", result.participants().get(1).uniqueCustomerId());
        assertEquals("Jane", result.participants().get(1).firstName());
    }

    @Test
    void testGetTravel_withNumericId() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        when(request.getParameter("id")).thenReturn("1");
        when(jwtInterceptor.extractTokenInfo("token123")).thenReturn(Map.of("companyCode", "COMP"));

        TravelInformationEntity travel = new TravelInformationEntity(
                1, "Voyage Test", "Description test",
                LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 7),
                "Test Destination", 1000.0, null, null, null);

        when(travelService.getTravelById(1)).thenReturn(travel);

        GetTravelResponseDTO result = travelGetService.getTravel(request);

        assertEquals("TRAVEL1", result.uniqueTravelId());
        assertEquals("Voyage Test", result.title());
        assertNull(result.groupId());
        assertTrue(result.groups().isEmpty());
        assertTrue(result.participants().isEmpty());
    }

    @Test
    void testGetTravel_withGroupServiceError() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        when(request.getParameter("id")).thenReturn("TRAVEL1");
        when(jwtInterceptor.extractTokenInfo("token123")).thenReturn(Map.of("companyCode", "COMP"));

        TravelInformationEntity travel = new TravelInformationEntity(
                1, "Voyage Paris", "Description",
                LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 7),
                "Paris, France", 1500.0, 10, null, null);

        when(travelService.getTravelById(1)).thenReturn(travel);
        when(groupService.getGroupById(10)).thenThrow(new RuntimeException("group service error"));

        GetTravelResponseDTO result = travelGetService.getTravel(request);

        assertEquals("TRAVEL1", result.uniqueTravelId());
        assertTrue(result.groups().isEmpty());
        assertTrue(result.participants().isEmpty());
    }

    @Test
    void testGetTravel_withCustomerServiceError() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        when(request.getParameter("id")).thenReturn("TRAVEL1");
        when(jwtInterceptor.extractTokenInfo("token123")).thenReturn(Map.of("companyCode", "COMP"));

        TravelInformationEntity travel = new TravelInformationEntity(
                1, "Voyage Paris", "Description",
                LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 7),
                "Paris, France", 1500.0, 10, null, null);

        when(travelService.getTravelById(1)).thenReturn(travel);

        GroupEntity group = new GroupEntity(10, "Groupe Paris");
        when(groupService.getGroupById(10)).thenReturn(group);
        when(customerService.getCustomersByGroupId(10)).thenThrow(new RuntimeException("Customer service error"));

        GetTravelResponseDTO result = travelGetService.getTravel(request);

        assertEquals("TRAVEL1", result.uniqueTravelId());
        assertEquals(1, result.groups().size());
        assertTrue(result.participants().isEmpty());
    }

    @Test
    void testGetTravel_filterAnonymousCustomers() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        when(request.getParameter("id")).thenReturn("TRAVEL1");
        when(jwtInterceptor.extractTokenInfo("token123")).thenReturn(Map.of("companyCode", "COMP"));

        TravelInformationEntity travel = new TravelInformationEntity(
                1, "Voyage Paris", "Description",
                LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 7),
                "Paris, France", 1500.0, 10, null, null);

        when(travelService.getTravelById(1)).thenReturn(travel);

        GroupEntity group = new GroupEntity(10, "Groupe Paris");
        when(groupService.getGroupById(10)).thenReturn(group);

        CustomerEntity normalCustomer = new CustomerEntity(1, "John", "Doe", "john@example.com", "+33612345678", null, null, null, null, null, null, null, 1, 1, 1, 1);
        CustomerEntity anonymousCustomer = new CustomerEntity(2, "anonymous", "anonymous", "anonymous@example.com", "0000000000", null, null, null, null, null, null, null, 1, 1, 1, 1);
        when(customerService.getCustomersByGroupId(10)).thenReturn(List.of(normalCustomer, anonymousCustomer));

        GetTravelResponseDTO result = travelGetService.getTravel(request);

        assertEquals(1, result.participants().size());
        assertEquals("John", result.participants().get(0).firstName());
    }

    @Test
    void testGetAllTravels_throwsException() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        when(jwtInterceptor.extractTokenInfo("token123")).thenThrow(new RuntimeException("JWT error"));

        assertThrows(Exception.class, () -> travelGetService.getAllTravels(request));
    }

    @Test
    void testGetTravel_throwsException() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        when(request.getParameter("id")).thenReturn("TRAVEL1");
        when(jwtInterceptor.extractTokenInfo("token123")).thenThrow(new RuntimeException("JWT error"));

        assertThrows(Exception.class, () -> travelGetService.getTravel(request));
    }
}