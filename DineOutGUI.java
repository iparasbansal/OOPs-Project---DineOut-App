import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The main GUI class for the DineOut application.
 * It uses Swing to create the user interface and a CardLayout to manage different screens.
 */
public class DineOutGUI extends JFrame {
    private DineOutSystem dineOutSystem;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextArea historyTextArea;
    private JLabel welcomeLabel;
    private JList<Restaurant> restaurantJList;
    private DefaultListModel<Restaurant> restaurantListModel;

    public DineOutGUI() {
        dineOutSystem = new DineOutSystem();
        // The initializeData method should be part of DineOutSystem to keep logic separate
        DineOutSystem.initializeData(dineOutSystem); 

        setTitle("DineOut Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create different panels (screens)
        JPanel loginPanel = createLoginPanel();
        JPanel dashboardPanel = createDashboardPanel();
        JPanel restaurantsPanel = createRestaurantsPanel();
        JPanel historyPanel = createHistoryPanel();

        mainPanel.add(loginPanel, "Login");
        mainPanel.add(dashboardPanel, "Dashboard");
        mainPanel.add(restaurantsPanel, "Restaurants");
        mainPanel.add(historyPanel, "History");

        add(mainPanel);
        cardLayout.show(mainPanel, "Login"); // Start with the login screen
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField("parasb736@gmail.com", 20); // Pre-filled for convenience
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField("paras1234", 20); // Pre-filled
        JButton loginButton = new JButton("Login");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(emailLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(emailField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(passwordLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(passwordField, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.CENTER; panel.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            dineOutSystem.login(email, password);
            if (dineOutSystem.getCurrentCustomer() != null) {
                welcomeLabel.setText("Welcome, " + dineOutSystem.getCurrentCustomer().getName() + "!");
                cardLayout.show(mainPanel, "Dashboard");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        return panel;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        welcomeLabel = new JLabel("", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        JButton browseButton = new JButton("Browse & Select a Restaurant");
        JButton searchButton = new JButton("Search for a Restaurant");
        JButton historyButton = new JButton("View My History");
        JButton logoutButton = new JButton("Logout");

        buttonPanel.add(browseButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(historyButton);
        buttonPanel.add(logoutButton);
        
        panel.add(welcomeLabel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);

        browseButton.addActionListener(e -> {
            restaurantListModel.clear();
            dineOutSystem.getRestaurants().forEach(restaurantListModel::addElement);
            cardLayout.show(mainPanel, "Restaurants");
        });

        searchButton.addActionListener(e -> {
            String query = JOptionPane.showInputDialog(this, "Enter search term (name, cuisine, location):");
            if (query != null && !query.trim().isEmpty()) {
                List<Restaurant> results = dineOutSystem.searchRestaurants(query);
                if (results.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No restaurants found.");
                } else {
                    restaurantListModel.clear();
                    results.forEach(restaurantListModel::addElement);
                    cardLayout.show(mainPanel, "Restaurants");
                }
            }
        });

        historyButton.addActionListener(e -> {
            historyTextArea.setText(dineOutSystem.getCurrentCustomer().getBookingHistoryAsString());
            historyTextArea.append("\n\n" + dineOutSystem.getCurrentCustomer().getOrderHistoryAsString());
            cardLayout.show(mainPanel, "History");
        });

        logoutButton.addActionListener(e -> {
            dineOutSystem.logout();
            cardLayout.show(mainPanel, "Login");
        });

        return panel;
    }

    private JPanel createRestaurantsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel title = new JLabel("Select a Restaurant", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        restaurantListModel = new DefaultListModel<>();
        restaurantJList = new JList<>(restaurantListModel);
        restaurantJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        restaurantJList.setCellRenderer(new RestaurantListCellRenderer());

        JScrollPane scrollPane = new JScrollPane(restaurantJList);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton selectButton = new JButton("Interact with Selected Restaurant");
        JButton backButton = new JButton("Back to Dashboard");
        buttonPanel.add(selectButton);
        buttonPanel.add(backButton);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        selectButton.addActionListener(this::handleRestaurantInteraction);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));

        return panel;
    }
    
    private void handleRestaurantInteraction(ActionEvent e) {
        Restaurant selectedRestaurant = restaurantJList.getSelectedValue();
        if (selectedRestaurant == null) {
            JOptionPane.showMessageDialog(this, "Please select a restaurant first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] options = {"View Menu", "Make a Reservation", "Place an Order", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this, "What would you like to do at " + selectedRestaurant.getName() + "?",
                "Restaurant Actions", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0: // View Menu
                JOptionPane.showMessageDialog(this, selectedRestaurant.getMenuAsString(), "Menu: " + selectedRestaurant.getName(), JOptionPane.PLAIN_MESSAGE);
                break;
            case 1: // Make Reservation
                handleMakeReservation(selectedRestaurant);
                break;
            case 2: // Place Order
                handlePlaceOrder(selectedRestaurant);
                break;
            default: // Cancel or close
                break;
        }
    }
    
    private void handleMakeReservation(Restaurant restaurant) {
        String guestsStr = JOptionPane.showInputDialog(this, "Enter number of guests:");
        try {
            int guests = Integer.parseInt(guestsStr);
            // In a real app, you'd use a date picker. This is simplified.
            String dateTimeStr = JOptionPane.showInputDialog(this, "Enter date and time (YYYY-MM-DD HH:MM):");
            String[] parts = dateTimeStr.replace("-", " ").replace(":", " ").split(" ");
            dineOutSystem.makeReservation(restaurant, 
                Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]),
                Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), guests);
            JOptionPane.showMessageDialog(this, "Reservation successful!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handlePlaceOrder(Restaurant restaurant) {
        // This is a complex interaction, simplified here.
        Map<MenuItem, Integer> orderItems = new HashMap<>();
        while(true) {
            String menu = restaurant.getMenuAsString() + "\n\nEnter item number to add (or 'done' to finish):";
            String itemNumStr = JOptionPane.showInputDialog(this, menu);
            if(itemNumStr == null || itemNumStr.equalsIgnoreCase("done")) break;

            try {
                int itemIndex = Integer.parseInt(itemNumStr) - 1;
                List<MenuItem> menuItems = restaurant.getMenu();
                if(itemIndex >= 0 && itemIndex < menuItems.size()) {
                    MenuItem selected = menuItems.get(itemIndex);
                    String qtyStr = JOptionPane.showInputDialog(this, "Enter quantity for " + selected.getName() + ":");
                    int quantity = Integer.parseInt(qtyStr);
                    if(quantity > 0) {
                        orderItems.put(selected, orderItems.getOrDefault(selected, 0) + quantity);
                    }
                } else {
                     JOptionPane.showMessageDialog(this, "Invalid item number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch(NumberFormatException e) {
                 JOptionPane.showMessageDialog(this, "Invalid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        if(!orderItems.isEmpty()) {
            Order order = dineOutSystem.placeOrder(restaurant, orderItems);
            JOptionPane.showMessageDialog(this, order.getDetailsAsString(), "Order Placed", JOptionPane.INFORMATION_MESSAGE);
            // Simplified payment
            Payment card = new CardPayment("1234-5678-9012-3456", dineOutSystem.getCurrentCustomer().getName());
            dineOutSystem.processOrderPayment(order, card);
            JOptionPane.showMessageDialog(this, "Payment of ₹" + order.getTotalBill() + " processed successfully via Card.");
        }
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Your Booking and Order History", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        
        historyTextArea = new JTextArea();
        historyTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(historyTextArea);
        
        JButton backButton = new JButton("Back to Dashboard");
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));
        return panel;
    }
    
    // Custom cell renderer to display restaurant details nicely in a JList
    class RestaurantListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Restaurant) {
                Restaurant restaurant = (Restaurant) value;
                setText("<html><b>" + restaurant.getName() + "</b><br>" 
                      + restaurant.getCuisineType() + " - " + restaurant.getLocation() 
                      + "<br>Rating: " + restaurant.getRating() + "/5.0</html>");
                setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            }
            return this;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DineOutGUI app = new DineOutGUI();
            app.setVisible(true);
        });
    }
}

