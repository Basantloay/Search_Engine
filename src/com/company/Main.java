package com.company;
import java.io.File;
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Scanner;
public class Main {

    public static void main(String[] args) {
        LinkedList<Website> q= new LinkedList<>();
        try {
            File file = new File("websites.txt");
            Scanner scannedFile = new Scanner(file);
            while (scannedFile.hasNextLine()) {
                String URL = scannedFile.nextLine();
                q.add(new Website(URL));
                //System.out.println(q.getLast().getURL());
            }
            scannedFile.close();
            for(int i=0;i<q.size();i++)
            {
                Website w=q.get(i);
                URL website = q.get(i).getRobotsURL();
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                File fos = new FileOutputStream("information.html");
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            }

        } catch (FileNotFoundException | MalformedURLException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
