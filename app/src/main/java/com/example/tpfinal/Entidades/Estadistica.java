package com.example.tpfinal.Entidades;

public class Estadistica {

    private int cantidadUsuarios;
    private int cantidadEntrenamientos;
    private int cantidadRutinas;
    private int cantidadMinutosEntrenados;

    public Estadistica() {
    }

    public Estadistica(int cantidadUsuarios, int cantidadEntrenamientos, int cantidadRutinas, int cantidadMinutosEntrenados) {
        this.cantidadUsuarios = cantidadUsuarios;
        this.cantidadEntrenamientos = cantidadEntrenamientos;
        this.cantidadRutinas = cantidadRutinas;
        this.cantidadMinutosEntrenados = cantidadMinutosEntrenados;
    }

    public int getCantidadUsuarios() {
        return cantidadUsuarios;
    }

    public void setCantidadUsuarios(int cantidadUsuarios) {
        this.cantidadUsuarios = cantidadUsuarios;
    }

    public int getCantidadEntrenamientos() {
        return cantidadEntrenamientos;
    }

    public void setCantidadEntrenamientos(int cantidadEntrenamientos) {
        this.cantidadEntrenamientos = cantidadEntrenamientos;
    }

    public int getCantidadRutinas() {
        return cantidadRutinas;
    }

    public void setCantidadRutinas(int cantidadRutinas) {
        this.cantidadRutinas = cantidadRutinas;
    }

    public int getCantidadMinutosEntrenados() {
        return cantidadMinutosEntrenados;
    }

    public void setCantidadMinutosEntrenados(int cantidadMinutosEntrenados) {
        this.cantidadMinutosEntrenados = cantidadMinutosEntrenados;
    }
}
