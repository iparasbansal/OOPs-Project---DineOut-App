import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Restaurant, containing its details and a menu.
 * Demonstrates Encapsulation and Composition.
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

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public double getRating() {
        return rating;
    }

    /**
     * Added the missing getter method for averageCostForTwo.
     * This fixes the compilation error in DineOutGUI.java.
     */
    public double getAverageCostForTwo() {
        return averageCostForTwo;
    }
    
    public String getLocation() {
        return location;
    }

    /**
     * Returns a copy of the menu to maintain encapsulation.
     */
    public List<MenuItem> getMenu() {
        return new ArrayList<>(menu); 
    }

    // --- Public Methods ---

    public void addMenuItem(MenuItem item) {
        this.menu.add(item);
    }

    /**
     * Returns a simple string representation of the restaurant.
     * Used by the JList component.
     */
    @Override
    public String toString() {
        return name + " (" + cuisineType + ")";
    }

    /**
     * Generates a formatted string of the restaurant's details.
     * Required by the GUI.
     * @return String representation of restaurant details.
     */
    public String getDetailsAsString() {
        return "Restaurant: " + name + "\n" +
               "Cuisine: " + cuisineType + " | Rating: " + rating + "/5.0\n" +
               "Location: " + location + "\n" +
               "Avg. Cost for Two: ₹" + averageCostForTwo;
    }

    /**
     * Generates a formatted string of the restaurant's menu.
     * Required by the GUI.
     * @return String representation of the menu.
     */
    public String getMenuAsString() {
        if (menu.isEmpty()) {
            return "Menu is not available for " + name;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("--- Menu for " + name + " ---\n");
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.get(i);
            sb.append(String.format("%d. %-20s - ₹%.2f\n", (i + 1), item.getName(), item.getPrice()));
        }
        sb.append("------------------------\n");
        return sb.toString();
    }
}

