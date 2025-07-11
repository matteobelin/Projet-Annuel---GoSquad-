package com.gosquad.infrastructure.persistence.customergroup.impl;

import com.gosquad.infrastructure.persistence.customergroup.CustomerGroupRepository;
import com.gosquad.infrastructure.config.DataConfig;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class CustomerGroupRepositoryImpl implements CustomerGroupRepository {
    @Override
    public void addCustomerToGroup(int customerId, int groupId) {
        String query = "INSERT INTO customer_group (customer_id, group_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (Connection connection = DataConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            stmt.setInt(2, groupId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout du participant au groupe : " + e.getMessage(), e);
        }
    }

    @Override
    public void removeCustomerFromGroup(int customerId, int groupId) {
        String query = "DELETE FROM customer_group WHERE customer_id = ? AND group_id = ?";
        try (Connection connection = DataConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            stmt.setInt(2, groupId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du participant du groupe : " + e.getMessage(), e);
        }
    }
}
