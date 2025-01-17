package iota.com.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * A singleton class that manages a single database connection.
 * It provides methods to establish and close the connection.
 */
public class DatabaseSingleton {

    /**
     * The single instance of the DatabaseSingleton class.
     */
    private static Connection connection;

    /**
     * Private constructor to prevent instantiation from outside the class.
     */
    private DatabaseSingleton() {
    }

    /**
     * Returns the single instance of the DatabaseSingleton class.
     * If the connection is not established yet, it loads the database properties from the config file,
     * establishes the MySQL connection, and returns it.
     *
     * @return The database connection.
     * @throws RuntimeException If an error occurs while establishing the database connection.
     */
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
     * Closes the database connection if it is open.
     * It also resets the instance for future use.
     *
     * @throws RuntimeException If an error occurs while closing the database connection.
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

    /**
     * Loads the database properties from the config file.
     *
     * @return The loaded database properties.
     * @throws IOException If the database configuration file is not found.
     */
    private static Properties loadDatabaseProperties() throws IOException {
        Properties properties = new Properties();

        try (InputStream input = DatabaseSingleton.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new IOException("Database configuration file 'config.properties' not found.");
            }

            properties.load(input);
        }

        return properties;
    }
}

