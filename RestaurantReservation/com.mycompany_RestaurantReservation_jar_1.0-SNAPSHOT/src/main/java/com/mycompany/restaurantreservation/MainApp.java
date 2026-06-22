package com.mycompany.restaurantreservation;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

public class MainApp extends Application {

    private Restaurant restaurant = new Restaurant("Dine Palace");
    private Stage stage;

    private final int W = 600;
    private final int H = 450;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("Dine Palace Reservation System");
        stage.setResizable(false);
        stage.setScene(mainMenu());
        stage.show();
    }

    private Scene mainMenu() {
        BorderPane root = new BorderPane();

        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));

        Text title = heading("Dine Palace", 28);
        Text sub = new Text("Restaurant Reservation System");

        Button reserve = btn("Make Reservation");
        Button cancel = btn("Cancel Reservation");
        Button order = btn("Place Food Order");
        Button tables = btn("View All Tables");
        Button orders = btn("View All Orders");
        Button records = btn("View Saved Records");
        Button exit = btn("Exit");

        reserve.setOnAction(e -> stage.setScene(reservationScene()));
        cancel.setOnAction(e -> stage.setScene(cancelScene()));
        order.setOnAction(e -> stage.setScene(orderScene()));
        tables.setOnAction(e -> stage.setScene(tableScene()));
        orders.setOnAction(e -> stage.setScene(orderViewScene()));
        records.setOnAction(e -> stage.setScene(recordsScene()));
        exit.setOnAction(e -> stage.close());

        box.getChildren().addAll(
                title, sub,
                reserve, cancel, order,
                tables, orders, records, exit
        );

        root.setCenter(box);
        return new Scene(root, W, H);
    }

    private Scene reservationScene() {
        VBox root = page();

        Text heading = heading("Make Reservation", 22);

        TextField name = new TextField();
        TextField phone = new TextField();
        TextField email = new TextField();
        TextField table = new TextField();
        TextField date = new TextField();
        TextField time = new TextField();
        TextField guests = new TextField();

        Label msg = new Label();

        Button submit = btn("Confirm");
        Button back = btn("Back");

        submit.setOnAction(e -> {
            try {
                String n = name.getText().trim();
                String p = phone.getText().trim();
                String em = email.getText().trim();
                int t = Integer.parseInt(table.getText().trim());
                String d = date.getText().trim();
                String ti = time.getText().trim();

                if (n.isEmpty() || p.isEmpty() || d.isEmpty() || ti.isEmpty()) {
                    throw new ReservationException("All fields are required.");
                }

                Customer c = new Customer(n, p, em);
                restaurant.makeReservation(c, t, d, ti);

                msg.setText("Reservation confirmed for table " + t);

            } catch (NumberFormatException ex) {
                msg.setText("Invalid table number.");
            } catch (ReservationException ex) {
                msg.setText(ex.getMessage());
            }
        });

        back.setOnAction(e -> stage.setScene(mainMenu()));

        root.getChildren().addAll(
                heading,
                row("Name:", name, "Phone:", phone),
                row("Email:", email, "Table:", table),
                row("Date:", date, "Time:", time),
                singleRow("Guests:", guests),
                buttons(submit, back),
                msg
        );

        return scene(root);
    }

    private Scene cancelScene() {
        VBox root = page();

        TextField table = new TextField();
        Label msg = new Label();

        Button cancel = btn("Cancel");
        Button back = btn("Back");

        cancel.setOnAction(e -> {
            try {
                int t = Integer.parseInt(table.getText().trim());
                restaurant.cancelReservation(t);
                msg.setText("Reservation cancelled for table " + t);
            } catch (NumberFormatException ex) {
                msg.setText("Enter valid table number.");
            } catch (ReservationException ex) {
                msg.setText(ex.getMessage());
            }
        });

        back.setOnAction(e -> stage.setScene(mainMenu()));

        root.getChildren().addAll(
                heading("Cancel Reservation", 22),
                singleRow("Table Number:", table),
                buttons(cancel, back),
                msg
        );

        return scene(root);
    }

    private Scene orderScene() {
        VBox root = page();

        TextField name = new TextField();
        TextField phone = new TextField();

        ArrayList<MenuItem> menu = new ArrayList<>();
        menu.add(new MenuItem("Burger", 350, "Main"));
        menu.add(new MenuItem("Pizza", 500, "Main"));
        menu.add(new MenuItem("Pasta", 400, "Main"));
        menu.add(new MenuItem("Pepsi", 100, "Drinks"));
        menu.add(new MenuItem("Soup", 200, "Starter"));
        menu.add(new MenuItem("Ice Cream", 180, "Dessert"));

        ListView<String> list = new ListView<>();
        for (MenuItem item : menu) {
            list.getItems().add(item.toString());
        }

        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        list.setPrefHeight(130);

        Label total = new Label("Total: Rs.0");
        Label msg = new Label();

        Button place = btn("Place Order");
        Button back = btn("Back");

        place.setOnAction(e -> {
            try {
                if (name.getText().trim().isEmpty() || phone.getText().trim().isEmpty()) {
                    throw new ReservationException("Name and phone required.");
                }

                ObservableList<Integer> selected = list.getSelectionModel().getSelectedIndices();

                if (selected.isEmpty()) {
                    throw new ReservationException("Select at least one item.");
                }

                Customer c = new Customer(name.getText(), phone.getText(), "");
                Order order = new Order(c);

                for (int i : selected) {
                    order.addItem(menu.get(i));
                }

                restaurant.placeOrder(order);

                Order.OrderSummary summary = order.new OrderSummary();
                msg.setText(summary.getSummary());
                total.setText("Total: Rs." + order.calculateTotal());

            } catch (ReservationException ex) {
                msg.setText(ex.getMessage());
            }
        });

        back.setOnAction(e -> stage.setScene(mainMenu()));

        root.getChildren().addAll(
                heading("Place Food Order", 22),
                row("Name:", name, "Phone:", phone),
                new Label("Select Menu Items:"),
                list,
                total,
                buttons(place, back),
                msg
        );

        return scene(root);
    }

    private Scene tableScene() {
        VBox root = page();

        TableView<Table> tv = new TableView<>();
        tv.setPrefHeight(270);

        TableColumn<Table, Integer> c1 = new TableColumn<>("Table");
        c1.setCellValueFactory(new PropertyValueFactory<>("tableNumber"));

        TableColumn<Table, Integer> c2 = new TableColumn<>("Capacity");
        c2.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        TableColumn<Table, String> c3 = new TableColumn<>("Status");
        c3.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Table, String> c4 = new TableColumn<>("Date");
        c4.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));

        TableColumn<Table, String> c5 = new TableColumn<>("Time");
        c5.setCellValueFactory(new PropertyValueFactory<>("reservationTime"));

        tv.getColumns().addAll(c1, c2, c3, c4, c5);
        tv.setItems(FXCollections.observableArrayList(restaurant.getTables()));

        Button back = btn("Back");
        back.setOnAction(e -> stage.setScene(mainMenu()));

        root.getChildren().addAll(
                heading("All Tables", 22),
                tv,
                new Label("Total Tables: " + Table.getTotalTables()),
                buttons(back)
        );

        return scene(root);
    }

    private Scene orderViewScene() {
        VBox root = page();

        TextArea area = new TextArea();
        area.setEditable(false);
        area.setPrefHeight(300);

        ArrayList<Order> orders = restaurant.getOrders();

        if (orders.isEmpty()) {
            area.setText("No orders placed yet.");
        } else {
            StringBuilder sb = new StringBuilder();

            for (Order o : orders) {
                sb.append(o).append("\n\n---\n\n");
            }

            area.setText(sb.toString());
        }

        Button back = btn("Back");
        back.setOnAction(e -> stage.setScene(mainMenu()));

        root.getChildren().addAll(
                heading("All Orders", 22),
                area,
                buttons(back)
        );

        return scene(root);
    }

    private Scene recordsScene() {
        VBox root = page();

        TextArea area = new TextArea();
        area.setEditable(false);
        area.setPrefHeight(300);

        ArrayList<String> records = FileHandler.loadReservations();

        if (records.isEmpty()) {
            area.setText("No records saved.");
        } else {
            StringBuilder sb = new StringBuilder();

            for (String r : records) {
                sb.append(r).append("\n");
            }

            area.setText(sb.toString());
        }

        Button clear = btn("Clear");
        Button back = btn("Back");

        clear.setOnAction(e -> {
            FileHandler.clearFile();
            area.setText("Records cleared.");
        });

        back.setOnAction(e -> stage.setScene(mainMenu()));

        root.getChildren().addAll(
                heading("Saved Records", 22),
                area,
                buttons(clear, back)
        );

        return scene(root);
    }

    private Scene scene(VBox root) {
        return new Scene(root, W, H);
    }

    private VBox page() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_LEFT);
        return root;
    }

    private Button btn(String text) {
        Button b = new Button(text);
        b.setPrefWidth(190);
        return b;
    }

    private Text heading(String text, int size) {
        Text t = new Text(text);
        t.setFont(Font.font("Arial", FontWeight.BOLD, size));
        return t;
    }

    private HBox row(String l1, TextField f1, String l2, TextField f2) {
        Label label1 = new Label(l1);
        Label label2 = new Label(l2);

        label1.setPrefWidth(80);
        label2.setPrefWidth(70);

        f1.setPrefWidth(150);
        f2.setPrefWidth(150);

        HBox row = new HBox(10, label1, f1, label2, f2);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private HBox singleRow(String label, TextField field) {
        Label l = new Label(label);
        l.setPrefWidth(100);
        field.setPrefWidth(150);

        HBox row = new HBox(10, l, field);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private HBox buttons(Button... btns) {
        HBox box = new HBox(10, btns);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    public static void main(String[] args) {
        launch(args);
    }
}