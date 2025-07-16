package com.gosquad.usecase.travels.impl;

import com.gosquad.domain.travels.TravelInformationEntity;
import com.gosquad.domain.group.GroupEntity;
import com.gosquad.presentation.DTO.travels.VoyageRequestDTO;
import com.gosquad.usecase.travels.TravelService;
import com.gosquad.usecase.group.GroupService;
import com.gosquad.usecase.customergroup.CustomerGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TravelCreationServiceImplTest {

    private TravelService travelService;
    private GroupService groupService;
    private CustomerGroupService customerGroupService;

    private TravelCreationServiceImpl travelCreationService;

    @BeforeEach
    void setUp() {
        travelService = mock(TravelService.class);
        groupService = mock(GroupService.class);
        customerGroupService = mock(CustomerGroupService.class);

        travelCreationService = new TravelCreationServiceImpl(
                travelService, groupService, customerGroupService);
    }

    @Test
    void testCreateTravel_withExistingGroup() throws Exception {
        VoyageRequestDTO voyageRequest = mock(VoyageRequestDTO.class);
        when(voyageRequest.getTitle()).thenReturn("Voyage Paris");
        when(voyageRequest.getDescription()).thenReturn("Description du voyage");
        when(voyageRequest.getDestination()).thenReturn("Paris, France");
        when(voyageRequest.getStartDate()).thenReturn(LocalDate.of(2024, 6, 1));
        when(voyageRequest.getEndDate()).thenReturn(LocalDate.of(2024, 6, 7));
        when(voyageRequest.getBudget()).thenReturn(1500.0);
        when(voyageRequest.getSelectedGroupId()).thenReturn(10L);
        when(voyageRequest.getParticipantIds()).thenReturn(List.of(1L, 2L));

        doAnswer(invocation -> {
            TravelInformationEntity travel = invocation.getArgument(0);
            travel.setId(1);
            return null;
        }).when(travelService).addTravel(any(TravelInformationEntity.class));

        TravelInformationEntity result = travelCreationService.createTravel(voyageRequest);

        assertNotNull(result);
        assertEquals("Voyage Paris", result.getTitle());
        assertEquals("Description du voyage", result.getDescription());
        assertEquals("Paris, France", result.getDestination());
        assertEquals(LocalDate.of(2024, 6, 1), result.getStartDate());
        assertEquals(LocalDate.of(2024, 6, 7), result.getEndDate());
        assertEquals(1500.0, result.getBudget());
        assertEquals(10, result.getGroupId());

        verify(customerGroupService).addCustomerToGroup(1, 10);
        verify(customerGroupService).addCustomerToGroup(2, 10);
        verify(travelService).addTravel(any(TravelInformationEntity.class));
        verifyNoInteractions(groupService);
    }

    @Test
    void testCreateTravel_withNewGroupMultipleParticipants() throws Exception {
        VoyageRequestDTO voyageRequest = mock(VoyageRequestDTO.class);
        when(voyageRequest.getTitle()).thenReturn("Voyage Rome");
        when(voyageRequest.getDescription()).thenReturn("Voyage en groupe");
        when(voyageRequest.getDestination()).thenReturn("Rome, Italie");
        when(voyageRequest.getStartDate()).thenReturn(LocalDate.of(2024, 7, 15));
        when(voyageRequest.getEndDate()).thenReturn(LocalDate.of(2024, 7, 22));
        when(voyageRequest.getBudget()).thenReturn(2000.0);
        when(voyageRequest.getSelectedGroupId()).thenReturn(null);
        when(voyageRequest.getGroupName()).thenReturn("Groupe Rome");
        when(voyageRequest.getParticipantIds()).thenReturn(List.of(1L, 2L, 3L));

        GroupEntity newGroup = new GroupEntity(null, "Groupe Rome");
        doAnswer(invocation -> {
            GroupEntity group = invocation.getArgument(0);
            group.setId(20);
            return null;
        }).when(groupService).addGroup(any(GroupEntity.class));

        doAnswer(invocation -> {
            TravelInformationEntity travel = invocation.getArgument(0);
            travel.setId(2);
            return null;
        }).when(travelService).addTravel(any(TravelInformationEntity.class));

        TravelInformationEntity result = travelCreationService.createTravel(voyageRequest);

        assertNotNull(result);
        assertEquals("Voyage Rome", result.getTitle());
        assertEquals(20, result.getGroupId());

        ArgumentCaptor<GroupEntity> groupCaptor = ArgumentCaptor.forClass(GroupEntity.class);
        verify(groupService).addGroup(groupCaptor.capture());
        
        GroupEntity capturedGroup = groupCaptor.getValue();
        assertEquals("Groupe Rome", capturedGroup.getName());
        assertTrue(capturedGroup.getVisible()); // visible = true pour plusieurs participants

        verify(customerGroupService).addCustomerToGroup(1, 20);
        verify(customerGroupService).addCustomerToGroup(2, 20);
        verify(customerGroupService).addCustomerToGroup(3, 20);
    }

    @Test
    void testCreateTravel_withNewGroupSingleParticipant() throws Exception {
        VoyageRequestDTO voyageRequest = mock(VoyageRequestDTO.class);
        when(voyageRequest.getTitle()).thenReturn("Voyage Solo");
        when(voyageRequest.getDescription()).thenReturn("Voyage individuel");
        when(voyageRequest.getDestination()).thenReturn("Londres, UK");
        when(voyageRequest.getStartDate()).thenReturn(LocalDate.of(2024, 8, 1));
        when(voyageRequest.getEndDate()).thenReturn(LocalDate.of(2024, 8, 5));
        when(voyageRequest.getBudget()).thenReturn(800.0);
        when(voyageRequest.getSelectedGroupId()).thenReturn(null);
        when(voyageRequest.getGroupName()).thenReturn(null);
        when(voyageRequest.getParticipantIds()).thenReturn(List.of(1L));

        doAnswer(invocation -> {
            GroupEntity group = invocation.getArgument(0);
            group.setId(30);
            return null;
        }).when(groupService).addGroup(any(GroupEntity.class));

        doAnswer(invocation -> {
            TravelInformationEntity travel = invocation.getArgument(0);
            travel.setId(3);
            return null;
        }).when(travelService).addTravel(any(TravelInformationEntity.class));

        TravelInformationEntity result = travelCreationService.createTravel(voyageRequest);

        assertNotNull(result);
        assertEquals("Voyage Solo", result.getTitle());
        assertEquals(30, result.getGroupId());

        ArgumentCaptor<GroupEntity> groupCaptor = ArgumentCaptor.forClass(GroupEntity.class);
        verify(groupService).addGroup(groupCaptor.capture());
        
        GroupEntity capturedGroup = groupCaptor.getValue();
        assertEquals("Groupe Voyage", capturedGroup.getName()); // nom par défaut
        assertFalse(capturedGroup.getVisible()); // visible = false pour un seul participant

        verify(customerGroupService).addCustomerToGroup(1, 30);
    }

    @Test
    void testCreateTravel_validationError_missingTitle() {
        VoyageRequestDTO voyageRequest = mock(VoyageRequestDTO.class);
        when(voyageRequest.getTitle()).thenReturn(null);
        when(voyageRequest.getDestination()).thenReturn("Paris, France");
        when(voyageRequest.getStartDate()).thenReturn(LocalDate.of(2024, 6, 1));
        when(voyageRequest.getEndDate()).thenReturn(LocalDate.of(2024, 6, 7));

        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> travelCreationService.createTravel(voyageRequest));
        
        assertTrue(exception.getMessage().contains("Le title du voyage est obligatoire"));
        verifyNoInteractions(travelService);
        verifyNoInteractions(groupService);
        verifyNoInteractions(customerGroupService);
    }

    @Test
    void testCreateTravel_validationError_emptyTitle() {
        VoyageRequestDTO voyageRequest = mock(VoyageRequestDTO.class);
        when(voyageRequest.getTitle()).thenReturn("   ");
        when(voyageRequest.getDestination()).thenReturn("Paris, France");
        when(voyageRequest.getStartDate()).thenReturn(LocalDate.of(2024, 6, 1));
        when(voyageRequest.getEndDate()).thenReturn(LocalDate.of(2024, 6, 7));

        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> travelCreationService.createTravel(voyageRequest));
        
        assertTrue(exception.getMessage().contains("Le title du voyage est obligatoire"));
    }

    @Test
    void testCreateTravel_validationError_missingDestination() {
        VoyageRequestDTO voyageRequest = mock(VoyageRequestDTO.class);
        when(voyageRequest.getTitle()).thenReturn("Voyage Test");
        when(voyageRequest.getDestination()).thenReturn(null);
        when(voyageRequest.getStartDate()).thenReturn(LocalDate.of(2024, 6, 1));
        when(voyageRequest.getEndDate()).thenReturn(LocalDate.of(2024, 6, 7));

        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> travelCreationService.createTravel(voyageRequest));
        
        assertTrue(exception.getMessage().contains("La destination est obligatoire"));
    }

    @Test
    void testCreateTravel_validationError_missingStartDate() {
        VoyageRequestDTO voyageRequest = mock(VoyageRequestDTO.class);
        when(voyageRequest.getTitle()).thenReturn("Voyage Test");
        when(voyageRequest.getDestination()).thenReturn("Paris, France");
        when(voyageRequest.getStartDate()).thenReturn(null);
        when(voyageRequest.getEndDate()).thenReturn(LocalDate.of(2024, 6, 7));

        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> travelCreationService.createTravel(voyageRequest));
        
        assertTrue(exception.getMessage().contains("La date de début est obligatoire"));
    }

    @Test
    void testCreateTravel_validationError_missingEndDate() {
        VoyageRequestDTO voyageRequest = mock(VoyageRequestDTO.class);
        when(voyageRequest.getTitle()).thenReturn("Voyage Test");
        when(voyageRequest.getDestination()).thenReturn("Paris, France");
        when(voyageRequest.getStartDate()).thenReturn(LocalDate.of(2024, 6, 1));
        when(voyageRequest.getEndDate()).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> travelCreationService.createTravel(voyageRequest));
        
        assertTrue(exception.getMessage().contains("La date de fin est obligatoire"));
    }

    @Test
    void testCreateTravel_validationError_invalidDateRange() {
        VoyageRequestDTO voyageRequest = mock(VoyageRequestDTO.class);
        when(voyageRequest.getTitle()).thenReturn("Voyage Test");
        when(voyageRequest.getDestination()).thenReturn("Paris, France");
        when(voyageRequest.getStartDate()).thenReturn(LocalDate.of(2024, 6, 7));
        when(voyageRequest.getEndDate()).thenReturn(LocalDate.of(2024, 6, 1));

        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> travelCreationService.createTravel(voyageRequest));
        
        assertTrue(exception.getMessage().contains("La date de début doit être antérieure à la date de fin"));
    }

    @Test
    void testCreateTravel_validationError_noParticipants() {
        VoyageRequestDTO voyageRequest = mock(VoyageRequestDTO.class);
        when(voyageRequest.getTitle()).thenReturn("Voyage Test");
        when(voyageRequest.getDestination()).thenReturn("Paris, France");
        when(voyageRequest.getStartDate()).thenReturn(LocalDate.of(2024, 6, 1));
        when(voyageRequest.getEndDate()).thenReturn(LocalDate.of(2024, 6, 7));
        when(voyageRequest.getSelectedGroupId()).thenReturn(null);
        when(voyageRequest.getParticipantIds()).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> travelCreationService.createTravel(voyageRequest));
        
        assertTrue(exception.getMessage().contains("Aucun participant fourni pour le voyage"));
    }

    @Test
    void testCreateTravel_groupServiceError() throws Exception {
        VoyageRequestDTO voyageRequest = mock(VoyageRequestDTO.class);
        when(voyageRequest.getTitle()).thenReturn("Voyage Test");
        when(voyageRequest.getDestination()).thenReturn("Paris, France");
        when(voyageRequest.getStartDate()).thenReturn(LocalDate.of(2024, 6, 1));
        when(voyageRequest.getEndDate()).thenReturn(LocalDate.of(2024, 6, 7));
        when(voyageRequest.getSelectedGroupId()).thenReturn(null);
        when(voyageRequest.getParticipantIds()).thenReturn(List.of(1L));

        doThrow(new RuntimeException("group service error")).when(groupService).addGroup(any(GroupEntity.class));

        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> travelCreationService.createTravel(voyageRequest));
        
        assertTrue(exception.getMessage().contains("Erreur lors de la gestion du groupe pour le voyage"));
        verifyNoInteractions(travelService);
    }

    @Test
    void testCreateTravel_travelServiceError() throws Exception {
        VoyageRequestDTO voyageRequest = mock(VoyageRequestDTO.class);
        when(voyageRequest.getTitle()).thenReturn("Voyage Test");
        when(voyageRequest.getDestination()).thenReturn("Paris, France");
        when(voyageRequest.getStartDate()).thenReturn(LocalDate.of(2024, 6, 1));
        when(voyageRequest.getEndDate()).thenReturn(LocalDate.of(2024, 6, 7));
        when(voyageRequest.getSelectedGroupId()).thenReturn(10L);
        when(voyageRequest.getParticipantIds()).thenReturn(List.of(1L));

        doThrow(new RuntimeException("Travel service error")).when(travelService).addTravel(any(TravelInformationEntity.class));

        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> travelCreationService.createTravel(voyageRequest));
        
        assertTrue(exception.getMessage().contains("Erreur lors de la création du voyage"));
    }
}