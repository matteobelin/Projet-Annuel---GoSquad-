package com.gosquad.usecase.travels;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.domain.travels.TravelInformationEntity;
import com.gosquad.presentation.DTO.travels.VoyageRequestDTO;

import java.sql.SQLException;

public interface TravelPostService {
    void addTravel(TravelInformationEntity travel) throws SQLException, ConstraintViolationException;
    TravelInformationEntity createTravelFromDTO(VoyageRequestDTO travelRequestDTO) throws SQLException, ConstraintViolationException;
}
