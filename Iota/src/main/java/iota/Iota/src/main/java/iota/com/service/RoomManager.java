package iota.com.service;

import iota.com.core.EntityManager;
import iota.com.model.Booking;
import iota.com.model.Room;

import java.util.List;

/**
 * Manages operations related to rooms in a hotel booking system.
 */
public class RoomManager {
    private final EntityManager entityManager;

    /**
     * Constructs a new RoomManager instance.
     *
     * @param entityManager The EntityManager used for database operations.
     */
    public RoomManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Adds a new room to the database.
     *
     * @param room The room to be added.
     * @throws Exception If an error occurs during database operation.
     */
    public void addRoom(Room room) throws Exception {
        entityManager.persist(room);
    }

    /**
     * Finds a room by its ID.
     *
     * @param id The ID of the room to be found.
     * @return The room with the specified ID, or null if not found.
     * @throws Exception If an error occurs during database operation.
     */
    public Room findRoomById(Long id) throws Exception {
        return entityManager.find(Room.class, id);
    }

    /**
     * Finds a room by its number.
     *
     * @param number The number of the room to be found.
     * @return The room with the specified number, or null if not found.
     * @throws Exception If an error occurs during database operation.
     */
    public Room findRoomByNumber(Integer number) throws Exception {
        String sql = "SELECT * FROM room WHERE room_number = ?";
        List<Room> rooms = entityManager.query(Room.class, sql, List.of(number));
        // Return the first room found or null if no room matches
        return rooms.isEmpty() ? null : rooms.getFirst();
    }

    /**
     * Retrieves all rooms from the database.
     *
     * @return A list of all rooms.
     * @throws Exception If an error occurs during database operation.
     */
    public List<Room> findAllRooms() throws Exception {
        String query = "SELECT * FROM room";
        return entityManager.query(Room.class, query, List.of());
    }

    /**
     * Updates an existing room in the database.
     *
     * @param room The room to be updated.
     * @throws Exception If an error occurs during database operation.
     */
    public void updateRoom(Room room) throws Exception {
        entityManager.update(room);
    }

    /**
     * Deletes a room from the database by its ID.
     *
     * @param id The ID of the room to be deleted.
     * @throws Exception If an error occurs during database operation.
     */
    public void deleteRoom(Long id) throws Exception {
        entityManager.delete(Room.class, id);
    }

    /**
     * Deletes a room from the database by its ID, along with all associated bookings.
     *
     * @param roomId The ID of the room to be deleted.
     * @throws Exception If an error occurs during database operation.
     */
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

    /**
     * Marks a room as unavailable in the database.
     *
     * @param roomId The ID of the room to be marked as unavailable.
     * @throws Exception If an error occurs during database operation.
     */
    public void markRoomAsUnavailable(Long roomId) throws Exception {
        Room room = findRoomById(roomId);
        if (room != null) {
            room.setAvailable(false);
            updateRoom(room);
        }
    }

    /**
     * Marks a room as available in the database.
     *
     * @param roomId The ID of the room to be marked as available.
     * @throws Exception If an error occurs during database operation.
     */
    public void markRoomAsAvailable(Long roomId) throws Exception {
        Room room = findRoomById(roomId);
        if (room != null) {
            room.setAvailable(true);
            updateRoom(room);
        }
    }
}

