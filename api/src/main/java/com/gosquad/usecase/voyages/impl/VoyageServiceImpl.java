package com.gosquad.usecase.voyages.impl;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.voyages.VoyageEntity;
import com.gosquad.infrastructure.persistence.voyages.VoyageModel;
import com.gosquad.infrastructure.persistence.voyages.VoyageRepository;
import com.gosquad.usecase.voyages.VoyageMapper;
import com.gosquad.usecase.voyages.VoyageService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoyageServiceImpl implements VoyageService {

    private final VoyageRepository voyageRepository;

    public VoyageServiceImpl(VoyageRepository voyageRepository) {
        this.voyageRepository = voyageRepository;
    }

    @Override
    public List<VoyageEntity> getAllVoyages() throws SQLException {
        List<VoyageModel> models = voyageRepository.getAll();
        return models.stream()
                .map(VoyageMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public VoyageEntity getVoyageById(int id) throws SQLException, NotFoundException {
        VoyageModel model = voyageRepository.getById(id);
        return VoyageMapper.toEntity(model);
    }

    @Override
    public VoyageEntity createVoyage(VoyageEntity voyage) throws SQLException {
        // Validation
        validateVoyage(voyage);
        
        // Set default status if not provided
        if (voyage.getStatut() == null || voyage.getStatut().isEmpty()) {
            voyage.setStatut("PLANIFIE");
        }
        
        VoyageModel model = VoyageMapper.toModel(voyage);
        VoyageModel createdModel = voyageRepository.create(model);
        return VoyageMapper.toEntity(createdModel);
    }

    @Override
    public VoyageEntity updateVoyage(VoyageEntity voyage) throws SQLException, NotFoundException {
        // Validation
        validateVoyage(voyage);
        
        VoyageModel model = VoyageMapper.toModel(voyage);
        VoyageModel updatedModel = voyageRepository.update(model);
        return VoyageMapper.toEntity(updatedModel);
    }

    @Override
    public void deleteVoyage(int id) throws SQLException, NotFoundException {
        voyageRepository.delete(id);
    }

    @Override
    public List<VoyageEntity> getVoyagesByClientId(int clientId) throws SQLException {
        List<VoyageModel> models = voyageRepository.getByClientId(clientId);
        return models.stream()
                .map(VoyageMapper::toEntity)
                .collect(Collectors.toList());
    }

    private void validateVoyage(VoyageEntity voyage) {
        if (voyage.getTitre() == null || voyage.getTitre().trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre du voyage est obligatoire");
        }
        
        if (voyage.getDestination() == null || voyage.getDestination().trim().isEmpty()) {
            throw new IllegalArgumentException("La destination est obligatoire");
        }
        
        if (voyage.getDateDepart() == null) {
            throw new IllegalArgumentException("La date de départ est obligatoire");
        }
        
        if (voyage.getDateRetour() == null) {
            throw new IllegalArgumentException("La date de retour est obligatoire");
        }
        
        if (voyage.getDateDepart().isAfter(voyage.getDateRetour())) {
            throw new IllegalArgumentException("La date de départ ne peut pas être après la date de retour");
        }
        
        if (voyage.getNombreParticipants() == null || voyage.getNombreParticipants() <= 0) {
            throw new IllegalArgumentException("Le nombre de participants doit être supérieur à 0");
        }
        
        if (voyage.getBudget() == null || voyage.getBudget().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le budget doit être supérieur à 0");
        }
        
        if (voyage.getClientId() == null) {
            throw new IllegalArgumentException("L'ID du client est obligatoire");
        }
    }
}