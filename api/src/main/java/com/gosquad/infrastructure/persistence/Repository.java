package com.gosquad.infrastructure.persistence;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.config.DataConfig;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

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

    public T findByMultiple(Map<String, Object> conditions, String... selectedColumns) throws SQLException, NotFoundException {
        String cols = (selectedColumns.length > 0) ? String.join(", ", selectedColumns) : "*";
        StringBuilder whereClause = new StringBuilder();
        List<Object> values = new ArrayList<>();

        for (String col : conditions.keySet()) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }
            whereClause.append(col).append(" = ?");
            values.add(conditions.get(col));
        }
        String query = "SELECT " + cols + " FROM " + tableName + " WHERE " + whereClause;

        try (Connection connection = DataConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToEntity(rs);
            } else {
                throw new NotFoundException("No record found matching conditions " + conditions);
            }
        }
    }

    public List<T> findAllBy(String column, Object value, String... selectedColumns) throws SQLException {
        List<T> results = new ArrayList<>();
        String cols = (selectedColumns.length > 0) ? String.join(", ", selectedColumns) : "*";
        String query = "SELECT " + cols + " FROM " + tableName + " WHERE " + column + " = ?";

        try (Connection connection = DataConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setObject(1, value);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                T item = mapResultSetToEntity(rs);
                results.add(item);
            }
        }

        return results;
    }

    public int insert(Map<String, Object> values) throws SQLException {
        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();
        List<Object> params = new ArrayList<>();

        for (String col : values.keySet()) {
            if (columns.length() > 0) {
                columns.append(", ");
                placeholders.append(", ");
            }
            columns.append(col);
            placeholders.append("?");
            params.add(values.get(col));
        }

        String query = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";

        try (Connection connection = DataConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {  // <== récupérer la clé générée
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // retourne l'ID généré
                } else {
                    throw new SQLException("Creating record failed, no ID obtained.");
                }
            }
        }
    }

    public void updateBy(String whereColumn, Object whereValue, Map<String, Object> values) throws SQLException {
        StringBuilder setClause = new StringBuilder();
        List<Object> params = new ArrayList<>();

        for (String col : values.keySet()) {
            if (setClause.length() > 0) {
                setClause.append(", ");
            }
            setClause.append(col).append(" = ?");
            params.add(values.get(col));
        }

        String query = "UPDATE " + tableName + " SET " + setClause + " WHERE " + whereColumn + " = ?";

        try (Connection connection = DataConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            stmt.setObject(params.size() + 1, whereValue);

            stmt.executeUpdate();
        }
    }






    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;
}
