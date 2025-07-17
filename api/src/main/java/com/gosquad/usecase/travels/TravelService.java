package com.gosquad.usecase.travels;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.travels.TravelInformationEntity;

import java.sql.SQLException;
import java.util.List;

public interface TravelService {
    TravelInformationEntity getTravelById(int id) throws SQLException, ConstraintViolationException, NotFoundException;
    List<TravelInformationEntity> getAllTravels(int companyId) throws ConstraintViolationException;
    void addTravel(TravelInformationEntity travel) throws SQLException, ConstraintViolationException;
    void updateTravel(TravelInformationEntity travel) throws SQLException, ConstraintViolationException;
    void deleteTravel(int id) throws SQLException;
}
