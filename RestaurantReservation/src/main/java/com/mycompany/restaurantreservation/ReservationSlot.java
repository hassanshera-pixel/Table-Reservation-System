package com.mycompany.restaurantreservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReservationSlot {

    private Customer customer;
    private String date;
    private String time;

    public ReservationSlot(Customer customer, String date, String time) {
        this.customer = customer;
        this.date = date;
        this.time = time;
    }

    public LocalDateTime getStartDateTime() {
        return LocalDateTime.of(
                LocalDate.parse(date),
                LocalTime.parse(time)
        );
    }

    public LocalDateTime getEndDateTime() {
        return getStartDateTime().plusHours(2);
    }

    public String getEndTime() {
        return getEndDateTime().toLocalTime().toString();
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
