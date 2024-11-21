package com.example.tpfinal.Entidades;

import java.util.List;

public class RutinaCargaDatos {

    private String nombre;
    private String descripcion;
    private String frecuencia;
    private List<ConfiguracionEjercicio> ejercicios;

    public RutinaCargaDatos(){

    }

    public RutinaCargaDatos(String nombre, String descripcion, String frecuencia, List<ConfiguracionEjercicio> ejercicios) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.frecuencia = frecuencia;
        this.ejercicios = ejercicios;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public List<ConfiguracionEjercicio> getEjercicios() {
        return ejercicios;
    }

    public void setEjercicios(List<ConfiguracionEjercicio> ejercicios) {
        this.ejercicios = ejercicios;
    }
}
