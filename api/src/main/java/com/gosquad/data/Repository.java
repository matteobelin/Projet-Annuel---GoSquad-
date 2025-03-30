package com.gosquad.data;

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

    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;
}
