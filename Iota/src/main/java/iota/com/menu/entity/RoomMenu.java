package iota.com.menu.entity;

import iota.com.menu.Menu;
import iota.com.menu.MenuItem;
import iota.com.model.Room;
import iota.com.model.RoomType;
import iota.com.service.RoomManager;
import iota.com.utils.ValidationUtils;

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
        menu.add(new MenuItem("List All Available Rooms", this::listAllAvailableRooms));
        menu.add(new MenuItem("Find Room by ID", this::findRoomById));
        menu.add(new MenuItem("Delete Room", this::deleteRoom));
        menu.add(new MenuItem("Back to Main Menu", () -> System.out.println("Returning to the main menu...")));
        menu.execute();
    }

    private void addNewRoom() {
        try {
            System.out.print("Enter Room Number: ");
            String roomNumberInput = scanner.nextLine();
            int roomNumber = Integer.parseInt(roomNumberInput);
            ValidationUtils.validatePositive(roomNumber, "Room Number");

            System.out.print("Enter Room Type (e.g., Single, Double, Suite): ");
            String roomTypeInput = scanner.nextLine();
            ValidationUtils.validateEnum(RoomType.class, roomTypeInput, "Room Type");
            RoomType roomType = RoomType.valueOf(roomTypeInput);

            System.out.print("Enter Room Price Per Night: ");
            String priceInput = scanner.nextLine();
            float pricePerNight = Float.parseFloat(priceInput);
            ValidationUtils.validatePositive(pricePerNight, "Room Price");

            System.out.print("Is the room available? (true/false): ");
            String availabilityInput = scanner.nextLine();
            ValidationUtils.validateBoolean(availabilityInput, "Room availability");
            boolean availabilityValue = Boolean.parseBoolean(availabilityInput);
            Room room = new Room(roomNumber, roomType, pricePerNight, availabilityValue);
            roomManager.addRoom(room);

            System.out.println("Room added successfully: " + room);

        } catch (NumberFormatException e) {
            System.err.println("Invalid number format. Please enter a valid number: " + e.getMessage());
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
        try {
            System.out.print("Enter Room ID: ");
            String idInput = scanner.nextLine();
            long id = Long.parseLong(idInput);
            ValidationUtils.validatePositive(id, "Room ID");

            Room room = roomManager.findRoomById(id);
            if (room != null) {
                System.out.println(room);
            } else {
                System.out.println("No room found with ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid Room ID format. Please enter a valid number: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error finding room: " + e.getMessage());
        }
    }

    private void deleteRoom() {
        try {
            System.out.print("Enter Room ID to delete: ");
            String idInput = scanner.nextLine();
            long id = Long.parseLong(idInput);
            ValidationUtils.validatePositive(id, "Room ID");

            roomManager.deleteRoom(id);
            System.out.println("Room deleted successfully!");
        } catch (NumberFormatException e) {
            System.err.println("Invalid Room ID format. Please enter a valid number: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Failed to delete room: " + e.getMessage());
        }
    }
}
