package iota.com.menu.entity;

import iota.com.menu.Menu;
import iota.com.menu.MenuItem;
import iota.com.model.Booking;
import iota.com.model.BookingStatus;
import iota.com.model.Customer;
import iota.com.model.Room;
import iota.com.service.BookingManager;
import iota.com.service.CustomerManager;
import iota.com.service.RoomManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class BookingMenu {
    private final BookingManager bookingManager;
    private final CustomerManager customerManager;
    private final RoomManager roomManager; // Needed for room availability
    private final Scanner scanner = new Scanner(System.in);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public BookingMenu(BookingManager bookingManager, CustomerManager customerManager, RoomManager roomManager) {
        this.bookingManager = bookingManager;
        this.customerManager = customerManager;
        this.roomManager = roomManager;
    }

    public void showMenu() {
        Menu bookingMenu = new Menu("Booking Management");
        bookingMenu.add(new MenuItem("Create New Booking", this::createBooking));
        bookingMenu.add(new MenuItem("List Customer Bookings", this::listBookingsForCustomer));
        bookingMenu.add(new MenuItem("Cancel Booking", this::cancelBooking));
        bookingMenu.add(new MenuItem("Update Booking Status", this::updateBookingStatus));
        bookingMenu.add(new MenuItem("Back to Main Menu", () -> System.out.println("Returning to main menu...")));
        bookingMenu.execute();
    }

    /**
     * Create a new booking
     */
    private void createBooking() {
        try {
            System.out.print("Enter Customer ID: ");
            Long customerId = Long.parseLong(scanner.nextLine());
            Customer customer = customerManager.findCustomerById(customerId);

            if (customer == null) {
                System.out.println("No customer found with ID " + customerId);
                return;
            }

            System.out.print("Enter Room ID: ");
            Long roomId = Long.parseLong(scanner.nextLine());
            Room room = roomManager.findRoomById(roomId);

            if (room == null) {
                System.out.println("No room found with ID " + roomId);
                return;
            } else if (!room.isAvailable()) {
                System.out.println("Room with ID " + roomId + " is unavailable.");
                return;
            }

            System.out.print("Enter Check-In Date (yyyy-MM-dd): ");
            Date checkInDate = dateFormat.parse(scanner.nextLine());

            System.out.print("Enter Check-Out Date (yyyy-MM-dd): ");
            Date checkOutDate = dateFormat.parse(scanner.nextLine());

            if (!checkInDate.before(checkOutDate)) {
                System.out.println("Check-Out Date must be after Check-In Date.");
                return;
            }

            System.out.print("Enter Total Amount: ");
            float totalAmount = Float.parseFloat(scanner.nextLine());

            bookingManager.createBooking(customer, room, checkInDate, checkOutDate, totalAmount);
            System.out.println("Booking created successfully!");
        } catch (Exception e) {
            System.err.println("Error while creating booking: " + e.getMessage());
        }
    }

    /**
     * List all bookings for a given customer
     */
    private void listBookingsForCustomer() {
        try {
            System.out.print("Enter Customer ID: ");
            Long customerId = Long.parseLong(scanner.nextLine());
            List<Booking> bookings = bookingManager.getBookingsForCustomer(customerId);

            if (bookings.isEmpty()) {
                System.out.println("No bookings found for customer ID " + customerId);
            } else {
                bookings.forEach(booking -> {
                    System.out.println(booking.toString());
                });
            }
        } catch (Exception e) {
            System.err.println("Error while retrieving bookings: " + e.getMessage());
        }
    }

    /**
     * Cancel a booking
     */
    private void cancelBooking() {
        try {
            System.out.print("Enter Booking ID to cancel: ");
            Long bookingId = Long.parseLong(scanner.nextLine());
            bookingManager.cancelBooking(bookingId);
            System.out.println("Booking canceled successfully!");
        } catch (Exception e) {
            System.err.println("Error while canceling booking: " + e.getMessage());
        }
    }

    /**
     * Update booking status
     */
    private void updateBookingStatus() {
        try {
            System.out.print("Enter Booking ID: ");
            Long bookingId = Long.parseLong(scanner.nextLine());

            System.out.println("Select New Status:");
            for (BookingStatus status : BookingStatus.values()) {
                System.out.println("- " + status.name());
            }
            System.out.print("Enter Status: ");
            String newStatusString = scanner.nextLine();

            BookingStatus newStatus;
            try {
                newStatus = BookingStatus.valueOf(newStatusString.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status. Please choose a valid status.");
                return;
            }

            bookingManager.updateBookingStatus(bookingId, newStatus);
            System.out.println("Booking status updated to " + newStatus.name() + " successfully!");
        } catch (Exception e) {
            System.err.println("Error while updating booking status: " + e.getMessage());
        }
    }
}
