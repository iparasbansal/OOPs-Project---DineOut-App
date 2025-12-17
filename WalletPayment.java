/**
 * Concrete class for processing a digital wallet payment.
 * Extends Payment and demonstrates Polymorphism.
 */
public class WalletPayment extends Payment {
    
    private String walletId; // e.g., email or phone number
    private String password; // Added field

    /**
     * Constructor updated to accept both walletId and password.
     * This fixes the compilation error in DineOutGUI.java.
     */
    public WalletPayment(String walletId, String password) {
        this.walletId = walletId;
        this.password = password;
    }

    /**
     * The concrete implementation of processPayment for a wallet.
     * This method correctly overrides the abstract method from Payment.
     */
    @Override
    public void processPayment(double amount) {
        // Simulate wallet payment processing
        System.out.println("Authenticating wallet user: " + walletId);
        
        // Simple authentication check
        if (password != null && !password.isEmpty()) {
            System.out.println("Authentication successful.");
            System.out.println("Processing wallet payment of ₹" + amount + " from " + walletId);
            System.out.println("Payment successful.");
        } else {
            System.out.println("Authentication failed: No password provided.");
            System.out.println("Payment failed.");
        }
    }
}

