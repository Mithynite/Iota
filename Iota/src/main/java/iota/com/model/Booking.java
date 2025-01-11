package iota.com.model;

import iota.com.annotations.*;

import java.util.Date;

@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", isPrimaryKey = true, canBeNull = false)
    private Long id;

    @ManyToOne(foreignKeyName = "customer_id") // Foreign key to customer
    @Column(name = "customer_id", canBeNull = false)
    private Customer customer;

    @ManyToOne(foreignKeyName = "room_id")
    @Column(name = "room_id", canBeNull = false)
    private Room room;

    @Column(name = "check_in_date", canBeNull = false)
    private Date checkInDate;
    @Column(name = "check_out_date", canBeNull = false)
    private Date checkOutDate;
    @Column(name = "total_amount", canBeNull = false)
    private float totalAmount;

    @Column(name = "booking_status")
    private BookingStatus bookingStatus;

    public Booking(Customer customer, Room room, Date checkInDate, Date checkOutDate, float totalAmount, BookingStatus bookingStatus) {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalAmount = totalAmount;
        this.bookingStatus = bookingStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "customer=" + customer +
                ", room=" + room +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", totalAmount=" + totalAmount +
                ", bookingStatus=" + bookingStatus +
                '}';
    }
}
