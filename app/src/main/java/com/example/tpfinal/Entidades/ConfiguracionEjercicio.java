package com.example.tpfinal.Entidades;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;

public class ConfiguracionEjercicio implements Parcelable {

    private int id;
    private Ejercicio ejercicio;
    private int series;
    private int repeticiones;
    private String tiempo;

    public ConfiguracionEjercicio() {
    }

    public ConfiguracionEjercicio(int id, Ejercicio ejercicio, int series, int repeticiones, String tiempo) {
        this.id = id;
        this.ejercicio = ejercicio;
        this.series = series;
        this.repeticiones = repeticiones;
        this.tiempo = tiempo;
    }

    protected ConfiguracionEjercicio(Parcel in) {
        id = in.readInt();
        series = in.readInt();
        repeticiones = in.readInt();
        tiempo = in.readString();
    }

    public static final Creator<ConfiguracionEjercicio> CREATOR = new Creator<ConfiguracionEjercicio>() {
        @Override
        public ConfiguracionEjercicio createFromParcel(Parcel in) {
            return new ConfiguracionEjercicio(in);
        }

        @Override
        public ConfiguracionEjercicio[] newArray(int size) {
            return new ConfiguracionEjercicio[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(series);
        dest.writeInt(repeticiones);
        dest.writeString(tiempo);
    }
}
