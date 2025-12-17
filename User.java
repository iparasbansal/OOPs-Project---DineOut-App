/**
 * Represents a base User.
 * This class is extended by Customer.
 * Demonstrates Inheritance (as a base class).
 */
public class User {
    // Fields are 'protected' so child classes (like Customer) can access them
    protected String userId;
    protected String name;
    protected String email;

    /**
     * Corrected 3-argument constructor.
     * This fixes the compilation error in Customer.java.
     */
    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    // --- Getters ---

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}

