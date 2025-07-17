package com.gosquad.usecase.travels.impl;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.domain.travels.TravelInformationEntity;
import com.gosquad.presentation.DTO.travels.VoyageRequestDTO;
import com.gosquad.usecase.travels.TravelService;
import com.gosquad.usecase.travels.TravelPostService;
import com.gosquad.usecase.travels.TravelCreationService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class TravelPostServiceImpl implements TravelPostService {
    
    private final TravelService travelService;
    private final TravelCreationService travelCreationService;

    public TravelPostServiceImpl(TravelService travelService, TravelCreationService travelCreationService) {
        this.travelService = travelService;
        this.travelCreationService = travelCreationService;
    }

    @Override
    public void addTravel(TravelInformationEntity travel) {
        try {
            travelService.addTravel(travel);
        } catch (Exception ignored) {}
    }

    @Override
    public TravelInformationEntity createTravelFromDTO(jakarta.servlet.http.HttpServletRequest request, VoyageRequestDTO travelRequestDTO) {
        try {
            return travelCreationService.createTravel(request, travelRequestDTO);
        } catch (Exception e) {
            return null;
        }
    }
}