package com.example.juliana.appfeelings.Clases;

import java.util.ArrayList;

public class Global {

    public static ArrayList<Test> listItems = new ArrayList<>();

    public static ArrayList<Test> getListaLinks(){
        return Global.listItems;
    }




    public static ContactoRow contacto = new ContactoRow();

    public static ContactoRow getContacto(){
        return Global.contacto;
    }
    public static void setContacto(ContactoRow contactoParam){
        Global.contacto = contactoParam;
    }

}
