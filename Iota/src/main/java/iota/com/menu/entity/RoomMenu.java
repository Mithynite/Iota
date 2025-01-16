package iota.com.menu.entity;

import iota.com.menu.Menu;
import iota.com.menu.MenuItem;
import iota.com.model.Room;
import iota.com.model.RoomType;
import iota.com.service.RoomManager;
import iota.com.utils.CsvManager;
import iota.com.utils.ValidationUtils;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class RoomMenu {
    private final RoomManager roomManager;
    private final Scanner scanner = new Scanner(System.in);
    private final CsvManager csvManager;

    public RoomMenu(RoomManager roomManager, CsvManager csvManager) {
        this.roomManager = roomManager;
        this.csvManager = csvManager;
    }

    public void showMenu() {
        Menu menu = new Menu("Room Management");
        menu.add(new MenuItem("Add New Room", this::addNewRoom));
        menu.add(new MenuItem("List All Available Rooms", this::listAllRooms));
        menu.add(new MenuItem("Find Room by ID", this::findRoomById));
        menu.add(new MenuItem("Delete Room", this::deleteRoom));
        menu.add(new MenuItem("Import data from CSV", this::importRoomsFromCsv));
        menu.add(new MenuItem("Back to Main Menu", () -> System.out.println("Returning to the main menu...")));
        menu.execute();
    }

    private void addNewRoom() {
        try {
            System.out.print("Enter Room Number: ");
            String roomNumberInput = scanner.nextLine();
            int roomNumber = Integer.parseInt(roomNumberInput);
            ValidationUtils.validatePositive(roomNumber, "Room Number");

            // Check if the room number already exists in the database
            Room roomExistenceCheck = roomManager.findRoomByNumber(roomNumber);
            if (roomExistenceCheck != null) {
                // Display a message and stop further processing
                System.out.println("Room with number " + roomNumber + " already exists in the database!");
                return; // Exit the method to prevent continuing further
            }

            System.out.print("Enter Room Type (Single/Twin/Suite): ");
            String roomTypeInput = scanner.nextLine();
            ValidationUtils.validateEnum(RoomType.class, roomTypeInput, "Room Type", false);
            RoomType roomType = RoomType.valueOf(roomTypeInput.toLowerCase());

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

    private void listAllRooms() {
        try {
            var rooms = roomManager.findAllRooms();
            if (rooms.isEmpty()) {
                System.out.println("No rooms found.");
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
            Room room = roomManager.findRoomById(id);
            if (room == null) {
                System.out.println("No room found with ID: " + id);
                return;
            }

            roomManager.deleteRoomWithBookings(id);
            System.out.println("Room successfully deleted with bookings!");
        } catch (NumberFormatException e) {
            System.err.println("Invalid Room ID format. Please enter a valid number: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Failed to delete room: " + e.getMessage());
        }
    }

    private void importRoomsFromCsv() {
        System.out.print("Enter the absolute path to the CSV file (e.g. C:\\Downloads\\file.csv): ");
        String csvFilePath = scanner.nextLine();

        // Check if the file exists before proceeding
        File file = new File(csvFilePath);
        if (!file.exists()) {
            System.err.println("Error: File not found at " + csvFilePath);
            return;
        }

        // Define the column mapping for the Room entity
        List<String> columnMapping = List.of("roomNumber", "roomType", "pricePerNight", "isAvailable");

        try {
            csvManager.importCsv(Room.class, csvFilePath, columnMapping);
            System.out.println("Rooms imported successfully from " + csvFilePath);
        } catch (Exception e) {
            System.err.println("Error importing rooms from the file. Please check its structure and try again! ");
        }
    }

}
