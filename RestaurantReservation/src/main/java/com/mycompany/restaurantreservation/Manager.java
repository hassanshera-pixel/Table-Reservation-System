package com.mycompany.restaurantreservation;

public class Manager extends Staff {

    private final String department;

    public Manager(String name, String phone, String department) {
        super(name, phone, "Manager");
        this.department = department;
    }

    public String getDepartment() { return department; }

    @Override
    public String getRole() {
        return super.getRole() + " of " + department;
    }
}
