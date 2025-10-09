/**
 * Represents a single item on a restaurant's menu.
 * Encapsulates item details.
 */
public class MenuItem {
    private String itemId;
    private String name;
    private double price;

    public MenuItem(String itemId, String name, double price) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
    }

    // Getters
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void display() {
        System.out.println(name + " - ₹" + price);
    }
}
