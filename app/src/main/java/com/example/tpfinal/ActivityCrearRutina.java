package com.example.tpfinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinal.Conexion.ConexionRutinas;
import com.example.tpfinal.Conexion.ConexionUsuario;
import com.example.tpfinal.Entidades.ConfiguracionEjercicio;
import com.example.tpfinal.Entidades.RutinaCargaDatos;

import java.util.ArrayList;
import java.util.List;

public class ActivityCrearRutina extends AppCompatActivity {

    private EditText etNombre, etDescripcion, etFrecuencia;
    private List<EditText> etEjercicios, etSeries, etRepeticiones;
    private Button btnCrear;
    private ImageButton btnVolver;
    private ConexionRutinas conRutinas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_rutina);

        btnVolver = findViewById(R.id.btnVolverCrearRutina);
        btnCrear = findViewById(R.id.btnCrearRutina);

        setElementos();

        btnCrear.setOnClickListener(v -> {

            if(!checkValues()){
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            }
            else{

                // hacer logica de carga a base de datos
                RutinaCargaDatos rutina = crearRutina();
                conRutinas.insertRutina(rutina);
                finish();

            }


        });

        btnVolver.setOnClickListener(v -> {

            finish();

        });

    }

    private void setElementos() {

        etNombre = findViewById(R.id.etNombreRutinaCrear);
        etDescripcion = findViewById(R.id.etDescripcionRutinaCrear);
        etFrecuencia = findViewById(R.id.etFrecuenciaCrear);

        int[] ejerciciosIds = {R.id.txtEj1, R.id.txtEj2, R.id.txtEj3, R.id.txtEj4, R.id.txtEj5, R.id.txtEj6, R.id.txtEj7, R.id.txtEj8};
        int[] seriesIds = {R.id.txtTS1, R.id.txtTS2, R.id.txtTS3, R.id.txtTS4, R.id.txtTS5, R.id.txtTS6, R.id.txtTS7, R.id.txtTS8};
        int[] repeticionesIds = {R.id.txtTR1, R.id.txtTR2, R.id.txtTR3, R.id.txtTR4, R.id.txtTR5, R.id.txtTR6, R.id.txtTR7, R.id.txtTR8};

        etEjercicios = findEditTextsByIds(ejerciciosIds);
        etSeries = findEditTextsByIds(seriesIds);
        etRepeticiones = findEditTextsByIds(repeticionesIds);
    }

    private List<EditText> findEditTextsByIds(int[] ids) {
        List<EditText> editTexts = new ArrayList<>();
        for (int id : ids) {
            editTexts.add(findViewById(id));
        }
        return editTexts;
    }

    private boolean checkValues() {

        if (etNombre.getText().toString().trim().isEmpty() ||
                etDescripcion.getText().toString().trim().isEmpty() ||
                etFrecuencia.getText().toString().trim().isEmpty()) {
            return false;
        }

        for (int i = 0; i < etEjercicios.size(); i++) {
            if (!etEjercicios.get(i).getText().toString().trim().isEmpty() &&
                    (etSeries.get(i).getText().toString().trim().isEmpty() ||
                            etRepeticiones.get(i).getText().toString().trim().isEmpty())) {
                return false;
            }
        }
        return true;
    }

    private RutinaCargaDatos crearRutina() {

        String nombre = etNombre.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String frecuencia = etFrecuencia.getText().toString().trim();

        List<ConfiguracionEjercicio> ejercicios = new ArrayList<>();
        for (int i = 0; i < etEjercicios.size(); i++) {
            String nombreEjercicio = etEjercicios.get(i).getText().toString().trim();
            if (!nombreEjercicio.isEmpty()) {
                int series = Integer.parseInt(etSeries.get(i).getText().toString().trim());
                int repeticiones = Integer.parseInt(etRepeticiones.get(i).getText().toString().trim());
                ejercicios.add(new ConfiguracionEjercicio(nombreEjercicio, series, repeticiones));
            }
        }

        return new RutinaCargaDatos(nombre, descripcion, frecuencia, ejercicios);
    }

/*
    private void setElementos(){

        etNombre = findViewById(R.id.etNombreRutinaCrear);
        valores.add(etNombre.toString());
        etDescripcion = findViewById(R.id.etDescripcionRutinaCrear);
        valores.add(etDescripcion.toString());
        etFrecuencia = findViewById(R.id.etFrecuenciaCrear);
        valores.add(etFrecuencia.toString());

        etEj1 = findViewById(R.id.txtEj1);
        valores.add(etEj1.toString());
        etEj2 = findViewById(R.id.txtEj2);
        valores.add(etEj2.toString());
        etEj3 = findViewById(R.id.txtEj3);
        valores.add(etEj3.toString());
        etEj4 = findViewById(R.id.txtEj4);
        valores.add(etEj4.toString());
        etEj5 = findViewById(R.id.txtEj5);
        valores.add(etEj5.toString());
        etEj6 = findViewById(R.id.txtEj6);
        valores.add(etEj6.toString());
        etEj7 = findViewById(R.id.txtEj7);
        valores.add(etEj7.toString());
        etEj8 = findViewById(R.id.txtEj8);
        valores.add(etEj8.toString());

        etSeries1 = findViewById(R.id.txtTS1);
        valores.add(etSeries1.toString());
        etSeries2 = findViewById(R.id.txtTS2);
        valores.add(etSeries2.toString());
        etSeries3 = findViewById(R.id.txtTS3);
        valores.add(etSeries3.toString());
        etSeries4 = findViewById(R.id.txtTS4);
        valores.add(etSeries4.toString());
        etSeries5 = findViewById(R.id.txtTS5);
        valores.add(etSeries5.toString());
        etSeries6 = findViewById(R.id.txtTS6);
        valores.add(etSeries6.toString());
        etSeries7 = findViewById(R.id.txtTS7);
        valores.add(etSeries7.toString());
        etSeries8 = findViewById(R.id.txtTS8);
        valores.add(etSeries8.toString());

        etRep1 = findViewById(R.id.txtTR1);
        valores.add(etRep1.toString());
        etRep2 = findViewById(R.id.txtTR2);
        valores.add(etRep2.toString());
        etRep3 = findViewById(R.id.txtTR3);
        valores.add(etRep3.toString());
        etRep4 = findViewById(R.id.txtTR4);
        valores.add(etRep4.toString());
        etRep5 = findViewById(R.id.txtTR5);
        valores.add(etRep5.toString());
        etRep6 = findViewById(R.id.txtTR6);
        valores.add(etRep6.toString());
        etRep7 = findViewById(R.id.txtTR7);
        valores.add(etRep7.toString());
        etRep8 = findViewById(R.id.txtTR8);
        valores.add(etRep8.toString());


    }

    private boolean checkValues(){

        for(String st : valores){
            if(st.isEmpty()){
                return false;
            }
        }

        return true;
    }
*/
}