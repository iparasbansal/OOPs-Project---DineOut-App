import java.util.HashMap;
import java.util.Map;

/**
 * Represents a food order placed by a customer.
 */
public class Order {
    private String orderId;
    private Customer customer;
    private Restaurant restaurant;
    private Map<MenuItem, Integer> items; // MenuItem and its quantity
    private double totalBill;

    public Order(String orderId, Customer customer, Restaurant restaurant) {
        this.orderId = orderId;
        this.customer = customer;
        this.restaurant = restaurant;
        this.items = new HashMap<>();
        this.totalBill = 0.0;
    }

    public void addItem(MenuItem item, int quantity) {
        this.items.put(item, this.items.getOrDefault(item, 0) + quantity);
        calculateTotalBill();
    }

    private void calculateTotalBill() {
        this.totalBill = 0;
        for (Map.Entry<MenuItem, Integer> entry : items.entrySet()) {
            this.totalBill += entry.getKey().getPrice() * entry.getValue();
        }
    }

    public double getTotalBill() {
        return totalBill;
    }

    public String getDetailsAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Order Details ---\n");
        sb.append("Order ID: ").append(orderId).append("\n");
        sb.append("Restaurant: ").append(restaurant.getName()).append("\n");
        sb.append("Customer: ").append(customer.getName()).append("\n");
        sb.append("Items:\n");
        for (Map.Entry<MenuItem, Integer> entry : items.entrySet()) {
            sb.append(String.format("  - %s (x%d) - ₹%.1f\n",
                    entry.getKey().getName(), entry.getValue(), entry.getKey().getPrice() * entry.getValue()));
        }
        sb.append("--------------------\n");
        sb.append(String.format("Total Bill: ₹%.2f\n", totalBill));
        return sb.toString();
    }
}

