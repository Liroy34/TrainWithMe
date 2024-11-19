package com.example.tpfinal;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinal.Conexion.ConexionEntrenamientos;
import com.example.tpfinal.Entidades.Entrenamiento;
import com.example.tpfinal.Entidades.Rutina;

import java.util.ArrayList;
import java.util.List;


public class ActivityEntrenamientos extends AppCompatActivity {

    private int idUsuario;
    private ImageButton btnVolver;
    private Button btnRegistrarEntrenamiento;
    private ConexionEntrenamientos conEntrenamientos;

    public ListView lvEntrenamientos;
    public List<Entrenamiento> entrenamientosList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_entrenamientos);

        idUsuario = getIntent().getIntExtra("idUsuario", -1);
        btnVolver = findViewById(R.id.btnVolverListaEntrenamientos);
        btnRegistrarEntrenamiento = findViewById(R.id.btnRegistrarEntrenamiento);

    // aca va la logica de cada uno de los items del listView
        lvEntrenamientos.findViewById(R.id.entrenamientosList);
        conEntrenamientos = new ConexionEntrenamientos(ActivityEntrenamientos.this, lvEntrenamientos);
        conEntrenamientos.getEntrenamientosPropios(idUsuario);

        lvEntrenamientos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Entrenamiento selectedEntrenamiento = entrenamientosList.get(position);

                Intent intent = new Intent(ActivityEntrenamientos.this, VerEntrenamientosActivity.class);
//                intent.putExtra("idEntrenamiento", selectedEntrenamiento.getId());
//                intent.putExtra("idUsuario", selectedEntrenamiento.getIdUsuario());
//                intent.putExtra("Duracion", selectedEntrenamiento.getDuracion());
//                intent.putExtra("Fecha", selectedEntrenamiento.getFecha());
//                intent.putExtra("Nombre", selectedEntrenamiento.getNombre());

                intent.putExtra("entrenamiento", selectedEntrenamiento);

                startActivity(intent);
            }
        });


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
