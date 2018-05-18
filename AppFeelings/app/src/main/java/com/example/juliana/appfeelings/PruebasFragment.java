package com.example.juliana.appfeelings;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.juliana.appfeelings.Clases.Adaptador;
import com.example.juliana.appfeelings.Clases.Global;
import com.example.juliana.appfeelings.Clases.Test;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PruebasFragment extends Fragment {
    private FragmentManager manager;
    private View rootView;
    private ListView listViewLinks;
    private Adaptador adaptador;
    public final static ArrayList<Test> listItems = new ArrayList<>();

    FirebaseDatabase database;
    DatabaseReference myRef;

    public PruebasFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_pruebas, container, false);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Test");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listItems.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    System.out.println("POST "+postSnapshot.getValue());
                    Test test = postSnapshot.getValue(Test.class);
                    listItems.add(test);
                }
                //adaptador.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Nada", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        myRef.addValueEventListener(postListener);

        cargarTest();

        return rootView;

    }

    private void cargarTest() {
        listViewLinks = (ListView)rootView.findViewById(R.id.listViewLinks);
        listViewLinks.setAdapter(new Adaptador(getActivity(),listItems ));


        listViewLinks.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Test testSelect = listItems.get(position);
                String url = testSelect.getLink();
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    public void callFragment(Fragment fragment){
        manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.ContentForFragments,fragment).addToBackStack("tag").commit();
    }
    public void finish(){
        getFragmentManager().popBackStack();
    }

}
