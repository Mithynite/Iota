package iota.com.service;

import iota.com.core.EntityManager;
import iota.com.model.Booking;
import iota.com.model.Room;

import java.util.List;

public class RoomManager{
    private final EntityManager entityManager;

    public RoomManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void addRoom(Room room) throws Exception {
        entityManager.persist(room);
    }

    public Room findRoomById(Long id) throws Exception {
        return entityManager.find(Room.class, id);
    }

    public Room findRoomByNumber(Integer number) throws Exception {
        String sql = "SELECT * FROM room WHERE room_number = ?";
        List<Room> rooms = entityManager.query(Room.class, sql, List.of(number));
        // Return the first room found or null if no room matches
        return rooms.isEmpty() ? null : rooms.getFirst();
    }


    public List<Room> findAllRooms() throws Exception {
        String query = "SELECT * FROM room";
        return entityManager.query(Room.class, query, List.of());
    }

    public void updateRoom(Room room) throws Exception {
        entityManager.update(room);
    }

    public void deleteRoom(Long id) throws Exception {
        entityManager.delete(Room.class, id);
    }

    public void deleteRoomWithBookings(Long roomId) throws Exception {
        entityManager.beginTransaction();
        try {
            // Check if the room exists
            Room room = findRoomById(roomId);
            if (room == null) {
                throw new IllegalArgumentException("No room found with ID: " + roomId);
            }

            // Find and delete all bookings for this room
            String bookingQuery = "SELECT * FROM booking WHERE room_id = ?";
            List<Booking> bookings = entityManager.query(Booking.class, bookingQuery, List.of(roomId));
            for (Booking booking : bookings) {
                entityManager.delete(Booking.class, booking.getId());
            }

            // Delete the room itself
            entityManager.delete(Room.class, roomId);

            entityManager.commitTransaction();
        } catch (Exception e) {
            entityManager.rollbackTransaction();
            throw e; // Propagate the exception
        }
    }

    public void markRoomAsUnavailable(Long roomId) throws Exception {
        Room room = findRoomById(roomId);
        if (room != null) {
            room.setAvailable(false);
            updateRoom(room);
        }
    }

    public void markRoomAsAvailable(Long roomId) throws Exception {
        Room room = findRoomById(roomId);
        if (room != null) {
            room.setAvailable(true);
            updateRoom(room);
        }
    }
}

