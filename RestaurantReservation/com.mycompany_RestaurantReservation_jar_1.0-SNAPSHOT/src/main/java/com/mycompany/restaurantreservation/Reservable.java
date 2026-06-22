package com.mycompany.restaurantreservation;

public interface Reservable {

    boolean reserve(Customer customer, String date, String time);

    boolean cancel();

    boolean isAvailable();

    String getStatus();
}