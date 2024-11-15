package com.example.tpfinal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityEditarRutina extends AppCompatActivity {

    private EditText etNombre, etDescripcion, etFrecuencia;
    private EditText etEj1, etSeries1, etEj2, etSeries2, etEj3, etSeries3, etEj4, etSeries4, etEj5, etSeries5, etEj6, etSeries6, etEj7, etSeries7, etEj8, etSeries8;
    private EditText etRep1, etRep2, etRep3, etRep4, etRep5, etRep6, etRep7, etRep8;
    private Button btnEditar;
    private ImageButton btnVolver;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_rutina);

        btnVolver = findViewById(R.id.btnVolverEditarRutina);
        btnEditar = findViewById(R.id.btnFinEditarRutina);


        btnVolver.setOnClickListener(v -> {

            finish();

        });

    }

}
