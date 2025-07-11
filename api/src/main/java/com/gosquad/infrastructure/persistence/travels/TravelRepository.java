package com.gosquad.infrastructure.persistence.travels;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface TravelRepository {
    TravelModel getById(Integer id) throws SQLException, NotFoundException;
    List<TravelModel> getAllTravels() throws ConstraintViolationException;
    void addTravel(TravelModel travel) throws SQLException, ConstraintViolationException;
    void updateTravel(TravelModel travel) throws SQLException, ConstraintViolationException;
    void deleteTravel(Integer id) throws SQLException;
    TravelModel save(TravelModel travel) throws SQLException, ConstraintViolationException;
}
