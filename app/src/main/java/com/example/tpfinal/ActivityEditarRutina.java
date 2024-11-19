package com.example.tpfinal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinal.Conexion.ConexionRutinas;
import com.example.tpfinal.Entidades.ConfiguracionEjercicio;
import com.example.tpfinal.Entidades.RutinaCargaDatos;

import java.util.ArrayList;
import java.util.List;

public class ActivityEditarRutina extends AppCompatActivity {

    private EditText etNombre, etDescripcion, etFrecuencia;
    private List<EditText> etEjerciciosEdit, etSeriesEdit, etRepeticionesEdit;
    private Button btnEditar;
    private ImageButton btnVolver;
    int idRutina;
    int frecuenciaRutina;
    String nombreRutina;
    String descripcionRutina;
    List<ConfiguracionEjercicio> configEjerciciosList = new ArrayList<>();
    private ConexionRutinas conRutinas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_rutina);

        idRutina = getIntent().getIntExtra("idRutina", -1);
        frecuenciaRutina = getIntent().getIntExtra("rutinaFrecuencia", -1);
        nombreRutina = getIntent().getStringExtra("nombreRutina");
        descripcionRutina = getIntent().getStringExtra("descripcionRutina");

        configEjerciciosList = getIntent().getParcelableArrayListExtra("listaConfig");

        conRutinas = new ConexionRutinas(ActivityEditarRutina.this);

        btnVolver = findViewById(R.id.btnVolverEditarRutina);
        btnEditar = findViewById(R.id.btnFinEditarRutina);

        setElementos(); //modificar para que se carguen con los datos de la lista

        loadElementos();

        btnEditar.setOnClickListener(v -> {

            if (!checkValues()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            } else {

                RutinaCargaDatos rutina = crearRutina();
                conRutinas.updateRutina(idRutina, rutina);
                finish();

            }
        });


        btnVolver.setOnClickListener(v -> {

            finish();

        });

    }

    private void loadElementos(){

        etNombre.setText(nombreRutina);
        etDescripcion.setText(descripcionRutina);
        etFrecuencia.setText(String.valueOf(frecuenciaRutina));

        for (int i = 0; i < configEjerciciosList.size(); i++) {
            ConfiguracionEjercicio config = configEjerciciosList.get(i);

            etEjerciciosEdit.get(i).setText(config.getEjercicio());
            etSeriesEdit.get(i).setText(String.valueOf(config.getSeries()));
            etRepeticionesEdit.get(i).setText(String.valueOf(config.getRepeticiones()));
        }

    }

    private void setElementos() {

        etNombre = findViewById(R.id.etNombreRutinaEditar);
        etDescripcion = findViewById(R.id.etDescripcionRutinaEditar);
        etFrecuencia = findViewById(R.id.etFrecuenciaEditar);

        int[] ejerciciosIds = {R.id.txtEj1Editar, R.id.txtEj2Editar, R.id.txtEj3Editar, R.id.txtEj4Editar, R.id.txtEj5Editar, R.id.txtEj6Editar, R.id.txtEj7Editar, R.id.txtEj8Editar};
        int[] seriesIds = {R.id.txtTS1Editar, R.id.txtTS2Editar, R.id.txtTS3Editar, R.id.txtTS4Editar, R.id.txtTS5Editar, R.id.txtTS6Editar, R.id.txtTS7Editar, R.id.txtTS8Editar};
        int[] repeticionesIds = {R.id.txtTR1Editar, R.id.txtTR2Editar, R.id.txtTR3Editar, R.id.txtTR4Editar, R.id.txtTR5Editar, R.id.txtTR6Editar, R.id.txtTR7Editar, R.id.txtTR8Editar};

        etEjerciciosEdit = findEditTextsByIds(ejerciciosIds);
        etSeriesEdit = findEditTextsByIds(seriesIds);
        etRepeticionesEdit = findEditTextsByIds(repeticionesIds);
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

        for (int i = 0; i < etEjerciciosEdit.size(); i++) {
            if (!etEjerciciosEdit.get(i).getText().toString().trim().isEmpty() &&
                    (etSeriesEdit.get(i).getText().toString().trim().isEmpty() ||
                            etRepeticionesEdit.get(i).getText().toString().trim().isEmpty())) {
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
        for (int i = 0; i < etEjerciciosEdit.size(); i++) {
            String nombreEjercicio = etEjerciciosEdit.get(i).getText().toString().trim();
            if (!nombreEjercicio.isEmpty()) {
                int series = Integer.parseInt(etSeriesEdit.get(i).getText().toString().trim());
                int repeticiones = Integer.parseInt(etRepeticionesEdit.get(i).getText().toString().trim());
                ejercicios.add(new ConfiguracionEjercicio(nombreEjercicio, series, repeticiones));
            }
        }

        return new RutinaCargaDatos(nombre, descripcion, frecuencia, ejercicios);
    }

}
