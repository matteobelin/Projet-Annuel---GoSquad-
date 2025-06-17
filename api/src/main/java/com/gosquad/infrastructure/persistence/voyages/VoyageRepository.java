package com.gosquad.infrastructure.persistence.voyages;

import com.gosquad.core.exceptions.NotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface VoyageRepository {
    List<VoyageModel> getAll() throws SQLException;
    VoyageModel getById(int id) throws SQLException, NotFoundException;
    VoyageModel create(VoyageModel voyage) throws SQLException;
    VoyageModel update(VoyageModel voyage) throws SQLException, NotFoundException;
    void delete(int id) throws SQLException, NotFoundException;
    List<VoyageModel> getByClientId(int clientId) throws SQLException;
}