package iota.com.app;

import iota.com.config.DatabaseSingleton;
import iota.com.core.EntityManager;
import iota.com.service.BookingManager;
import iota.com.service.CustomerManager;
import iota.com.service.RoomManager;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        System.out.println("Initializing the Hotel Room Booking System...");

        // Establish the database connection
        Connection connection = null;
        try {
            connection = DatabaseSingleton.getInstance();
            System.out.println("Database connection established successfully.");
        } catch (Exception e) {
            System.err.println("Failed to initialize the database connection: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            // Initialize the service managers
            EntityManager entityManager = new EntityManager(connection);  // Assume EntityManager is a custom implementation for database operations
            RoomManager roomManager = new RoomManager(entityManager);
            CustomerManager customerManager = new CustomerManager(entityManager);
            BookingManager bookingManager = new BookingManager(entityManager);

            // Start the application console
            AppConsole appConsole = new AppConsole(roomManager, customerManager, bookingManager);
            appConsole.start();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while starting the application: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close the database connection on shutdown
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Database connection closed.");
                } catch (Exception e) {
                    System.err.println("Failed to close the database connection: " + e.getMessage());
                }
            }
        }
    }
}
