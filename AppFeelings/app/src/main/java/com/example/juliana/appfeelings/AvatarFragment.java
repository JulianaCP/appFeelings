package com.example.juliana.appfeelings;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AvatarFragment extends Fragment {

    //clases
    private FragmentManager manager;
    private View rootView;
    private SharedPreferences sharedPreferences;


    //componentes
    ImageButton avatarButtonMicrofono;
    ImageView avatarImageView;

    //strings - ints - arrays
    ArrayList<String> avatarResultadoMicrofono;
    ArrayList<String> matchesText;
    private static final int REQUEST_CODE = 1234;

    //constentes emociones drawable
    private static final int EMOCION_ENOJADO_IMAGEN = R.drawable.enojado;
    private static final int EMOCION_FELIZ_IMAGEN = R.drawable.feliz;
    private static final int EMOCION_SIN_ESTADO_IMAGEN = R.drawable.sin_emocion;

    private static final String EMOCION_ENOJADO_TITULO = "Enojado";
    private static final String EMOCION_FELIZ_TITULO = "Feliz";
    private static final String EMOCION_SIN_ESTADO_TITULO = "Sin_emocion";


    public AvatarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_avatar, container, false);
        avatarImageView = (ImageView) rootView.findViewById(R.id.avatarimageView);
        avatarButtonMicrofono = (ImageButton) rootView.findViewById(R.id.avatarButtonMicrofono);
        avatarButtonMicrofono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected()){
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    //   intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE , "es-ES");
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    //Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                    System.out.println("NO HAY CONEXION");
                }

            }
        });
        verificarSharedPreferences();


        return rootView;
    }

    public void verificarSharedPreferences() {
        sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String emocionSharePreference = sharedPreferences.getString("nombre_usuario", "");
        verificarAvatar(emocionSharePreference);
    }

    public void verificarAvatar(String emocionActual) {

        switch (emocionActual) {
            case EMOCION_FELIZ_TITULO:
                avatarImageView.setImageResource(EMOCION_FELIZ_IMAGEN);
                break;
            case EMOCION_ENOJADO_TITULO:
                avatarImageView.setImageResource(EMOCION_ENOJADO_IMAGEN);
                break;
            default:
                avatarImageView.setImageResource(EMOCION_SIN_ESTADO_IMAGEN);
                break;
        }
    }


    public void verificar(String frase) {
        System.out.println("palabra en microfono: " + frase);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            matchesText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS); //Get data of data
            String palabra = matchesText.get(0);
            System.out.println("Palabra: " + palabra);
            verificar(palabra);
        }
    }

    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        if (net!= null && net.isAvailable() && net.isConnected()){
            return true;
        }   else {
            return false;
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
