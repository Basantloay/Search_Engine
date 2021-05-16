package com.company.Crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.nio.file.FileSystems;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.File;
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.*;

public class Crawler {
    private static int max = 5000;
    public static int crawlerNumber = 5;
    private Set<String> seedSetVisited;
    private Vector<Integer> removedItems;
    private LinkedList<String> seedSet;
    private int ID;
    AtomicInteger crawlerCount = new AtomicInteger();
    private Date recrawlTime;
    int turn;
    boolean recrawl;
    //Map<String, Vector<String>>
    Vector<String> disallowed;
    Vector<String> allowed;

    public Crawler(int id, LinkedList<String> seedSet, Set<String> seedSetVisited, Vector<String> disallowed, Vector<String> allowed) {
        this.ID = id;
        this.seedSet = seedSet;
        this.seedSetVisited = seedSetVisited;
        this.disallowed = disallowed;
        this.allowed = allowed;
    }

    public boolean robots(String args, Integer num) throws IOException {

        URL w = new URL(args + "/robots.txt");
        //Document doc;
        try {
            ReadableByteChannel rbc = Channels.newChannel(w.openStream());
            FileOutputStream robots = new FileOutputStream("robots" + num.toString() + ".txt");
            robots.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            int j = 0;
            try {
                Scanner scannedFile = new Scanner((Readable) robots);
                while (scannedFile.hasNextLine()) {
                    String line = scannedFile.nextLine();
                    System.out.println("\n" + line);
                    if (line.contains("<!DOCTYPE html>")) {
                        System.out.println("\nl2naha");
                        robots.close();
                        new File("robots" + num.toString() + ".txt").delete();
                        return false;
                    }

                }
                return true;
            } catch (ClassCastException a) {
                return false;

            }
        }
        catch (IOException x) {
                return false;
            }




    }





    public void parse(String args)
    {
        int j=0;
        try {
            File file = new File(args);
            Scanner scannedFile = new Scanner(file);
            while (scannedFile.hasNextLine()) {
                String URL = scannedFile.nextLine();
                seedSet.add(URL);
                j++;
            }
            scannedFile.close();
            Integer i=0;
                while (!seedSet.isEmpty() || crawlerCount.intValue()<max) {
                    crawlerCount.incrementAndGet();
                    String website=seedSet.get(i);
                    robots(website, i);
                    Document doc = Jsoup.connect(website).get();

                    Elements links = doc.select("a[href]");

                    i++;
                    boolean flag1, flag2;
                    for (Element link : links) {
                        String str = link.attr("abs:href");
                        synchronized (seedSet) {
                            flag1 = seedSetVisited.contains(str);
                        }
                        synchronized (seedSetVisited) {
                            flag2 = seedSet.contains(str);
                        }
                        if (!flag1 && !flag2) {
                            synchronized (seedSet) {
                                seedSet.add(str);
                            }
                        }
                        //System.out.println("\nlink:" + str);


                    }
                    synchronized (seedSetVisited) {
                        seedSetVisited.add(website);
                    }
                    synchronized (seedSet) {
                        seedSet.remove(website);
                    }

                }
        } catch (FileNotFoundException | MalformedURLException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
