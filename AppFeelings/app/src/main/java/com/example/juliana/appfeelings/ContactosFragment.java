package com.example.juliana.appfeelings;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.app.AlertDialog;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juliana.appfeelings.Clases.Contacto;
import com.example.juliana.appfeelings.Clases.ContactoRow;
import com.example.juliana.appfeelings.Clases.Global;
import com.example.juliana.appfeelings.Clases.HistorialEmociones;
import com.example.juliana.appfeelings.Clases.Persona;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactosFragment extends Fragment {
    private FragmentManager manager;
    private View rootView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Context context;

    private SharedPreferences sharedPreferences;
    private String sharedPreferences_nombre_usuario;
    ListView listView;
    createContactFragment createContactFragment;

    ArrayList<ContactoRow> listaContactos = new ArrayList<ContactoRow>();

    editarContactosFragment editarContactosFragment;

    public ContactosFragment() {
        // Required empty public constructor
    }

    Button button_crear;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView =  inflater.inflate(R.layout.fragment_contactos, container, false);
        listView = (ListView)rootView.findViewById(R.id.listView_contactos);

        button_crear = (Button)rootView.findViewById(R.id.contactos_Crear);

        button_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createContactFragment = new createContactFragment();
                callFragment(createContactFragment);
            }
        });


        context = getActivity().getApplicationContext();
        sharedPreferences =  context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        obtenerContactos();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Global.setContacto(listaContactos.get(i));
                editarContactosFragment = new editarContactosFragment();
                callFragment(editarContactosFragment);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        obtenerContactos();
        super.onResume();
    }

    public void obtenerContactos() {
        sharedPreferences_nombre_usuario = sharedPreferences.getString("nombre_usuario","");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Contacto");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaContactos.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    System.out.println("postSnapshot.getKey() " + postSnapshot.getKey());
                    //if (postSnapshot.getKey().equals("Jane")) {
                    if (postSnapshot.getKey().equals(sharedPreferences_nombre_usuario)) {
                        for (DataSnapshot contactoRow : postSnapshot.getChildren()) {
                            Contacto contacto = contactoRow.getValue(Contacto.class);
                            ContactoRow contactoRowElement = new ContactoRow();
                            contactoRowElement.setNombre(contacto.getNombre());
                            contactoRowElement.setTelefono(contacto.getTelefono());
                            contactoRowElement.setClave(contactoRow.getKey());
                            System.out.println("contactoRow.getKey(): " + contactoRow.getKey());
                            listaContactos.add(contactoRowElement);
                        }
                    }
                    llenarListView();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Nada", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        myRef.addValueEventListener(postListener);
    }
    public void llenarListView(){
        System.out.println("Len lista: " + listaContactos.size());
        listView.setAdapter(new viewAdapter(getActivity().getApplicationContext()));
    }


    public class viewAdapter extends BaseAdapter {
        LayoutInflater layoutInflater;
        public viewAdapter(Context context){
            layoutInflater = layoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return listaContactos.size();
        }

        @Override
        public Object getItem(int i) {
            return listaContactos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view==null){
                view = layoutInflater.inflate(R.layout.contacts_row,null);
            }
            final int posicionListView = i;
            final TextView tituloNombre = (TextView)view.findViewById(R.id.TEXT_contactos_Nombre_Titulo);
            final TextView tituloNumero = (TextView)view.findViewById(R.id.TEXT_contactos_Numero_Titulo);

            final TextView valorNombre = (TextView)view.findViewById(R.id.TEXT_contactos_Nombre_Valor);
            final TextView valorNumero = (TextView)view.findViewById(R.id.TEXT_contactos_Numero_Valor);

            valorNombre.setText(listaContactos.get(posicionListView).getNombre());
            valorNumero.setText(listaContactos.get(posicionListView).getTelefono());


            tituloNombre.setTextColor(getResources().getColor(R.color.colorNegro));
            tituloNumero.setTextColor(getResources().getColor(R.color.colorNegro));
            valorNombre.setTextColor(getResources().getColor(R.color.colorNegro));
            valorNumero.setTextColor(getResources().getColor(R.color.colorNegro));

            return view;
        }
    }

    public void callFragment(Fragment fragment){
        manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.ContentForFragments,fragment).addToBackStack("tag").commit();
    }
    public void finish(){
        getFragmentManager().popBackStack();
    }

}
