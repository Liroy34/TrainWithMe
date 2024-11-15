package com.example.tpfinal.Entidades;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;

public class ConfiguracionEjercicio implements Parcelable {

    private int id;
    private String ejercicio;
    private int series;
    private int repeticiones;

    public ConfiguracionEjercicio() {
    }

    public ConfiguracionEjercicio( String ejercicio, int series, int repeticiones) {

        this.ejercicio = ejercicio;
        this.series = series;
        this.repeticiones = repeticiones;

    }

    protected ConfiguracionEjercicio(Parcel in) {
        id = in.readInt();
        series = in.readInt();
        repeticiones = in.readInt();

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

    public String getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(String ejercicio) {
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



    @Override
    public String toString() {
        return "ConfiguracionEjercicio{" +
                "id='" + id + '\'' +
                ", ejercicio=" + ejercicio +
                ", series=" + series +
                ", repeticiones=" + repeticiones +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfiguracionEjercicio that = (ConfiguracionEjercicio) o;
        return series == that.series && repeticiones == that.repeticiones && Objects.equals(id, that.id) && Objects.equals(ejercicio, that.ejercicio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ejercicio, series, repeticiones);
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

    }
}
