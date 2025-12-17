import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The main GUI class for the DineOut application.
 *
 * --- MAX EFFORT UPGRADE ---
 * This version replaces the JOptionPane-based ordering system with a 
 * dedicated, persistent "Menu & Cart" panel.
 * - Adds a new "MenuPanel" to the CardLayout.
 * - Uses a JSplitPane for a side-by-side menu and cart view.
 * - The cart updates in real-time as items are added.
 */
public class DineOutGUI extends JFrame {

    // --- CUSTOM ZOMATO-INSPIRED LIGHT THEME ---
    private static final Color COLOR_LIGHT_PRIMARY_RED = new Color(203, 32, 45); // Zomato Red
    private static final Color COLOR_LIGHT_BACKGROUND = new Color(245, 245, 245);
    private static final Color COLOR_LIGHT_PANEL = Color.WHITE;
    private static final Color COLOR_LIGHT_TEXT_DARK = new Color(51, 51, 51);
    private static final Color COLOR_LIGHT_TEXT_LIGHT = new Color(102, 102, 102);
    private static final Color COLOR_LIGHT_SELECTION = new Color(255, 235, 235);
    private static final Color COLOR_LIGHT_BORDER = Color.LIGHT_GRAY;

    // --- CUSTOM ZOMATO-INSPIRED DARK THEME ---
    private static final Color COLOR_DARK_PRIMARY_RED = new Color(239, 79, 95); // Lighter Zomato Red
    private static final Color COLOR_DARK_BACKGROUND = new Color(40, 40, 40);
    private static final Color COLOR_DARK_PANEL = new Color(51, 51, 51);
    private static final Color COLOR_DARK_TEXT_DARK = new Color(230, 230, 230); // Light text
    private static final Color COLOR_DARK_TEXT_LIGHT = new Color(180, 180, 180); // Brighter gray for "Paras's"
    private static final Color COLOR_DARK_SELECTION = new Color(100, 20, 30);
    private static final Color COLOR_DARK_BORDER = new Color(80, 80, 80);

    // --- Theme-Aware Color Variables ---
    private Color colorPrimary;
    private Color colorBackground;
    private Color colorPanel;
    private Color colorTextDark;
    private Color colorTextLight;
    private Color colorSelection;
    private Color colorBorder;

    // --- Fonts ---
    private static final Font FONT_PRIMARY_BOLD = new Font("Arial", Font.BOLD, 16);
    private static final Font FONT_PRIMARY_PLAIN = new Font("Arial", Font.PLAIN, 14);
    private static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 24);

    // --- Component Instance Variables ---
    private boolean isDarkMode = false;
    private DineOutSystem dineOutSystem;
    private CardLayout cardLayout;
    
    // --- NEW: Added menuPanel and its components ---
    private JPanel mainPanel, loginPanel, loginOuterPanel, dashboardPanel, restaurantsPanel, historyPanel, menuPanel;
    private JPanel dashboardTopPanel, dashboardCenterPanel, dashboardButtonPanel, dashboardThemePanel;
    private JPanel restaurantTopPanel, restaurantButtonPanel;
    private JPanel historyTopPanel, historyButtonPanel;
    private JPanel menuTopPanel, menuListPanel, menuActionPanel, menuCartPanel, menuCartTotalPanel, menuCartButtonPanel; // Added more panel vars for theming

    // Logo labels
    private JLabel logoLabelLogin, logoLabelDashboard, logoLabelRestaurant, logoLabelHistory, logoLabelMenu;
    
    // --- NEW: Components for the Menu/Cart Panel ---
    private JList<MenuItem> menuJList;
    private DefaultListModel<MenuItem> menuListModel;
    private JTextArea cartTextArea;
    private JLabel cartTotalLabel;
    private JButton menuAddToCartButton, menuPlaceOrderButton, menuBackButton;
    private JSpinner menuQtySpinner;
    private JLabel menuRestaurantNameLabel;
    private Restaurant currentSelectedRestaurant; // Track which restaurant we're ordering from
    private JSplitPane menuSplitPane;

    private JTextArea historyTextArea;
    private JLabel welcomeLabel, loginTitleLabel, restaurantTitleLabel, historyTitleLabel;
    private JList<Restaurant> restaurantJList;
    private DefaultListModel<Restaurant> restaurantListModel;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton, browseButton, searchButton, historyButton, logoutButton, restaurantBackButton, historyBackButton;
    private JToggleButton themeToggleButton;

    public DineOutGUI() {
        dineOutSystem = new DineOutSystem();
        DineOutSystem.initializeData(dineOutSystem);

        setTitle("Paras's DineOut Application");
        setSize(900, 700); // Made window slightly larger for the cart
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create different panels (screens)
        loginPanel = createLoginPanel();
        dashboardPanel = createDashboardPanel();
        restaurantsPanel = createRestaurantsPanel();
        historyPanel = createHistoryPanel();
        menuPanel = createMenuPanel(); // --- NEW ---

        mainPanel.add(loginPanel, "Login");
        mainPanel.add(dashboardPanel, "Dashboard");
        mainPanel.add(restaurantsPanel, "Restaurants");
        mainPanel.add(historyPanel, "History");
        mainPanel.add(menuPanel, "MenuPanel"); // --- NEW ---

        add(mainPanel);
        
        updateTheme(false); // Start in Light Mode
        cardLayout.show(mainPanel, "Login");
    }

    /**
     * Creates a reusable, styled JLabel to act as a text-based logo.
     */
    private JLabel createLogoLabel() {
        // Text will be set by updateTheme()
        return new JLabel("", JLabel.CENTER); 
    }

    private JPanel createLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        loginTitleLabel = new JLabel("Application Login");
        loginTitleLabel.setFont(FONT_TITLE);
        loginTitleLabel.setHorizontalAlignment(JLabel.CENTER);

        logoLabelLogin = createLogoLabel(); // Use instance variable
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; loginPanel.add(logoLabelLogin, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; loginPanel.add(loginTitleLabel, gbc);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(FONT_PRIMARY_PLAIN);
        emailField = new JTextField("parasb736@gmail.com", 25);
        emailField.setFont(FONT_PRIMARY_PLAIN);
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(FONT_PRIMARY_PLAIN);
        passwordField = new JPasswordField("paras1234", 25);
        passwordField.setFont(FONT_PRIMARY_PLAIN);

        loginButton = new JButton("Login");
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; loginPanel.add(emailLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST; loginPanel.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST; loginPanel.add(passwordLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.WEST; loginPanel.add(passwordField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE; loginPanel.add(loginButton, gbc);

        loginButton.addActionListener(this::performLogin);
        passwordField.addActionListener(this::performLogin);

        loginOuterPanel = new JPanel(new GridBagLayout());
        loginOuterPanel.add(loginPanel);
        return loginOuterPanel;
    }

    private void performLogin(ActionEvent e) {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        
        boolean loginSuccess = dineOutSystem.login(email, password);
        
        if (loginSuccess) {
            welcomeLabel.setText("Welcome, " + dineOutSystem.getCurrentCustomer().getName() + "!");
            cardLayout.show(mainPanel, "Dashboard");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createDashboardPanel() {
        dashboardPanel = new JPanel(new BorderLayout(20, 20));
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        welcomeLabel = new JLabel("", JLabel.CENTER);
        welcomeLabel.setFont(FONT_TITLE);
        
        dashboardTopPanel = new JPanel(new BorderLayout());
        logoLabelDashboard = createLogoLabel(); // Use instance variable
        dashboardTopPanel.add(logoLabelDashboard, BorderLayout.NORTH);
        dashboardTopPanel.add(welcomeLabel, BorderLayout.CENTER);

        dashboardButtonPanel = new JPanel(new GridLayout(4, 1, 15, 15));
        browseButton = new JButton("Browse & Select a Restaurant");
        searchButton = new JButton("Search for a Restaurant");
        historyButton = new JButton("View My History");
        logoutButton = new JButton("Logout");

        dashboardButtonPanel.add(browseButton);
        dashboardButtonPanel.add(searchButton);
        dashboardButtonPanel.add(historyButton);
        dashboardButtonPanel.add(logoutButton);
        
        dashboardCenterPanel = new JPanel(new GridBagLayout());
        dashboardCenterPanel.add(dashboardButtonPanel);

        // --- Theme Toggle Button ---
        dashboardThemePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        themeToggleButton = new JToggleButton("Switch to Dark Mode");
        themeToggleButton.addActionListener(e -> {
            updateTheme(themeToggleButton.isSelected()); 
        });
        dashboardThemePanel.add(themeToggleButton);

        dashboardPanel.add(dashboardTopPanel, BorderLayout.NORTH);
        dashboardPanel.add(dashboardCenterPanel, BorderLayout.CENTER);
        dashboardPanel.add(dashboardThemePanel, BorderLayout.SOUTH);

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
                    JOptionPane.showMessageDialog(this, "No restaurants found matching your search.");
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
            historyTextArea.setCaretPosition(0);
            cardLayout.show(mainPanel, "History");
        });

        logoutButton.addActionListener(e -> {
            dineOutSystem.logout();
            emailField.setText("parasb736@gmail.com");
            passwordField.setText("paras1234");
            cardLayout.show(mainPanel, "Login");
        });

        return dashboardPanel;
    }

    private JPanel createRestaurantsPanel() {
        restaurantsPanel = new JPanel(new BorderLayout(10, 10));
        restaurantsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        restaurantTitleLabel = new JLabel("Select a Restaurant (Double-Click)", JLabel.CENTER);
        restaurantTitleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        restaurantTopPanel = new JPanel(new BorderLayout());
        logoLabelRestaurant = createLogoLabel(); // Use instance variable
        restaurantTopPanel.add(logoLabelRestaurant, BorderLayout.NORTH);
        restaurantTopPanel.add(restaurantTitleLabel, BorderLayout.CENTER);

        restaurantListModel = new DefaultListModel<>();
        restaurantJList = new JList<>(restaurantListModel);
        restaurantJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        restaurantJList.setCellRenderer(new RestaurantListCellRenderer());

        restaurantJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = restaurantJList.locationToIndex(evt.getPoint());
                    if (index >= 0) {
                        restaurantJList.setSelectedIndex(index);
                        handleRestaurantInteraction(null);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(restaurantJList);
        
        restaurantButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        restaurantBackButton = new JButton("Back to Dashboard");
        restaurantButtonPanel.add(restaurantBackButton);
        
        restaurantsPanel.add(restaurantTopPanel, BorderLayout.NORTH);
        restaurantsPanel.add(scrollPane, BorderLayout.CENTER);
        restaurantsPanel.add(restaurantButtonPanel, BorderLayout.SOUTH);

        restaurantBackButton.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));

        return restaurantsPanel;
    }
    
    private void handleRestaurantInteraction(ActionEvent e) {
        currentSelectedRestaurant = restaurantJList.getSelectedValue(); // Store this
        if (currentSelectedRestaurant == null) {
            JOptionPane.showMessageDialog(this, "Please select a restaurant first.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // --- Create a custom panel for the dialog message ---
        JPanel dialogMessagePanel = new JPanel(new BorderLayout(0, 10));
        JLabel logo = new JLabel(); // Create an empty label
        logo.setHorizontalAlignment(JLabel.CENTER);
        
        // Update logo colors to match the *current* theme
        String logoText;
        if (isDarkMode) {
             logoText = "<html><div style='text-align: center; padding-bottom: 10px;'>"
                        + "<span style='font-family: Arial; font-size: 20px; color: " + toHex(COLOR_DARK_TEXT_LIGHT) + "; font-weight: bold;'>Paras's</span> " // Fixed color
                        + "<span style='font-family: Arial; font-size: 20px; color: " + toHex(COLOR_DARK_PRIMARY_RED) + "; font-weight: bold;'>DineOut</span>"
                        + "</div></html>";
        } else {
             logoText = "<html><div style='text-align: center; padding-bottom: 10px;'>"
                        + "<span style='font-family: Arial; font-size: 20px; color: " + toHex(COLOR_LIGHT_TEXT_DARK) + "; font-weight: bold;'>Paras's</span> "
                        + "<span style='font-family: Arial; font-size: 20px; color: " + toHex(COLOR_LIGHT_PRIMARY_RED) + "; font-weight: bold;'>DineOut</span>"
                        + "</div></html>";
        }
        logo.setText(logoText);

        JLabel prompt = new JLabel("What would you like to do at " + currentSelectedRestaurant.getName() + "?");
        prompt.setHorizontalAlignment(JLabel.CENTER);
        
        // --- This is a theming hack for JOptionPane ---
        if (isDarkMode) {
            dialogMessagePanel.setBackground(COLOR_DARK_PANEL);
            prompt.setForeground(COLOR_DARK_TEXT_DARK);
        } else {
            dialogMessagePanel.setBackground(COLOR_LIGHT_PANEL);
            prompt.setForeground(COLOR_LIGHT_TEXT_DARK);
        }
        // --- End Hack ---

        dialogMessagePanel.add(logo, BorderLayout.NORTH);
        dialogMessagePanel.add(prompt, BorderLayout.CENTER);
        // --- END NEW ---

        String[] options = {"View Menu", "Make a Reservation", "Place an Order", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this, 
                dialogMessagePanel, // Use the custom panel
                "Restaurant Actions", 
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.PLAIN_MESSAGE, 
                null, options, options[0]);

        switch (choice) {
            case 0: // View Menu
                JTextArea menuArea = new JTextArea(currentSelectedRestaurant.getMenuAsString());
                menuArea.setEditable(false);
                menuArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                menuArea.setBackground(isDarkMode ? COLOR_DARK_PANEL : COLOR_LIGHT_PANEL);
                menuArea.setForeground(isDarkMode ? COLOR_DARK_TEXT_DARK : COLOR_LIGHT_TEXT_DARK);
                JScrollPane menuScrollPane = new JScrollPane(menuArea);
                menuScrollPane.setPreferredSize(new Dimension(350, 400));
                JOptionPane.showMessageDialog(this, menuScrollPane, "Menu: " + currentSelectedRestaurant.getName(), JOptionPane.PLAIN_MESSAGE);
                break;
            case 1: // Make Reservation
                handleMakeReservation(currentSelectedRestaurant);
                break;
            case 2: // --- "Place an Order" is NOW DIFFERENT ---
                // Load the menu for the selected restaurant
                menuRestaurantNameLabel.setText("Ordering from: " + currentSelectedRestaurant.getName());
                menuListModel.clear();
                currentSelectedRestaurant.getMenu().forEach(menuListModel::addElement);
                
                // Clear the cart and update the UI
                dineOutSystem.clearCart();
                updateCartPanel();
                
                // Switch to the MenuPanel screen
                cardLayout.show(mainPanel, "MenuPanel");
                break;
            default: // Cancel or close
                break;
        }
    }

    // Helper to convert Color to Hex for HTML
    private String toHex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }
    
    private void handleMakeReservation(Restaurant restaurant) {
        JTextField guestsField = new JTextField(5);
        JTextField dateField = new JTextField("YYYY-MM-DD", 10);
        JTextField timeField = new JTextField("HH:MM", 5);

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Number of Guests:"));
        panel.add(guestsField);
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Time (HH:MM / 24hr):"));
        panel.add(timeField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Make Reservation at " + restaurant.getName(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int guests = Integer.parseInt(guestsField.getText());
                String[] dateParts = dateField.getText().split("-");
                String[] timeParts = timeField.getText().split(":");
                
                dineOutSystem.makeReservation(restaurant, 
                    Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]),
                    Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]), guests);
                
                JOptionPane.showMessageDialog(this, "Reservation successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please check formats and try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    // --- +++ NEW METHOD: createMenuPanel +++ ---
    /**
     * Creates the new dedicated panel for ordering, with a live cart.
     */
    private JPanel createMenuPanel() {
        menuPanel = new JPanel(new BorderLayout(10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- TOP PANEL (Logo & Title) ---
        menuTopPanel = new JPanel(new BorderLayout());
        logoLabelMenu = createLogoLabel();
        menuRestaurantNameLabel = new JLabel("Ordering from: ...");
        menuRestaurantNameLabel.setFont(new Font("Arial", Font.BOLD, 22));
        menuTopPanel.add(logoLabelMenu, BorderLayout.NORTH);
        menuTopPanel.add(menuRestaurantNameLabel, BorderLayout.CENTER);

        // --- LEFT PANEL (Menu List) ---
        menuListPanel = new JPanel(new BorderLayout(10, 10));
        menuListPanel.add(new JLabel("Menu Items (Select to add)", JLabel.CENTER), BorderLayout.NORTH);

        menuListModel = new DefaultListModel<>();
        menuJList = new JList<>(menuListModel);
        menuJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuJList.setCellRenderer(new MenuItemListCellRenderer()); // New custom renderer
        JScrollPane menuScrollPane = new JScrollPane(menuJList);

        // -- Action panel for adding to cart --
        menuActionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        menuActionPanel.add(new JLabel("Qty:"));
        menuQtySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        menuQtySpinner.setEditor(new JSpinner.NumberEditor(menuQtySpinner, "#"));
        menuAddToCartButton = new JButton("Add Selected Item to Cart");
        menuActionPanel.add(menuQtySpinner);
        menuActionPanel.add(menuAddToCartButton);
        
        menuListPanel.add(menuScrollPane, BorderLayout.CENTER);
        menuListPanel.add(menuActionPanel, BorderLayout.SOUTH);

        // --- RIGHT PANEL (Cart) ---
        menuCartPanel = new JPanel(new BorderLayout(10, 10));
        menuCartPanel.add(new JLabel("Your Cart", JLabel.CENTER), BorderLayout.NORTH);

        cartTextArea = new JTextArea();
        cartTextArea.setEditable(false);
        cartTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane cartScrollPane = new JScrollPane(cartTextArea);

        menuCartTotalPanel = new JPanel(new BorderLayout());
        menuCartTotalPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        cartTotalLabel = new JLabel("Total: ₹0.00");
        cartTotalLabel.setFont(FONT_PRIMARY_BOLD);
        menuCartTotalPanel.add(cartTotalLabel, BorderLayout.EAST);
        
        menuCartPanel.add(cartScrollPane, BorderLayout.CENTER);
        menuCartPanel.add(menuCartTotalPanel, BorderLayout.SOUTH);

        // --- SPLIT PANE (To hold Menu and Cart) ---
        menuSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuListPanel, menuCartPanel);
        menuSplitPane.setDividerLocation(450); // Give menu panel more space

        // --- BOTTOM PANEL (Controls) ---
        menuCartButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        menuPlaceOrderButton = new JButton("Place Order");
        menuBackButton = new JButton("Cancel & Go Back");
        menuCartButtonPanel.add(menuBackButton);
        menuCartButtonPanel.add(menuPlaceOrderButton);
        
        // --- Assemble the final panel ---
        menuPanel.add(menuTopPanel, BorderLayout.NORTH);
        menuPanel.add(menuSplitPane, BorderLayout.CENTER);
        menuPanel.add(menuCartButtonPanel, BorderLayout.SOUTH);

        // --- ACTION LISTENERS for new buttons ---
        menuAddToCartButton.addActionListener(e -> handleAddToCart());
        menuPlaceOrderButton.addActionListener(e -> handlePlaceOrder());
        menuBackButton.addActionListener(e -> {
            // Go back to restaurant list
            cardLayout.show(mainPanel, "Restaurants");
        });

        return menuPanel;
    }
    
    // --- +++ NEW METHOD: handleAddToCart +++ ---
    /**
     * Called when the "Add to Cart" button is pressed on the MenuPanel.
     */
    private void handleAddToCart() {
        MenuItem selectedItem = menuJList.getSelectedValue();
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, "Please select an item from the menu.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int quantity = (Integer) menuQtySpinner.getValue();
        dineOutSystem.addToCart(selectedItem, quantity);
        updateCartPanel();
        
        // Reset spinner and selection for convenience
        menuQtySpinner.setValue(1);
        menuJList.clearSelection();
    }
    
    // --- +++ NEW METHOD: updateCartPanel +++ ---
    /**
     * Refreshes the cart JTextArea and total label with current cart data.
     */
    private void updateCartPanel() {
        cartTextArea.setText(""); // Clear old cart text
        Map<MenuItem, Integer> cart = dineOutSystem.getCart();
        
        if (cart.isEmpty()) {
            cartTextArea.setText("  Your cart is empty.");
        } else {
            for (Map.Entry<MenuItem, Integer> entry : cart.entrySet()) {
                MenuItem item = entry.getKey();
                int qty = entry.getValue();
                double itemTotal = item.getPrice() * qty;
                cartTextArea.append(String.format("  %d x %-20s (₹%.2f)\n", qty, item.getName(), itemTotal));
            }
        }
        
        cartTotalLabel.setText(String.format("Total: ₹%.2f", dineOutSystem.getCartTotal()));
    }
    
    // --- +++ NEW METHOD: handlePlaceOrder +++ ---
    /**
     * Called when the "Place Order" button is pressed on the MenuPanel.
     */
    private void handlePlaceOrder() {
        if (dineOutSystem.getCart().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Use the current restaurant
        Order order = dineOutSystem.placeOrder(currentSelectedRestaurant);
        
        // --- Payment logic (same as before, with radio buttons) ---
        JRadioButton cardRadio = new JRadioButton("Card Payment");
        cardRadio.setFont(FONT_PRIMARY_PLAIN);
        cardRadio.setSelected(true);
        JRadioButton walletRadio = new JRadioButton("Wallet Payment");
        walletRadio.setFont(FONT_PRIMARY_PLAIN);
        ButtonGroup paymentGroup = new ButtonGroup();
        paymentGroup.add(cardRadio);
        paymentGroup.add(walletRadio);
        JPanel paymentPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        paymentPanel.add(new JLabel("Select a payment method:"));
        paymentPanel.add(cardRadio);
        paymentPanel.add(walletRadio);

        int paymentResult = JOptionPane.showConfirmDialog(this, paymentPanel, 
            "Order Placed - Confirm Payment", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if(paymentResult == JOptionPane.OK_OPTION) {
            Payment paymentMethod;
            String paymentType;
            if(walletRadio.isSelected()) {
                paymentMethod = new WalletPayment("parasb736@gmail.com", "paras1234");
                paymentType = "Wallet";
            } else {
                paymentMethod = new CardPayment("1234-5678-9012-3456", dineOutSystem.getCurrentCustomer().getName());
                paymentType = "Card";
            }
            
            dineOutSystem.processOrderPayment(order, paymentMethod);
            JOptionPane.showMessageDialog(this, "Payment of ₹" + order.getTotalBill() + " processed successfully via " + paymentType + ".");
            
            // Clear cart and go back to dashboard
            dineOutSystem.clearCart();
            cardLayout.show(mainPanel, "Dashboard");
        } else {
            // Don't clear cart, let them retry payment or go back
            JOptionPane.showMessageDialog(this, "Payment cancelled. Your order is pending in the cart.", "Payment Cancelled", JOptionPane.WARNING_MESSAGE);
        }
    }

    private JPanel createHistoryPanel() {
        historyPanel = new JPanel(new BorderLayout(10, 10));
        historyPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        historyTitleLabel = new JLabel("Your Booking and Order History", JLabel.CENTER);
        historyTitleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        historyTopPanel = new JPanel(new BorderLayout());
        logoLabelHistory = createLogoLabel(); // Use instance variable
        historyTopPanel.add(logoLabelHistory, BorderLayout.NORTH);
        historyTopPanel.add(historyTitleLabel, BorderLayout.CENTER);
        
        historyTextArea = new JTextArea();
        historyTextArea.setEditable(false);
        historyTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(historyTextArea);
        
        historyBackButton = new JButton("Back to Dashboard");
        
        historyButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        historyButtonPanel.add(historyBackButton);
        
        historyPanel.add(historyTopPanel, BorderLayout.NORTH);
        historyPanel.add(scrollPane, BorderLayout.CENTER);
        historyPanel.add(historyButtonPanel, BorderLayout.SOUTH);

        historyBackButton.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));
        return historyPanel;
    }

    /**
     * --- THEME UPDATE METHOD ---
     */
    private void updateTheme(boolean isDark) {
        this.isDarkMode = isDark;

        // 1. Set theme color variables
        if (isDark) {
            colorPrimary = COLOR_DARK_PRIMARY_RED;
            colorBackground = COLOR_DARK_BACKGROUND;
            colorPanel = COLOR_DARK_PANEL;
            colorTextDark = COLOR_DARK_TEXT_DARK;
            colorTextLight = COLOR_DARK_TEXT_LIGHT;
            colorSelection = COLOR_DARK_SELECTION;
            colorBorder = COLOR_DARK_BORDER;
        } else {
            colorPrimary = COLOR_LIGHT_PRIMARY_RED;
            colorBackground = COLOR_LIGHT_BACKGROUND;
            colorPanel = COLOR_LIGHT_PANEL;
            colorTextDark = COLOR_LIGHT_TEXT_DARK;
            colorTextLight = COLOR_LIGHT_TEXT_LIGHT;
            colorSelection = COLOR_LIGHT_SELECTION;
            colorBorder = COLOR_LIGHT_BORDER;
        }

        // 2. Apply colors to all components
        mainPanel.setBackground(colorBackground);

        // Login Panel
        loginOuterPanel.setBackground(colorBackground);
        loginPanel.setBackground(colorPanel);
        loginTitleLabel.setForeground(colorTextDark);
        emailField.setBackground(isDark ? colorPanel : COLOR_LIGHT_PANEL);
        emailField.setForeground(colorTextDark);
        emailField.setCaretColor(colorTextDark);
        passwordField.setBackground(isDark ? colorPanel : COLOR_LIGHT_PANEL);
        passwordField.setForeground(colorTextDark);
        passwordField.setCaretColor(colorTextDark);

        // Dashboard Panel
        dashboardPanel.setBackground(colorBackground);
        dashboardTopPanel.setBackground(colorBackground);
        dashboardCenterPanel.setBackground(colorBackground);
        dashboardButtonPanel.setBackground(colorBackground);
        dashboardThemePanel.setBackground(colorBackground);
        welcomeLabel.setForeground(colorTextDark);
        
        // Restaurants Panel
        restaurantsPanel.setBackground(colorBackground);
        restaurantTopPanel.setBackground(colorBackground);
        restaurantTitleLabel.setForeground(colorTextDark);
        restaurantButtonPanel.setBackground(colorBackground);
        restaurantJList.setBackground(colorPanel);
        restaurantJList.setForeground(colorTextDark);
        restaurantJList.getComponent(0).setBackground(colorPanel); // Viewport

        // --- NEW: Menu Panel ---
        menuPanel.setBackground(colorBackground);
        menuTopPanel.setBackground(colorBackground);
        menuRestaurantNameLabel.setForeground(colorTextDark);
        menuListPanel.setBackground(colorPanel);
        menuActionPanel.setBackground(colorPanel);
        menuJList.setBackground(colorPanel);
        menuJList.setForeground(colorTextDark);
        menuJList.getComponent(0).setBackground(colorPanel); // Viewport
        menuCartPanel.setBackground(colorPanel);
        menuCartTotalPanel.setBackground(colorPanel);
        menuCartButtonPanel.setBackground(colorBackground);
        cartTextArea.setBackground(colorPanel);
        cartTextArea.setForeground(colorTextDark);
        cartTotalLabel.setForeground(colorTextDark);
        menuSplitPane.setDividerSize(isDark ? 5 : 8);

        // History Panel
        historyPanel.setBackground(colorBackground);
        historyTopPanel.setBackground(colorBackground);
        historyTitleLabel.setForeground(colorTextDark);
        historyButtonPanel.setBackground(colorBackground);
        historyTextArea.setBackground(colorPanel);
        historyTextArea.setForeground(colorTextDark);
        
        // 3. Restyle all buttons
        styleButton(loginButton);
        styleButton(browseButton);
        styleButton(searchButton);
        styleButton(historyButton);
        styleButton(logoutButton);
        styleButton(restaurantBackButton);
        styleButton(historyBackButton);
        styleButton(menuAddToCartButton);
        styleButton(menuPlaceOrderButton);
        styleButton(menuBackButton);
        styleToggleButton(themeToggleButton); // Style the toggle button

        // 4. Update Logo Text and Toggle Button State
        String logoText;
        if (isDark) {
            logoText = "<html><div style='text-align: center; padding-bottom: 10px;'>"
                     + "<span style='font-family: Arial; font-size: 20px; color: " + toHex(COLOR_DARK_TEXT_LIGHT) + "; font-weight: bold;'>Paras's</span> "
                     + "<span style='font-family: Arial; font-size: 20px; color: " + toHex(COLOR_DARK_PRIMARY_RED) + "; font-weight: bold;'>DineOut</span>"
                     + "</div></html>";
            themeToggleButton.setText("Switch to Light Mode");
            themeToggleButton.setSelected(true);
        } else {
            logoText = "<html><div style='text-align: center; padding-bottom: 10px;'>"
                     + "<span style='font-family: Arial; font-size: 20px; color: " + toHex(COLOR_LIGHT_TEXT_DARK) + "; font-weight: bold;'>Paras's</span> "
                     + "<span style='font-family: Arial; font-size: 20px; color: " + toHex(COLOR_LIGHT_PRIMARY_RED) + "; font-weight: bold;'>DineOut</span>"
                     + "</div></html>";
            themeToggleButton.setText("Switch to Dark Mode");
            themeToggleButton.setSelected(false);
        }
        logoLabelLogin.setText(logoText);
        logoLabelDashboard.setText(logoText);
        logoLabelRestaurant.setText(logoText);
        logoLabelHistory.setText(logoText);
        logoLabelMenu.setText(logoText); // --- NEW ---


        // 5. Repaint lists to update cell renderers
        restaurantJList.repaint();
        menuJList.repaint();
    }

    /**
     * A reusable method to style primary buttons.
     */
    private void styleButton(JButton button) {
        button.setFont(FONT_PRIMARY_BOLD);
        button.setBackground(colorPrimary);
        button.setForeground(Color.WHITE); // Text on red buttons is always white
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setOpaque(true);
        button.setBorderPainted(false);
    }
    
    /**
     * Styling method for the theme toggle button.
     */
    private void styleToggleButton(JToggleButton button) {
        button.setFont(FONT_PRIMARY_PLAIN); // A plainer font
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        button.setOpaque(true);
        button.setBorderPainted(false);
        
        if(isDarkMode) {
            button.setBackground(colorTextLight); // Light button on dark background
            button.setForeground(colorBackground); // Dark text
        } else {
            button.setBackground(colorTextDark); // Dark button on light background
            button.setForeground(Color.WHITE); // Light text
        }
    }
    
    /**
     * Custom cell renderer for the Restaurant JList.
     * Theme-aware.
     */
    class RestaurantListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof Restaurant) {
                Restaurant r = (Restaurant) value;
                String html = "<html><div style='padding: 8px;'>"
                            + "<b style='font-size: 13px; color: " + toHex(colorTextDark) + ";'>" + r.getName() + "</b><br>"
                            + "<span style='font-size: 11px; color: " + toHex(colorTextLight) + ";'>" + r.getCuisineType() + " - " + r.getLocation() + "</span><br>"
                            + "<span style='font-size: 11px; color: #007700;'>Rating: " + r.getRating() + "/5.0 | Avg. Cost: ₹" + r.getAverageCostForTwo() + "</span>"
                            + "</div></html>";
                label.setText(html);
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, colorBorder));
            }
            
            if (isSelected) {
                label.setBackground(colorSelection);
                label.setForeground(colorTextDark);
            } else {
                label.setBackground(colorPanel);
                label.setForeground(colorTextDark);
            }
            
            return label;
        }
    }
    
    // --- +++ NEW CLASS: MenuItemListCellRenderer +++ ---
    /**
     * Custom cell renderer for the Menu JList.
     * Theme-aware.
     */
    class MenuItemListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof MenuItem) {
                MenuItem item = (MenuItem) value;
                // Use HTML with theme colors
                String html = "<html><div style='padding: 6px;'>"
                            + "<b style='font-size: 12px; color: " + toHex(colorTextDark) + ";'>" + item.getName() + "</b><br>"
                            + "<span style='font-size: 11px; color: " + toHex(colorTextLight) + ";'>₹" + String.format("%.2f", item.getPrice()) + "</span>"
                            + "</div></html>";
                label.setText(html);
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, colorBorder));
            }
            
            if (isSelected) {
                label.setBackground(colorSelection);
                label.setForeground(colorTextDark);
            } else {
                label.setBackground(colorPanel);
                label.setForeground(colorTextDark);
            }
            
            return label;
        }
    }


    /**
     * Main method to run the application.
     */
    public static void main(String[] args) {
        try {
            // Set the native System Look and Feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            DineOutGUI app = new DineOutGUI();
            app.setVisible(true);
        });
    }
}

