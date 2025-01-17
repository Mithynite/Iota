package iota.com.app;

import iota.com.config.DatabaseSingleton;
import iota.com.core.EntityManager;
import iota.com.service.BookingManager;
import iota.com.service.CustomerManager;
import iota.com.service.RoomManager;
import iota.com.utils.CsvManager;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.println("Initializing the Hotel Management System...");

        // Establish the database connection
        Connection connection = null;
        try {
            connection = DatabaseSingleton.getInstance();
            System.out.println("Database connection established successfully.");
        } catch (Exception e) {
            System.err.println("Failed to initialize the database connection. Please restart the application and try again. :D");
            System.exit(1);
        }

        try {
            // Initialize the service managers
            EntityManager entityManager = new EntityManager(connection);
            CsvManager csvManager = new CsvManager(entityManager);
            RoomManager roomManager = new RoomManager(entityManager);
            CustomerManager customerManager = new CustomerManager(entityManager);
            BookingManager bookingManager = new BookingManager(entityManager);

            // Start the application console
            AppConsole appConsole = new AppConsole(csvManager, roomManager, customerManager, bookingManager);
            appConsole.start();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while starting the application. Please try again! ");
        } finally {
            // Close the database connection on shutdown
            DatabaseSingleton.closeConnection();
        }
    }
}
