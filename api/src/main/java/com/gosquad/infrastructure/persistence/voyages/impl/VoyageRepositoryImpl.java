package com.gosquad.infrastructure.persistence.voyages.impl;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.voyages.VoyageModel;
import com.gosquad.infrastructure.persistence.voyages.VoyageRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VoyageRepositoryImpl implements VoyageRepository {
    
    private static final String TABLE_NAME = "voyages";
    private final DataSource dataSource;

    public VoyageRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<VoyageModel> getAll() throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY created_at DESC";
        List<VoyageModel> voyages = new ArrayList<>();
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                voyages.add(mapResultSetToModel(resultSet));
            }
        }
        
        return voyages;
    }

    @Override
    public VoyageModel getById(int id) throws SQLException, NotFoundException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToModel(resultSet);
                } else {
                    throw new NotFoundException("Voyage with id " + id + " not found");
                }
            }
        }
    }

    @Override
    public VoyageModel create(VoyageModel voyage) throws SQLException {
        String sql = "INSERT INTO " + TABLE_NAME + 
                    " (titre, destination, date_depart, date_retour, participants, budget, client_id, statut) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, voyage.getTitre());
            statement.setString(2, voyage.getDestination());
            statement.setDate(3, voyage.getDateDepart());
            statement.setDate(4, voyage.getDateRetour());
            statement.setInt(5, voyage.getNombreParticipants());
            statement.setBigDecimal(6, voyage.getBudget());
            statement.setInt(7, voyage.getClientId());
            statement.setString(8, voyage.getStatut());
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    voyage.setId(resultSet.getInt("id"));
                    return voyage;
                }
            }
        }
        
        throw new SQLException("Failed to create voyage");
    }

    @Override
    public VoyageModel update(VoyageModel voyage) throws SQLException, NotFoundException {
        String sql = "UPDATE " + TABLE_NAME + 
                    " SET titre = ?, destination = ?, date_depart = ?, date_retour = ?, " +
                    "participants = ?, budget = ?, client_id = ?, statut = ?, updated_at = CURRENT_TIMESTAMP " +
                    "WHERE id = ?";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, voyage.getTitre());
            statement.setString(2, voyage.getDestination());
            statement.setDate(3, voyage.getDateDepart());
            statement.setDate(4, voyage.getDateRetour());
            statement.setInt(5, voyage.getNombreParticipants());
            statement.setBigDecimal(6, voyage.getBudget());
            statement.setInt(7, voyage.getClientId());
            statement.setString(8, voyage.getStatut());
            statement.setInt(9, voyage.getId());
            
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new NotFoundException("Voyage with id " + voyage.getId() + " not found");
            }
            
            return voyage;
        }
    }

    @Override
    public void delete(int id) throws SQLException, NotFoundException {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new NotFoundException("Voyage with id " + id + " not found");
            }
        }
    }

    @Override
    public List<VoyageModel> getByClientId(int clientId) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE client_id = ? ORDER BY created_at DESC";
        List<VoyageModel> voyages = new ArrayList<>();
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, clientId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    voyages.add(mapResultSetToModel(resultSet));
                }
            }
        }
        
        return voyages;
    }

    private VoyageModel mapResultSetToModel(ResultSet resultSet) throws SQLException {
        return new VoyageModel(
            resultSet.getInt("id"),
            resultSet.getString("titre"),
            resultSet.getString("destination"),
            resultSet.getDate("date_depart"),
            resultSet.getDate("date_retour"),
            resultSet.getInt("participants"),
            resultSet.getBigDecimal("budget"),
            resultSet.getInt("client_id"),
            resultSet.getString("statut")
        );
    }
}