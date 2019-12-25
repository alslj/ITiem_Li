package com.example.li_itime;

import java.io.Serializable;

public class Mytime implements Serializable {
    private int coverResoureID;
    private String title;
    private String deadline;
    private byte[] bitmap;
    private int judg = 0;
    private String beizhu = "";

    public String getTitle(){
        return title;
    }

    public String getData(){
        return deadline;
    }

    public int getCoverResoureID(){
        return coverResoureID;
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

    public void setBitmap(byte[] bytes){
         bitmap = (byte[])bytes.clone();
         judg = 1;
    }

    public int getJudg(){
        return judg;
    }

    public byte[] getBitmap(){
        return  bitmap;
    }

    public void setBeizhu(String s){
        this.beizhu = s;

    }

    public String getBeizhu(){
        return beizhu;
    }


    public Mytime(String title, String deadline, int coverResourceId) {
        this.setTitle(title);
        this.setData(deadline);
        this.setCoverResoureID(coverResourceId);
    }

    public Mytime(String title, String deadline, byte[] bytes){
        this.setTitle(title);
        this.setData(deadline);
        this.setBitmap(bytes);
    }

    public Mytime(String title, String deadline, int coverResourceId, String beizhu) {
        this.setTitle(title);
        this.setData(deadline);
        this.setCoverResoureID(coverResourceId);
        this.setBeizhu(beizhu);
    }

    public Mytime(String title, String deadline, byte[] bytes, String beizhu){
        this.setTitle(title);
        this.setData(deadline);
        this.setBitmap(bytes);
        this.setBeizhu(beizhu);
    }




}
