package com.company;
import com.company.Crawler.Crawler;
import com.company.Crawler.Database;
import com.company.Indexer.Website;

import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        LinkedList<String>l=new LinkedList<>() ;
         Vector<String>disallowed=new  Vector<String>();
        Vector<String>combactDescription=new  Vector<String>();
        //Map<String, Vector<String>> allowed=new HashMap<String, Vector<String>>();
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
    }
}
