import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap; // Import HashMap
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The main logic class (Controller) for the DineOut application.
 *
 * --- MAX EFFORT UPGRADE ---
 * - Manages a persistent "currentCart" for a real-time ordering experience.
 * - placeOrder() method is updated to use this cart.
 */
public class DineOutSystem {
    private List<Restaurant> restaurants;
    private List<User> users;
    private Customer currentCustomer;
    
    // --- NEW: Real-time cart ---
    private Map<MenuItem, Integer> currentCart;

    public DineOutSystem() {
        this.restaurants = new ArrayList<>();
        this.users = new ArrayList<>();
        this.currentCart = new HashMap<>(); // Initialize the cart
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void addRestaurant(Restaurant restaurant) {
        this.restaurants.add(restaurant);
    }

    public void registerCustomer(Customer customer) {
        this.users.add(customer);
    }
    
    /**
     * Attempts to log in a user.
     * @return true if login is successful, false otherwise.
     */
    public boolean login(String email, String password) {
        for (User user : users) {
            if (user instanceof Customer) {
                Customer customer = (Customer) user;
                // Check email and password
                if(customer.getEmail().equals(email) && customer.getPassword().equals(password)) {
                    this.currentCustomer = customer;
                    System.out.println("Login successful for: " + currentCustomer.getName());
                    return true;
                }
            }
        }
        System.out.println("Login failed. Invalid credentials.");
        return false;
    }
    
    public void logout() {
        System.out.println("Logging out: " + currentCustomer.getName());
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
        if (currentCustomer == null) {
            System.out.println("Error: No user logged in.");
            return null;
        }
        String reservationId = "RES" + System.currentTimeMillis();
        LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute);
        Reservation reservation = new Reservation(reservationId, currentCustomer, restaurant, dateTime, guests);
        
        currentCustomer.addReservation(reservation);
        System.out.println("Reservation created: " + reservationId);
        return reservation;
    }

    // --- +++ NEW CART MANAGEMENT METHODS +++ ---
    
    /**
     * Adds an item to the persistent cart.
     */
    public void addToCart(MenuItem item, int quantity) {
        currentCart.put(item, currentCart.getOrDefault(item, 0) + quantity);
    }
    
    /**
     * Returns the current cart.
     */
    public Map<MenuItem, Integer> getCart() {
        return currentCart;
    }
    
    /**
     * Calculates the total price of all items in the cart.
     */
    public double getCartTotal() {
        double total = 0;
        for (Map.Entry<MenuItem, Integer> entry : currentCart.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }
    
    /**
     * Clears all items from the cart.
     */
    public void clearCart() {
        currentCart.clear();
    }
    
    // --- MODIFIED placeOrder method ---
    /**
     * Creates and places an order for the current customer using the real-time cart.
     * This method NO LONGER takes a Map as an argument.
     */
    public Order placeOrder(Restaurant restaurant) {
         if (currentCustomer == null) {
            System.out.println("Error: No user logged in.");
            return null;
        }
        
        String orderId = "ORD" + System.currentTimeMillis();
        Order order = new Order(orderId, currentCustomer, restaurant);
        
        // Add items from the current cart to the order
        for (Map.Entry<MenuItem, Integer> entry : currentCart.entrySet()) {
            order.addItem(entry.getKey(), entry.getValue());
        }
        
        if(order.getTotalBill() > 0) {
            currentCustomer.addOrder(order);
            System.out.println("Order placed: " + orderId + " for " + currentCustomer.getName());
            return order;
        } else {
            System.out.println("Order cancelled as no items selected.");
            return null;
        }
    }
    
    /**
     * Processes the payment for a given order.
     */
    public void processOrderPayment(Order order, Payment paymentMethod) {
        if (order == null) {
            System.out.println("No order to process.");
            return;
        }
        System.out.println("\nProcessing payment for Order " + order.getOrderId() + " (Total: ₹" + order.getTotalBill() + ")");
        paymentMethod.processPayment(order.getTotalBill());
    }

    /**
     * Initializes the system with sample data.
     * This is static so it can be called from the GUI main method.
     * THIS METHOD IS NOW POPULATED WITH 20 RESTAURANTS FROM SANGRUR.
     */
    public static void initializeData(DineOutSystem app) {
        // Create the user
        Customer c1 = new Customer("C001", "Paras", "parasb736@gmail.com", "paras1234");
        app.registerCustomer(c1);

        // --- Create 20 new restaurants for Sangrur ---

        // 5 Punjabi/North Indian Restaurants
        Restaurant r1 = new Restaurant("R01", "Sangrur Zaika", "North Indian", 4.5, "Patiala Gate, Sangrur", 1200);
        addPunjabiMenu(r1);
        app.addRestaurant(r1);

        Restaurant r2 = new Restaurant("R02", "Royal Haveli", "Punjabi", 4.8, "Sunam Road, Sangrur", 1800);
        addPunjabiMenu(r2);
        app.addRestaurant(r2);
        
        Restaurant r3 = new Restaurant("R03", "Chawla's Chicken", "North Indian", 4.2, "Qila Market, Sangrur", 900);
        addPunjabiMenu(r3);
        app.addRestaurant(r3);

        Restaurant r4 = new Restaurant("R04", "Urban Tadka", "Modern Indian", 4.1, "Dhuri Gate, Sangrur", 1100);
        addPunjabiMenu(r4);
        app.addRestaurant(r4);

        Restaurant r5 = new Restaurant("R05", "Pind Balluchi Sangrur", "Punjabi", 4.4, "Nabha Road, Sangrur", 1500);
        addPunjabiMenu(r5);
        app.addRestaurant(r5);

        // 5 Fast Food/Pizza Restaurants
        Restaurant r6 = new Restaurant("R06", "Pizza Paradise", "Italian, Fast Food", 4.0, "Club Road, Sangrur", 800);
        addFastFoodMenu(r6);
        app.addRestaurant(r6);

        Restaurant r7 = new Restaurant("R07", "Burger Junction", "Fast Food", 3.8, "Sunami Gate, Sangrur", 500);
        addFastFoodMenu(r7);
        app.addRestaurant(r7);

        Restaurant r8 = new Restaurant("R08", "Domino's Pizza", "Pizza, Fast Food", 4.1, "Patiala Gate, Sangrur", 700);
        addFastFoodMenu(r8);
        app.addRestaurant(r8);

        Restaurant r9 = new Restaurant("R09", "Chinese Wok", "Chinese", 3.9, "Qila Market, Sangrur", 600);
        addFastFoodMenu(r9);
        app.addRestaurant(r9);

        Restaurant r10 = new Restaurant("R10", "KFC Sangrur", "Fast Food", 4.3, "Sunam Road, Sangrur", 800);
        addFastFoodMenu(r10);
        app.addRestaurant(r10);

        // 5 Cafes
        Restaurant r11 = new Restaurant("R11", "Cafe Brew", "Cafe", 4.6, "Club Road, Sangrur", 700);
        addCafeMenu(r11);
        app.addRestaurant(r11);

        Restaurant r12 = new Restaurant("R12", "The Coffee Bean", "Cafe", 4.3, "Dhuri Gate, Sangrur", 650);
        addCafeMenu(r12);
        app.addRestaurant(r12);

        Restaurant r13 = new Restaurant("R13", "Baker's Lounge", "Bakery, Cafe", 4.4, "Nabha Road, Sangrur", 500);
        addCafeMenu(r13);
        app.addRestaurant(r13);

        Restaurant r14 = new Restaurant("R14", "Hungry Birds Cafe", "Cafe, Fast Food", 4.0, "Patiala Gate, Sangrur", 750);
        addCafeMenu(r14);
        app.addRestaurant(r14);

        Restaurant r15 = new Restaurant("R15", "Mocha Magic", "Cafe", 4.2, "Sunam Road, Sangrur", 800);
        addCafeMenu(r15);
        app.addRestaurant(r15);

        // 5 Street Food/Dhabas
        Restaurant r16 = new Restaurant("R16", "Sharma Sweet Shop", "Sweets, Street Food", 4.7, "Qila Market, Sangrur", 300);
        addStreetFoodMenu(r16);
        app.addRestaurant(r16);

        Restaurant r17 = new Restaurant("R17", "Giani's Lassi", "Street Food", 4.9, "Sunami Gate, Sangrur", 200);
        addStreetFoodMenu(r17);
        app.addRestaurant(r17);

        Restaurant r18 = new Restaurant("R18", "Billu de Chole Bhature", "Street Food", 4.5, "Patiala Gate, Sangrur", 250);
        addStreetFoodMenu(r18);
        app.addRestaurant(r18);

        Restaurant r19 = new Restaurant("R19", "Raja Dhaba", "Dhaba, North Indian", 4.0, "Nabha Road, Sangrur", 400);
        addStreetFoodMenu(r19); // Use street food menu for dhaba
        app.addRestaurant(r19);

        Restaurant r20 = new Restaurant("R20", "South Indian Corner", "South Indian", 4.1, "Dhuri Gate, Sangrur", 500);
        addStreetFoodMenu(r20); // Use street food/misc menu
        app.addRestaurant(r20);
    }

    // --- Private Helper Methods to build Menus ---

    /**
     * Adds a 20-item standard Punjabi/North Indian menu to a restaurant.
     */
    private static void addPunjabiMenu(Restaurant r) {
        r.addMenuItem(new MenuItem("P01", "Butter Chicken", 450));
        r.addMenuItem(new MenuItem("P02", "Shahi Paneer", 380));
        r.addMenuItem(new MenuItem("P03", "Dal Makhani", 320));
        r.addMenuItem(new MenuItem("P04", "Kadhai Paneer", 360));
        r.addMenuItem(new MenuItem("P05", "Tandoori Chicken", 500));
        r.addMenuItem(new MenuItem("P06", "Paneer Tikka", 340));
        r.addMenuItem(new MenuItem("P07", "Malai Kofta", 370));
        r.addMenuItem(new MenuItem("P08", "Chana Masala", 280));
        r.addMenuItem(new MenuItem("P09", "Rajma Chawal", 250));
        r.addMenuItem(new MenuItem("P10", "Aloo Gobi", 260));
        r.addMenuItem(new MenuItem("P11", "Sarson da Saag", 350));
        r.addMenuItem(new MenuItem("P12", "Makki di Roti", 60));
        r.addMenuItem(new MenuItem("P13", "Butter Naan", 70));
        r.addMenuItem(new MenuItem("P14", "Garlic Naan", 80));
        r.addMenuItem(new MenuItem("P15", "Tandoori Roti", 30));
        r.addMenuItem(new MenuItem("P16", "Jeera Rice", 180));
        r.addMenuItem(new MenuItem("P17", "Veg Biryani", 300));
        r.addMenuItem(new MenuItem("P18", "Sweet Lassi", 100));
        r.addMenuItem(new MenuItem("P19", "Gulab Jamun", 120));
        r.addMenuItem(new MenuItem("P20", "Green Salad", 90));
    }

    /**
     * Adds a 20-item standard Fast Food/Chinese menu to a restaurant.
     */
    private static void addFastFoodMenu(Restaurant r) {
        r.addMenuItem(new MenuItem("F01", "Veggie Burger", 120));
        r.addMenuItem(new MenuItem("F02", "Chicken Zinger Burger", 180));
        r.addMenuItem(new MenuItem("F03", "French Fries", 100));
        r.addMenuItem(new MenuItem("F04", "Margherita Pizza", 250));
        r.addMenuItem(new MenuItem("F05", "Farmhouse Pizza", 350));
        r.addMenuItem(new MenuItem("F06", "Pepperoni Pizza", 450));
        r.addMenuItem(new MenuItem("F07", "Veg Hakka Noodles", 180));
        r.addMenuItem(new MenuItem("F08", "Chilli Paneer Dry", 280));
        r.addMenuItem(new MenuItem("F09", "Veg Manchurian", 260));
        r.addMenuItem(new MenuItem("F10", "Veg Fried Rice", 170));
        r.addMenuItem(new MenuItem("F11", "White Sauce Pasta", 240));
        r.addMenuItem(new MenuItem("F12", "Red Sauce Pasta", 230));
        r.addMenuItem(new MenuItem("F13", "Garlic Breadsticks", 140));
        r.addMenuItem(new MenuItem("F14", "Veg Spring Roll", 160));
        r.addMenuItem(new MenuItem("F15", "Steamed Momos", 100));
        r.addMenuItem(new MenuItem("F16", "Fried Momos", 120));
        r.addMenuItem(new MenuItem("F17", "Chilli Chicken", 320));
        r.addMenuItem(new MenuItem("F18", "Coke", 60));
        r.addMenuItem(new MenuItem("F19", "Veg Sandwich", 90));
        r.addMenuItem(new MenuItem("F20", "Hot & Sour Soup", 110));
    }

    /**
     * Adds a 20-item standard Cafe menu to a restaurant.
     */
    private static void addCafeMenu(Restaurant r) {
        r.addMenuItem(new MenuItem("C01", "Espresso", 100));
        r.addMenuItem(new MenuItem("C02", "Cappuccino", 140));
        r.addMenuItem(new MenuItem("C03", "Cafe Latte", 150));
        r.addMenuItem(new MenuItem("C04", "Hot Chocolate", 180));
        r.addMenuItem(new MenuItem("C05", "Cold Coffee", 160));
        r.addMenuItem(new MenuItem("C06", "Iced Lemon Tea", 130));
        r.addMenuItem(new MenuItem("C07", "Masala Chai", 80));
        r.addMenuItem(new MenuItem("C08", "Green Tea", 90));
        r.addMenuItem(new MenuItem("C09", "Veg Club Sandwich", 180));
        r.addMenuItem(new MenuItem("C10", "Paneer Tikka Sandwich", 220));
        r.addMenuItem(new MenuItem("C11", "Chicken Junglee Sandwich", 250));
        r.addMenuItem(new MenuItem("C12", "Veg Wrap", 170));
        r.addMenuItem(new MenuItem("C13", "Veg Puff", 60));
        r.addMenuItem(new MenuItem("C14", "Chocolate Brownie", 140));
        r.addMenuItem(new MenuItem("C15", "Red Velvet Pastry", 160));
        r.addMenuItem(new MenuItem("C16", "Blueberry Muffin", 110));
        r.addMenuItem(new MenuItem("C17", "Garlic Bread", 130));
        r.addMenuItem(new MenuItem("C18", "Omelette", 120));
        r.addMenuItem(new MenuItem("C19", "Pancakes", 200));
        r.addMenuItem(new MenuItem("C20", "Waffles", 220));
    }

    /**
     * Adds a 20-item standard Street Food/Misc menu to a restaurant.
     */
    private static void addStreetFoodMenu(Restaurant r) {
        r.addMenuItem(new MenuItem("S01", "Chole Bhature", 120));
        r.addMenuItem(new MenuItem("S02", "Pav Bhaji", 130));
        r.addMenuItem(new MenuItem("S03", "Amritsari Kulcha", 150));
        r.addMenuItem(new MenuItem("S04", "Samosa (2 pcs)", 50));
        r.addMenuItem(new MenuItem("S05", "Aloo Tikki Chaat", 80));
        r.addMenuItem(new MenuItem("S06", "Dahi Bhalla", 90));
        r.addMenuItem(new MenuItem("S07", "Golgappe (Pani Puri)", 60));
        r.addMenuItem(new MenuItem("S08", "Papdi Chaat", 80));
        r.addMenuItem(new MenuItem("S09", "Kachori with Aloo Sabzi", 70));
        r.addMenuItem(new MenuItem("S10", "Jalebi (100g)", 80));
        r.addMenuItem(new MenuItem("S11", "Masala Dosa", 140));
        r.addMenuItem(new MenuItem("S12", "Idli Sambhar", 100));
        r.addMenuItem(new MenuItem("S13", "Vada Sambhar", 110));
        r.addMenuItem(new MenuItem("S14", "Onion Uttapam", 150));
        r.addMenuItem(new MenuItem("S15", "Aloo Paratha", 90));
        r.addMenuItem(new MenuItem("S16", "Paneer Paratha", 120));
        r.addMenuItem(new MenuItem("S17", "Tandoori Aloo Paratha", 130));
        r.addMenuItem(new MenuItem("S18", "Plain Lassi", 80));
        r.addMenuItem(new MenuItem("S19", "Badam Milk", 100));
        r.addMenuItem(new MenuItem("S20", "Kulfi Falooda", 130));
    }
}

