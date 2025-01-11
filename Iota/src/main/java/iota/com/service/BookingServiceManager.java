package iota.com.service;

import iota.com.core.EntityManager;
import iota.com.model.Booking;
import iota.com.model.BookingService;
import iota.com.model.Service;

import java.util.List;

public class BookingServiceManager {
    private final EntityManager entityManager;

    public BookingServiceManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Link a service to a booking.
     */
    public void linkServiceToBooking(Booking booking, Service service) throws Exception {
        BookingService bookingService = new BookingService(booking, service);
        entityManager.persist(bookingService);
    }

    /**
     * Retrieve all services linked to a booking.
     */
    public List<Service> getServicesForBooking(Long bookingId) throws Exception {
        String query = """
            SELECT s.* 
            FROM service s 
            JOIN booking_service bs ON s.id = bs.service_id 
            WHERE bs.booking_id = ?
        """;
        return entityManager.query(Service.class, query, List.of(bookingId));
    }

    /**
     * Unlink a service from a booking.
     */
    public void unlinkServiceFromBooking(Long bookingId, Long serviceId) throws Exception {
        String query = "DELETE FROM booking_service WHERE booking_id = ? AND service_id = ?";
        entityManager.query(BookingService.class, query, List.of(bookingId, serviceId));
    }
}
