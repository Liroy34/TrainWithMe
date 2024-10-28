package com.example.tpfinal.Entidades;

import java.util.Objects;

public class ConfiguracionEjercicio {

    private String id;
    private Ejercicio ejercicio;
    private int series;
    private int repeticiones;
    private String tiempo;

    public ConfiguracionEjercicio(String id, Ejercicio ejercicio, int series, int repeticiones, String tiempo) {
        this.id = id;
        this.ejercicio = ejercicio;
        this.series = series;
        this.repeticiones = repeticiones;
        this.tiempo = tiempo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Ejercicio getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Ejercicio ejercicio) {
        this.ejercicio = ejercicio;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public int getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(int repeticiones) {
        this.repeticiones = repeticiones;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    @Override
    public String toString() {
        return "ConfiguracionEjercicio{" +
                "id='" + id + '\'' +
                ", ejercicio=" + ejercicio +
                ", series=" + series +
                ", repeticiones=" + repeticiones +
                ", tiempo='" + tiempo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfiguracionEjercicio that = (ConfiguracionEjercicio) o;
        return series == that.series && repeticiones == that.repeticiones && Objects.equals(id, that.id) && Objects.equals(ejercicio, that.ejercicio) && Objects.equals(tiempo, that.tiempo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ejercicio, series, repeticiones, tiempo);
    }
}
