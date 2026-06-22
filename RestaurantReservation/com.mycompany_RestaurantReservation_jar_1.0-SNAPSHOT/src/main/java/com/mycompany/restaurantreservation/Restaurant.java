package com.mycompany.restaurantreservation;

import java.util.ArrayList;

public class Restaurant {

    private String restaurantName;
    private ArrayList<Table> tables;
    private ArrayList<Customer> customers;
    private ArrayList<Order> orders;

    public Restaurant(String restaurantName) {
        this.restaurantName = restaurantName;
        this.tables = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.orders = new ArrayList<>();
        initializeTables();
    }

    private void initializeTables() {
        tables.add(new Table(1, 2));
        tables.add(new Table(2, 4));
        tables.add(new Table(3, 4));
        tables.add(new Table(4, 6));
        tables.add(new Table(5, 8));
        tables.add(new Table(6, 2));
    }

    public Table findAvailableTable(int minCapacity) throws ReservationException {
        for (Table t : tables) {
            if (t.isAvailable() && t.getCapacity() >= minCapacity) {
                return t;
            }
        }

        throw new ReservationException("No table available for " + minCapacity + " people.");
    }

    public boolean makeReservation(Customer customer, int tableNumber, String date, String time)
            throws ReservationException {

        Table table = getTableByNumber(tableNumber);

        if (table == null) {
            throw new ReservationException("Table " + tableNumber + " does not exist.");
        }

        boolean success = table.reserve(customer, date, time);

        if (!success) {
            throw new ReservationException("Table " + tableNumber + " is already reserved.");
        }

        customers.add(customer);

        String record = customer.getName() + ","
                + customer.getPhone() + ","
                + tableNumber + ","
                + date + ","
                + time;

        FileHandler.saveReservation(record);

        return true;
    }

    public boolean cancelReservation(int tableNumber) throws ReservationException {

        Table table = getTableByNumber(tableNumber);

        if (table == null) {
            throw new ReservationException("Table not found.");
        }

        boolean success = table.cancel();

        if (!success) {
            throw new ReservationException("Table " + tableNumber + " was not reserved.");
        }

        return true;
    }

    public void placeOrder(Order order) {
        orders.add(order);
    }

    public Table getTableByNumber(int number) {
        for (Table t : tables) {
            if (t.getTableNumber() == number) {
                return t;
            }
        }

        return null;
    }

    public ArrayList<Table> getTables() {
        return tables;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public String getRestaurantName() {
        return restaurantName;
    }
}