package com.company;
import java.io.File;

public class Website {
    private String URL;
    private String compactDescription;
    private File robots;
    private float TF;
    private float IDF;
    private float TF_IDF;
    private WebsiteType type;

    public Website(String URL)
    {
        this.URL=URL;
        robots=new File("robots.txt");
    }

    public void setURL(String URL)
    {
        this.URL=URL;
    }

    public String getURL() {
        return URL;
    }

    public void setCompactDescription(String compactDescription) {
        this.compactDescription = compactDescription;
    }

    public String getCompactDescription() {
        return compactDescription;
    }

    public void setRobots(File robots) {
        this.robots = robots;
    }

    public File getRobots() {
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
