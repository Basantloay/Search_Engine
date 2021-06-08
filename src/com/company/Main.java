package com.company;
import com.company.Crawler.Crawler;
import com.company.Crawler.Database;
import com.company.Indexer.Website;
import com.company.Indexer.index;

import java.io.IOException;
import java.util.*;

import static java.lang.Thread.sleep;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        LinkedList<String>l=new LinkedList<>() ;
         Vector<String>disallowed=new  Vector<String>();
        Vector<String>combactDescription=new  Vector<String>();
        Vector<String>allowed=new  Vector<String>();
        Queue<String> seedSetVisited=new LinkedList<>();
        Database database=new Database();
        Crawler c=new Crawler(0,l, (LinkedList<String>) seedSetVisited,disallowed,database,combactDescription);
        Thread t1 = new Thread (c);
        Thread t2 = new Thread (c);
        t1.setName("1");
        t2.setName("2");
        Thread t3 = new Thread (c);
        Thread t4 = new Thread (c);
        t3.setName("3");
        t4.setName("4");
        Thread t5 = new Thread (c);
        t5.setName("5");
        LinkedList<Website>list=new LinkedList<>();


        try {
            t1.start();
            t2.start();
            t3.start();
            t4.start();
            t5.start();
        }
        catch(Exception ex)
        {

        }
        //database.getCrawled(list);
        //System.out.println(list.size());
        t1.join();t2.join();t3.join();t4.join();t5.join();
        sleep(1000);
        //Indexer
        index indx = new index(database);
        Thread i1 = new Thread (indx);
        Thread i2 = new Thread (indx);
        i1.setName("1");
        i2.setName("2");
        Thread i3 = new Thread (indx);
        Thread i4 = new Thread (indx);
        i3.setName("3");
        i4.setName("4");
        Thread i5 = new Thread (indx);
        i5.setName("5");
        try {
            i1.start(); i2.start();i3.start(); i4.start();i5.start();
        }
        catch(Exception ex)
        {}

    }
}
