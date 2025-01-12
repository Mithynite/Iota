package iota.com.menu;

public class MenuItem {
    private String description;
    private Runnable action;

    public MenuItem(String description, Runnable action) {
        if(action == null || description == null) {
            throw new NullPointerException("Action and Description cannot be null!");
        }
        if(description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty!");
        }
        this.description = description;
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void execute() {
        action.run();
    }
}

