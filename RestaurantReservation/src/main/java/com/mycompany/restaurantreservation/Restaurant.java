package com.mycompany.restaurantreservation;

import java.util.ArrayList;

public class Restaurant {

    private String restaurantName;
    private ArrayList<Table> tables;
    private ArrayList<Order> orders;

    public Restaurant(String restaurantName) {
        this.restaurantName = restaurantName;
        tables = new ArrayList<>();
        orders = new ArrayList<>();
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

    public void makeReservation(Customer customer, int tableNumber, String date, String time)
            throws ReservationException {

        Table table = findTable(tableNumber);

        if (table == null) {
            throw new ReservationException("Table not found.");
        }

        table.reserve(customer, date, time);
    }

    public void cancelReservation(int tableNumber) throws ReservationException {
        Table table = findTable(tableNumber);

        if (table == null) {
            throw new ReservationException("Table not found.");
        }

        table.cancel();
    }

    private Table findTable(int tableNumber) {
        for (Table t : tables) {
            if (t.getTableNumber() == tableNumber) {
                return t;
            }
        }
        return null;
    }

    public void placeOrder(Order order) {
        orders.add(order);
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