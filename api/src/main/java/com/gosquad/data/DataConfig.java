package com.gosquad.data;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataConfig {
    private static final Dotenv dotenv = Dotenv.load();

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/" + dotenv.get("POSTGRES_DB");
    private static final String DB_USER = dotenv.get("POSTGRES_USER");
    private static final String DB_PASSWORD = dotenv.get("POSTGRES_PASSWORD");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
