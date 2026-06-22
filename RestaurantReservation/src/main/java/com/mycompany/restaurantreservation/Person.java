package com.mycompany.restaurantreservation;

public abstract class Person {

    protected String name;
    protected String phone;

    public Person() {
        this.name = "Unknown";
        this.phone = "0000";
    }

    public Person(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public Person(Person other) {
        this.name = other.name;
        this.phone = other.phone;
    }

    public String getName() { return name; }
    public String getPhone() { return phone; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }

    public abstract String getRole();

    @Override
    public String toString() {
        return name + " (" + phone + ")";
    }
}
