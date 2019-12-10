package com.example.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import controlador.AnalizadorJSON;

public class Login extends AppCompatActivity {

    private TextInputEditText txtUsuario, txtContraseña;
    private MaterialButton btnLogin;
    private String url_servidor = "http://10.0.2.2/PruebasPHP/Sistema_ABCC_MSQL/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        configView();
    }

    private void configView(){
        txtUsuario = findViewById(R.id.txtUsuario);
        txtContraseña = findViewById(R.id.txtContraseña);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u = txtUsuario.getText().toString();
                String c = txtContraseña.getText().toString();

                try {
                    if (new AuteticaUsuario().execute(u,c).get()){
                        startActivity(new Intent(Login.this, MenuAlumno.class));
                    }else{
                        Snackbar.make(btnLogin, "No se pudo autenticar usuario...", Snackbar.LENGTH_SHORT)
                                .setAction("Ok", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                })
                                .show();

                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class AuteticaUsuario extends AsyncTask<String, String, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            AnalizadorJSON json = new AnalizadorJSON();

            String url = url_servidor+"login.php";
            JSONObject jsonObject = json.loginHTTP(url,strings[0],strings[1]);

            try {
                if (jsonObject.getInt("exito") == 1){
                    return true;
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return false;
        }
    }
}
