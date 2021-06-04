package com.company.Crawler;
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
import java.net.UnknownHostException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static java.util.concurrent.TimeUnit.SECONDS;


public class Database {
    DB crawlerDatabase;
    MongoClient mongoClient;
    DBCollection websites ;
    DBCollection disallowedWebsite;
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
        //websites.createIndex("HTMLDocuments");
        websites.createIndex("Time");
        disallowedWebsite=crawlerDatabase.getCollection("DisallowedWebsites");
        //hyberlinks = crawlerDatabase.getCollection("hyberlinks");
        //hyberlinks.createIndex("URL");
        //hyberlinks.createIndex("refTo");
        //htmlDocuments = crawlerDatabase.getCollection("HTMLDocuments");
        //time = crawlerDatabase.getCollection("time");


    }

    public void AddVisited(String website, String time){

        BasicDBObject row = new BasicDBObject("URL", website)
                    .append("crawled", 1)
                    .append("indexed", 0)
                    .append("rank",(double) 0.0)
                    .append("Time",time);

            websites.insert(row);

    }
    public void AddHyberlinks(String website, String time){

        BasicDBObject row = new BasicDBObject("URL", website)
                .append("crawled", 0)
                .append("indexed", 0)
                .append("rank",(double) 0.0)
                .append("Time",time);

        websites.insert(row);

    }
}
