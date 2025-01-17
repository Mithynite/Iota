package iota.com.menu;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents a menu that can be displayed and interacted with.
 */
public class Menu {
    private String caption;
    private ArrayList<MenuItem> menuItems;
    private Scanner scanner = new Scanner(System.in);

    /**
     * Constructs a new Menu with the given caption.
     *
     * @param caption The caption to display at the top of the menu.
     */
    public Menu(String caption) {
        this.caption = caption;
        this.menuItems = new ArrayList<>();
    }

    /**
     * Displays the menu to the console.
     */
    public void show() {
        System.out.println("\n" + caption);
        for (int i = 0; i < menuItems.size(); i++) {
            System.out.println((i + 1) + ". " + menuItems.get(i).getDescription()); // Calls toString() on MenuItem
        }
    }

    /**
     * Selects a menu item based on the given user input (index).
     *
     * @param userInput The index of the menu item to select.
     * @return The selected menu item, or null if the input is invalid.
     */
    public MenuItem selection(int userInput) {
        int index = userInput - 1;
        if (index < 0 || index >= menuItems.size()) {
            System.err.println("Index " + userInput + " is not a valid input.");
            return null;
        }
        return menuItems.get(index);
    }

    /**
     * Selects a menu item based on the given user input (string).
     *
     * @param userInput The string representation of the menu item to select.
     * @return The selected menu item, or null if the input is invalid.
     */
    public MenuItem selection(String userInput) {
        try {
            int index = Integer.parseInt(userInput);
            return selection(index);
        } catch (NumberFormatException e) {
            System.err.println("Invalid input '" + userInput + "'");
            return null;
        }
    }

    /**
     * Prompts the user for input and selects a menu item based on the input.
     *
     * @return The selected menu item.
     */
    public MenuItem selection() {
        System.out.print("Enter your selection: ");
        String input = scanner.nextLine();
        return selection(input);
    }

    /**
     * Executes the menu in a loop, displaying the menu, accepting user input, and executing the selected menu item.
     */
    public void execute() {
        boolean running = true;
        while (running) {
            show(); // Show the menu
            MenuItem selectedItem = null;

            do {
                selectedItem = selection();
            } while (selectedItem == null);

            if (selectedItem.getDescription().toLowerCase().contains("back")) {
                System.out.println("Returning to the previous menu...");
                running = false;
            } else {
                selectedItem.execute();
            }
        }
    }

    /**
     * Adds a menu item to the menu.
     *
     * @param menuItem The menu item to add.
     */
    public void add(MenuItem menuItem) {
        this.menuItems.add(menuItem);
    }
}
