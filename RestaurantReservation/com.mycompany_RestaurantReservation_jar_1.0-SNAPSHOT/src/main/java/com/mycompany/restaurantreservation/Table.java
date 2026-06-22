package com.mycompany.restaurantreservation;

public class Table implements Reservable {

    private int tableNumber;
    private int capacity;
    private String status;
    private Customer reservedBy;
    private String reservationDate;
    private String reservationTime;
    private static int totalTables = 0;

    static {
        totalTables = 0;
    }

    public Table() {
        this.tableNumber = 0;
        this.capacity = 2;
        this.status = "Available";
        totalTables++;
    }

    public Table(int tableNumber, int capacity) {
        this();
        this.tableNumber = tableNumber;
        this.capacity = capacity;
    }

    public Table(Table other) {
        this.tableNumber = other.tableNumber;
        this.capacity = other.capacity;
        this.status = other.status;
        this.reservedBy = other.reservedBy;
        this.reservationDate = other.reservationDate;
        this.reservationTime = other.reservationTime;
    }

    @Override
    public boolean reserve(Customer customer, String date, String time) {
        if (!status.equals("Available"))
            return false;

        this.reservedBy = customer;
        this.reservationDate = date;
        this.reservationTime = time;
        this.status = "Reserved";

        return true;
    }

    @Override
    public boolean cancel() {
        if (status.equals("Available"))
            return false;

        this.reservedBy = null;
        this.reservationDate = null;
        this.reservationTime = null;
        this.status = "Available";

        return true;
    }

    @Override
    public boolean isAvailable() {
        return status.equals("Available");
    }

    @Override
    public String getStatus() {
        return status;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public Customer getReservedBy() {
        return reservedBy;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public String getReservationTime() {
        return reservationTime;
    }

    public static int getTotalTables() {
        return totalTables;
    }

    @Override
    public String toString() {
        return "Table " + tableNumber + " | Capacity: " + capacity + " | Status: " + status;
    }
}