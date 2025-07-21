
package com.gosquad.infrastructure.persistence.travels;

import com.gosquad.core.exceptions.NotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface TravelRepository {
    List<TravelModel> getAllByCompanyId(int companyId) throws Exception;
    TravelModel getById(int id) throws NotFoundException, SQLException;
    List<TravelModel> findByIds(List<Integer> ids) throws SQLException;
    TravelModel getTravelByTitleAndCompanyId(String title, int companyId) throws SQLException, NotFoundException;
    void createTravel(TravelModel travel) throws SQLException;
    void updateTravel(TravelModel travel) throws SQLException;
    void deleteTravel(int id) throws SQLException;
}
