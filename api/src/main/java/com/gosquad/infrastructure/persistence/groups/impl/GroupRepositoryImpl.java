package com.gosquad.infrastructure.persistence.groups.impl;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.groups.GroupModel;
import com.gosquad.infrastructure.persistence.groups.GroupRepository;
import com.gosquad.infrastructure.config.DataConfig;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GroupRepositoryImpl implements GroupRepository {

    @Override
    public GroupModel getById(int id) throws SQLException, NotFoundException {
        String query = "SELECT id, name, visible, created_at, updated_at FROM \"group\" WHERE id = ?";
        try (Connection connection = DataConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGroup(rs);
                } else {
                    throw new NotFoundException("Group not found with ID: " + id);
                }
            }
        }
    }

    @Override
    public List<GroupModel> getAllGroups() throws ConstraintViolationException {
        List<GroupModel> groups = new ArrayList<>();
        String query = "SELECT id, name, visible, created_at, updated_at FROM \"group\"";
        try (Connection connection = DataConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                groups.add(mapResultSetToGroup(rs));
            }
        } catch (SQLException e) {
            throw new ConstraintViolationException("Erreur lors de la récupération des groupes: " + e.getMessage());
        }
        return groups;
    }

    @Override
    public void addGroup(GroupModel group) throws SQLException, ConstraintViolationException {
        String query = "INSERT INTO \"group\" (name, visible, created_at, updated_at) VALUES (?, ?, now(), now()) RETURNING id, created_at, updated_at";
        try (Connection connection = DataConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, group.getName());
            stmt.setBoolean(2, group.getVisible() != null ? group.getVisible() : true);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    group.setId(rs.getInt("id"));
                    group.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    group.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            throw new ConstraintViolationException("Erreur lors de la création du groupe: " + e.getMessage());
        }
    }

    @Override
    public void updateGroup(GroupModel group) throws SQLException, ConstraintViolationException {
        if (group.getId() == null) {
            throw new ConstraintViolationException("Cannot update group without ID");
        }
        String query = "UPDATE \"group\" SET name = ?, visible = ?, updated_at = now() WHERE id = ?";
        try (Connection connection = DataConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, group.getName());
            stmt.setBoolean(2, group.getVisible() != null ? group.getVisible() : true);
            stmt.setInt(3, group.getId());
            int updated = stmt.executeUpdate();
            if (updated == 0) {
                throw new ConstraintViolationException("Failed to update group with id: " + group.getId());
            }
            group.setUpdatedAt(LocalDateTime.now());
        } catch (SQLException e) {
            throw new ConstraintViolationException("Erreur lors de la mise à jour du groupe: " + e.getMessage());
        }
    }

    @Override
    public void deleteGroup(int id) throws SQLException {
        String query = "DELETE FROM \"group\" WHERE id = ?";
        try (Connection connection = DataConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private GroupModel mapResultSetToGroup(ResultSet rs) throws SQLException {
        return new GroupModel(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getBoolean("visible"),
            rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
            rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null
        );
    }
}