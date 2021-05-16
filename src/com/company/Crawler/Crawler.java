package com.company.Crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
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
        boolean cont=false;boolean find=false;
        URL w = new URL(args + "/robots.txt");
        try(BufferedReader in = new BufferedReader(new InputStreamReader(w.openStream()))) {
            String line = null;
            while((line = in.readLine()) != null) {
                System.out.println(line);
                if (line.contains("<!DOCTYPE html>")) {
                    System.out.println("\nl2naha");
                    return false;
                }
                else
                {
                    if(line.contains("User-agent"))
                    {
                        if (line.contains("*")) {
                            cont = true;
                        }
                        find=true;
                    }
                    else if(find && cont)
                    {
                        if(line.contains("Disallow"))
                        {
                            if(line.contains("*"))
                            {
                                
                            }
                                else{
                            //10 b3d disallow
                            disallowed.add(args + (line.substring(10)));
                            System.out.println(args + (line.substring(10)));
                        }
                        }
                        else if(line.contains("Allow"))
                        {
                            //7 b3d allow
                            allowed.add(args+(line.substring(7)));
                            System.out.println(args+(line.substring(7)));
                        }
                    }
                }
            }
            return true;

        } catch (FileNotFoundException e ) {
           return false;
        }catch( IOException a)
        {
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
