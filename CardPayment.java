/**
 * Represents a payment made via a credit or debit card.
 * This demonstrates polymorphism by overriding the processPayment method.
 */
public class CardPayment extends Payment {
    private String cardNumber;
    private String cardHolderName;

    public CardPayment(String cardNumber, String cardHolderName) {
        super();
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
    }

    @Override
    public void processPayment(double amount) {
        setAmount(amount);
        System.out.println("Processing card payment of ₹" + amount + " for " + cardHolderName);
        // Simulate payment gateway interaction
        System.out.println("Payment successful with Card ending in " + cardNumber.substring(cardNumber.length() - 4));
    }
}

