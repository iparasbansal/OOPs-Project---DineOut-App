/**
 * A general representation of a payment, implementing the Payable interface.
 * This can be extended by specific payment methods.
 */
public abstract class Payment implements Payable {
    private String paymentId;
    private double amount;

    public Payment() {
        this.paymentId = "PAY" + System.currentTimeMillis();
    }

    public String getPaymentId() {
        return paymentId;
    }

    public double getAmount() {
        return amount;
    }

    protected void setAmount(double amount) {
        this.amount = amount;
    }

    // This method will be implemented by subclasses (Polymorphism)
    @Override
    public abstract void processPayment(double amount);
}

