package com.example.tpfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinal.Adapters.RutinasAdapter;
import com.example.tpfinal.Conexion.ConexionRutinas;
import com.example.tpfinal.Entidades.Rutina;

import java.util.ArrayList;
import java.util.List;

public class ActivityRutinasPropias extends AppCompatActivity {

    private Button btnCrearRutinaPropia;
    private ImageButton btnVolverRutinaPropia;
    private ConexionRutinas conRutinas;
    public ListView lvRutinas;
    public List<Rutina> rutinasList = new ArrayList<>();
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutina_propias);

        btnCrearRutinaPropia = findViewById(R.id.btnCrearRutina);
        btnVolverRutinaPropia = findViewById(R.id.btnVolverRutinaPropia);

        Intent intentAnterior = getIntent();
        id = intentAnterior.getIntExtra("idUsuario",-1);

        lvRutinas = findViewById(R.id.rutinasList);

        conRutinas = new ConexionRutinas(lvRutinas, ActivityRutinasPropias.this);
        conRutinas.getRutinasPropias(id);


        lvRutinas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Rutina selectedRutina = rutinasList.get(position);

                Intent intent = new Intent(ActivityRutinasPropias.this, ActivityRutinaSeleccionada.class);
                intent.putExtra("idRutina", selectedRutina.getId());
                intent.putExtra("idUsuario", id);
                intent.putExtra("rutinaFrecuencia", selectedRutina.getFrecuencia());
                intent.putExtra("descripcionRutina", selectedRutina.getDescripcion());
                intent.putExtra("nombreRutina", selectedRutina.getNombre());
                startActivity(intent);
            }
        });


        btnCrearRutinaPropia.setOnClickListener(v -> {

            Intent intent = new Intent(ActivityRutinasPropias.this, ActivityCrearRutina.class);
            intent.putExtra("idUsuario",id );
            ActivityRutinasPropias.this.startActivity(intent);

        });

        btnVolverRutinaPropia.setOnClickListener(v -> {

            finish();

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                conRutinas.getRutinasPropias(id);
            }
        }, 2000);

    }
}
