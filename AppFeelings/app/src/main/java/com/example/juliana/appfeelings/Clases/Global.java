package com.example.juliana.appfeelings.Clases;

import java.util.ArrayList;

public class Global {

    public static ArrayList<Test> listItems = new ArrayList<>();
    Test test = new Test(1, "nombre", "link");
    //listItems.add(test);


    public static ArrayList<Test> getListaLinks(){
        return Global.listItems;
    }
}
