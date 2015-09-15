package com.example.administrator.newsexplorer.model;

/**
 * Created by Administrator on 9/9/2015.
 */
public class NewsItem {
    public String name,id,isVisible,datTime;
    public NewsItem( String name,String id,String isVisible,String datTime){
        this.name = name;
        this.id = id;
        this.isVisible =isVisible;
        this.datTime = datTime;
    }
}
