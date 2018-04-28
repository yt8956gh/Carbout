package com.nian.carbout.news;

public class news_item {
    private String title;
    private String text;
    private int imageID;

    public news_item(String title, String text,int imageID)
    {
        this.title = title;
        this.text = text;
        this.imageID = imageID;
    }

    public String getTitle(){ return title;}
    public String getText(){return text;}
    public int getImageID(){return imageID;}
}
