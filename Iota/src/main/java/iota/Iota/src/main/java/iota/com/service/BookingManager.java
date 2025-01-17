package iota.com.service;

import iota.com.core.EntityManager;
import iota.com.model.Booking;
import iota.com.model.Customer;
import iota.com.model.Room;
import iota.com.model.BookingStatus;

import java.util.Date;
import java.util.List;

/**
 * Manages booking operations for a hotel.
 */
public class BookingManager {
    private final EntityManager entityManager;

    /**
     * Constructs a new BookingManager instance.
     *
     * @param entityManager The EntityManager to use for database operations.
     */
    public BookingManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Creates a new booking for a customer in a specific room.
     *
     * @param customer The customer making the booking.
     * @param room The room to book.
     * @param checkInDate The check-in date.
     * @param checkOutDate The check-out date.
     * @param totalAmount The total amount of the booking.
     * @throws Exception If any error occurs during the booking process.
     */
    public void createBooking(Customer customer, Room room, Date checkInDate, Date checkOutDate, float totalAmount) throws Exception {
        // Start transaction in your EntityManager
        entityManager.beginTransaction();
        try {
            // 1. Validate Room Availability
            Room dbRoom = entityManager.find(Room.class, room.getId());
            if (dbRoom == null || !dbRoom.isAvailable()) {
                throw new IllegalStateException("Room with ID " + room.getId() + " is unavailable.");
            }

            // 2. Persist Booking
            Booking booking = new Booking(customer, dbRoom, checkInDate, checkOutDate, totalAmount, BookingStatus.pending);
            entityManager.persist(booking);

            // 3. Update Room to unavailable
            dbRoom.setAvailable(false);
            entityManager.update(dbRoom);

            // Commit transaction
            entityManager.commitTransaction();
        } catch (Exception ex) {
            entityManager.rollbackTransaction(); // Rollback transaction on failure
            throw new RuntimeException("Failed to create booking: " + ex.getMessage());
        }
    }

    /**
     * Retrieves a booking by its ID.
     *
     * @param bookingId The ID of the booking to retrieve.
     * @return The booking with the specified ID.
     * @throws Exception If any error occurs during the retrieval process.
     */
    public Booking getBooking(Long bookingId) throws Exception {
        return entityManager.find(Booking.class, bookingId);
    }

    /**
     * Cancels a booking and frees up the associated room.
     *
     * @param bookingId The ID of the booking to cancel.
     * @throws Exception If any error occurs during the cancellation process.
     */
    public void cancelBooking(Long bookingId) throws Exception {
        Booking booking = entityManager.find(Booking.class, bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("No booking found with ID " + bookingId);
        }

        Room room = booking.getRoom();
        if (room != null) {
            room.setAvailable(true);
            entityManager.update(room);
        }

        entityManager.delete(Booking.class, bookingId);
    }

    /**
     * Retrieves all bookings for a given customer ID.
     *
     * @param customerId The ID of the customer.
     * @return A list of bookings made by the customer.
     * @throws Exception If any error occurs during the retrieval process.
     */
    public List<Booking> getBookingsForCustomer(Long customerId) throws Exception {
        // Check if the customer exists
        Customer customer = entityManager.find(Customer.class, customerId);
        if (customer == null) {
            throw new IllegalArgumentException("No customer found with ID " + customerId);
        }

        // Query all bookings for this customer
        String query = "SELECT * FROM booking WHERE customer_id = ?";
        List<Booking> bookings = entityManager.query(Booking.class, query, List.of(customerId));

        // Validate if bookings include populated customer objects
        bookings.forEach(booking -> {
            if (booking.getCustomer() == null) {
                booking.setCustomer(customer); // Fix null customers, if needed
            }
        });

        return bookings;
    }

    /**
     * Updates the status of a booking.
     *
     * @param bookingId The ID of the booking to update.
     * @param newStatus The new status of the booking.
     * @throws Exception If any error occurs during the update process.
     */
    public void updateBookingStatus(Long bookingId, BookingStatus newStatus) throws Exception {
        // Begin transaction
        entityManager.beginTransaction();
        try {
            Booking booking = entityManager.find(Booking.class, bookingId);
            if (booking == null) {
                throw new IllegalArgumentException("No booking found with ID " + bookingId);
            }

            // Update booking status
            booking.setBookingStatus(newStatus);
            entityManager.update(booking);

            Room room = entityManager.find(Room.class, booking.getRoomId());

            // Retrieve associated room and update its availability
            room.setAvailable(newStatus == BookingStatus.cancelled);

            entityManager.update(room);

            // Commit transaction
            entityManager.commitTransaction();
        } catch (Exception ex) {
            entityManager.rollbackTransaction(); // Roll back if any error occurs
            throw new RuntimeException("Failed to update booking status: " + ex.getMessage());
        }
    }
}
