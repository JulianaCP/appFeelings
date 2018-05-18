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
import android.widget.ListView;

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
    public static ArrayList<Test> listItems = new ArrayList<>();

    public PruebasFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_pruebas, container, false);

        //Instancia a la base de datos
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        //apuntamos al nodo que queremos leer
        DatabaseReference myRef = fdb.getReference("Test");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                //leeremos un objeto de tipo Estudiante
                GenericTypeIndicator<Test> t = new GenericTypeIndicator<Test>() {};
                Test estudiante = dataSnapshot.getValue(t);

                //formamos el resultado en un string
                String resultado = "Como objeto java:\n\n";
                resultado += estudiante + "\n";
                resultado += "Propiedad Estudiante:\nNombre completo: " +estudiante.getTestName();

                //mostramos en el textview
                //textview.setText(resultado);

                listItems.add(estudiante);
            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.e("ERROR FIREBASE",error.getMessage());
            }

        });

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
