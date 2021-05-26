package com.company;
import com.company.Crawler.Crawler;

import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        LinkedList<String>l=new LinkedList<>() ;
         Vector<String>disallowed=new  Vector<String>();
        //Map<String, Vector<String>> allowed=new HashMap<String, Vector<String>>();
        Vector<String>allowed=new  Vector<String>();
        Queue<String> seedSetVisited=new LinkedList<>();
        Crawler c=new Crawler(0,l, (LinkedList<String>) seedSetVisited,disallowed,allowed);
        c.parse("websites.txt");
    }
}
