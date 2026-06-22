package com.mycompany.restaurantreservation;

import java.io.*;
import java.util.ArrayList;

public class FileHandler {

    private static final String FILE_PATH = "reservations.txt";

    public static void saveReservation(String data) {
        try (FileWriter fw = new FileWriter(FILE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(data);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }

    public static ArrayList<String> loadReservations() {
        ArrayList<String> records = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return records;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                records.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error loading: " + e.getMessage());
        }
        return records;
    }

    public static void clearFile() {
        try (FileWriter fw = new FileWriter(FILE_PATH, false)) {
            fw.write("");
        } catch (IOException e) {
            System.out.println("Error clearing file: " + e.getMessage());
        }
    }
}
