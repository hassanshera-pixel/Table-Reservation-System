package com.mycompany.restaurantreservation;

public interface Reservable {

    boolean reserve(Customer customer, String date, String time) throws ReservationException;

    boolean cancel();

    boolean isAvailable();

    String getStatus();
}