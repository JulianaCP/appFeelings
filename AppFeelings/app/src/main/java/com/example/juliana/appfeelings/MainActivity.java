package com.example.juliana.appfeelings;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juliana.appfeelings.Clases.Persona;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private LinearLayout linearLayoutFecha;
    private TextView mainLabelFecha;
    private int estadoPriorizadoDia, estadoPriorizadoMes, estadoPriorizadoAño;
    private DatePickerDialog datePickerDialog;

    private EditText mainEditTextUsuario;
    private Button mainButton;
    private RadioButton mainRadioButtonFemenino, mainRadioButtonMasculino;
    private Calendar calendario;

    //datos
    private String mainValorFecha;
    private String mainValorUsername;
    private String mainValorGenero;


    private SharedPreferences sharedPreferences;
    private Persona persona;

    private String sharedPreferences_nombre_usuario;
    private String sharedPreferences_fecha_nacimiento;
    private String sharedPreferences_genero;
    private Intent intent;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences =  getSharedPreferences("preferences", Context.MODE_PRIVATE);
        verificar_sharedPrefrences();
        setContentView(R.layout.activity_main);

        mainLabelFecha = (TextView)findViewById(R.id.mainLabelFecha) ;
        linearLayoutFecha  = (LinearLayout)findViewById(R.id.activityMainLinearLayoutFecha);
        mainEditTextUsuario = (EditText)findViewById(R.id.mainEditTextUsuario);
        mainButton = (Button)findViewById(R.id.mainButtonInisiar);
        mainRadioButtonFemenino = (RadioButton)findViewById(R.id.mainRadioButtonFemenino);
        mainRadioButtonMasculino = (RadioButton)findViewById(R.id.mainRadioButtonMasculino);


        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // --> obtener datos persona:

                //nombre usuario
                mainValorUsername = mainEditTextUsuario.getText().toString().trim();

                //genero
                if(mainRadioButtonFemenino.isChecked()){ mainValorGenero = "F"; }
                else{ mainValorGenero = "M"; }

                //fecha nacimiento
                mainValorFecha = mainLabelFecha.getText().toString();

                //verificar no faltan elementos
                if(!mainValorUsername.equals("")) {
                    persona = new Persona(mainValorUsername, mainValorFecha, mainValorGenero);
                    llenarDatosFireBase(); //FALTA -> insertar Persoma en firebase
                    llenarDatosSharePreferences(); //insertar datos de persona en shared preferences
                    callNavigation(); // abrir apllicacion avatar
                }
                else{
                    Toast.makeText(getApplicationContext(),"Error en los datos",Toast.LENGTH_LONG).show();
                }

            }
        });

        obtenerFecha();
        linearLayoutFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarCalendario();
            }
        });
    }
    public void llenarDatosFireBase(){ //FALTA IMPLEMENTAR

    }
    public void llenarDatosSharePreferences(){
        editor = sharedPreferences.edit();
        editor.putString("nombre_usuario",mainValorUsername);
        editor.putString("fecha_nacimiento",mainValorFecha);
        editor.putString("genero",mainValorGenero);
        editor.apply();
    }
    public void verificar_sharedPrefrences(){

        //obtiene datos almacenados en el sharedpreferens
        sharedPreferences_nombre_usuario = sharedPreferences.getString("nombre_usuario","");
        sharedPreferences_fecha_nacimiento = sharedPreferences.getString("fecha_nacimiento","");
        sharedPreferences_genero = sharedPreferences.getString("genero","");

        if(!sharedPreferences_nombre_usuario.equals("") && !sharedPreferences_fecha_nacimiento.equals("")
                && !sharedPreferences_genero.equals("")){
            callNavigation();
        }
    }
    public void callNavigation(){
        intent = new Intent(getApplicationContext(),NavigationDrawer.class);
        startActivity(intent);
    }
    public void mostrarCalendario(){
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mainLabelFecha.setText(dayOfMonth+"-"+(month+1)+"-"+year);
            }
        },estadoPriorizadoAño,estadoPriorizadoMes,estadoPriorizadoDia);
        datePickerDialog.show();
    }
    public void obtenerFecha(){
        calendario = Calendar.getInstance();
        estadoPriorizadoAño = calendario.get(Calendar.YEAR);
        estadoPriorizadoMes = calendario.get(Calendar.MONTH);
        estadoPriorizadoDia = calendario.get(Calendar.DAY_OF_MONTH);
        mainLabelFecha.setText(estadoPriorizadoDia+"-"+(estadoPriorizadoMes+1)+"-"+estadoPriorizadoAño);
    }
}
