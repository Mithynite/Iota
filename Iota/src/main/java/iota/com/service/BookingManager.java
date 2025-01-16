package iota.com.service;

import iota.com.core.EntityManager;
import iota.com.model.Booking;
import iota.com.model.Customer;
import iota.com.model.Room;
import iota.com.model.BookingStatus;

import java.util.Date;
import java.util.List;

public class BookingManager {
    private final EntityManager entityManager;

    public BookingManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

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
     * Get booking by ID
     */
    public Booking getBooking(Long bookingId) throws Exception {
        return entityManager.find(Booking.class, bookingId);
    }

    /**
     * Cancel a booking and free up the associated room
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
     * Retrieve all bookings for a given customer ID
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
