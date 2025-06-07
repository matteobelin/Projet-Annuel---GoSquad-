package com.gosquad.data;

import com.gosquad.core.exceptions.NotFoundException;

import java.sql.SQLException;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public abstract class Repository<T extends Model> {
    protected final String connectionString;
    private final String tableName;

    protected Repository(String tableName) throws SQLException {
        this.connectionString = String.valueOf(DataConfig.getConnection());
        this.tableName=tableName;
    }

    public List<T> getAll() throws SQLException {
        List<T> results = new ArrayList<>();
        String query = "SELECT * FROM " + tableName;

        try (Connection connection = DataConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                T item = mapResultSetToEntity(rs);
                results.add(item);
            }
        }
        return results;
    }

    public T getById(int id) throws SQLException, NotFoundException {
        String query = "SELECT * FROM " + tableName + " WHERE id = ?";
        try (Connection connection = DataConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToEntity(rs);
            } else {
                throw new NotFoundException("No record found with ID: " + id);
            }
        }
    }

    public T findBy(String column, Object value, String... selectedColumns) throws SQLException, NotFoundException {
        String cols = (selectedColumns.length > 0) ? String.join(", ", selectedColumns) : "*";
        String query = "SELECT " + cols + " FROM " + tableName + " WHERE " + column + " = ?";

        try (Connection connection = DataConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setObject(1, value);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToEntity(rs);
            } else {
                throw new NotFoundException("No record found where " + column + " = " + value);
            }
        }
    }



    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;
}
