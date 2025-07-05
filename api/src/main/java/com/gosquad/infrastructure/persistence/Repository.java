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

    public List<T> findAllBy(Map<String, Object> conditions, String... selectedColumns) throws SQLException {
        List<T> results = new ArrayList<>();
        String cols = (selectedColumns.length > 0) ? String.join(", ", selectedColumns) : "*";

        // Construire la clause WHERE avec toutes les conditions
        StringBuilder whereClause = new StringBuilder();
        List<Object> values = new ArrayList<>();

        for (Map.Entry<String, Object> entry : conditions.entrySet()) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }
            whereClause.append(entry.getKey()).append(" = ?");
            values.add(entry.getValue());
        }

        String query = "SELECT " + cols + " FROM " + tableName + " WHERE " + whereClause.toString();

        try (Connection connection = DataConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            // Définir tous les paramètres
            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }

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


    public void updateByMultiple(Map<String, Object> whereConditions, Map<String, Object> valuesToUpdate) throws SQLException {
        if (whereConditions == null || whereConditions.isEmpty()) {
            throw new IllegalArgumentException("WHERE conditions cannot be null or empty.");
        }

        if (valuesToUpdate == null || valuesToUpdate.isEmpty()) {
            throw new IllegalArgumentException("Values to update cannot be null or empty.");
        }

        StringBuilder setClause = new StringBuilder();
        StringBuilder whereClause = new StringBuilder();
        List<Object> parameters = new ArrayList<>();

        // Build SET clause
        for (Map.Entry<String, Object> entry : valuesToUpdate.entrySet()) {
            if (setClause.length() > 0) {
                setClause.append(", ");
            }
            setClause.append(entry.getKey()).append(" = ?");
            parameters.add(entry.getValue());
        }

        // Build WHERE clause
        for (Map.Entry<String, Object> entry : whereConditions.entrySet()) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }
            whereClause.append(entry.getKey()).append(" = ?");
            parameters.add(entry.getValue());
        }

        String query = "UPDATE " + tableName + " SET " + setClause + " WHERE " + whereClause;

        try (Connection connection = DataConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }

            stmt.executeUpdate();
        }
    }

    public void deleteBy(Map<String, Object> conditions) throws SQLException {
        if (conditions == null || conditions.isEmpty()) {
            throw new IllegalArgumentException("Conditions map cannot be null or empty.");
        }

        StringBuilder whereClause = new StringBuilder();
        List<Object> parameters = new ArrayList<>();

        for (Map.Entry<String, Object> entry : conditions.entrySet()) {
            String column = entry.getKey();
            Object value = entry.getValue();

            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            if (value instanceof List) {
                List<?> listValues = (List<?>) value;
                if (listValues.isEmpty()) {
                    throw new IllegalArgumentException("List for column '" + column + "' cannot be empty.");
                }

                String placeholders = String.join(", ", listValues.stream().map(v -> "?").toArray(String[]::new));
                whereClause.append(column).append(" IN (").append(placeholders).append(")");
                parameters.addAll(listValues);
            } else {
                whereClause.append(column).append(" = ?");
                parameters.add(value);
            }
        }

        String query = "DELETE FROM " + tableName + " WHERE " + whereClause;

        try (Connection connection = DataConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }

            stmt.executeUpdate();
        }
    }







    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;
}
