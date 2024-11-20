package com.example.tpfinal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinal.Conexion.ConexionUsuario;

public class MainActivity extends AppCompatActivity {

    private EditText nombreUsuario;
    private EditText password;
    private Button btnLogin;
    private Button btnRegistrar;
    private Button btnRecuperar;
    private Button btnVerEstadisticas;
    private ConexionUsuario conUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conUsuario = new ConexionUsuario(MainActivity.this);

        nombreUsuario = findViewById(R.id.etUsuarioMain);
        password = findViewById(R.id.etPasswordMain);
        btnLogin = findViewById(R.id.loginButton);
        btnRegistrar = findViewById(R.id.btnRegistrarseRegistrarse);
        btnRecuperar = findViewById(R.id.btnForgotPass);
        btnVerEstadisticas = findViewById(R.id.btnVerEstadisticasApp);

        btnLogin.setOnClickListener(v -> {

            if(checkValues(nombreUsuario) || checkValues(password)){
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            }
            else{
                String usuario =nombreUsuario.getText().toString();
                String pass = password.getText().toString();
                conUsuario.iniciarSesion(usuario, pass, new ConexionUsuario.IniciarSesionCallback() {
                    @Override
                    public void onIniciarSesion(boolean autenticado, int idUsuario) {
                        if(autenticado){
                            Intent intent = new Intent(MainActivity.this, ActivityPaginaInicio.class);
                            intent.putExtra("idUsuario", idUsuario );
                            startActivity(intent);
                        }else {
                            Toast.makeText(MainActivity.this, "Error al iniciar sesion", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }

        });

        btnVerEstadisticas.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, ActivityVerEstadisticas.class);
            startActivity(intent);

        });

        btnRegistrar.setOnClickListener(v -> {

            Log.d("MainActivity", "Context: " + MainActivity.this);
            Intent intent = new Intent(MainActivity.this, ActivityRegistrarse.class);
            Log.d("MainActivity", "Intent: " + intent);
            startActivity(intent);

        });

        btnRecuperar.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, ActivityRecuperarPass.class);
            startActivity(intent);

        });

    }

    private boolean checkValues(TextView value){

        String valueString = value.getText().toString();

        return valueString.isEmpty();
    }
}