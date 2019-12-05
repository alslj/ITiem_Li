package com.example.li_itime;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Mytime_FileResoure {

    Context context;
    ArrayList<Mytime> mytimes = new ArrayList<Mytime>();

    public Mytime_FileResoure(Context context){
        this.context = context;
    }

    public void Mytime_save (){
        try {
            ObjectOutputStream out = new ObjectOutputStream(context.openFileOutput("Mytime.txt", Context.MODE_PRIVATE));
            out.writeObject(mytimes);
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Mytime> getMytimes() {
        return mytimes;
    }
    //没有指定储存的文件格式
    public ArrayList<Mytime> Mytime_load(){
        try {
            ObjectInputStream in = new ObjectInputStream(context.openFileInput("Mytime.txt"));
            mytimes = (ArrayList<Mytime>) in.readObject();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mytimes;
    }

}
