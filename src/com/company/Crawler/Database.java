package com.company.Crawler;
import com.company.Indexer.Website;
import com.company.Indexer.Word;
import org.bson.Document;
import org.bson.types.ObjectId;
import com.mongodb.*;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ParallelScanOptions;
import com.mongodb.ServerAddress;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import javax.swing.*;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.*;

import static java.util.concurrent.TimeUnit.SECONDS;


public class Database {
    DB crawlerDatabase;
    MongoClient mongoClient;
    DBCollection websites;
    DBCollection disallowedWebsite;
    DBCollection IndexerCollection;
    //DBCollection hyberlinks;
    //DBCollection htmlDocuments;
    //DBCollection time;
    public Database() {
        this.mongoClient = new MongoClient("localhost", 27017);
        //create database
        this.crawlerDatabase = mongoClient.getDB("CrawlerDatabase");
        //create collections and fields
        websites = crawlerDatabase.getCollection("websites");
        websites.createIndex("URL");
        websites.createIndex("crawled");
        websites.createIndex("indexed");
        websites.createIndex("rank");
        //websites.createIndex("hyberlinks");
        websites.createIndex("HTMLDocuments");
        websites.createIndex("Time");
        websites.createIndex("Title");
        websites.createIndex("Headers");
        websites.createIndex("paragraph");
        websites.createIndex("bold");
        websites.createIndex("italic");
        websites.createIndex("small");
        websites.createIndex("ordered_list");
        websites.createIndex("unordered_list");

        disallowedWebsite=crawlerDatabase.getCollection("DisallowedWebsites");
        disallowedWebsite.createIndex("URL");
        disallowedWebsite.createIndex("crawled");
        disallowedWebsite.createIndex("indexed");
        disallowedWebsite.createIndex("rank");
        //disallowedWebsite.createIndex("hyberlinks");
        //disallowedWebsite.createIndex("HTMLDocuments");
        disallowedWebsite.createIndex("Time");
        //hyberlinks = crawlerDatabase.getCollection("hyberlinks");
        //hyberlinks.createIndex("URL");
        //hyberlinks.createIndex("refTo");
        //htmlDocuments = crawlerDatabase.getCollection("HTMLDocuments");
        //time = crawlerDatabase.getCollection("time");

        IndexerCollection = crawlerDatabase.getCollection("IndexerCollection");
        IndexerCollection.createIndex("Value");
        IndexerCollection.createIndex("DF");
        IndexerCollection.createIndex("ListOfDocuments");
        IndexerCollection.createIndex("Time");
    }

    public void AddWebsite(String website, String time){
        DBObject SearchQ = new BasicDBObject("URL", website);

        if(websites.find(SearchQ).count() == 0)
        {
        BasicDBObject row = new BasicDBObject("URL", website)
                    .append("crawled", 0)
                    .append("indexed", 0)
                    .append("rank",(double) 0.0)
                    .append("Time",time);

            websites.insert(row);
        }

    }
    public void AddHyberlinks(String website, String time){
        DBObject SearchQ = new BasicDBObject("URL", website);

        if(websites.find(SearchQ).count() == 0){
        BasicDBObject row = new BasicDBObject("URL", website)
                .append("crawled", 0)
                .append("indexed", 0)
                .append("rank",(double) 0.0)
                .append("Time",time);

        websites.insert(row);
        }

    }

    public void AddDisallowed(String website1, String time){
        DBObject SearchQ = new BasicDBObject("URL", website1);

        if(websites.find(SearchQ).count() == 0)
        {
            BasicDBObject row = new BasicDBObject("URL", website1)
                .append("crawled", 0)
                .append("indexed", 0)
                .append("rank",(double) 0.0)
                .append("Time",time);

            disallowedWebsite.insert(row);
        }

    }

    public void AddIndexed(Word word, String time)
    {
        DBObject SearchQ = new BasicDBObject("Value", word.getValue());

        if(IndexerCollection.find(SearchQ).count() == 0)
        {
            BasicDBObject row = new BasicDBObject("Value", word.getValue())
                    .append("DF", word.getDF())
                    .append("ListOfDocuments",word.getListOfDocuments())
                    .append("Time",time);

            IndexerCollection.insert(row);
        }
    }

    public void Update(String str,String document, String time)
    {
        DBObject SearchQ = new BasicDBObject("URL", str);
        DBObject ObjectQ = new BasicDBObject("crawled", 1)
                .append("Time",time).append("HTMLDocuments",document);
        DBObject UpdateQ = new BasicDBObject("$set",ObjectQ);
        if(websites.find(SearchQ).count() != 0)
            websites.update(SearchQ, UpdateQ);

    }

    public void UpdateIndex(String str, String time)
    {
        DBObject SearchQ = new BasicDBObject("URL", str);
        DBObject ObjectQ = new BasicDBObject("indexed", 1)
                .append("Time",time);
        DBObject UpdateQ = new BasicDBObject("$set",ObjectQ);
        if(websites.find(SearchQ).count() != 0)
            websites.update(SearchQ, UpdateQ);

    }

    public void getCrawled(LinkedList<Website> Visited) throws MalformedURLException, FileNotFoundException {
        DBCursor cur =  websites.find(new BasicDBObject("crawled", 1).append("indexed",0));
        int size = cur.size();
        for(int i = 0 ;i< size;i++) {
            DBObject doc = cur.next();
            String URL = (String) doc.get("URL");
            Website w = new Website(URL, i);
            w.setHtml((String)doc.get("HTMLDocuments"));
            Visited.add(w);
        }
    }
    public boolean SearchQuery(String str,List<String> URLList) {
        DBCursor cur = IndexerCollection.find(new BasicDBObject("Value", str));
        int size = cur.size();
        if (size == 0) {
            return false;
        }
        else{
            DBObject doc = cur.next();
            String[] TotResults =  doc.get("ListOfDocuments").toString().split(",");
            //System.out.println(TotResults);

            for(int i=0;i<TotResults.length;i++)
            {
                if(TotResults[i].contains("["))
                    URLList.add(TotResults[i]);
            }

        }
        return true;

    }
}

