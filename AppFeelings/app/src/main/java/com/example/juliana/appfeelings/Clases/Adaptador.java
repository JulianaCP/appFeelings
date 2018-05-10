package com.example.juliana.appfeelings.Clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.juliana.appfeelings.R;

public class Adaptador extends BaseAdapter{
    LayoutInflater layoutInflater;

    public Adaptador(Context context) {
        layoutInflater = layoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return Global.getListaLinks().size();
    }
    @Override
    public Object getItem(int i) {
        return Global.getListaLinks().get(i);
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = layoutInflater.inflate(R.layout.fragment_pruebas, null);
        }
        final int posicionListView = i;
        final TextView idTest = (TextView) view.findViewById(R.id.idTest);
        final TextView testName = (TextView) view.findViewById(R.id.testName);
        final TextView testLink = (TextView) view.findViewById(R.id.testLink);

        idTest.setText(Integer.toString(Global.getListaLinks().get(posicionListView).getIdTest()));
        testName.setText(Global.getListaLinks().get(posicionListView).getTestName());
        testLink.setText(Global.getListaLinks().get(posicionListView).getLink());

        //idTest.setText(Item.getIdTest());
        //testName.setText(Item.getTestName());
        //testLink.setText(Item.getLink());
        return view;
    }
}
