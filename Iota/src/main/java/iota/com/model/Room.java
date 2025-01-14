package iota.com.model;

import iota.com.annotations.*;

@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", isPrimaryKey = true, canBeNull = false)
    private Long id;

    @Column(name = "room_number", canBeNull = false)
    private Integer roomNumber;

    @Column(name = "room_type", canBeNull = false)
    private RoomType roomType;

    @Column(name = "price_per_night", canBeNull = false)
    private float pricePerNight;

    @Column(name = "is_available", canBeNull = false)
    private boolean isAvailable;

    public Room() {
    }

    public Room(int roomNumber, RoomType roomType, float pricePerNight, boolean isAvailable) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.isAvailable = isAvailable;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public float getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(float pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Room " + "id=" + id + ", roomNumber=" + roomNumber + ", roomType=" + roomType +
                ", pricePerNight=" + pricePerNight + ", isAvailable=" + isAvailable;
    }
}