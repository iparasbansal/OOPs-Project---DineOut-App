/**
 * Represents a payment made via a digital wallet.
 * This demonstrates polymorphism by overriding the processPayment method.
 */
public class WalletPayment extends Payment {
    private String walletId; // e.g., email or phone number

    public WalletPayment(String walletId) {
        super();
        this.walletId = walletId;
    }

    @Override
    public void processPayment(double amount) {
        setAmount(amount);
        System.out.println("Processing wallet payment of ₹" + amount + " for wallet ID: " + walletId);
        // Simulate wallet transaction
        System.out.println("Payment successful with Wallet: " + walletId);
    }
}

