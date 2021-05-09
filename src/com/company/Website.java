package com.company;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.FileOutputStream;
import java.io.IOException;
public class Website {
    private static Integer ID=0;
    private URL url;
    private String compactDescription;
    private URL robotsURL;
    private FileOutputStream robots;
    private float TF;
    private float IDF;
    private float TF_IDF;
    private WebsiteType type;

    public Website(String str) throws MalformedURLException, FileNotFoundException {
        this.url=new URL(str);
        robotsURL=new URL(str+"/robots.txt");
        robots=new FileOutputStream("robots"+ID.toString()+".txt");
    }

    public void setURL(String str) throws MalformedURLException {
        this.url=new URL(str);
    }

    public URL getURL() {
        return url;
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
