package com.example.tpfinal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinal.Conexion.ConexionRutinas;

public class ActivityRutinasPropias extends AppCompatActivity {

    private Button btnCrearRutinaPropia;
    private ImageButton btnVolverRutinaPropia;
    private Context context;
    private ConexionRutinas conRutinas;
    private ListView lvRutinas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutina_propias);

        btnCrearRutinaPropia = findViewById(R.id.btnCrearRutina);
        btnVolverRutinaPropia = findViewById(R.id.btnVolverRutinaPropia);

        Intent intentAnterior = getIntent();
        int id = intentAnterior.getIntExtra("idUsuario",-1);

        lvRutinas = findViewById(R.id.rutinasList);

        conRutinas = new ConexionRutinas(lvRutinas, context);
        conRutinas.getRutinasPropias(id);


        btnCrearRutinaPropia.setOnClickListener(v -> {

            Intent intent = new Intent(context, ActivityCrearRutina.class);
            context.startActivity(intent);

        });

        btnVolverRutinaPropia.setOnClickListener(v -> {

            finish();

        });

    }

}
