package com.company.QueryProcessor;
import opennlp.tools.stemmer.*;
import com.company.Crawler.Database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Query {

    public void proccessorQuery(String Entered) throws IOException {
        Database data=new Database();
        //String Entered="circular";
        PorterStemmer s = new PorterStemmer();
        String temp=s.stem(Entered);
        System.out.println(temp);
        List<String> TotResults=new ArrayList<String>();//=new Vector<List<String>>();
        boolean isFound=data.SearchQuery(temp,TotResults);
        System.out.println(isFound);
        //System.out.println(TotResults.toCharArray());
        if(isFound){
            Vector<String> v=new Vector<String>();
            for(int i=0;i<TotResults.size();i++){
                v.add(TotResults.get(i).replace("[",""));
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

    public static void main(String[] args) throws IOException {
        Query q=new Query();
        q.proccessorQuery("circular");
    }

}
