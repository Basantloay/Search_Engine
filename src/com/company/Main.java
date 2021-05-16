package com.company;
import com.company.Crawler.Crawler;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        LinkedList<String>l=new LinkedList<>() ;
         Vector<String>disallowed=new  Vector<String>();
        //Map<String, Vector<String>> allowed=new HashMap<String, Vector<String>>();
        Vector<String>allowed=new  Vector<String>();
        Set<String> seedSetVisited=new HashSet<String>();
        Crawler c=new Crawler(0,l,seedSetVisited,disallowed,allowed);
        c.parse("websites.txt");
    }
}
