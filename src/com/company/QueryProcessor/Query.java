package com.company.QueryProcessor;
import opennlp.tools.stemmer.*;
import com.company.Crawler.Database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Vector;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Query {

    public static void main(String[] args) throws IOException {
        Database data=new Database();
        String Entered="traveler";
        PorterStemmer s = new PorterStemmer();
        String temp=s.stem(Entered);
        Vector<List<String>> TotResults=new Vector<List<String>>();
        boolean isFound=data.SearchQuery(temp,TotResults);
        if(isFound){
            Vector<String> v=new Vector<String>();
            for(int i=0;i<TotResults.size();i++){
                v.add(TotResults.get(i).get(0));
            }

            for(int i=0;i<v.size();i++){
                Document doc = Jsoup.connect(v.get(i)).userAgent("Mozilla/5.0 (Windows NT 6.0) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.121 Safari/535.2").followRedirects(true).method(Connection.Method.GET).timeout(1200000).ignoreHttpErrors(true).get();
                String title=doc.title();
                Element paragraph = doc.select("p").first();
                String firstParagraph=paragraph.text();
                System.out.println(title);
                System.out.println(v.get(i));
                System.out.println(firstParagraph);
            }
        }
    }
}
