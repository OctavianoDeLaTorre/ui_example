package com.tutorias.vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.tutorias.vista.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import controlador.AnalizadorJSON;

public class Login extends AppCompatActivity {

    private TextInputEditText txtUsuario, txtContraseña;
    private MaterialButton btnLogin;
    private MaterialCardView cvLogin;
    private String url_servidor = "http://176.48.16.22/PruebasPHP/Sistema_ABCC_MSQL/";

    private Animation animation1 ;
    private Animation animation2 ;


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
        cvLogin = findViewById(R.id.cvLogin);

        animation1 = AnimationUtils.loadAnimation(this,R.anim.animacion_login_in);
        animation2 = AnimationUtils.loadAnimation(this,R.anim.animacion_login_out);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //revisar si existe conexion con wifi
                ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ni =  cn.getActiveNetworkInfo();

                //condicio para ver si hay coneccion
                if (ni != null && ni.isConnected()){
                    String u = txtUsuario.getText().toString();
                    String c = txtContraseña.getText().toString();

                    if (validarCajas(txtUsuario,txtContraseña)){
                        new AuteticaUsuario().execute(u,c);
                    } else {
                        Snackbar.make(btnLogin, "Falta información!", Snackbar.LENGTH_LONG)
                                .setAction("Ok", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                })
                                .show();
                    }
                } else {
                    Snackbar.make(btnLogin, "Compruebe su conexión a intenet!!", Snackbar.LENGTH_LONG)
                            .setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .show();
                }

            }
        });

        cvLogin.startAnimation(animation1);
    }

    private boolean validarCajas(TextInputEditText...cajas){
        String texto;
        for (TextInputEditText caja : cajas){
            texto = caja.getText().toString();
            texto = texto.replace(" ","");
            if (texto.equals(""))
                return false;
        }
        return true;
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
            } catch (NullPointerException e){
                return false;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                cvLogin.startAnimation(animation2);
                startActivity(new Intent(Login.this, MenuAlumno.class));
            }else{
                Snackbar.make(btnLogin, "No se pudo autenticar usuario...", Snackbar.LENGTH_LONG)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .show();
            }
        }
    }
}
