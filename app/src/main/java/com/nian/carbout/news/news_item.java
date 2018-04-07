package com.nian.carbout.news;

public class news_item {
    private String title;
    private String text;

    public news_item(String title, String text)
    {
        this.title = title;
        this.text = text;
    }

    public String getTitle(){ return title;}
    public String getText(){return text;}
}
