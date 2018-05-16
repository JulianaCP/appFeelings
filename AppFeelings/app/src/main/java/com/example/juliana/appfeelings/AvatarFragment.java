package com.example.juliana.appfeelings;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.juliana.appfeelings.Clases.Constantes;
import com.example.juliana.appfeelings.Clases.HistorialEmociones;
import com.example.juliana.appfeelings.Clases.Persona;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class AvatarFragment extends Fragment {

    //clases
    private FragmentManager manager;
    private View rootView;
    private SharedPreferences sharedPreferences;
    private Calendar calendario;

    //componentes
    ImageButton avatarButtonMicrofono;
    ImageView avatarImageView;

    //strings - ints - arrays
    ArrayList<String> avatarResultadoMicrofono;
    ArrayList<String> matchesText;
    private static final int REQUEST_CODE = 1234;
    private String sharedPreferences_nombre_usuario;
    private int estadoPriorizadoDia, estadoPriorizadoMes, estadoPriorizadoAño;
    private String fechaActual;
    String emocionSharePreference;

    private HistorialEmociones historialEmociones;
    FirebaseDatabase database;
    DatabaseReference myRef;
    SharedPreferences.Editor editor;
    Context context;
    public AvatarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_avatar, container, false);
        context = getActivity().getApplicationContext();

        sharedPreferences =  context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        System.out.println("sharedPreferences ref: "+sharedPreferences);

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
        //sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        emocionSharePreference = sharedPreferences.getString("emocion", "");
        verificarAvatar(emocionSharePreference);
    }

    public void verificarAvatar(String emocionActual) {

        switch (emocionActual) {
            case Constantes.EMOCION_FELIZ_TITULO:
                avatarImageView.setImageResource(Constantes.EMOCION_FELIZ_IMAGEN);
                break;
            case Constantes.EMOCION_ENOJADO_TITULO:
                avatarImageView.setImageResource(Constantes.EMOCION_ENOJADO_IMAGEN);
                break;
            case Constantes.EMOCION_EMOCIONADO_TITULO:
                avatarImageView.setImageResource(Constantes.EMOCION_EMOCIONADO_IMAGEN);
                break;
            case Constantes.EMOCION_TRISTE_TITULO:
                avatarImageView.setImageResource(Constantes.EMOCION_TRISTE_IMAGEN);
                break;
            default:
                avatarImageView.setImageResource(Constantes.EMOCION_SIN_ESTADO_IMAGEN);
                break;
        }
    }


    public void verificar(String frase) {
        System.out.println("frase; " + frase);

        String cadena = frase;
        int resultado = cadena.indexOf("Llamar");
        if(resultado != -1) {
            System.out.println("IDENTIFICA");
            realizarLlamada(frase);
        }

        String emocion = "";
        switch (frase){
            case Constantes.EMOCION_FELIZ_RECONOCIMIENTO_VOZ:
                emocion = Constantes.EMOCION_FELIZ_TITULO;
                break;
            case Constantes.EMOCION_ENOJADO_RECONOCIMIENTO_VOZ:
                emocion = Constantes.EMOCION_ENOJADO_TITULO;
                break;
            case Constantes.EMOCION_TRISTE_RECONOCIMIENTO_VOZ:
                emocion = Constantes.EMOCION_TRISTE_TITULO;
                break;
            case Constantes.EMOCION_EMOCIONADO_RECONOCIMIENTO_VOZ:
                emocion = Constantes.EMOCION_EMOCIONADO_TITULO;
                break;
        }
        if(!emocion.equals("")){
            System.out.println("emocion escuchada: "+ emocion);
            verificarAvatar(emocion);
            actualizarSharePreferences(emocion);
            guardarDatoFireBase(emocion);
        }
        else{
            Toast.makeText(context,"No se reconoce el comando Comando",Toast.LENGTH_LONG).show();
        }

    }

    public void realizarLlamada(String frase){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:86593117"));
        startActivity(intent);
    }

    public void actualizarSharePreferences(String emocion){
       // sharedPreferences =  getActivity().getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("emocion",emocion);
        editor.apply();
    }

    public void guardarDatoFireBase(String emocion){
        System.out.println("emocion}: " + emocion);

        sharedPreferences_nombre_usuario = sharedPreferences.getString("nombre_usuario","");
        historialEmociones = new HistorialEmociones();
        historialEmociones.setEmocion(emocion);
        obtenerFecha();
        historialEmociones.setFecha(fechaActual);
        historialEmociones.setNombre_usuario(sharedPreferences_nombre_usuario);

        System.out.println("sharedPreferences_nombre_usuario " +sharedPreferences_nombre_usuario );
        System.out.println("emocion " +emocion );
        System.out.println("fechaActual " + fechaActual);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("HistorialEmociones");
        myRef.child(String.valueOf(sharedPreferences_nombre_usuario)).push().setValue(historialEmociones);
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

    public void obtenerFecha(){
        calendario = Calendar.getInstance();
        estadoPriorizadoAño = calendario.get(Calendar.YEAR);
        estadoPriorizadoMes = calendario.get(Calendar.MONTH);
        estadoPriorizadoDia = calendario.get(Calendar.DAY_OF_MONTH);
        fechaActual = estadoPriorizadoDia+"-"+(estadoPriorizadoMes+1)+"-"+estadoPriorizadoAño;
    }

    public void callFragment(Fragment fragment){
        manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.ContentForFragments,fragment).addToBackStack("tag").commit();
    }
    public void finish(){
        getFragmentManager().popBackStack();
    }

}
