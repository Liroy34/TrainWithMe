package com.example.tpfinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinal.Conexion.ConexionUsuario;
import com.example.tpfinal.Entidades.Usuario;

public class ActivityPerfil extends AppCompatActivity {

    private EditText tvNombre, tvApellido, tvEmail;
    private Spinner generos;
    private Button btnEditarPerfil, btnDarDeBaja;
    private ImageButton btnVolverMiPerfil;
    private int idUsuario;
    private ConexionUsuario conUsuario;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        conUsuario = new ConexionUsuario(ActivityPerfil.this);

        generos = findViewById(R.id.spinnerGeneroEditarPerfil);
        String[] opciones = {"Masculino", "Femenino", "Otro"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        generos.setAdapter(adapter);

        tvNombre = findViewById(R.id.tvNombre);
        tvApellido = findViewById(R.id.tvApellido);
        tvEmail = findViewById(R.id.tvEmail);
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
        btnDarDeBaja = findViewById(R.id.btnDarDeBaja);
        btnVolverMiPerfil = findViewById(R.id.btnVolverMiPerfil);

         idUsuario = getIntent().getIntExtra("idUsuario", -1);

        if (idUsuario == -1) {

            Toast.makeText(this, "Error el idusuario es -1", Toast.LENGTH_SHORT).show();

            finish();
            return;
        }

        btnVolverMiPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityPerfil.this, ActivityPaginaInicio.class);
            intent.putExtra("idUsuario", idUsuario );
            startActivity(intent);
        });

        cargarDatosUsuario();

        configurarBotonDarDeBaja();


        btnEditarPerfil.setOnClickListener(v -> {
            editarPerfil();
        });

    }

    private void cargarDatosUsuario() {

        conUsuario.traerUsuario(idUsuario, usuario -> {
            if(usuario!= null){
                tvNombre.setText(usuario.getNombre());
                tvApellido.setText(usuario.getApellido());
                tvEmail.setText(usuario.getMail());
                setSpinnerSelection(generos, usuario.getGenero());
            }
        });

    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        for (int position = 0; position < adapter.getCount(); position++) {
            if(adapter.getItem(position).equals(value)) {
                spinner.setSelection(position);
                return;
            }
        }

        Log.e("ActivityPerfil", "Valor '" + value + "' no encontrado en el spinner.");
    }

    private void editarPerfil() {

        String nombre = tvNombre.getText().toString();
        String apellido = tvApellido.getText().toString();
        String email = tvEmail.getText().toString();
        String genero = generos.getSelectedItem().toString();

        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setMail(email);
        usuario.setGenero(genero);

        ConexionUsuario conexionUsuario = new ConexionUsuario(this);
        conexionUsuario.updateUsuario(usuario);


        finish();
        Toast.makeText(this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
    }

    private void configurarBotonDarDeBaja() {
        btnDarDeBaja.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmar acción");
            builder.setMessage("¿Estás seguro de que deseas dar de baja este usuario? Esta acción no se puede deshacer.");

            builder.setPositiveButton("Sí, dar de baja", (dialog, which) -> {
                darDeBajaUsuario();
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> {
                dialog.dismiss();
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void darDeBajaUsuario() {

        ConexionUsuario conexionUsuario = new ConexionUsuario(this);
        conexionUsuario.deleteUsuario(idUsuario);

        Intent intent = new Intent(ActivityPerfil.this, MainActivity.class);
        startActivity(intent);
    }
}
