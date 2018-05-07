package com.example.juliana.appfeelings.Clases;

/**
 * Created by Juliana on 06/05/2018.
 */

public class Persona {
    private String nombre_usuario;
    private String fecha_nacimimento;
    private String genero;

    public Persona(String nombre_usuario, String fecha_nacimimento, String genero) {
        this.nombre_usuario = nombre_usuario;
        this.fecha_nacimimento = fecha_nacimimento;
        this.genero = genero;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getFecha_nacimimento() {
        return fecha_nacimimento;
    }

    public void setFecha_nacimimento(String fecha_nacimimento) {
        this.fecha_nacimimento = fecha_nacimimento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}
