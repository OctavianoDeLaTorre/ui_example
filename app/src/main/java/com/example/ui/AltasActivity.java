package com.example.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import controlador.AnalizadorJSON;

public class AltasActivity extends AppCompatActivity {

   private TextInputEditText txtNc, txtn,txtpa,txtsa;
   private AutoCompleteTextView txte,txts,txtc;
   private Button btnAgregar, btnLimpiar;
   private String url_servidor = "http://10.0.2.2/PruebasPHP/Sistema_ABCC_MSQL/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altas);


        configView();
    }

    private void configView(){

        txtNc = findViewById(R.id.txtBNumControl);
        txtn = findViewById(R.id.txtCN);
        txtsa = findViewById(R.id.txtConsultas);
        txtpa = findViewById(R.id.txtCPa);
        txtc = findViewById(R.id.txtCC);
        txte = findViewById(R.id.txtCE);
        txts = findViewById(R.id.txtCS);

        btnAgregar = findViewById(R.id.btnBuscar);
        btnLimpiar = findViewById(R.id.btnEliminar);


        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarAlumno();
            }
        });

        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarCajas();
            }
        });

        String[] edades = new String[120];
        for (int i= 0; i < edades.length; i++){
            edades[i] = String.valueOf(i+1);
        }
        ArrayAdapter<String> edadAdapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.support_simple_spinner_dropdown_item,
                        edades);


        String[] semestres = new String[12];
        for (int i= 0; i < semestres.length; i++){
            semestres[i] = String.valueOf(i+1);
        }
        ArrayAdapter<String> semestreAdapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.support_simple_spinner_dropdown_item,
                        semestres);

        String[] carreras = new String[]{"ISC","IA","CP","LA"};
        ArrayAdapter<String> carreraAdapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.support_simple_spinner_dropdown_item,
                        carreras);



        txte.setDropDownBackgroundResource(R.color.colorAccent);
        txte.setAdapter(edadAdapter);

        txts.setDropDownBackgroundResource(R.color.colorAccent);
        txts.setAdapter(semestreAdapter);

        txtc.setDropDownBackgroundResource(R.color.colorAccent);
        txtc.setAdapter(carreraAdapter);
    }

    public void agregarAlumno(){
        String nc = txtNc.getText().toString();
        String n = txtn.getText().toString();
        String pa = txtpa.getText().toString();
        String sa = txtsa.getText().toString();
        String s = txts.getText().toString();
        String c = txtc.getText().toString();
        String e = txte.getText().toString();

        //revisar si existe conexion con wifi
        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni =  cn.getActiveNetworkInfo();

        //condicio para ver si hay coneccion
        if (ni != null && ni.isConnected()){
            //conectar y enviar datos para guardar en MySQL
            new AgregarAlumno().execute(nc,n,pa,sa,s,c,e);
        }
    }

    //Clase interna

    class AgregarAlumno extends AsyncTask<String, String, Integer> {
        @Override
        protected Integer doInBackground(String... args) {

            Map<String,String> datos = new HashMap<String,String>();
            datos.put("nc",args[0]);
            datos.put("n",args[1]);
            datos.put("pa",args[2]);
            datos.put("sa",args[3]);
            datos.put("s",args[4]);
            datos.put("c",args[5]);
            datos.put("e",args[6]);

            AnalizadorJSON aJESON = new AnalizadorJSON();

            String url = url_servidor+"altas_alumnos.php";
            String metodo = "POST";

            JSONObject resultado = aJESON.peticionesHTTP(url,metodo,datos);

            int r = 0;
            try {
                r = resultado.getInt("exito");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (r ==1){
                Log.i("Msj resultado", "Registro Agregado");
            }else {
                Log.i("Msj resultado", "Error Registro");
            }

            return r;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if (integer == 1){
                Snackbar.make(btnAgregar, "Alumno agregado con exito!", Snackbar.LENGTH_LONG)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        })
                        .show();
                limpiarCajas();
            } else{
                Snackbar.make(btnAgregar, "No fue posible agregar el alumno!", Snackbar.LENGTH_LONG)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        })
                        .show();
            }


        }
    }


    private void limpiarCajas(){
        txtNc.setText("");
        txtn .setText("");
        txtsa .setText("");
        txtpa .setText("");
        txtc .setText("");
        txte.setText("");
        txts.setText("");
    }

}
