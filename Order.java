import java.util.HashMap;
import java.util.Map;

/**
 * Represents a food order from a restaurant.
 * Contains a map of MenuItems and their quantities.
 */
public class Order {
    private String orderId;
    private Customer customer;
    private Restaurant restaurant;
    private Map<MenuItem, Integer> items; // Key: MenuItem, Value: Quantity
    private double totalBill;

    public Order(String orderId, Customer customer, Restaurant restaurant) {
        this.orderId = orderId;
        this.customer = customer;
        this.restaurant = restaurant;
        this.items = new HashMap<>();
        this.totalBill = 0.0;
    }

    // --- Getters ---

    /**
     * Added the missing getter for orderId.
     * This fixes the compilation error in DineOutSystem.java.
     */
    public String getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Map<MenuItem, Integer> getItems() {
        return items;
    }

    public double getTotalBill() {
        return totalBill;
    }

    // --- Public Methods ---

    /**
     * Adds an item to the order and updates the total bill.
     */
    public void addItem(MenuItem item, int quantity) {
        items.put(item, items.getOrDefault(item, 0) + quantity);
        calculateTotalBill(); // Recalculate bill
    }

    /**
     * Recalculates the total bill based on the items in the map.
     */
    private void calculateTotalBill() {
        this.totalBill = 0.0;
        for (Map.Entry<MenuItem, Integer> entry : items.entrySet()) {
            this.totalBill += entry.getKey().getPrice() * entry.getValue();
        }
    }

    /**
     * Generates a formatted string of the order details.
     * Required by the GUI.
     * @return String representation of the order.
     */
    public String getDetailsAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: " + orderId + "\n");
        sb.append("Restaurant: " + restaurant.getName() + "\n");
        sb.append("Items:\n");
        for (Map.Entry<MenuItem, Integer> entry : items.entrySet()) {
            MenuItem item = entry.getKey();
            int quantity = entry.getValue();
            sb.append(String.format("  - %-20s (Qty: %d) - ₹%.2f\n", 
                item.getName(), quantity, (item.getPrice() * quantity)));
        }
        sb.append(String.format("Total Bill: ₹%.2f\n", totalBill));
        return sb.toString();
    }
}

