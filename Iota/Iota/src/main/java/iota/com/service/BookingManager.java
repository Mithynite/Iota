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

    /**
     * Create a new booking and update room availability
     */
    public void createBooking(Customer customer, Room room, Date checkInDate, Date checkOutDate, float totalAmount) throws Exception {
        // Check room availability
        Room dbRoom = entityManager.find(Room.class, room.getId());
        if (dbRoom == null || !dbRoom.isAvailable()) {
            throw new IllegalStateException("Room with ID " + room.getId() + " is unavailable.");
        }

        // Create new booking
        Booking booking = new Booking(customer, dbRoom, checkInDate, checkOutDate, totalAmount, BookingStatus.PENDING);
        entityManager.persist(booking);

        // Mark room as unavailable
        dbRoom.setAvailable(false);
        entityManager.update(dbRoom);
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
        Customer customer = entityManager.find(Customer.class, customerId);
        if (customer == null) {
            throw new IllegalArgumentException("No customer found with ID " + customerId);
        }

        // Query all bookings for this customer
        String query = "SELECT * FROM booking WHERE customer_id = ?";
        return entityManager.query(Booking.class, query, List.of(customerId));
    }

    /**
     * Update booking status
     */
    public void updateBookingStatus(Long bookingId, BookingStatus newStatus) throws Exception {
        Booking booking = entityManager.find(Booking.class, bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("No booking found with ID " + bookingId);
        }

        booking.setBookingStatus(newStatus);
        entityManager.update(booking);
    }
}
