package iota.com.menu;

/**
 * Represents a menu item with a description and an action to be executed.
 */
public class MenuItem {
    private String description;
    private Runnable action;

    /**
     * Constructs a new MenuItem with the given description and action.
     *
     * @param description The description of the menu item. Cannot be null or empty.
     * @param action      The action to be executed when the menu item is selected. Cannot be null.
     * @throws NullPointerException     If either description or action is null.
     * @throws IllegalArgumentException If description is empty.
     */
    public MenuItem(String description, Runnable action) {
        if (action == null || description == null) {
            throw new NullPointerException("Action and Description cannot be null!");
        }
        if (description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty!");
        }
        this.description = description;
        this.action = action;
    }

    /**
     * Returns the description of the menu item.
     *
     * @return The description of the menu item.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Executes the action associated with the menu item.
     */
    public void execute() {
        action.run();
    }
}

