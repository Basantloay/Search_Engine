package com.company.Crawler;
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

import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

import static java.util.concurrent.TimeUnit.SECONDS;


public class Database {
    MongoClient mongoClient;
    DB crawlerDB;
    public Database() throws UnknownHostException {
        this.mongoClient = new MongoClient("localhost", 27017);
        crawlerDB = mongoClient.getDB("CrawlerDB");
    }
}
