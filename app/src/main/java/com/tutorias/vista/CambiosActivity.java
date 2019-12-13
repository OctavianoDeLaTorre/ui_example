package com.tutorias.vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.tutorias.vista.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import controlador.AnalizadorJSON;

public class CambiosActivity extends AppCompatActivity {

    private TextInputEditText txtCNc, txtCn,txtCpa,txtCsa;
    private AutoCompleteTextView txtCe,txtCs,txtCc;
    private Button btnCBuscar, btnCGuardar, btnLimpiar;
    private InputMethodManager imm ;
    private AnalizadorJSON json = new AnalizadorJSON();
    private String url_servidor = "http://176.48.16.22/PruebasPHP/Sistema_ABCC_MSQL/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambios);

        configView();
    }

    private void configView() {
        txtCNc = findViewById(R.id.txtBNumControl);
        txtCn = findViewById(R.id.txtCN);
        txtCsa = findViewById(R.id.txtConsultas);
        txtCpa = findViewById(R.id.txtCPa);
        txtCc = findViewById(R.id.txtCC);
        txtCe = findViewById(R.id.txtCE);
        txtCs = findViewById(R.id.txtCS);
        btnCBuscar = findViewById(R.id.btnCBuscar);
        btnCGuardar = findViewById(R.id.btnBuscar);
        btnLimpiar = findViewById(R.id.btnEliminar);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


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



        txtCe.setDropDownBackgroundResource(R.color.colorAccent);
        txtCe.setAdapter(edadAdapter);

        txtCs.setDropDownBackgroundResource(R.color.colorAccent);
        txtCs.setAdapter(semestreAdapter);

        txtCc.setDropDownBackgroundResource(R.color.colorAccent);
        txtCc.setAdapter(carreraAdapter);


        btnCBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numControl = txtCNc.getText().toString();
                if (!numControl.replace(" ","").equals("")){
                    new BuscarAlumno().execute("numControl",numControl);
                } else {
                    Snackbar.make(btnCGuardar, "Debe ingresar el numero de control!!", Snackbar.LENGTH_LONG)
                            .setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            })
                            .show();
                }

            }
        });

        btnCGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nc = txtCNc.getText().toString();
                String n = txtCn.getText().toString();
                String pa = txtCpa.getText().toString();
                String sa = txtCsa.getText().toString();
                String s = txtCs.getText().toString();
                String c = txtCc.getText().toString();
                String e = txtCe.getText().toString();

                ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ni =  cn.getActiveNetworkInfo();

                //condicio para ver si hay coneccion
                if (ni != null && ni.isConnected()){

                    if (validarCajas(txtCNc,txtCn,txtCpa,txtCsa) && validarCombos(txtCs,txtCe,txtCc)){
                        //conectar y enviar datos para guardar en MySQL

                           new ActualizarAlumno().execute(nc,n,pa,sa,s,c,e);

                    } else {
                        Snackbar.make(btnCGuardar, "Falta información!", Snackbar.LENGTH_LONG)
                                .setAction("Ok", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                    }
                                })
                                .show();
                    }

                }
            }
        });

        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarCajas();
            }
        });
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

    private boolean validarCombos(AutoCompleteTextView...cajas){
        String texto;
        for (AutoCompleteTextView caja : cajas){
            texto = caja.getText().toString();
            texto = texto.replace(" ","");
            if (texto.equals(""))
                return false;
        }
        return true;
    }

    public void limpiarCajas(){
        txtCNc.setText("");
        txtCn.setText("");
        txtCsa.setText("");
        txtCpa.setText("");
        txtCc.setText("");
        txtCe.setText("");
        txtCs.setText("");
        txtCNc.requestFocus();
        imm.hideSoftInputFromWindow(txtCNc.getWindowToken(), 0);
    }

    class BuscarAlumno extends AsyncTask<String, String, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            ArrayList<String> lista = null;

            String url = url_servidor+"consultas.php";
            JSONObject jsonObject = json.buscarHTTP(url,strings[0],strings[1]);


            try {
                if (jsonObject.getInt("exito") == 1){
                    lista = new ArrayList<>();
                    JSONArray jesonArray = jsonObject.getJSONArray("alumnos");

                    String cadena = "";
                    for (int i = 0; i < jesonArray.length(); i++){
                        lista.add(jesonArray.getJSONObject(i).getString("nc"));
                        lista.add(jesonArray.getJSONObject(i).getString("n"));
                        lista.add(jesonArray.getJSONObject(i).getString("pa"));
                        lista.add(jesonArray.getJSONObject(i).getString("sa"));
                        lista.add(jesonArray.getJSONObject(i).getString("c"));
                        lista.add(jesonArray.getJSONObject(i).getString("s"));
                        lista.add(jesonArray.getJSONObject(i).getString("e"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
                return null;
            }

            return lista;
        }

        @Override
        protected void onPostExecute(ArrayList<String> lista) {
            super.onPostExecute(lista);
            if (lista != null){
                txtCn.setText(lista.get(1));
                txtCpa.setText(lista.get(2));
                txtCsa.setText(lista.get(3));
                txtCc.setText(lista.get(4));
                txtCs.setText(lista.get(5));
                txtCe.setText(lista.get(6));
                txtCNc.requestFocus();
                imm.hideSoftInputFromWindow(txtCNc.getWindowToken(), 0);
            }else{
                Snackbar.make(btnCGuardar, "Nose econtro ningun registro!", Snackbar.LENGTH_LONG)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        })
                        .show();
                limpiarCajas();
            }
        }
    }

    class ActualizarAlumno extends AsyncTask <String, String, Boolean> {
        @Override
        protected Boolean doInBackground(String... args) {

            Map<String,String> datos = new HashMap<String,String>();
            datos.put("nc",args[0]);
            datos.put("n",args[1]);
            datos.put("pa",args[2]);
            datos.put("sa",args[3]);
            datos.put("s",args[4]);
            datos.put("c",args[5]);
            datos.put("e",args[6]);

            AnalizadorJSON aJESON = new AnalizadorJSON();

            String url = url_servidor+"cambios_alumnos.php";
            String metodo = "POST";

            JSONObject resultado = aJESON.peticionesHTTP(url,metodo,datos);

            int r = 0;
            try {
                r = resultado.getInt("exito");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (r ==1){
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            if (resultado){
                limpiarCajas();
                Snackbar.make(btnCGuardar, "Registro actualizado con exito!", Snackbar.LENGTH_LONG)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        })
                        .show();
            }else{
                Snackbar.make(btnCGuardar, "Registro actualizado no se pudo actualizar!", Snackbar.LENGTH_LONG)
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
