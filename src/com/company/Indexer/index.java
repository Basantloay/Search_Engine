package com.company.Indexer;
import com.company.Crawler.Database;
import com.company.Indexer.Website;

import javax.swing.text.Document;
import javax.swing.text.html.HTML;
import java.util.LinkedList;

public class index implements Runnable {
    LinkedList <Website> websiteList;
    Database db;
    index(LinkedList<Website> websiteList,Database datab){
        this.websiteList=websiteList;
        this.db=datab;
    }
    public void indexing(Website web){
        Document htmldoc=web.getHtml();
        if(htmldoc==null) {
            return;
        }
        synchronized (db)
        {
            db.update();
        }


    }
}
