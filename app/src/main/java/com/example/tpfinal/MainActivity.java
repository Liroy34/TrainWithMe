package com.example.tpfinal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    private ConexionUsuario conUsuario;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intentAnterior = getIntent();
        int idUsuario = intentAnterior.getIntExtra("idUsuario", -1);

        nombreUsuario = findViewById(R.id.etUsuarioMain);
        password = findViewById(R.id.etPasswordMain);
        btnLogin = findViewById(R.id.loginButton);
        btnRegistrar = findViewById(R.id.btnRegistrarseRegistrarse);
        btnRecuperar = findViewById(R.id.btnForgotPass);

        btnLogin.setOnClickListener(v -> {

            if(checkValues(nombreUsuario) || checkValues(password)){
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            }
            else{
                String usuario =nombreUsuario.toString();
                String pass = password.toString();
                conUsuario.iniciarSesion(usuario, pass, new ConexionUsuario.IniciarSesionCallback() {
                    @Override
                    public void onIniciarSesion(boolean autenticado, int idUsuario) {
                        Intent intent = new Intent(context, ActivityPaginaInicio.class);
                        intent.putExtra("idUsuario", idUsuario );
                        context.startActivity(intent);
                    }
                });
            }

        });

        btnRegistrar.setOnClickListener(v -> {

            Intent intent = new Intent(context, ActivityRegistrarse.class);
            context.startActivity(intent);

        });

        btnRecuperar.setOnClickListener(v -> {

            Intent intent = new Intent(context, ActivityRecuperarPass.class);
            context.startActivity(intent);

        });

    }

    private boolean checkValues(TextView value){

        String valueString = value.getText().toString();

        return valueString.isEmpty();
    }
}