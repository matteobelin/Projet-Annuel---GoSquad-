package com.gosquad.usecase.travels;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.domain.travels.TravelInformationEntity;
import com.gosquad.presentation.DTO.travels.VoyageRequestDTO;

import java.sql.SQLException;

public interface TravelUpdateService {
    void updateTravel(TravelInformationEntity travel) throws SQLException, ConstraintViolationException;
    void updateTravelFromDTO(int id, VoyageRequestDTO travelRequestDTO, int companyId) throws SQLException, ConstraintViolationException;
    void deleteTravel(int id) throws SQLException;
}