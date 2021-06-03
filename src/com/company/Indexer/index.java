package com.company.Indexer;
import com.company.Crawler.Database;
import org.jsoup.Jsoup;

import javax.swing.text.Document;
import javax.swing.text.Element;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


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

public class index implements Runnable {
    LinkedList <Website> websiteList;
    Database db;
    List<HTMLElement> HTMLElements;
    index(LinkedList<Website> websiteList,Database datab){
        this.websiteList = websiteList;
        this.db = datab;
        this.HTMLElements = new LinkedList<HTMLElement>();
    }
    public void indexing(Website web){
        Document htmldoc = web.getHtml();
        if(htmldoc==null) {
            return;
        }
        String Url = HTMLElements.get(0).getElement();
        String Title = HTMLElements.get(1).getElement();
        String Headers = HTMLElements.get(2).getElement();
        try {
            List<String> Keywords = new LinkedList<>();
            for(int i = 3 ; i< HTMLElements.size() ; i++)
            {
                synchronized (db) {
                    //
                }
                String fullText = HTMLElements.get(i).getElement();
                fullText = fullText.replaceAll("-+", "-0");
                fullText = fullText.replaceAll("[\\p{Punct}&&[^'-]]+", " ");
                fullText = fullText.replaceAll("(?:'(?:[tdsm]|[vr]e|ll))+\\b", "");

                StandardTokenizer stdToken = new StandardTokenizer();
                stdToken.setReader(new StringReader(fullText));
                TokenStream tokenStream = new StopFilter(new ASCIIFoldingFilter(new ClassicFilter(new LowerCaseFilter(stdToken))), EnglishAnalyzer.getDefaultStopSet());
                tokenStream.reset();
                CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);

                //Stem

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }






    }

    void HTMLElement_Init(List<HTMLElement> HTMLElement , Website web) throws IOException
    {
        org.jsoup.nodes.Document ParsedHTML = null;
        try {
            ParsedHTML = Jsoup.connect(String.valueOf(web.getUrl())).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HTMLElement.add(new HTMLElement(String.valueOf(web.getUrl()), "url"));
        HTMLElement.add(new HTMLElement(ParsedHTML.title(), "title"));


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
        HTMLElement.add(new HTMLElement(Headers, "header"));

        for (org.jsoup.nodes.Element header : ParsedHTML.getElementsByTag("p")) {
            HTMLElement.add(new HTMLElement(header.text(), "paragraph"));
        }
        for (org.jsoup.nodes.Element header : ParsedHTML.getElementsByTag("b")) {
            HTMLElement.add(new HTMLElement(header.text(), "bold"));
        }
        for (org.jsoup.nodes.Element header : ParsedHTML.getElementsByTag("i")) {
            HTMLElement.add(new HTMLElement(header.text(), "italic"));
        }
        for (org.jsoup.nodes.Element header : ParsedHTML.getElementsByTag("small")) {
            HTMLElement.add(new HTMLElement(header.text(), "small"));
        }
        for (org.jsoup.nodes.Element header : ParsedHTML.getElementsByTag("ol")) {
            HTMLElement.add(new HTMLElement(header.text(), "ordered_list"));
        }
        for (org.jsoup.nodes.Element header : ParsedHTML.getElementsByTag("ul")) {
            HTMLElement.add(new HTMLElement(header.text(), "unordered_list"));
        }
        for (org.jsoup.nodes.Element header : ParsedHTML.getElementsByTag("td")) {
            HTMLElement.add(new HTMLElement(header.text(), "table_column"));
        }
        for (org.jsoup.nodes.Element header : ParsedHTML.getElementsByTag("tr")) {
            HTMLElement.add(new HTMLElement(header.text(), "table_row"));
        }
    }

    @Override
    public void run(){

    }
}

