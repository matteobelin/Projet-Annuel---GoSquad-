package com.gosquad.infrastructure.persistence.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class TestDatabaseHelper {

    private static Connection connection;

    /**
     * Initialise la base de données de test H2 avec le schéma
     * @return Connection la connexion à la base de données
     * @throws SQLException en cas d'erreur SQL
     */
    public static Connection setupTestDatabase() throws SQLException {
        // Set test environment flag
        System.setProperty("test.env", "true");

        // Create H2 connection
        connection = DriverManager.getConnection(
                "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
                "sa",
                ""
        );

        // Load and execute schema
        loadSchema();

        return connection;
    }

    /**
     * Charge et exécute le fichier schema.sql
     * @throws SQLException en cas d'erreur SQL
     */
    private static void loadSchema() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Read schema.sql from resources
            String schemaContent = loadResourceFile("/schema.sql");

            // Split by semicolon and execute each statement
            String[] statements = schemaContent.split(";");
            for (String sql : statements) {
                sql = sql.trim();
                if (!sql.isEmpty()) {
                    stmt.execute(sql);
                }
            }
        }
    }

    /**
     * Charge un fichier depuis les resources
     * @param filename le nom du fichier à charger
     * @return String le contenu du fichier
     */
    private static String loadResourceFile(String filename) {
        try (Scanner scanner = new Scanner(TestDatabaseHelper.class.getResourceAsStream(filename))) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }

    /**
     * Ferme la connexion à la base de données de test
     * @throws SQLException en cas d'erreur SQL
     */
    public static void tearDown() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS CUSTOMER CASCADE");
            stmt.execute("DROP TABLE IF EXISTS ADDRESSES CASCADE");
            stmt.execute("DROP TABLE IF EXISTS CITIES CASCADE");
            stmt.execute("DROP TABLE IF EXISTS COUNTRIES CASCADE");
        }
        if (connection != null && !connection.isClosed()) {
            connection.close();
            connection = null;
        }
    }

    /**
     * Récupère la connexion courante
     * @return Connection la connexion active
     */
    public static Connection getConnection() {
        return connection;
    }
}