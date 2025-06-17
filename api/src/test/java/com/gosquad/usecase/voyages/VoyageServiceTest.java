package com.gosquad.usecase.voyages;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.voyages.VoyageEntity;
import com.gosquad.infrastructure.persistence.voyages.VoyageModel;
import com.gosquad.infrastructure.persistence.voyages.VoyageRepository;
import com.gosquad.usecase.voyages.impl.VoyageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoyageServiceTest {

    @Mock
    private VoyageRepository voyageRepository;

    private VoyageServiceImpl voyageService;

    @BeforeEach
    void setUp() {
        voyageService = new VoyageServiceImpl(voyageRepository);
    }

    @Test
    void getAllVoyages_ShouldReturnListOfVoyages() throws SQLException {
        // Given
        VoyageModel model1 = createVoyageModel(1, "Voyage Paris", "Paris");
        VoyageModel model2 = createVoyageModel(2, "Voyage Rome", "Rome");
        List<VoyageModel> models = Arrays.asList(model1, model2);

        when(voyageRepository.getAll()).thenReturn(models);

        // When
        List<VoyageEntity> result = voyageService.getAllVoyages();

        // Then
        assertEquals(2, result.size());
        assertEquals("Voyage Paris", result.get(0).getTitre());
        assertEquals("Voyage Rome", result.get(1).getTitre());
        verify(voyageRepository).getAll();
    }

    @Test
    void getVoyageById_ShouldReturnVoyage_WhenVoyageExists() throws SQLException, NotFoundException {
        // Given
        int voyageId = 1;
        VoyageModel model = createVoyageModel(voyageId, "Voyage Paris", "Paris");

        when(voyageRepository.getById(voyageId)).thenReturn(model);

        // When
        VoyageEntity result = voyageService.getVoyageById(voyageId);

        // Then
        assertNotNull(result);
        assertEquals(voyageId, result.getId());
        assertEquals("Voyage Paris", result.getTitre());
        assertEquals("Paris", result.getDestination());
        verify(voyageRepository).getById(voyageId);
    }

    @Test
    void getVoyageById_ShouldThrowNotFoundException_WhenVoyageDoesNotExist() throws SQLException, NotFoundException {
        // Given
        int voyageId = 999;
        when(voyageRepository.getById(voyageId)).thenThrow(new NotFoundException("Voyage not found"));

        // When & Then
        assertThrows(NotFoundException.class, () -> voyageService.getVoyageById(voyageId));
        verify(voyageRepository).getById(voyageId);
    }

    @Test
    void createVoyage_ShouldCreateVoyage_WhenValidData() throws SQLException {
        // Given
        VoyageEntity voyage = createVoyageEntity(null, "Nouveau Voyage", "Tokyo");
        VoyageModel createdModel = createVoyageModel(1, "Nouveau Voyage", "Tokyo");

        when(voyageRepository.create(any(VoyageModel.class))).thenReturn(createdModel);

        // When
        VoyageEntity result = voyageService.createVoyage(voyage);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Nouveau Voyage", result.getTitre());
        assertEquals("PLANIFIE", result.getStatut());
        verify(voyageRepository).create(any(VoyageModel.class));
    }

    @Test
    void createVoyage_ShouldThrowException_WhenTitreIsEmpty() {
        // Given
        VoyageEntity voyage = createVoyageEntity(null, "", "Tokyo");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> voyageService.createVoyage(voyage));
        assertEquals("Le titre du voyage est obligatoire", exception.getMessage());
        verifyNoInteractions(voyageRepository);
    }

    @Test
    void createVoyage_ShouldThrowException_WhenDestinationIsEmpty() {
        // Given
        VoyageEntity voyage = createVoyageEntity(null, "Voyage", "");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> voyageService.createVoyage(voyage));
        assertEquals("La destination est obligatoire", exception.getMessage());
        verifyNoInteractions(voyageRepository);
    }

    @Test
    void createVoyage_ShouldThrowException_WhenDateDepartIsAfterDateRetour() {
        // Given
        VoyageEntity voyage = createVoyageEntity(null, "Voyage", "Paris");
        voyage.setDateDepart(LocalDate.of(2024, 8, 15));
        voyage.setDateRetour(LocalDate.of(2024, 8, 10)); // Date retour avant date départ

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> voyageService.createVoyage(voyage));
        assertEquals("La date de départ ne peut pas être après la date de retour", exception.getMessage());
        verifyNoInteractions(voyageRepository);
    }

    @Test
    void createVoyage_ShouldThrowException_WhenNombreParticipantsIsZero() {
        // Given
        VoyageEntity voyage = createVoyageEntity(null, "Voyage", "Paris");
        voyage.setNombreParticipants(0);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> voyageService.createVoyage(voyage));
        assertEquals("Le nombre de participants doit être supérieur à 0", exception.getMessage());
        verifyNoInteractions(voyageRepository);
    }

    @Test
    void createVoyage_ShouldThrowException_WhenBudgetIsZero() {
        // Given
        VoyageEntity voyage = createVoyageEntity(null, "Voyage", "Paris");
        voyage.setBudget(BigDecimal.ZERO);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> voyageService.createVoyage(voyage));
        assertEquals("Le budget doit être supérieur à 0", exception.getMessage());
        verifyNoInteractions(voyageRepository);
    }

    @Test
    void updateVoyage_ShouldUpdateVoyage_WhenValidData() throws SQLException, NotFoundException {
        // Given
        VoyageEntity voyage = createVoyageEntity(1, "Voyage Modifié", "Londres");
        VoyageModel updatedModel = createVoyageModel(1, "Voyage Modifié", "Londres");

        when(voyageRepository.update(any(VoyageModel.class))).thenReturn(updatedModel);

        // When
        VoyageEntity result = voyageService.updateVoyage(voyage);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Voyage Modifié", result.getTitre());
        assertEquals("Londres", result.getDestination());
        verify(voyageRepository).update(any(VoyageModel.class));
    }

    @Test
    void deleteVoyage_ShouldCallRepository() throws SQLException, NotFoundException {
        // Given
        int voyageId = 1;

        // When
        voyageService.deleteVoyage(voyageId);

        // Then
        verify(voyageRepository).delete(voyageId);
    }

    @Test
    void getVoyagesByClientId_ShouldReturnClientVoyages() throws SQLException {
        // Given
        int clientId = 1;
        VoyageModel model1 = createVoyageModel(1, "Voyage 1", "Paris");
        VoyageModel model2 = createVoyageModel(2, "Voyage 2", "Rome");
        List<VoyageModel> models = Arrays.asList(model1, model2);

        when(voyageRepository.getByClientId(clientId)).thenReturn(models);

        // When
        List<VoyageEntity> result = voyageService.getVoyagesByClientId(clientId);

        // Then
        assertEquals(2, result.size());
        verify(voyageRepository).getByClientId(clientId);
    }

    private VoyageEntity createVoyageEntity(Integer id, String titre, String destination) {
        VoyageEntity voyage = new VoyageEntity();
        voyage.setId(id);
        voyage.setTitre(titre);
        voyage.setDestination(destination);
        voyage.setDateDepart(LocalDate.of(2024, 7, 15));
        voyage.setDateRetour(LocalDate.of(2024, 7, 22));
        voyage.setNombreParticipants(2);
        voyage.setBudget(new BigDecimal("1500.00"));
        voyage.setClientId(1);
        voyage.setStatut("PLANIFIE");
        return voyage;
    }

    private VoyageModel createVoyageModel(Integer id, String titre, String destination) {
        return new VoyageModel(
            id,
            titre,
            destination,
            Date.valueOf("2024-07-15"),
            Date.valueOf("2024-07-22"),
            2,
            new BigDecimal("1500.00"),
            1,
            "PLANIFIE"
        );
    }
}