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
import com.example.juliana.appfeelings.Clases.ContactoRow;
import com.example.juliana.appfeelings.Clases.Global;
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
public class editarContactosFragment extends Fragment {
    private FragmentManager manager;
    private View rootView;

    EditText editarNombre, editarNumeroTelefono;
    Button editarContact;
    private Contacto contacto;
    FirebaseDatabase database;
    DatabaseReference myRef;

    private SharedPreferences sharedPreferences;
    private String sharedPreferences_nombre_usuario;
    Context context;
    ContactoRow contactoGlobal;
    public editarContactosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView =  inflater.inflate(R.layout.fragment_editar_contactos, container, false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageViewContactEditar);
        editarNombre = (EditText) rootView.findViewById(R.id.contactNameEditar);
        editarNumeroTelefono= (EditText) rootView.findViewById(R.id.contactPhoneEditar);
        editarContact= (Button)rootView.findViewById(R.id.buttonEditar);
        context = getActivity().getApplicationContext();
        sharedPreferences =  context.getSharedPreferences("preferences", Context.MODE_PRIVATE);

        contactoGlobal = Global.getContacto();

        editarNombre.setText(contactoGlobal.getNombre());
        editarNumeroTelefono.setText(contactoGlobal.getTelefono());

        editarContact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (editarNombre.getText().toString().trim().equalsIgnoreCase(""))
                    Toast. makeText ( getActivity() , "The name is empty", Toast . LENGTH_SHORT ) . show () ;
                if (editarNumeroTelefono.getText().toString().trim().equalsIgnoreCase(""))
                    Toast. makeText ( getActivity() , "The number phone is empty", Toast . LENGTH_SHORT ) . show () ;
                else{
                    Toast. makeText ( getActivity() , "The contact was added successfully", Toast . LENGTH_SHORT ) . show () ;

                    llenarDatosFireBase(editarNombre.getText().toString(), editarNumeroTelefono.getText().toString());
                    finish();
                }
            }
        });
        //contenindo fragment
        return rootView;
    }

    public void llenarDatosFireBase(final String nombre, final String numero){ //FALTA IMPLEMENTAR
        sharedPreferences_nombre_usuario = sharedPreferences.getString("nombre_usuario","");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Contacto");

        contacto = new Contacto();
        contacto.setNombre(nombre);
        contacto.setTelefono(numero);

        System.out.println("contactoGlobal.getClave(): "  +contactoGlobal.getClave());
        myRef.child(sharedPreferences_nombre_usuario).child(contactoGlobal.getClave()).setValue(contacto);

    }


    public void callFragment(Fragment fragment){
        manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.ContentForFragments,fragment).addToBackStack("tag").commit();
    }
    public void finish(){
        getFragmentManager().popBackStack();
    }
}

