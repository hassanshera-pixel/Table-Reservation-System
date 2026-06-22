package com.mycompany.restauranteservation;

import java.io.*;
import java.util.ArrayList;

public class FileHandler {

    private static final String FILE_NAME = "reservations.txt";

    public static void saveReservation(String data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(data);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("File saving error: " + e.getMessage());
        }
    }

    public static ArrayList<String> loadReservations() {
        ArrayList<String> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String record;

            while ((record = br.readLine()) != null) {
                list.add(record);
            }

        } catch (IOException e) {
            System.out.println("No saved records found.");
        }

        return list;
    }

    public static void clearFile() {
        try (PrintWriter pw = new PrintWriter(FILE_NAME)) {
            pw.print("");
        } catch (IOException e) {
            System.out.println("File clearing error: " + e.getMessage());
        }
    }
}
