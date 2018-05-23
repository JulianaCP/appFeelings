package com.example.juliana.appfeelings;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.app.AlertDialog;
import android.widget.Toast;

import com.example.juliana.appfeelings.Clases.Contacto;
import com.example.juliana.appfeelings.Clases.HistorialEmociones;
import com.example.juliana.appfeelings.Clases.Persona;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class createContactFragment extends Fragment {
    private FragmentManager manager;
    private View rootView;

    EditText nombre, numeroTelefono;
    Button addContact;
    private Contacto contacto;
    FirebaseDatabase database;
    DatabaseReference myRef;

    private SharedPreferences sharedPreferences;
    private String sharedPreferences_nombre_usuario;
    Context context;
    public createContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView =  inflater.inflate(R.layout.fragment_create_contact, container, false);
        ImageView maps = (ImageView) rootView.findViewById(R.id.imageViewContact);
        nombre = (EditText) rootView.findViewById(R.id.contactName);
        numeroTelefono= (EditText) rootView.findViewById(R.id.contactPhone);
        addContact= (Button)rootView.findViewById(R.id.addButton);
        context = getActivity().getApplicationContext();
        sharedPreferences =  context.getSharedPreferences("preferences", Context.MODE_PRIVATE);


        addContact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (nombre.getText().toString().trim().equalsIgnoreCase(""))
                    Toast. makeText ( getActivity() , "The name is empty", Toast . LENGTH_SHORT ) . show () ;
                if (numeroTelefono.getText().toString().trim().equalsIgnoreCase(""))
                    Toast. makeText ( getActivity() , "The number phone is empty", Toast . LENGTH_SHORT ) . show () ;
                else{
                    Toast. makeText ( getActivity() , "Se creo correctamente", Toast . LENGTH_SHORT ) . show () ;
                    // guardarDatoFireBase(nombre.getText().toString(), numeroTelefono.getText().toString());
                    llenarDatosFireBase(nombre.getText().toString(), numeroTelefono.getText().toString());
                    finish();
                }
            }
        });
        //contenindo fragment
        return rootView;
    }


    public void llenarDatosFireBase(final String nombre, final String numero){ //FALTA IMPLEMENTAR



        try{
            sharedPreferences_nombre_usuario = sharedPreferences.getString("nombre_usuario","");
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("Contacto");
            myRef.child(nombre)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot data) {
                            if(data.getValue() == null){
                                database = FirebaseDatabase.getInstance();
                                myRef = database.getReference("Contacto");

                                contacto = new Contacto();
                                contacto.setNombre(nombre);
                                contacto.setTelefono(numero);
                                System.out.println("Shared "+sharedPreferences_nombre_usuario);
                                myRef.child(String.valueOf(sharedPreferences_nombre_usuario)).child(String.valueOf(nombre)).setValue(contacto);
                            }
                            else{
                                Toast.makeText(getActivity(),"El usuario ya existe:",Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getActivity(), "Ocurrio un error", Toast.LENGTH_LONG).show();
                        }
                    });
        }catch (Exception e){
            Toast. makeText ( getActivity() , "Error de conexion", Toast . LENGTH_SHORT ) . show () ;

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
