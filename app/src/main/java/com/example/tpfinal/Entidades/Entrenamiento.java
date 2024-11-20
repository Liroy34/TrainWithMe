package com.example.tpfinal.Entidades;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class Entrenamiento implements Parcelable {

    private int id;
    private int idUsuario;
    private String duracion;
    private String fecha;
    private String nombre;
    private List<ConfiguracionEjercicio> configuracionesEjercicio; // Lista de configuraciones de ejercicios

    public Entrenamiento() {
    }

    public Entrenamiento(int idUsuario, String duracion, String fecha, String nombre, List<ConfiguracionEjercicio> configuracionesEjercicio) {
        this.idUsuario = idUsuario;
        this.duracion = duracion;
        this.fecha = fecha;
        this.nombre = nombre;
        this.configuracionesEjercicio = configuracionesEjercicio;
    }

    public Entrenamiento(int id, int idUsuario, String duracion, String fecha, String nombre, List<ConfiguracionEjercicio> configuracionesEjercicio) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.duracion = duracion;
        this.fecha = fecha;
        this.nombre = nombre;
        this.configuracionesEjercicio = configuracionesEjercicio;
    }

    protected Entrenamiento(Parcel in) {
        id = in.readInt();
        idUsuario = in.readInt();
        duracion = in.readString();
        fecha = in.readString();
        nombre = in.readString();
        configuracionesEjercicio = in.createTypedArrayList(ConfiguracionEjercicio.CREATOR);

    }

    public static final Creator<Entrenamiento> CREATOR = new Creator<Entrenamiento>() {
        @Override
        public Entrenamiento createFromParcel(Parcel in) {
            return new Entrenamiento(in);
        }

        @Override
        public Entrenamiento[] newArray(int size) {
            return new Entrenamiento[size];
        }
    };

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(idUsuario);
        dest.writeString(duracion);
        dest.writeString(fecha);
        dest.writeString(nombre);
        dest.writeTypedList(configuracionesEjercicio);

    }

}
