package com.example.juliana.appfeelings.Clases;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.juliana.appfeelings.R;

import java.util.ArrayList;

public class Adaptador extends BaseAdapter{
    Activity context;
    LayoutInflater layoutInflater;
    ArrayList<Test> testArrayList = new ArrayList<>();

    public Adaptador(Activity context, ArrayList<Test> testArrayList) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.testArrayList = testArrayList;
        this.context = context;
    }
    @Override
    public int getCount() {
        return testArrayList.size();
    }
    @Override
    public Object getItem(int i) {
        return testArrayList.get(i);
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = layoutInflater.inflate(R.layout.item, null);
        }
        final int posicionListView = i;
        final TextView testName = (TextView) view.findViewById(R.id.testName);
        final TextView testLink = (TextView) view.findViewById(R.id.testLink);

        testName.setText(testArrayList.get(posicionListView).getTestName());
        testLink.setText(testArrayList.get(posicionListView).getLink());
        return view;
    }
}
