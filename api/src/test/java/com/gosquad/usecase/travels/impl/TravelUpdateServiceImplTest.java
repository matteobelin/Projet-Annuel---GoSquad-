package com.gosquad.usecase.travels.impl;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.travels.TravelInformationEntity;
import com.gosquad.infrastructure.persistence.travels.TravelModel;
import com.gosquad.infrastructure.persistence.travels.TravelRepository;
import com.gosquad.presentation.DTO.travels.VoyageRequestDTO;
import com.gosquad.usecase.travels.TravelMapper;
import com.gosquad.usecase.travels.TravelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TravelUpdateServiceImplTest {

    private TravelRepository travelRepository;
    private TravelMapper travelMapper;
    private TravelService travelService;

    private TravelUpdateServiceImpl travelUpdateService;

    @BeforeEach
    void setUp() {
        travelRepository = mock(TravelRepository.class);
        travelMapper = mock(TravelMapper.class);
        travelService = mock(TravelService.class);

        travelUpdateService = new TravelUpdateServiceImpl(
                travelRepository, travelMapper, travelService);
    }

    @Test
    void testUpdateTravel_success() throws SQLException, ConstraintViolationException {
        TravelInformationEntity travel = new TravelInformationEntity(
                1, "Voyage Paris", "Description",
                LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 7),
                "Paris, France", 1500.0, 10,
                LocalDateTime.of(2024, 1, 1, 10, 0),
                LocalDateTime.of(2024, 1, 2, 15, 30));

        TravelModel travelModel = new TravelModel(
                1, "Voyage Paris", "Paris, France",
                java.sql.Date.valueOf("2024-06-01"), java.sql.Date.valueOf("2024-06-07"),
                1500.0, "Description", 10, 1);

        when(travelMapper.entityToModel(travel)).thenReturn(travelModel);

        travelUpdateService.updateTravel(travel);

        verify(travelMapper).entityToModel(travel);
        verify(travelRepository).updateTravel(travelModel);
    }

    @Test
    void testUpdateTravel_repositoryThrowsException() throws SQLException, ConstraintViolationException {
        TravelInformationEntity travel = new TravelInformationEntity(
                1, "Voyage Paris", "Description",
                LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 7),
                "Paris, France", 1500.0, 10, null, null);

        TravelModel travelModel = new TravelModel();
        when(travelMapper.entityToModel(travel)).thenReturn(travelModel);
        doThrow(new SQLException("Database error")).when(travelRepository).updateTravel(travelModel);

        assertThrows(SQLException.class, () -> travelUpdateService.updateTravel(travel));
    }

    @Test
    void testUpdateTravelFromDTO_success() throws SQLException, ConstraintViolationException, NotFoundException {
        int travelId = 1;
        VoyageRequestDTO dto = mock(VoyageRequestDTO.class);
        when(dto.getTitle()).thenReturn("Voyage Paris Modifié");
        when(dto.getDescription()).thenReturn("Description modifiée");
        when(dto.getStartDate()).thenReturn(LocalDate.of(2024, 6, 5));
        when(dto.getEndDate()).thenReturn(LocalDate.of(2024, 6, 12));
        when(dto.getDestination()).thenReturn("Paris, France");
        when(dto.getBudget()).thenReturn(1800.0);

        TravelInformationEntity existingTravel = new TravelInformationEntity(
                1, "Voyage Paris", "Description originale",
                LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 7),
                "Paris, France", 1500.0, 10,
                LocalDateTime.of(2024, 1, 1, 10, 0),
                LocalDateTime.of(2024, 1, 1, 10, 0));

        when(travelService.getTravelById(travelId)).thenReturn(existingTravel);

        TravelModel updatedModel = new TravelModel();
        when(travelMapper.entityToModel(any(TravelInformationEntity.class))).thenReturn(updatedModel);

        travelUpdateService.updateTravelFromDTO(travelId, dto);

        verify(travelService).getTravelById(travelId);
        
        ArgumentCaptor<TravelInformationEntity> captor = ArgumentCaptor.forClass(TravelInformationEntity.class);
        verify(travelMapper).entityToModel(captor.capture());
        
        TravelInformationEntity updatedTravel = captor.getValue();
        assertEquals("Voyage Paris Modifié", updatedTravel.getTitle());
        assertEquals("Description modifiée", updatedTravel.getDescription());
        assertEquals(LocalDate.of(2024, 6, 5), updatedTravel.getStartDate());
        assertEquals(LocalDate.of(2024, 6, 12), updatedTravel.getEndDate());
        assertEquals("Paris, France", updatedTravel.getDestination());
        assertEquals(1800.0, updatedTravel.getBudget());
        assertNotNull(updatedTravel.getUpdatedAt());
        
        verify(travelRepository).updateTravel(updatedModel);
    }

    @Test
    void testUpdateTravelFromDTO_travelServiceThrowsException() throws Exception {
        int travelId = 1;
        VoyageRequestDTO dto = mock(VoyageRequestDTO.class);
        
        when(travelService.getTravelById(travelId)).thenThrow(new RuntimeException("Travel not found"));

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, 
                () -> travelUpdateService.updateTravelFromDTO(travelId, dto));
        
        assertTrue(exception.getMessage().contains("Failed to update travel"));
        verify(travelService).getTravelById(travelId);
        verifyNoInteractions(travelMapper);
        verifyNoInteractions(travelRepository);
    }

    @Test
    void testUpdateTravelFromDTO_repositoryThrowsException() throws Exception {
        int travelId = 1;
        VoyageRequestDTO dto = mock(VoyageRequestDTO.class);
        when(dto.getTitle()).thenReturn("Voyage Test");
        when(dto.getDescription()).thenReturn("Description");
        when(dto.getStartDate()).thenReturn(LocalDate.of(2024, 6, 1));
        when(dto.getEndDate()).thenReturn(LocalDate.of(2024, 6, 7));
        when(dto.getDestination()).thenReturn("Test Destination");
        when(dto.getBudget()).thenReturn(1000.0);

        TravelInformationEntity existingTravel = new TravelInformationEntity(
                1, "Old Title", "Old Description",
                LocalDate.of(2024, 5, 1), LocalDate.of(2024, 5, 7),
                "Old Destination", 800.0, 10, null, null);

        when(travelService.getTravelById(travelId)).thenReturn(existingTravel);

        TravelModel travelModel = new TravelModel();
        when(travelMapper.entityToModel(any(TravelInformationEntity.class))).thenReturn(travelModel);
        doThrow(new SQLException("Database error")).when(travelRepository).updateTravel(travelModel);

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, 
                () -> travelUpdateService.updateTravelFromDTO(travelId, dto));
        
        assertTrue(exception.getMessage().contains("Failed to update travel"));
    }

    @Test
    void testDeleteTravel_success() throws SQLException {
        int travelId = 1;

        travelUpdateService.deleteTravel(travelId);

        verify(travelRepository).deleteTravel(travelId);
    }

    @Test
    void testDeleteTravel_repositoryThrowsException() throws SQLException {
        int travelId = 1;
        doThrow(new SQLException("Database error")).when(travelRepository).deleteTravel(travelId);

        assertThrows(SQLException.class, () -> travelUpdateService.deleteTravel(travelId));
        verify(travelRepository).deleteTravel(travelId);
    }

    @Test
    void testUpdateTravel_withNullTravel() throws SQLException, ConstraintViolationException {
        when(travelMapper.entityToModel((TravelInformationEntity) null)).thenReturn(null);

        travelUpdateService.updateTravel(null);

        verify(travelMapper).entityToModel((TravelInformationEntity) null);
        verify(travelRepository).updateTravel(null);
    }

    @Test
    void testUpdateTravelFromDTO_preservesOriginalFields() throws SQLException, ConstraintViolationException, NotFoundException {
        int travelId = 1;
        VoyageRequestDTO dto = mock(VoyageRequestDTO.class);
        when(dto.getTitle()).thenReturn("Nouveau Titre");
        when(dto.getDescription()).thenReturn("Nouvelle Description");
        when(dto.getStartDate()).thenReturn(LocalDate.of(2024, 8, 1));
        when(dto.getEndDate()).thenReturn(LocalDate.of(2024, 8, 7));
        when(dto.getDestination()).thenReturn("Nouvelle Destination");
        when(dto.getBudget()).thenReturn(2500.0);

        TravelInformationEntity existingTravel = new TravelInformationEntity(
                1, "Ancien Titre", "Ancienne Description",
                LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 7),
                "Ancienne Destination", 1500.0, 10,
                LocalDateTime.of(2024, 1, 1, 10, 0),
                LocalDateTime.of(2024, 1, 1, 10, 0));

        when(travelService.getTravelById(travelId)).thenReturn(existingTravel);

        TravelModel updatedModel = new TravelModel();
        when(travelMapper.entityToModel(any(TravelInformationEntity.class))).thenReturn(updatedModel);

        travelUpdateService.updateTravelFromDTO(travelId, dto);

        ArgumentCaptor<TravelInformationEntity> captor = ArgumentCaptor.forClass(TravelInformationEntity.class);
        verify(travelMapper).entityToModel(captor.capture());
        
        TravelInformationEntity updatedTravel = captor.getValue();
        
        // Vérifier que les champs non-modifiables sont préservés
        assertEquals(1, updatedTravel.getId());
        assertEquals(10, updatedTravel.getGroupId());
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), updatedTravel.getCreatedAt());
        
        // Vérifier que les champs modifiables sont mis à jour
        assertEquals("Nouveau Titre", updatedTravel.getTitle());
        assertEquals("Nouvelle Description", updatedTravel.getDescription());
        assertEquals(LocalDate.of(2024, 8, 1), updatedTravel.getStartDate());
        assertEquals(LocalDate.of(2024, 8, 7), updatedTravel.getEndDate());
        assertEquals("Nouvelle Destination", updatedTravel.getDestination());
        assertEquals(2500.0, updatedTravel.getBudget());
        
        // Vérifier que updatedAt a été mis à jour
        assertNotNull(updatedTravel.getUpdatedAt());
        assertTrue(updatedTravel.getUpdatedAt().isAfter(LocalDateTime.of(2024, 1, 1, 10, 0)));
    }
}