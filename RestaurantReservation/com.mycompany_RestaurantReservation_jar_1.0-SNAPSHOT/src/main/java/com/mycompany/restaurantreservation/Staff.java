package com.mycompany.restaurantreservation;

public class Staff extends Person {

    private String role;

    public Staff(String name, String phone, String role) {
        super(name, phone);
        this.role = role;
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return super.toString() + " | Role: " + role;
    }
}