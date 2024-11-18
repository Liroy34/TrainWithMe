package com.example.tpfinal.Entidades;

import java.util.Objects;

public class Rutina {

    private int id;
    private String nombre;
    private String descripcion;
    private String tipo;
    private int frecuencia;

    public Rutina(){

    }

    public Rutina(int id, String nombre, String descripcion, String tipo, int frecuencia) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.frecuencia = frecuencia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(int frecuencia) {
        this.frecuencia = frecuencia;
    }

    @Override
    public String toString() {
        return "Rutina{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", tipo='" + tipo + '\'' +
                ", frecuencia='" + frecuencia + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rutina rutina = (Rutina) o;
        return id == rutina.id && Objects.equals(nombre, rutina.nombre) && Objects.equals(descripcion, rutina.descripcion) && Objects.equals(tipo, rutina.tipo) && Objects.equals(frecuencia, rutina.frecuencia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, descripcion, tipo, frecuencia);
    }
}
