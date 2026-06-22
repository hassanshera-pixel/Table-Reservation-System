package com.mycompany.restaurantreservation;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.util.*;

public class MainApp extends Application {

    private final Restaurant restaurant = new Restaurant("Dine Palace");
    private Stage stage;
    private final int W = 700, H = 520;

    @Override
    public void start(Stage s) {
        stage = s;
        stage.setTitle("Dine Palace Reservation System");
        stage.setResizable(false);
        stage.setScene(menu());
        stage.show();
    }

    private Scene menu() {
        VBox box = page(Pos.CENTER);

        Text title = heading("Dine Palace", 34);

        Text sub = new Text("Restaurant Reservation System");
        sub.setStyle("-fx-fill: #cbd5e1; -fx-font-size: 16px;");

        Button reserve = btn("Make Reservation", () -> stage.setScene(reservation()));
        Button cancel = btn("Cancel Reservation", () -> stage.setScene(cancel()));
        Button order = btn("Place Food Order", () -> stage.setScene(order()));
        Button tables = btn("View All Tables", () -> stage.setScene(tables()));
        Button orders = btn("View All Orders", () -> stage.setScene(viewOrders()));
        Button records = btn("View Saved Records", () -> stage.setScene(records()));
        Button exit = btn("Exit", () -> stage.close());

        box.getChildren().addAll(title, sub, reserve, cancel, order, tables, orders, records, exit);

        return scene(box);
    }

    private Scene reservation() {
        VBox root = page(Pos.TOP_LEFT);

        TextField name = tf();
        TextField phone = tf();
        TextField email = tf();
        TextField table = tf();
        TextField time = tf();
        TextField guests = tf();

        table.setPromptText("Example: 1");
        time.setPromptText("Example: 14:00");

        DatePicker date = new DatePicker();
        date.setPrefWidth(160);
        date.setStyle(
                "-fx-background-radius: 8px;" +
                        "-fx-border-radius: 8px;");

        Label msg = msgLabel();

        Button submit = btn("Confirm", () -> {
            try {
                if (name.getText().isBlank()
                        || phone.getText().isBlank()
                        || table.getText().isBlank()
                        || date.getValue() == null
                        || time.getText().isBlank()) {

                    throw new ReservationException("Please fill all required fields.");
                }

                int tableNo = Integer.parseInt(table.getText().trim());

                Customer c = new Customer(
                        name.getText().trim(),
                        phone.getText().trim(),
                        email.getText().trim());

                restaurant.makeReservation(
                        c,
                        tableNo,
                        date.getValue().toString(),
                        time.getText().trim());

                msg.setText("Reservation confirmed for Table " + tableNo
                        + " from " + time.getText().trim()
                        + " to next 2 hours.");

                FileHandler.saveReservation(
                        "Name: " + name.getText()
                                + ", Phone: " + phone.getText()
                                + ", Table: " + tableNo
                                + ", Date: " + date.getValue()
                                + ", Time: " + time.getText()
                                + ", Duration: 2 hours");

            } catch (NumberFormatException ex) {
                msg.setText("Invalid table number.");
            } catch (ReservationException ex) {
                msg.setText(ex.getMessage());
            }
        });

        root.getChildren().addAll(
                heading("Make Reservation", 24),
                row("Name:", name, "Phone:", phone),
                row("Email:", email, "Table:", table),
                dateRow("Date:", date, "Time:", time),
                singleRow("Guests:", guests),
                buttons(submit, back()),
                msg);

        return scene(root);
    }

    private Scene cancel() {
        VBox root = page(Pos.TOP_LEFT);

        TextField table = tf();
        table.setPromptText("Example: 1");

        Label msg = msgLabel();

        Button cancel = btn("Cancel", () -> {
            try {
                int t = Integer.parseInt(table.getText().trim());
                restaurant.cancelReservation(t);
                msg.setText("All reservations cancelled for Table " + t);
            } catch (NumberFormatException e) {
                msg.setText("Invalid table number.");
            } catch (Exception e) {
                msg.setText(e.getMessage());
            }
        });

        root.getChildren().addAll(
                heading("Cancel Reservation", 24),
                singleRow("Table No:", table),
                buttons(cancel, back()),
                msg);

        return scene(root);
    }

    private Scene order() {
        VBox root = page(Pos.TOP_LEFT);

        TextField name = tf();
        TextField phone = tf();

        ArrayList<MenuItem> menu = new ArrayList<>(List.of(
                new MenuItem("Burger", 350, "Main"),
                new MenuItem("Pizza", 500, "Main"),
                new MenuItem("Pasta", 400, "Main"),
                new MenuItem("Pepsi", 100, "Drinks"),
                new MenuItem("Soup", 200, "Starter"),
                new MenuItem("Ice Cream", 180, "Dessert")));

        ListView<String> list = new ListView<>();
        menu.forEach(i -> list.getItems().add(i.toString()));
        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        list.setPrefHeight(150);

        Label total = whiteLabel("Total: Rs.0");
        total.setStyle("-fx-text-fill: #86efac; -fx-font-size: 16px; -fx-font-weight: bold;");

        Label msg = msgLabel();

        Button place = btn("Place Order", () -> {
            try {
                if (name.getText().isBlank() || phone.getText().isBlank()) {
                    throw new ReservationException("Name and phone required.");
                }

                var selected = list.getSelectionModel().getSelectedIndices();

                if (selected.isEmpty()) {
                    throw new ReservationException("Select at least one item.");
                }

                Order o = new Order(new Customer(name.getText(), phone.getText(), ""));

                selected.forEach(i -> o.addItem(menu.get(i)));

                restaurant.placeOrder(o);

                msg.setText(o.toString());
                total.setText("Total: Rs." + o.calculateTotal());

            } catch (ReservationException e) {
                msg.setText(e.getMessage());
            }
        });

        root.getChildren().addAll(
                heading("Place Food Order", 24),
                row("Name:", name, "Phone:", phone),
                whiteLabel("Select Menu Items:"),
                list,
                total,
                buttons(place, back()),
                msg);

        return scene(root);
    }

    private Scene tables() {
        VBox root = page(Pos.TOP_LEFT);

        TableView<Table> tv = new TableView<>();
        tv.setPrefHeight(320);

        tv.getColumns().add(col("Table", "tableNumber"));
        tv.getColumns().add(col("Capacity", "capacity"));
        tv.getColumns().add(col("Status", "status"));
        tv.getColumns().add(col("Bookings", "bookingDetails"));

        tv.setItems(FXCollections.observableArrayList(restaurant.getTables()));

        Label total = whiteLabel("Total Tables: " + Table.getTotalTables());

        root.getChildren().addAll(
                heading("All Tables", 24),
                tv,
                total,
                buttons(back()));

        return scene(root);
    }

    private Scene viewOrders() {
        String text = restaurant.getOrders().isEmpty()
                ? "No orders placed yet."
                : restaurant.getOrders().toString();

        return textPage("All Orders", text);
    }

    private Scene records() {
        TextArea area = area();

        ArrayList<String> records = FileHandler.loadReservations();
        area.setText(records.isEmpty() ? "No records saved." : String.join("\n", records));

        Button clear = btn("Clear", () -> {
            FileHandler.clearFile();
            area.setText("Records cleared.");
        });

        VBox root = page(Pos.TOP_LEFT);
        root.getChildren().addAll(
                heading("Saved Records", 24),
                area,
                buttons(clear, back()));

        return scene(root);
    }

    private Scene textPage(String title, String text) {
        VBox root = page(Pos.TOP_LEFT);

        TextArea area = area();
        area.setText(text);

        root.getChildren().addAll(
                heading(title, 24),
                area,
                buttons(back()));

        return scene(root);
    }

    private Button back() {
        return btn("Back", () -> stage.setScene(menu()));
    }

    private Button btn(String text, Runnable action) {
        Button b = new Button(text);

        b.setPrefWidth(210);

        String normalStyle = "-fx-background-color: #2563eb;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 10px;" +
                "-fx-padding: 8px;" +
                "-fx-cursor: hand;";

        String hoverStyle = "-fx-background-color: #1d4ed8;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 10px;" +
                "-fx-padding: 8px;" +
                "-fx-cursor: hand;";

        b.setStyle(normalStyle);

        b.setOnMouseEntered(e -> b.setStyle(hoverStyle));
        b.setOnMouseExited(e -> b.setStyle(normalStyle));

        b.setOnAction(e -> action.run());

        return b;
    }

    private VBox page(Pos pos) {
        VBox v = new VBox(12);

        v.setPadding(new Insets(25));
        v.setAlignment(pos);

        v.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #0f172a, #1e293b);");

        return v;
    }

    private TextField tf() {
        TextField f = new TextField();

        f.setPrefWidth(160);

        f.setStyle(
                "-fx-background-radius: 8px;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-border-color: #93c5fd;" +
                        "-fx-padding: 6px;");

        return f;
    }

    private TextArea area() {
        TextArea a = new TextArea();

        a.setEditable(false);
        a.setPrefHeight(330);

        a.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-border-radius: 8px;");

        return a;
    }

    private Scene scene(VBox root) {
        return new Scene(root, W, H);
    }

    private Text heading(String text, int size) {
        Text t = new Text(text);

        t.setFont(Font.font("Arial", FontWeight.BOLD, size));

        t.setStyle("-fx-fill: white;");

        return t;
    }

    private Label whiteLabel(String text) {
        Label l = new Label(text);

        l.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;");

        return l;
    }

    private Label msgLabel() {
        Label l = new Label();

        l.setStyle(
                "-fx-text-fill: #facc15;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;");

        return l;
    }

    private HBox row(String l1, Control f1, String l2, Control f2) {
        Label a = whiteLabel(l1);
        Label b = whiteLabel(l2);

        a.setPrefWidth(80);
        b.setPrefWidth(70);

        HBox h = new HBox(10, a, f1, b, f2);
        h.setAlignment(Pos.CENTER_LEFT);

        return h;
    }

    private HBox dateRow(String l1, DatePicker d, String l2, TextField t) {
        return row(l1, d, l2, t);
    }

    private HBox singleRow(String label, Control field) {
        Label l = whiteLabel(label);

        l.setPrefWidth(100);

        HBox h = new HBox(10, l, field);
        h.setAlignment(Pos.CENTER_LEFT);

        return h;
    }

    private HBox buttons(Button... buttons) {
        HBox h = new HBox(10, buttons);
        h.setAlignment(Pos.CENTER_LEFT);
        return h;
    }

    private TableColumn<Table, Object> col(String title, String prop) {
        TableColumn<Table, Object> c = new TableColumn<>(title);

        c.setCellValueFactory(new PropertyValueFactory<>(prop));
        c.setPrefWidth(160);

        return c;
    }

    public static void main(String[] args) {
        launch(args);
    }
}