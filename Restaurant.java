import java.util.ArrayList;
import java.util.List;

/**
 * Represents a restaurant with its details and menu.
 */
public class Restaurant {
    private String restaurantId;
    private String name;
    private String cuisineType;
    private double rating;
    private String location;
    private double averageCostForTwo;
    private List<MenuItem> menu;

    public Restaurant(String restaurantId, String name, String cuisineType, double rating, String location, double averageCostForTwo) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.cuisineType = cuisineType;
        this.rating = rating;
        this.location = location;
        this.averageCostForTwo = averageCostForTwo;
        this.menu = new ArrayList<>();
    }

    // --- Getters ---
    public String getName() {
        return name;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public double getRating() {
        return rating;
    }

    public String getLocation() {
        return location;
    }

    public List<MenuItem> getMenu() {
        return new ArrayList<>(menu); // Return a copy to maintain encapsulation
    }
    
    // --- Methods ---
    public void addMenuItem(MenuItem item) {
        this.menu.add(item);
    }

    public String getDetailsAsString() {
        return String.format("Restaurant: %s\nCuisine: %s | Rating: %.1f/5.0\nLocation: %s\nAverage Cost for Two: ₹%.1f",
                name, cuisineType, rating, location, averageCostForTwo);
    }

    public String getMenuAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Menu for ").append(name).append(" ---\n");
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.get(i);
            sb.append(String.format("%d. %s - ₹%.1f\n", (i + 1), item.getName(), item.getPrice()));
        }
        sb.append("------------------------");
        return sb.toString();
    }
    
    @Override
    public String toString() {
        // Used by JList for displaying the name
        return name;
    }
}

