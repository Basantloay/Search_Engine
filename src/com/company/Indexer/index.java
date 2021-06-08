package com.company.Indexer;
import com.company.Crawler.Database;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import javafx.util.Pair;
import org.jsoup.Jsoup;

import javax.swing.text.Document;
import javax.xml.crypto.Data;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


import org.apache.http.Header;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.StringReader;

import opennlp.tools.stemmer.*;
import org.jsoup.nodes.Element;

public class index implements Runnable {
    Database db;
    LinkedList <Website> websiteList;
    List<String> HTML_Parts;
    Map<String,Word> KeyWords = new HashMap<String, Word>();
    public index(Database datab){
        websiteList = new LinkedList<Website>();
        this.db = datab;
        this.HTML_Parts = new LinkedList<String>();
    }

    public void indexing(Website web){
        //org.jsoup.nodes.Document htmldoc = null;
        //try {
        //    htmldoc = Jsoup.connect(String.valueOf(web.getUrl())).get();
        //} catch (IOException e) {
        //    e.printStackTrace();

        //}
        org.jsoup.nodes.Document htmldoc =Jsoup.parse(web.getHtml());
        if(htmldoc==null) {
            return;
        }
        try {
            HTML_Parts_Init(HTML_Parts,web);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String Url = HTML_Parts.get(0);
        String Title = HTML_Parts.get(1);
        String Headers = HTML_Parts.get(2);
        try {
            //List<String> Keywords = new LinkedList<>();
            for(int i = 3 ; i< HTML_Parts.size() ; i++)
            {
                synchronized (db) {
                    //
                }
                String HTMLtext = HTML_Parts.get(i);
                //Remove Special Characters
                HTMLtext = HTMLtext.replaceAll("-+", "-0");
                HTMLtext = HTMLtext.replaceAll("[\\p{Punct}&&[^'-]]+", " ");
                HTMLtext = HTMLtext.replaceAll("(?:'(?:[tdsm]|[vr]e|ll))+\\b", "");

                StandardTokenizer stdToken = new StandardTokenizer();
                stdToken.setReader(new StringReader(HTMLtext));
                TokenStream tokenStream = new StopFilter(new ASCIIFoldingFilter(new ClassicFilter(new LowerCaseFilter(stdToken))), EnglishAnalyzer.getDefaultStopSet());    //Remove Stop Words
                tokenStream.reset();
                CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
                PorterStemmer s = new PorterStemmer();

                while (tokenStream.incrementToken()) {
                    //List<String> list1 = new ArrayList<String>();
                    //list1.add(web.getUrlString());
                    //list1.add("0");
                    String term = token.toString();
                    String stemmed = s.stem(term);
                    boolean inTitle = false;
                    boolean inHeader = false;

                    if (Headers.contains(term)) {
                        inHeader = true;
                    }
                    if (Title.contains(term)) {
                        inTitle = true;
                    }


                    //System.out.println(stemmed);
                    Word keyword;
                    synchronized (KeyWords) {
                        if (KeyWords.containsKey(stemmed)) {
                            keyword = KeyWords.get(stemmed);
                        } else {
                            keyword = new Word(stemmed);
                            KeyWords.put(stemmed, keyword);
                        }
                    }
                    if(keyword.getListOfDocuments().isEmpty())
                    {
                        List<String> newList = new ArrayList<String>();
                        newList.add(web.getUrlString());
                        int n = 0;
                        newList.add(String.valueOf(n));
                        if(inHeader)
                        {
                            newList.add("Header");  n++;
                        }
                        else if(inTitle)
                        {
                            newList.add("Title");  n++;
                        }
                        else
                        {
                            newList.add("Paragraph");  n++;
                        }

                        newList.set(1, String.valueOf(n));

                        keyword.insertList(newList);
                    }
                    else
                    {
                        Vector<List<String>> WordList = keyword.getListOfDocuments();
                        int index = -1;
                        for(int j = 0; j < WordList.size(); j++)
                        {
                            if(WordList.get(j).contains(web.getUrlString()))
                            {
                                index = j;
                                break;
                            }
                        }
                        if(index != -1)
                        {
                            int TF_new = Integer.parseInt(WordList.get(index).get(1)) + 1;
                            WordList.get(index).set(1,String.valueOf(TF_new));
                            WordList.get(index).add("Paragraph");
                        }
                        else
                        {
                            List<String> newList = new ArrayList<String>();
                            newList.add(web.getUrlString());
                            int n = 0;
                            newList.add(String.valueOf(n));
                            if(inHeader)
                            {
                                newList.add("Header");  n++;
                            }
                            else if(inTitle)
                            {
                                newList.add("Title");  n++;
                            }
                            else
                            {
                                newList.add("Paragraph");  n++;
                            }
                            newList.set(1, String.valueOf(n));

                            keyword.insertList(newList);
                        }
                    }
                }
            }
            DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime time2 = LocalDateTime.now();
            this.db.UpdateIndex(web.getUrlString(), dtf2.format(time2));



        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    public void UpdateDatabase()
    {
        for(Map.Entry<String,Word> entry : KeyWords.entrySet())
        {
            System.out.println("dbdb");
            DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime time2 = LocalDateTime.now();
            entry.getValue().UpdateDF();
            this.db.AddIndexed(entry.getValue(), dtf2.format(time2));
        }
    }
    public void HTML_Parts_Init(List<String> HTML , Website web) throws IOException
    {
        org.jsoup.nodes.Document ParsedHTML = null;

            ParsedHTML = Jsoup.parse(web.getHtml());//Jsoup.connect(String.valueOf(web.getUrl())).get();
        //URL
        HTML.add(new String(String.valueOf(web.getUrl())));
        //Title
        HTML.add(new String(ParsedHTML.title()));

        //Headers
        String Headers = "";
        for (org.jsoup.nodes.Element header : ParsedHTML.getElementsByTag("h1")) {
            Headers += header.text();
        }
        for (org.jsoup.nodes.Element header : ParsedHTML.getElementsByTag("h2")) {
            Headers += header.text();
        }
        for (org.jsoup.nodes.Element header : ParsedHTML.getElementsByTag("h3")) {
            Headers += header.text();
        }
        for (org.jsoup.nodes.Element header : ParsedHTML.getElementsByTag("h4")) {
            Headers += header.text();
        }
        for (org.jsoup.nodes.Element header : ParsedHTML.getElementsByTag("h5")) {
            Headers += header.text();
        }
        for (org.jsoup.nodes.Element header : ParsedHTML.getElementsByTag("h6")) {
            Headers += header.text();
        }
        for (org.jsoup.nodes.Element header : ParsedHTML.getElementsByTag("th")) {
            Headers += header.text();
        }
        HTML.add(new String(Headers));

        //Images
        String Images = "";
        // Extracting Images
        for (Element image : ParsedHTML.getElementsByTag("img")) {
            Images += image.attr("alt");;
        }
        HTML.add(new String(Images));

        for (org.jsoup.nodes.Element header : ParsedHTML.getElementsByTag("p")) {
            HTML.add(new String(header.text()));     //Add Paragraph
        }
        for (org.jsoup.nodes.Element header : ParsedHTML.getElementsByTag("b")) {
            HTML.add(new String(header.text()));     //Add Bold
        }
        for (org.jsoup.nodes.Element header : ParsedHTML.getElementsByTag("i")) {
            HTML.add(new String(header.text()));     //Add Italic
        }
        for (org.jsoup.nodes.Element header : ParsedHTML.getElementsByTag("small")) {
            HTML.add(new String(header.text()));     //Add Small
        }
        for (org.jsoup.nodes.Element header : ParsedHTML.getElementsByTag("ol")) {
            HTML.add(new String(header.text()));     //Add ordered_list
        }
        for (org.jsoup.nodes.Element header : ParsedHTML.getElementsByTag("ul")) {
            HTML.add(new String(header.text()));     //Add unordered_list
        }
        for (org.jsoup.nodes.Element header : ParsedHTML.getElementsByTag("td")) {
            HTML.add(new String(header.text()));     //Add table_column
        }
        for (org.jsoup.nodes.Element header : ParsedHTML.getElementsByTag("tr")) {
            HTML.add(new String(header.text()));     //Add table_row
        }
    }
    public void StartIndexing(LinkedList <Website> websiteList)
    {
        for(int i = 0; i < websiteList.size(); i++)
        {
            this.indexing(websiteList.get(i));
        }
    }

    Boolean flag = true;
    @Override
    public void run() {
        try {
            if ((Thread.currentThread().getName()) == "1") {


                this.db.getCrawled(this.websiteList);
                //System.out.println(Thread.currentThread().getName() + "eee");
                synchronized (flag) {
                    flag = false;
                }
            }
            else
                while (flag) {
                    System.out.println(Thread.currentThread().getName() + "eee");
                }
            }
            catch(MalformedURLException e){
                e.printStackTrace();
            } catch(FileNotFoundException e){
                e.printStackTrace();
            }



        Integer i = 0;
        Website web;
        synchronized (this.websiteList) {
            while (!(this.websiteList.isEmpty())) {
                System.out.println("aaaaa");
                if (!(this.websiteList.isEmpty())) {
                    web = websiteList.remove();

                    indexing(web);
                }


            }
        }




    }
    //For Testing
    public static void main(String[] args) throws MalformedURLException, FileNotFoundException {
        Database db = new Database();

        index indx = new index(db);
        Thread t1 = new Thread (indx);
        Thread t2 = new Thread (indx);
        t1.setName("1");
        t2.setName("2");
        Thread t3 = new Thread (indx);
        Thread t4 = new Thread (indx);
        t3.setName("3");
        t4.setName("4");
        Thread t5 = new Thread (indx);
        t5.setName("5");
        try {
            t1.start(); t2.start();t3.start(); t4.start();t5.start();
        }
        catch(Exception ex)
        {

        }
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        indx.UpdateDatabase();
    }


}

