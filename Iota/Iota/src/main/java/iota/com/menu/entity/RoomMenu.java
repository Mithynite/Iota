package iota.com.menu.entity;

import iota.com.menu.Menu;
import iota.com.menu.MenuItem;
import iota.com.model.Room;
import iota.com.service.RoomManager;

import java.util.Scanner;

public class RoomMenu {
    private final RoomManager roomManager;
    private final Scanner scanner = new Scanner(System.in);

    public RoomMenu(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    public void showMenu() {
        Menu menu = new Menu("Room Management");
        menu.add(new MenuItem("Add New Room", this::addNewRoom));
        menu.add(new MenuItem("List All Rooms", this::listAllAvailableRooms));
        menu.add(new MenuItem("Find Room by ID", this::findRoomById));
        menu.add(new MenuItem("Delete Room", this::deleteRoom));
        menu.add(new MenuItem("Back to Main Menu", () -> System.out.println("Returning to the main menu...")));
        menu.execute();
    }

    private void addNewRoom() {
        System.out.print("Enter Room Number: ");
        String roomNumber = scanner.nextLine();
        System.out.print("Enter Room Type (e.g., Single, Double, Suite): ");
        String roomType = scanner.nextLine();
        System.out.print("Enter Room Price Per Night: ");
        float pricePerNight = Float.parseFloat(scanner.nextLine());
        System.out.print("Is the room available? (true/false): ");
        boolean isAvailable = Boolean.parseBoolean(scanner.nextLine());
        try {
            // Create a Room object with the correct constructor
            Room room = new Room(roomNumber, roomType, pricePerNight, isAvailable);
            roomManager.addRoom(room);
            System.out.println("Room added successfully: " + room);
        } catch (Exception e) {
            System.err.println("Failed to add room: " + e.getMessage());
        }
    }

    private void listAllAvailableRooms() {
        try {
            var rooms = roomManager.findAvailableRooms();
            if (rooms.isEmpty()) {
                System.out.println("No available rooms found.");
            } else {
                rooms.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.err.println("Failed to list rooms: " + e.getMessage());
        }
    }

    private void findRoomById() {
        System.out.print("Enter Room ID: ");
        long id = Long.parseLong(scanner.nextLine());
        try {
            Room room = roomManager.findRoomById(id);
            if (room != null) {
                System.out.println(room);
            } else {
                System.out.println("No room found with ID: " + id);
            }
        } catch (Exception e) {
            System.err.println("Error finding room: " + e.getMessage());
        }
    }

    private void deleteRoom() {
        System.out.print("Enter Room ID to delete: ");
        long id = Long.parseLong(scanner.nextLine());
        try {
            roomManager.deleteRoom(id);
            System.out.println("Room deleted successfully!");
        } catch (Exception e) {
            System.err.println("Failed to delete room: " + e.getMessage());
        }
    }
}

