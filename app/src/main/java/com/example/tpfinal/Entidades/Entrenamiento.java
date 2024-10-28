package com.example.tpfinal.Entidades;

import java.util.Objects;

public class Entrenamiento {

    private String id;
    private String idUsuario;
    private String duracion;
    private String fecha;

    public Entrenamiento(String id, String idUsuario, String duracion, String fecha) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.duracion = duracion;
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Entrenamiento{" +
                "id='" + id + '\'' +
                ", idUsuario='" + idUsuario + '\'' +
                ", duracion='" + duracion + '\'' +
                ", fecha='" + fecha + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entrenamiento that = (Entrenamiento) o;
        return Objects.equals(id, that.id) && Objects.equals(idUsuario, that.idUsuario) && Objects.equals(duracion, that.duracion) && Objects.equals(fecha, that.fecha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idUsuario, duracion, fecha);
    }
}
