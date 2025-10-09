import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer, extending the base User class.
 * Manages booking and order history specific to the customer.
 */
public class Customer extends User {
    private List<Reservation> bookingHistory;
    private List<Order> orderHistory;

    public Customer(String userId, String name, String email, String password) {
        super(userId, name, email, password);
        this.bookingHistory = new ArrayList<>();
        this.orderHistory = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        this.bookingHistory.add(reservation);
    }

    public void addOrder(Order order) {
        this.orderHistory.add(order);
    }

    public String getBookingHistoryAsString() {
        if (bookingHistory.isEmpty()) {
            return "--- No Booking History ---";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("--- Booking History for ").append(getName()).append(" ---\n");
        for (Reservation reservation : bookingHistory) {
            sb.append(reservation.getDetailsAsString()).append("\n--------------------\n");
        }
        return sb.toString();
    }

    public String getOrderHistoryAsString() {
        if (orderHistory.isEmpty()) {
            return "--- No Order History ---";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("--- Order History for ").append(getName()).append(" ---\n");
        for (Order order : orderHistory) {
            sb.append(order.getDetailsAsString()).append("\n");
        }
        return sb.toString();
    }
}

