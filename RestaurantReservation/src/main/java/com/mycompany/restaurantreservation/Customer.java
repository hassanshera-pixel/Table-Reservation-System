package com.mycompany.restaurantreservation;

public class Customer extends Person {

    private String email;
    private static int totalCustomers = 0;

    static {
        totalCustomers = 0;
        System.out.println("Customer class loaded.");
    }

    public Customer() {
        super();
        this.email = "unknown@email.com";
        totalCustomers++;
    }

    public Customer(String name, String phone, String email) {
        super(name, phone);
        this.email = email;
        totalCustomers++;
    }

    public Customer(Customer other) {
        super(other);
        this.email = other.email;
        totalCustomers++;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public static int getTotalCustomers() { return totalCustomers; }

    @Override
    public String getRole() { return "Customer"; }

    @Override
    public String toString() {
        return super.toString() + " | Email: " + email;
    }
}
