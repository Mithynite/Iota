package iota.com.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseSingleton {


    private static Connection connection;

    private DatabaseSingleton() {

    }
    public static Connection getInstance() {
        if (connection == null) {
            try {
                // Load database properties from the config file
                Properties dbProperties = loadDatabaseProperties();

                String url = dbProperties.getProperty("db.url");
                String username = dbProperties.getProperty("db.username");
                String password = dbProperties.getProperty("db.password");

                // Establish the MySQL connection
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException | IOException e) {
                throw new RuntimeException("Error while establishing the database connection: " + e.getMessage(), e);
            }
        }
        return connection;
    }

    /**
     * Close the database connection.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null; // Reset the instance for future use
            } catch (SQLException e) {
                throw new RuntimeException("Error while closing the database connection: " + e.getMessage(), e);
            }
        }
    }

    private static Properties loadDatabaseProperties() throws IOException {
        Properties properties = new Properties();

        try (InputStream input = DatabaseSingleton.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {  // Reference relative to 'src/main/resources'

            if (input == null) {
                throw new IOException("Database configuration file 'config.properties' not found.");
            }

            properties.load(input);
        }

        return properties;
    }
}

