package iota.com.model;

import iota.com.annotations.*;

@Table(name = "booking_service")
public class BookingService {

    @ManyToOne(foreignKeyName = "booking_id")
    @Column(name = "booking_id", canBeNull = false)
    private Booking booking;

    @ManyToOne(foreignKeyName = "service_id")
    @Column(name = "service_id", canBeNull = false)
    private Service service;

    public BookingService() {}

    public BookingService(Booking booking, Service service) {
        this.booking = booking;
        this.service = service;
    }
}
