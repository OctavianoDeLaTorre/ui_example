package com.tutorias.vista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.tutorias.vista.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import adaptador.myAdapatador;
import controlador.AnalizadorJSON;
import modelo.Alumno;

public class ConsultasActivity extends AppCompatActivity {

    RadioButton rbNc;
    RadioButton rbN;
    RadioButton rbPa;
    TextInputEditText txtConsulta;
    RecyclerView rv;

    private AnalizadorJSON json = new AnalizadorJSON();
    private String url_servidor = "http://176.48.16.22/PruebasPHP/Sistema_ABCC_MSQL/";

    String campo = "numControl";
    String valor;
    private ArrayList<Alumno> alumnos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas);

        configView();
    }

    private void configView() {

        rbNc = findViewById(R.id.rbNc);
        rbN = findViewById(R.id.rbN);
        rbPa = findViewById(R.id.rbPa);
        txtConsulta = findViewById(R.id.txtConsultas);
        rv = findViewById(R.id.lsFiltado);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        mostrarAlumnos("1","1");

        rbPa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    campo = "primerAp";
                }
            }
        });
        rbN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    campo = "Nombre";
                }
            }
        });
        rbNc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    campo = "numControl";
                }
            }
        });

        txtConsulta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String valor = txtConsulta.getText().toString();
                mostrarAlumnos(campo,valor);
            }
        });

    }

    public void mostrarAlumnos(String campo, String valor){
        new BuscarAlumno().execute(campo, valor);
    }

    private class BuscarAlumno extends AsyncTask<String, String, ArrayList<Alumno>> {
        @Override
        protected ArrayList<Alumno> doInBackground(String... strings) {
            ArrayList<Alumno> lista = null;

            String url = url_servidor+"consultas_patrones.php";
            JSONObject jsonObject = json.buscarHTTP(url,strings[0],strings[1]);

            try {
                if (jsonObject.getInt("exito") == 1){
                    lista = new ArrayList<>();
                    JSONArray jesonArray = jsonObject.getJSONArray("alumnos");

                    for (int i = 0; i < jesonArray.length(); i++){

                        lista.add(new Alumno(
                                jesonArray.getJSONObject(i).getString("n"),
                                jesonArray.getJSONObject(i).getString("pa"),
                                jesonArray.getJSONObject(i).getString("c"),
                                jesonArray.getJSONObject(i).getString("s")

                        ));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return lista;
        }

        @Override
        protected void onPostExecute(ArrayList<Alumno> alumnos) {
            if (alumnos != null){
                myAdapatador adaptador = new myAdapatador(alumnos);
                rv.setAdapter(adaptador);
            } else {
                Snackbar.make(rv, "No se ha econtrado alumnos registrados!.", Snackbar.LENGTH_SHORT)
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
