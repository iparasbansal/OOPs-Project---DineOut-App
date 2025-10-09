import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a table reservation made by a customer.
 */
public class Reservation {
    private String reservationId;
    private Customer customer;
    private Restaurant restaurant;
    private LocalDateTime dateTime;
    private int numberOfGuests;

    public Reservation(String reservationId, Customer customer, Restaurant restaurant, LocalDateTime dateTime, int numberOfGuests) {
        this.reservationId = reservationId;
        this.customer = customer;
        this.restaurant = restaurant;
        this.dateTime = dateTime;
        this.numberOfGuests = numberOfGuests;
    }

    public String getDetailsAsString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("Reservation ID: %s\nRestaurant: %s\nCustomer: %s\nDate & Time: %s\nGuests: %d",
                reservationId, restaurant.getName(), customer.getName(), dateTime.format(formatter), numberOfGuests);
    }
}

