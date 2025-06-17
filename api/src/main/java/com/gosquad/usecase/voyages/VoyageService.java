package com.gosquad.usecase.voyages;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.voyages.VoyageEntity;

import java.sql.SQLException;
import java.util.List;

public interface VoyageService {
    List<VoyageEntity> getAllVoyages() throws SQLException;
    VoyageEntity getVoyageById(int id) throws SQLException, NotFoundException;
    VoyageEntity createVoyage(VoyageEntity voyage) throws SQLException;
    VoyageEntity updateVoyage(VoyageEntity voyage) throws SQLException, NotFoundException;
    void deleteVoyage(int id) throws SQLException, NotFoundException;
    List<VoyageEntity> getVoyagesByClientId(int clientId) throws SQLException;
}