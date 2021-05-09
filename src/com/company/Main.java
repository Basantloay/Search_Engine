package com.company;
import java.io.File;
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Queue;
import java.util.LinkedList;
import java.util.Scanner;
public class Main {

    public static void main(String[] args) {
        Queue<Website> q= new LinkedList<>();
        try {
            File file = new File("websites.txt");
            Scanner scannedFile = new Scanner(file);
            while (scannedFile.hasNextLine()) {
                String URL = scannedFile.nextLine();
                q.add(new Website(URL));
                System.out.println(URL);
            }
            scannedFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
