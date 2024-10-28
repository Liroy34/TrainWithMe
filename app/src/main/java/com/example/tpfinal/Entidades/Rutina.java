package com.example.tpfinal.Entidades;

import java.util.Objects;

public class Rutina {

    private String id;
    private String nombre;
    private String descripcion;
    private String tipo;

    public Rutina(String id, String nombre, String descripcion, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    @Override
    public String toString() {
        return "Rutina{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rutina rutina = (Rutina) o;
        return Objects.equals(id, rutina.id) && Objects.equals(nombre, rutina.nombre) && Objects.equals(descripcion, rutina.descripcion) && Objects.equals(tipo, rutina.tipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, descripcion, tipo);
    }
}
