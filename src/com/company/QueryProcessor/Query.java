package com.company.QueryProcessor;
import opennlp.tools.stemmer.*;
import com.company.Crawler.Database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Query {
    String Entered;
    public Vector<Vector<String>> proccessorQuery() throws IOException {
        Database data=new Database();
        //String Entered="circular";

        Vector<Vector<String>> returnedList = new Vector<Vector<String>> ();
        //String temp=s.stem(Entered);
        //System.out.println(Entered);
        //System.out.println(temp);
        List<String> TotResults=new ArrayList<String>();//=new Vector<List<String>>();
        boolean isFound=data.SearchQuery(Entered,TotResults);
        System.out.println(isFound);


        if(isFound) {
            Vector<String> v = new Vector<String>();

            for (int i = 0; i < TotResults.size(); i++) {
                v.add(TotResults.get(i).replace("[", ""));
            }

            for (int i = 0; i < v.size(); i++) {
                // add in vector the urls
                Vector<String> searchList = new Vector<String>();

                Document doc = Jsoup.connect(v.get(i)).userAgent("Mozilla/5.0 (Windows NT 6.0) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.121 Safari/535.2").followRedirects(true).method(Connection.Method.GET).timeout(1200000).ignoreHttpErrors(true).get();
                String title = doc.title();
                // add in vector the titles

                Element paragraph = doc.select("p").first();
                String firstParagraph = "";
                if (paragraph != null) {
                    searchList.add(v.get(i));
                    searchList.add(title);
                    firstParagraph = paragraph.text();
                    // add in vector the paragraphs
                    searchList.add(firstParagraph);
                }
                returnedList.addElement(searchList);
            }
        }
        return returnedList;
    }
    public void  SetSearched (String simpleword)
    {
        Entered = simpleword;
        //System.out.println(Entered.replace("=",""));
    }
    public String getSearched()
    {
        return Entered;
    }

    public static void main(String[] args) throws IOException {
        Query q=new Query();
        q.proccessorQuery();
    }

}