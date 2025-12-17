import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer, extending the base User class.
 * It adds a password and history lists for reservations and orders.
 * Demonstrates Inheritance.
 */
public class Customer extends User {
    
    private String password; // Added for login
    private List<Reservation> reservationHistory;
    private List<Order> orderHistory;

    public Customer(String userId, String name, String email, String password) {
        super(userId, name, email);
        this.password = password;
        this.reservationHistory = new ArrayList<>();
        this.orderHistory = new ArrayList<>();
    }

    // --- Getters ---

    /**
     * Added the missing getter for password.
     * This fixes the compilation error in DineOutSystem.java.
     */
    public String getPassword() {
        return password;
    }

    public List<Reservation> getReservationHistory() {
        return reservationHistory;
    }

    public List<Order> getOrderHistory() {
        return orderHistory;
    }

    // --- Public Methods ---

    public void addReservation(Reservation reservation) {
        this.reservationHistory.add(reservation);
    }

    public void addOrder(Order order) {
        this.orderHistory.add(order);
    }

    /**
     * Generates a formatted string of the booking history.
     * Required by the GUI.
     * @return String representation of booking history.
     */
    public String getBookingHistoryAsString() {
        if (reservationHistory.isEmpty()) {
            return "--- No Booking History ---";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("--- Your Booking History ---\n");
        sb.append("============================\n");
        for (Reservation reservation : reservationHistory) {
            sb.append(reservation.getDetailsAsString());
            sb.append("----------------------------\n");
        }
        return sb.toString();
    }

    /**
     * Generates a formatted string of the order history.
     * Required by the GUI.
     * @return String representation of order history.
     */
    public String getOrderHistoryAsString() {
        if (orderHistory.isEmpty()) {
            return "--- No Order History ---";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("--- Your Order History ---\n");
        sb.append("==========================\n");
        for (Order order : orderHistory) {
            sb.append(order.getDetailsAsString());
            sb.append("--------------------------\n");
        }
        return sb.toString();
    }
}

