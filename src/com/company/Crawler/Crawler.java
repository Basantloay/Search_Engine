package com.company.Crawler;

import com.company.Indexer.Website;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.net.*;
import java.nio.file.FileSystems;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.*;
import java.util.regex.*;

@SuppressWarnings("ALL")
public class Crawler implements Runnable{
    public String name="SENinja" ;
    private static int max = 5000;
    public static int crawlerNumber = 5;
    private Queue<String> seedSetVisited;
    private Vector<Integer> removedItems;
    private Queue<String> seedSet;
    private int ID;
    AtomicInteger crawlerCount = new AtomicInteger();
    private Date time;
    //Map<String, Vector<String>>
    Vector<String> disallowed;

    Database database;

    public Crawler(int id, Queue<String> seedSet, LinkedList<String> seedSetVisited, Vector<String> disallowed, Database database) {
        this.ID = id;
        this.seedSet = seedSet;
        this.seedSetVisited = seedSetVisited;
        this.disallowed = disallowed;
        this.database=database;
        this.time=new Date();

    }

    public boolean robots(String args, Integer num) throws IOException, URISyntaxException {
        boolean cont=false;boolean find=false;
        if (args=="")
            return false;

        URL w = new URL(args + "/robots.txt");
        if (w.getHost() == null)
            return false;
         w.toURI();
        try(BufferedReader in = new BufferedReader(new InputStreamReader(w.openStream()))) {
            String line = null;
            int index;
            String website="";
            while((line = in.readLine()) != null) {
                //System.out.println(line);
                if (line.contains("<!DOCTYPE html>")) {
                   // System.out.println("\nl2naha");
                    return false;
                }
                else {
                    if (line.contains("User-agent")) {
                        if (line.contains(name))
                            cont = true;
                        if (line.contains("*")) {
                            cont = true;
                        }
                        find = true;
                    } else if (find && cont) {
                        if (line.contains("Disallow")) {
                            if(line.length()<10)
                                return false;
                            website = args + (line.substring(10) );
                            website=website.replace("*",".*");
                            website=website.replace("?","\\?");
                            website=website.replace("+","\\+");
                            website=website.replace(".","\\.");
                                    //System.out.println(args + (line.substring(10,index)+line.substring(index+1)));
                                    synchronized (disallowed) {
                                        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                                        LocalDateTime time2 = LocalDateTime.now();
                                        //System.out.println(website);
                                        database.AddDisallowed(website,dtf2.format(time2));
                                        disallowed.add(website);
                                    }
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




    Boolean flag =true;
    public void parse(String args) throws IOException {

        int j=0;
        try {

            File file = new File(args);
            Scanner scannedFile = new Scanner(file);
            if ((Thread.currentThread().getName()) == "1") {
                while (scannedFile.hasNextLine()) {
                    String URL = scannedFile.nextLine();
                    synchronized (seedSet) {
                        if (!seedSet.contains(URL))
                        {
                            DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                            LocalDateTime time2 = LocalDateTime.now();
                            database.AddWebsite(URL,dtf2.format(time2));
                            seedSet.add(URL);
                        }
                        j++;
                    }
                }
                synchronized (flag) {
                    flag = false;
                }
            } else
                while (flag) {
                    //System.out.println(Thread.currentThread().getName());
                    }
            scannedFile.close();

        }catch(FileNotFoundException e){
            System.out.println("Error in file");
        }
        Integer i = 0;
        HttpURLConnection con;
        while (!seedSet.isEmpty() && crawlerCount.intValue() <= max) {
            try {

                System.out.println(Thread.currentThread().getName());
                String website = "";
                synchronized (seedSet) {
                    if (!seedSet.isEmpty()) {
                        website = seedSet.remove();
                    } else {
                        System.out.println("Empty");
                        return;
                    }

                }

                int timeout;


                if (website != null && website.length() != 0) {

                            robots(website, i);
                            Document doc = Jsoup.connect(website).userAgent("Mozilla/5.0 (Windows NT 6.0) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.121 Safari/535.2").followRedirects(true).method(Connection.Method.GET).timeout(1200000).ignoreHttpErrors(true).get();
                            String str2=doc.toString();
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                            LocalDateTime time = LocalDateTime.now();
                            database.Update(website,str2,dtf.format(time));
                            crawlerCount.incrementAndGet();
                            Elements links = doc.select("a[href]");

                            i++;
                            boolean flag1, flag2;
                            for (Element link : links) {
                                String str = link.attr("abs:href");
                                synchronized (seedSetVisited) {
                                    flag1 = seedSetVisited.contains(str);
                                }
                                synchronized (seedSet) {
                                    flag2 = seedSet.contains(str);
                                }
                                int iter = 0;
                                boolean flag3 = false;
                                synchronized (disallowed) {
                                    while (iter < disallowed.size()) {
                                        if (Pattern.matches(disallowed.get(iter), str)) {
                                            flag3 = true;
                                            break;
                                        }
                                        iter++;
                                    }
                                }
                                if (!flag1 && !flag2 && !flag3) {
                                    synchronized (seedSet) {
                                        seedSet.add(str);
                                        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                                        LocalDateTime time2 = LocalDateTime.now();
                                        database.AddHyberlinks(str,dtf2.format(time2));
                                    }
                                }
                            }
                            synchronized (seedSetVisited) {
                                seedSetVisited.add(website);
                            }
                            synchronized (seedSet) {
                                seedSet.remove(website);
                            }

                        }
            }
                catch(FileNotFoundException e){
                //e.printStackTrace();
                System.out.println("File is not found");
                System.out.println(crawlerCount);
            } catch(IOException e){
                //e.printStackTrace();
                System.out.println("Invalid");
                System.out.println(crawlerCount);
            }
        catch(IllegalArgumentException | URISyntaxException x)
            {
                //x.printStackTrace();
                System.out.println("Inavalid URL syntax");
                System.out.println(crawlerCount);
            }
        }
    }




    @Override
    public void run()  {
        try {
            parse("websites.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
