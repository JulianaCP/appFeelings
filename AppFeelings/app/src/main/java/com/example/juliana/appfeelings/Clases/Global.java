package com.example.juliana.appfeelings.Clases;

import java.util.ArrayList;

public class Global {

    public static ArrayList<Test> listItems = new ArrayList<>();

    public static ArrayList<Test> getListaLinks(){
        return Global.listItems;
    }
}
