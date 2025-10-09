import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The main logic class that simulates the DineOut application.
 * It manages restaurants, users, and the overall workflow.
 */
public class DineOutSystem {
    private List<Restaurant> restaurants;
    private List<User> users;
    private Customer currentCustomer; // Simulating a logged-in user

    public DineOutSystem() {
        this.restaurants = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public void addRestaurant(Restaurant restaurant) {
        this.restaurants.add(restaurant);
    }

    public void registerCustomer(Customer customer) {
        this.users.add(customer);
    }
    
    public Customer getCurrentCustomer() {
        return currentCustomer;
    }
    
    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void login(String email, String password) {
        // Simple login simulation
        for(User user : users) {
            if(user instanceof Customer && user.getEmail().equals(email)) {
                this.currentCustomer = (Customer) user;
                return;
            }
        }
    }
    
    public void logout() {
        this.currentCustomer = null;
    }

    public List<Restaurant> searchRestaurants(String query) {
        String lowerCaseQuery = query.toLowerCase();
        return restaurants.stream()
                .filter(r -> r.getName().toLowerCase().contains(lowerCaseQuery) ||
                             r.getCuisineType().toLowerCase().contains(lowerCaseQuery) ||
                             r.getLocation().toLowerCase().contains(lowerCaseQuery))
                .collect(Collectors.toList());
    }

    public Reservation makeReservation(Restaurant restaurant, int year, int month, int day, int hour, int minute, int guests) {
        if (currentCustomer == null) return null;
        
        String reservationId = "RES" + System.currentTimeMillis();
        LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute);
        Reservation reservation = new Reservation(reservationId, currentCustomer, restaurant, dateTime, guests);
        currentCustomer.addReservation(reservation);
        return reservation;
    }

    public Order placeOrder(Restaurant restaurant, Map<MenuItem, Integer> items) {
         if (currentCustomer == null) return null;
        
        String orderId = "ORD" + System.currentTimeMillis();
        Order order = new Order(orderId, currentCustomer, restaurant);
        
        for (Map.Entry<MenuItem, Integer> entry : items.entrySet()) {
            order.addItem(entry.getKey(), entry.getValue());
        }
        
        if(order.getTotalBill() > 0) {
            currentCustomer.addOrder(order);
            return order;
        } else {
            return null;
        }
    }
    
    public void processOrderPayment(Order order, Payment paymentMethod) {
        if (order == null) {
            System.out.println("No order to process.");
            return;
        }
        // In a real GUI app, this would update a status label, not print to console.
        System.out.println("\nProcessing payment for a total of ₹" + order.getTotalBill());
        paymentMethod.processPayment(order.getTotalBill());
    }
    
    public static void initializeData(DineOutSystem app) {
        // Create customers
        Customer c1 = new Customer("C001", "Paras Bansal", "parasb736@gmail.com", "paras1234");
        app.registerCustomer(c1);

        // Create restaurants and menus
        Restaurant r1 = new Restaurant("R01", "The Italian Corner", "Italian", 4.5, "Koramangala, Bangalore", 1500);
        r1.addMenuItem(new MenuItem("M01", "Margherita Pizza", 450));
        r1.addMenuItem(new MenuItem("M02", "Pasta Alfredo", 380));
        r1.addMenuItem(new MenuItem("M03", "Garlic Bread", 220));

        Restaurant r2 = new Restaurant("R02", "Punjabi Dhaba", "North Indian", 4.2, "Indiranagar, Bangalore", 1200);
        r2.addMenuItem(new MenuItem("M04", "Butter Chicken", 420));
        r2.addMenuItem(new MenuItem("M05", "Dal Makhani", 350));
        r2.addMenuItem(new MenuItem("M06", "Naan", 60));

        Restaurant r3 = new Restaurant("R03", "Sushi Central", "Japanese", 4.8, "MG Road, Bangalore", 2500);
        r3.addMenuItem(new MenuItem("M07", "California Roll", 600));
        r3.addMenuItem(new MenuItem("M08", "Miso Soup", 250));
        
        app.addRestaurant(r1);
        app.addRestaurant(r2);
        app.addRestaurant(r3);
    }
}

