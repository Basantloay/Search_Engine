package com.company.Indexer;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import javax.swing.text.Document;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Vector;


public class Website {
    private Integer ID;
    private URL url;
    private String urlString;
    private String compactDescription;
    private URL robotsURL;
    private FileOutputStream robots;
    private float TF;
    private float IDF;
    private float TF_IDF;
    private WebsiteType type;
    private org.jsoup.nodes.Document html;
    private String header;
    private double rank;
    private String area;
    private Date date;


    public Website(String str,Integer ID) throws MalformedURLException, FileNotFoundException {
        this.urlString = str;
        this.url = new URL(str);
        this.ID = ID;
        robotsURL = new URL(str + "/robots.txt");
        robots = new FileOutputStream("robots" + this.ID.toString() + ".txt");
        if (str != null && str.length() != 0) {
            URL w = new URL(str);
            HttpURLConnection con;
            try {
                con = (HttpURLConnection) w.openConnection();
                if (w.getHost() != null) {
                    con.getResponseCode();

                    org.jsoup.nodes.Document doc = Jsoup.connect(str).userAgent("Mozilla/5.0 (Windows NT 6.0) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.121 Safari/535.2").followRedirects(true).method(Connection.Method.GET).timeout(1200000).ignoreHttpErrors(true).get();
                    this.html = doc;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    public URL getUrl() {
        return url;
    }

    public void setRobotsURL(URL robotsURL) {
        this.robotsURL = robotsURL;
    }

    public Date getDate() {
        return date;
    }

    public org.jsoup.nodes.Document getHtml() {
        return html;
    }

    public double getRank() {
        return rank;
    }

    public String getHeader() {
        return header;
    }

    public String getArea() {
        return area;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public void setHtml(org.jsoup.nodes.Document html) {
        this.html = html;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getID() {
        return ID;
    }

    public void setURL(String str) throws MalformedURLException {
        this.url=new URL(str);
    }

    public URL getURL() {
        return url;
    }

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public void setCompactDescription(String compactDescription) {
        this.compactDescription = compactDescription;
    }

    public String getCompactDescription() {
        return compactDescription;
    }

    public void setRobotsURL(String str) throws MalformedURLException {
        robotsURL=new URL(str);
    }

    public URL getRobotsURL() {
        return robotsURL;
    }

    public void setRobots(FileOutputStream robots) {
        this.robots = robots;
    }

    public FileOutputStream getRobots() {
        return robots;
    }

    public void setTF(float TF) {
        this.TF = TF;
    }

    public float getTF() {
        return TF;
    }

    public void setIDF(float IDF) {
        this.IDF = IDF;
    }

    public float getIDF() {
        return IDF;
    }

    public void setTF_IDF(float TF_IDF) {
        this.TF_IDF = TF_IDF;
    }

    public float getTF_IDF() {
        return TF_IDF;
    }

    public void setType(WebsiteType type) {
        this.type = type;
    }

    public WebsiteType getType() {
        return type;
    }
}
