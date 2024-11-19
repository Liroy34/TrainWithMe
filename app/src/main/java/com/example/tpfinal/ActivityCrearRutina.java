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

        int idUsuario = getIntent().getIntExtra("idUsuario",-1);

        conRutinas = new ConexionRutinas(ActivityCrearRutina.this);

        btnVolver = findViewById(R.id.btnVolverCrearRutina);
        btnCrear = findViewById(R.id.btnCrearRutina);

        setElementos();

        btnCrear.setOnClickListener(v -> {

            if (!checkValues()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            } else {

                RutinaCargaDatos rutina = crearRutina();
                conRutinas.insertRutina(rutina, idUsuario);
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
}