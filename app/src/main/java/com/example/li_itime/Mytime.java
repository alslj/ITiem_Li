package com.example.li_itime;

import java.io.Serializable;

public class Mytime implements Serializable {
    private int coverResoureID;
    private String title;
    private String deadline;
    private String imagePath = "";

    public String getTitle(){
        return title;
    }

    public String getData(){
        return deadline;
    }

    public int getCoverResoureID(){
        return coverResoureID;
    }

    public String getImagePath(){
        return  this.imagePath;
    }


    public void setTitle(String str){
        this.title = str;
    }

    public void setData(String str){
        this.deadline = str;
    }

    public void setCoverResoureID(int i){
        this.coverResoureID = i;
    }

    public void setImagePath(String s){
        this.imagePath = s;
    }

    public Mytime(String title, String deadline, int coverResourceId) {
        this.setTitle(title);
        this.setData(deadline);
        this.setCoverResoureID(coverResourceId);
    }

    public Mytime(String title, String deadline, String imagePath){
        this.title = title;
        this.imagePath = imagePath;
        this.deadline = deadline;
    }

}
