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
import iota.com.utils.ValidationUtils;

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
            String customerIdInput = scanner.nextLine();
            long customerId = Long.parseLong(customerIdInput);
            ValidationUtils.validatePositive(customerId, "Customer ID");
            Customer customer = customerManager.findCustomerById(customerId);

            if (customer == null) {
                System.out.println("No customer found with ID " + customerId);
                return;
            }

            System.out.print("Enter Room ID: ");
            String roomIdInput = scanner.nextLine();
            long roomId = Long.parseLong(roomIdInput);
            ValidationUtils.validatePositive(roomId, "Room ID");
            Room room = roomManager.findRoomById(roomId);

            if (room == null) {
                System.out.println("No room found with ID " + roomId);
                return;
            } else if (!room.isAvailable()) {
                System.out.println("Room with ID " + roomId + " is unavailable.");
                return;
            }

            System.out.print("Enter Check-In Date (yyyy-MM-dd): ");
            String checkInDateInput = scanner.nextLine();
            ValidationUtils.validateDate(checkInDateInput, "Check-In Date");
            Date checkInDate = dateFormat.parse(checkInDateInput);

            System.out.print("Enter Check-Out Date (yyyy-MM-dd): ");
            String checkOutDateInput = scanner.nextLine();
            ValidationUtils.validateDate(checkOutDateInput, "Check-Out Date");
            Date checkOutDate = dateFormat.parse(checkOutDateInput);

            if (!checkInDate.before(checkOutDate)) {
                System.out.println("Check-Out Date must be after Check-In Date.");
                return;
            }

            bookingManager.createBooking(customer, room, checkInDate, checkOutDate, 0);
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
            String customerIdInput = scanner.nextLine();
            long customerId = Long.parseLong(customerIdInput);
            ValidationUtils.validatePositive(customerId, "Customer ID");
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
            String bookingIdInput = scanner.nextLine();
            long bookingId = Long.parseLong(bookingIdInput);
            ValidationUtils.validatePositive(bookingId, "Booking ID");
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
            String bookingIdInput = scanner.nextLine();
            long bookingId = Long.parseLong(bookingIdInput);
            ValidationUtils.validatePositive(bookingId, "Booking ID");

            System.out.println("Select New Status:");
            for (BookingStatus status : BookingStatus.values()) {
                System.out.println("- " + status.name());
            }
            System.out.print("Enter Status: ");
            String newStatusString = scanner.nextLine();
            ValidationUtils.validateEnum(BookingStatus.class, newStatusString, "Booking Status");

            BookingStatus newStatus = BookingStatus.valueOf(newStatusString.toUpperCase());
            bookingManager.updateBookingStatus(bookingId, newStatus);
            System.out.println("Booking status updated to " + newStatus.name() + " successfully!");
        } catch (Exception e) {
            System.err.println("Error while updating booking status: " + e.getMessage());
        }
    }
}
