package com.example.juliana.appfeelings;


import android.content.Context;
import android.content.Intent;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactosFragment extends Fragment {
    private FragmentManager manager;
    private View rootView;
    EditText nombre, numeroTelefono;
    Button addContact;
    public ContactosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView =  inflater.inflate(R.layout.fragment_contactos, container, false);
        ImageView maps = (ImageView) rootView.findViewById(R.id.imageViewContact);
        nombre = (EditText) rootView.findViewById(R.id.contactName);
        numeroTelefono= (EditText) rootView.findViewById(R.id.contactPhone);
        addContact= (Button)rootView.findViewById(R.id.addButton);


        addContact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (nombre.getText().toString().trim().equalsIgnoreCase(""))
                    Toast. makeText ( getActivity() , "The name is empty", Toast . LENGTH_SHORT ) . show () ;
                if (numeroTelefono.getText().toString().trim().equalsIgnoreCase(""))
                    Toast. makeText ( getActivity() , "The number phone is empty", Toast . LENGTH_SHORT ) . show () ;
                else{
                    Toast. makeText ( getActivity() , "The contact was added successfully", Toast . LENGTH_SHORT ) . show () ;

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });

        //contenindo fragment
        return rootView;
    }

    public void callFragment(Fragment fragment){
        manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.ContentForFragments,fragment).addToBackStack("tag").commit();
    }
    public void finish(){
        getFragmentManager().popBackStack();
    }

}
