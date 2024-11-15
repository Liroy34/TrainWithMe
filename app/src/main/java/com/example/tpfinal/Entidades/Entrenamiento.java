package com.example.tpfinal.Entidades;

import java.util.List;
import java.util.Objects;

public class Entrenamiento {

    private int id;
    private String idUsuario;
    private String duracion;
    private String fecha;
    private List<ConfiguracionEjercicio> configuracionesEjercicio; // Lista de configuraciones de ejercicios

    public Entrenamiento(int id, String idUsuario, String duracion, String fecha, List<ConfiguracionEjercicio> configuracionesEjercicio) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.duracion = duracion;
        this.fecha = fecha;
        this.configuracionesEjercicio = configuracionesEjercicio;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public List<ConfiguracionEjercicio> getConfiguracionesEjercicio() {
        return configuracionesEjercicio;
    }

    public void setConfiguracionesEjercicio(List<ConfiguracionEjercicio> configuracionesEjercicio) {
        this.configuracionesEjercicio = configuracionesEjercicio;
    }

    @Override
    public String toString() {
        return "Entrenamiento{" +
                "id='" + id + '\'' +
                ", idUsuario='" + idUsuario + '\'' +
                ", duracion='" + duracion + '\'' +
                ", fecha='" + fecha + '\'' +
                ", configuracionesEjercicio=" + configuracionesEjercicio +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entrenamiento that = (Entrenamiento) o;
        return id == that.id &&
                Objects.equals(idUsuario, that.idUsuario) &&
                Objects.equals(duracion, that.duracion) &&
                Objects.equals(fecha, that.fecha) &&
                Objects.equals(configuracionesEjercicio, that.configuracionesEjercicio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idUsuario, duracion, fecha, configuracionesEjercicio);
    }
}
