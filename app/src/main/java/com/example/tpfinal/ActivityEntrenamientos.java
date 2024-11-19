package com.example.tpfinal;

import android.os.Bundle;

import android.content.Intent;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;


public class ActivityEntrenamientos extends AppCompatActivity {

    private int idUsuario;
    private ImageButton btnVolver;
    private Button btnRegistrarEntrenamiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_entrenamientos);

        idUsuario = getIntent().getIntExtra("idUsuario", -1);
        btnVolver = findViewById(R.id.btnVolverListaEntrenamientos);
        btnRegistrarEntrenamiento = findViewById(R.id.btnRegistrarEntrenamiento);







        btnVolver.setOnClickListener(v-> {

            finish();

        });

        btnRegistrarEntrenamiento.setOnClickListener(v->{

                Intent intent = new Intent(ActivityEntrenamientos.this, RegistrarEntrenamientoActivity.class);
                intent.putExtra("idUsuario", idUsuario);
                startActivity(intent);

        });
    }
}
