package com.mycompany.restaurantreservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Table implements Reservable {

    private int tableNumber;
    private int capacity;

    private ArrayList<ReservationSlot> reservations;

    private static int totalTables = 0;

    public Table() {
        tableNumber = 0;
        capacity = 2;
        reservations = new ArrayList<>();
        totalTables++;
    }

    public Table(int tableNumber, int capacity) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        reservations = new ArrayList<>();
        totalTables++;
    }

    @Override
    public boolean reserve(Customer customer, String date, String time) throws ReservationException {

        LocalDate reservationDate;
        LocalTime startTime;

        try {
            reservationDate = LocalDate.parse(date);
            startTime = LocalTime.parse(time);
        } catch (Exception e) {
            throw new ReservationException("Invalid date or time format. Use time like 14:00");
        }

        LocalDateTime newStart = LocalDateTime.of(reservationDate, startTime);
        LocalDateTime newEnd = newStart.plusHours(2);

        for (ReservationSlot old : reservations) {

            LocalDateTime oldStart = old.getStartDateTime();
            LocalDateTime oldEnd = old.getEndDateTime();

            boolean overlap = newStart.isBefore(oldEnd) && newEnd.isAfter(oldStart);

            if (overlap) {
                throw new ReservationException(
                        "Table already reserved from "
                                + old.getTime()
                                + " to "
                                + old.getEndTime()
                                + " on "
                                + old.getDate());
            }
        }

        ReservationSlot slot = new ReservationSlot(customer, date, time);
        reservations.add(slot);

        return true;
    }

    @Override
    public boolean cancel() {
        if (reservations.isEmpty()) {
            return false;
        }

        reservations.clear();
        return true;
    }

    @Override
    public boolean isAvailable() {
        return reservations.isEmpty();
    }

    @Override
    public String getStatus() {
        if (reservations.isEmpty()) {
            return "Available";
        }
        return "Reserved";
    }

    public String getBookingDetails() {
        if (reservations.isEmpty()) {
            return "No booking";
        }

        StringBuilder sb = new StringBuilder();

        for (ReservationSlot r : reservations) {
            sb.append(r.getDate())
                    .append(" ")
                    .append(r.getTime())
                    .append("-")
                    .append(r.getEndTime())
                    .append("\n");
        }

        return sb.toString();
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public static int getTotalTables() {
        return totalTables;
    }

    @Override
    public String toString() {
        return "Table " + tableNumber
                + " | Capacity: " + capacity
                + " | Status: " + getStatus()
                + " | Bookings: " + getBookingDetails();
    }
}