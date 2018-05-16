package com.example.juliana.appfeelings;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.juliana.appfeelings.Clases.Adaptador;
import com.example.juliana.appfeelings.Clases.Global;
import com.example.juliana.appfeelings.Clases.Test;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PruebasFragment extends Fragment {
    private FragmentManager manager;
    private View rootView;
    private ListView listViewLinks;
    private Adaptador adaptador;
    public static ArrayList<Test> listItems = new ArrayList<>();

    public PruebasFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_pruebas, container, false);


        Test test = new Test(1, "nombre", "link");
        listItems.add(test);

        listViewLinks = (ListView)rootView.findViewById(R.id.listViewLinks);
        listViewLinks.setAdapter(new Adaptador(getActivity(),listItems ));

        return rootView;
    }

    public ArrayList<Test> getListaLinks(){
        //ArrayList<Test> listItems = new ArrayList<>();
        Global.listItems.add(new Test(1, "Test Personalidad", "Link del test"));
        Global.listItems.add(new Test(2, "Test Personalidad", "Link del test"));
        Global.listItems.add(new Test(3, "Test Personalidad", "Link del test"));

        return Global.listItems;
    }

    public void callFragment(Fragment fragment){
        manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.ContentForFragments,fragment).addToBackStack("tag").commit();
    }
    public void finish(){
        getFragmentManager().popBackStack();
    }

}
