package iota.com.app;

import iota.com.menu.Menu;
import iota.com.menu.MenuItem;
import iota.com.menu.entity.BookingMenu;
import iota.com.menu.entity.CustomerMenu;
import iota.com.menu.entity.RoomMenu;
import iota.com.service.BookingManager;
import iota.com.service.CustomerManager;
import iota.com.service.RoomManager;
import iota.com.utils.CsvManager;

import java.util.Scanner;

public class AppConsole {
    private final Scanner scanner = new Scanner(System.in);
    private final CsvManager csvManager;
    private final RoomManager roomManager;
    private final CustomerManager customerManager;
    private final BookingManager bookingManager;

    public AppConsole(CsvManager csvManager, RoomManager roomManager, CustomerManager customerManager, BookingManager bookingManager) {
        this.csvManager = csvManager;
        this.roomManager = roomManager;
        this.customerManager = customerManager;
        this.bookingManager = bookingManager;
    }

    public void start() {
        Menu mainMenu = new Menu("Welcome to the Hotel Management System! What would you like to do?");
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
        CustomerMenu customerMenu = new CustomerMenu(csvManager, customerManager,bookingManager);
        customerMenu.showMenu();
    }

    private void manageRooms() {
        RoomMenu roomMenu = new RoomMenu(roomManager, csvManager);
        roomMenu.showMenu();
    }

    private void handleBookings() {
        BookingMenu bookingMenu = new BookingMenu(bookingManager, customerManager, roomManager);
        bookingMenu.showMenu();
    }
}
