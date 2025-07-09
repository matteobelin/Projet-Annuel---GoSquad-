package com.gosquad.usecase.travels.impl;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.domain.travels.TravelInformationEntity;
import com.gosquad.infrastructure.persistence.travels.TravelModel;
import com.gosquad.infrastructure.persistence.travels.TravelRepository;
import com.gosquad.presentation.DTO.travels.VoyageRequestDTO;
import com.gosquad.usecase.travels.TravelMapper;
import com.gosquad.usecase.travels.TravelService;
import com.gosquad.usecase.travels.TravelUpdateService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;

@Service
public class TravelUpdateServiceImpl implements TravelUpdateService {

    private final TravelRepository travelRepository;
    private final TravelMapper travelMapper;
    private final TravelService travelService;

    public TravelUpdateServiceImpl(TravelRepository travelRepository, TravelMapper travelMapper, TravelService travelService) {
        this.travelRepository = travelRepository;
        this.travelMapper = travelMapper;
        this.travelService = travelService;
    }

    @Override
    public void updateTravel(TravelInformationEntity travel) throws SQLException, ConstraintViolationException {
        TravelModel travelModel = travelMapper.entityToModel(travel);
        travelRepository.updateTravel(travelModel);
    }

    @Override
    public void updateTravelFromDTO(int id, VoyageRequestDTO travelRequestDTO) throws SQLException, ConstraintViolationException {
        try {
            // Get existing travel
            TravelInformationEntity existingTravel = travelService.getTravelById(id);
            
            // Update the fields from DTO
            existingTravel.setTitle(travelRequestDTO.getTitle());
            existingTravel.setDescription(travelRequestDTO.getDescription());
            existingTravel.setStartDate(travelRequestDTO.getStartDate());
            existingTravel.setEndDate(travelRequestDTO.getEndDate());
            existingTravel.setDestination(travelRequestDTO.getDestination());
            existingTravel.setBudget(travelRequestDTO.getBudget());
            existingTravel.setUpdatedAt(LocalDateTime.now());
            
            // Save the updated travel
            updateTravel(existingTravel);
        } catch (Exception e) {
            throw new ConstraintViolationException("Failed to update travel: " + e.getMessage());
        }
    }

    @Override
    public void deleteTravel(int id) throws SQLException {
        travelRepository.deleteTravel(id);
    }
}
