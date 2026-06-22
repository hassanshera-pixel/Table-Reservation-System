package com.mycompany.restaurantreservation;

public class MenuItem {

    private final String itemName;
    private final double price;
    private final String category;

    public MenuItem(String itemName, double price, String category) {
        this.itemName = itemName;
        this.price = price;
        this.category = category;
    }

    public String getItemName() { return itemName; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }

    @Override
    public String toString() {
        return itemName + " [" + category + "] - Rs." + price;
    }
}
