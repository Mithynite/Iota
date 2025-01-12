package iota.com.app;

import iota.com.menu.Menu;
import iota.com.menu.MenuItem;
import iota.com.menu.entity.BookingMenu;
import iota.com.menu.entity.CustomerMenu;
import iota.com.menu.entity.RoomMenu;
import iota.com.service.BookingManager;
import iota.com.service.CustomerManager;
import iota.com.service.RoomManager;

import java.util.Scanner;

public class AppConsole {
    private final Scanner scanner = new Scanner(System.in);
    private final RoomManager roomManager;
    private final CustomerManager customerManager;
    private final BookingManager bookingManager;

    public AppConsole(RoomManager roomManager, CustomerManager customerManager, BookingManager bookingManager) {
        this.roomManager = roomManager;
        this.customerManager = customerManager;
        this.bookingManager = bookingManager;
    }

    public void start() {
        Menu mainMenu = new Menu("Welcome to the Room Booking System! What would you like to do?");
        mainMenu.add(new MenuItem("Manage Customers", this::manageCustomers));
        mainMenu.add(new MenuItem("Manage Rooms", this::manageRooms));
        mainMenu.add(new MenuItem("Handle Bookings", this::handleBookings));
        mainMenu.add(new MenuItem("Exit Program", () -> {
            System.out.println("Goodbye!");
            System.exit(0);
        }));
        mainMenu.execute();
    }

    private void manageCustomers() {
        CustomerMenu customerMenu = new CustomerMenu(customerManager,bookingManager);
        customerMenu.showMenu();
    }

    private void manageRooms() {
        RoomMenu roomMenu = new RoomMenu(roomManager);
        roomMenu.showMenu();
    }

    private void handleBookings() {
        // Pass all three managers to BookingMenu for full functionality
        BookingMenu bookingMenu = new BookingMenu(bookingManager, customerManager, roomManager);
        bookingMenu.showMenu();
    }
}
