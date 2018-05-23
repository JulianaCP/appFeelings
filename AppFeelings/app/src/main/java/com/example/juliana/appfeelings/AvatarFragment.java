package com.example.juliana.appfeelings;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.juliana.appfeelings.Clases.Consejo;
import com.example.juliana.appfeelings.Clases.Constantes;
import com.example.juliana.appfeelings.Clases.Contacto;
import com.example.juliana.appfeelings.Clases.HistorialEmociones;
import com.example.juliana.appfeelings.Clases.Persona;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;


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
    ArrayList<String> copia;
    private static final int REQUEST_CODE = 1234;
    private String sharedPreferences_nombre_usuario;
    private int estadoPriorizadoDia, estadoPriorizadoMes, estadoPriorizadoAño;
    private String fechaActual;
    String emocionSharePreference;
    public String contact;
    public boolean encontrado;

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

        copia = new ArrayList<String>();
        copia.add("Consejo1");
        copia.add("Consejo2");
        copia.add("Consejo3");

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

    public void obtenerContacto(final String frase) {


        try{


            encontrado= false;
            sharedPreferences_nombre_usuario = sharedPreferences.getString("nombre_usuario","");
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("Contacto");
            String[] parts = frase.split(" ");
            final String part2 = parts[2];
            System.out.println("part "+ part2);


            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        System.out.println("POST "+postSnapshot);
                        System.out.println("POST "+sharedPreferences_nombre_usuario);
                        if (postSnapshot.getKey().equals(sharedPreferences_nombre_usuario)) {
                            Contacto user = postSnapshot.child(part2).getValue(Contacto.class);
                            if (user != null) {
                                contact = user.getTelefono();
                                System.out.println("telefono: " + contact);
                                encontrado = true;
                                realizarLlamada(frase, contact);
                            } else {
                                System.out.println("Error");
                            }
                        }
                    }

                    if (encontrado == false){
                        Toast.makeText(context,"No se encontro el contacto",Toast.LENGTH_LONG).show();
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
        }catch (Exception e){
            Toast. makeText ( getActivity() , "Error de conexion", Toast . LENGTH_SHORT ) . show () ;

        }
    }


    public void obtenerConsejo(final String frase) {


        try{

            emocionSharePreference = sharedPreferences.getString("emocion", "");
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("Consejo");
            Random rand = new Random();
            int x = rand.nextInt(copia.size());
            final String elemento= copia.get(x);

            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        System.out.println("POST "+postSnapshot);
                        System.out.println("POST "+emocionSharePreference);
                        if (postSnapshot.getKey().equals(emocionSharePreference)){
                            System.out.println("Elemento "+ elemento);
                            Consejo consejoBase = postSnapshot.child(elemento).getValue(Consejo.class);

                            if (consejoBase != null) {
                                System.out.println(consejoBase.getTexto());
                                contact = consejoBase.getTexto();
                                alerta(contact);

                            } else {
                                System.out.println("Error");
                            }
                        }
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
        }catch (Exception e){
            Toast. makeText ( getActivity() , "Error de conexion", Toast . LENGTH_SHORT ) . show () ;

        }



    }


    public void alerta(String frase){
        new AlertDialog.Builder(getActivity())
                .setMessage(frase)
                .setTitle("Consejo")
                .setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        startActivity(intent);
                        System.out.println("cerrar sesion");
                    }
                })
                .create().show();
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
            obtenerContacto(frase);
        }

        int obtConsejo = cadena.indexOf("consejo");
        if(obtConsejo != -1) {
            System.out.println("IDENTIFICA CONSEJO");
            obtenerConsejo(frase);
            //alerta(contact);
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

    public void realizarLlamada(String frase, String numContacto){
        String s4 = "tel: "+ numContacto;
        System.out.println("Probando " + s4);
        try{
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(s4));
            startActivity(intent);
        }catch (Exception e){
            Toast. makeText ( getActivity() , "Error al realizar la llamada", Toast . LENGTH_SHORT ) . show () ;

        }


    }

    public void actualizarSharePreferences(String emocion){
       // sharedPreferences =  getActivity().getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("emocion",emocion);
        editor.apply();
    }

    public void guardarDatoFireBase(String emocion){

        try{

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



        }catch (Exception e){
            Toast. makeText ( getActivity() , "Error de conexion, algunos datos se pueden haber perdido", Toast . LENGTH_SHORT ) . show () ;

        }



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
