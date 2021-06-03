package com.company.Crawler;

import com.company.Website;
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
    private Date recrawlTime;
    int turn;
    boolean recrawl;
    //Map<String, Vector<String>>
    Vector<String> disallowed;
    Vector<String> allowed;

    public Crawler(int id, Queue<String> seedSet, LinkedList<String> seedSetVisited, Vector<String> disallowed, Vector<String> allowed) {
        this.ID = id;
        this.seedSet = seedSet;
        this.seedSetVisited = seedSetVisited;
        this.disallowed = disallowed;
        this.allowed = allowed;
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
            String website=args;
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
                            website += (line.substring(10) );
                            website=website.replace("*",".*");
                            website=website.replace("?","\\?");
                            website=website.replace("+","\\+");
                            website=website.replaceAll(".","\\.");
                                    //System.out.println(args + (line.substring(10,index)+line.substring(index+1)));
                                    synchronized (disallowed) {

                                        disallowed.add(website );
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

        FileWriter f1=new FileWriter("Test.txt");
        int j=0;

        try {
            //System.out.println("Hello");
            File file = new File(args);
            Scanner scannedFile = new Scanner(file);
            if ((Thread.currentThread().getName()) == "1") {
                while (scannedFile.hasNextLine()) {
                    String URL = scannedFile.nextLine();
                    synchronized (seedSet) {
                        if (!seedSet.contains(URL))
                            seedSet.add(URL);
                        j++;
                    }
                }
                synchronized (flag) {
                    flag = false;
                }
            } else
                while (flag) {
                    System.out.println(Thread.currentThread().getName());
                }
            scannedFile.close();

        }catch(FileNotFoundException e){
            System.out.println("Error in file");
        }
        Integer i = 0;
        HttpURLConnection con;
        while (!seedSet.isEmpty() && crawlerCount.intValue() <= max) {
            try {
                crawlerCount.incrementAndGet();
                System.out.println(Thread.currentThread().getName());
                //System.out.println(crawlerCount);
                String website = "";
                synchronized (seedSet) {
                    if (!seedSet.isEmpty()) {
                        website = seedSet.remove();
                    } else
                        return;

                }


                if (website != null || website.length() != 0)
                    robots(website, i);
                int timeout;
                //final Connection.Response postResponse = Jsoup.connect(website).execute();
                //if (Jsoup.connect(website).execute()!=null) {
                //System.out.println("a");

                if (website != null && website.length() != 0) {
                    URL w = new URL(website);
                     con = (HttpURLConnection) w.openConnection();

                    try {
                        if (w.getHost() != null) {
                        con.getResponseCode();

                            Document doc = Jsoup.connect(website).userAgent("Mozilla/5.0 (Windows NT 6.0) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.121 Safari/535.2").followRedirects(true).method(Connection.Method.GET).timeout(1200000).ignoreHttpErrors(true).get();
                            //Document doc = Jsoup.parseBodyFragment(website);
                            f1.write(website + '\n');
                            //System.out.println(doc);
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

                    }catch (UnknownHostException e) {
                        System.out.println("Unknown Host ");
                        con.disconnect();
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
        f1.close();
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
