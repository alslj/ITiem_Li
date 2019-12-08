package com.example.li_itime;

public class SetItem {
    private String biaoti;
    private String buchong;
    private int  Imageid;

    public String getBiaoti(){
        return this.biaoti;
    }

    public void  setBiaoti(String str){
        this.biaoti = str;
    }

    public String getBuchong(){
        return this.buchong;
    }

    public void setBuchong(String str){
        this.buchong = str;
    }

    public int getImageid(){
        return this.Imageid;
    }

    public void setImageid(int i){
        this.Imageid = i;
    }

    public SetItem(String biaoti, String buchong, int Imageid){
        this.biaoti = biaoti;
        this.buchong = buchong;
        this.Imageid = Imageid;
    }
}
