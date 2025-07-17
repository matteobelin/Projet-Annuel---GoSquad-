package com.gosquad.infrastructure.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DataConfig {

    private static final Dotenv dotenv;

    static {
        Dotenv tempDotenv;
        try {
            tempDotenv = Dotenv.load();
        } catch (Exception e) {
            tempDotenv = null;
        }
        dotenv = tempDotenv;
    }

    private static String getEnv(String key) {
        if (dotenv != null && dotenv.get(key) != null) {
            return dotenv.get(key);
        }
        return System.getenv(key);
    }

    private static final boolean IS_TEST =
        Boolean.parseBoolean(System.getProperty("test.env", "false")) ||
        Boolean.parseBoolean(System.getenv("test.env") != null ? System.getenv("test.env") : "false");

    // Détection de l'environnement Docker via variable d'environnement
    private static final boolean IS_DOCKER = System.getenv("DOCKER_ENV") != null || System.getProperty("java.home", "").contains("alpine");

    private static final String DB_HOST = IS_DOCKER ? "postgres" : "localhost";

    private static final String DB_URL = "jdbc:postgresql://" + DB_HOST + ":5432/" + getEnv("POSTGRES_DB");
    private static final String DB_USER = getEnv("POSTGRES_USER");
    private static final String DB_PASSWORD = getEnv("POSTGRES_PASSWORD");

    @Bean
    public Connection connection() throws SQLException {
        if (IS_TEST) {
            return DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL", "sa", "");
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static Connection getConnection() throws SQLException {
        if (IS_TEST) {
            return DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL", "sa", "");
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
