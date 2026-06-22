package com.mycompany.restaurantreservation;

import java.util.ArrayList;

public class Order {

    private final ArrayList<MenuItem> items;
    private final Customer customer;
    private static int orderCounter = 0;
    private final int orderId;

    public Order(Customer customer) {
        this.customer = customer;
        this.items = new ArrayList<>();
        orderCounter++;
        this.orderId = orderCounter;
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public double calculateTotal() {
        double total = 0;
        for (MenuItem item : items) {
            total += item.getPrice();
        }
        return total;
    }

    public ArrayList<MenuItem> getItems() {
        return items;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getOrderId() {
        return orderId;
    }

    public static int getOrderCounter() {
        return orderCounter;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order #").append(orderId).append(" for ").append(customer.getName()).append("\n");

        for (MenuItem item : items) {
            sb.append("  - ").append(item.toString()).append("\n");
        }

        sb.append("Total: Rs.").append(calculateTotal());
        return sb.toString();
    }

    public class OrderSummary {
        public String getSummary() {
            return "Order #" + orderId + " | Items: " + items.size() + " | Total: Rs." + calculateTotal();
        }
    }

    public static class OrderValidator {
        public static boolean isValid(Order order) {
            return order != null && !order.getItems().isEmpty();
        }
    }
}
