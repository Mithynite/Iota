package iota.com.service;

import iota.com.core.EntityManager;
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

    public List<Room> findAvailableRooms() throws Exception {
        String query = "SELECT * FROM room WHERE is_available = TRUE";
        return entityManager.query(Room.class, query, List.of());
    }

    public void updateRoom(Room room) throws Exception {
        entityManager.update(room);
    }

    public void deleteRoom(Long id) throws Exception {
        entityManager.delete(Room.class, id);
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

