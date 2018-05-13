package com.example.juliana.appfeelings.Clases;

/**
 * Created by Juliana on 13/05/2018.
 */

public class HistorialEmociones {
    private String nombre_usuario;
    private String fecha;
    private String emocion;

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEmocion() {
        return emocion;
    }

    public void setEmocion(String emocion) {
        this.emocion = emocion;
    }
}
