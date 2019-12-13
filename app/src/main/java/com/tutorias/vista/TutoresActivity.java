package com.tutorias.vista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adaptador.AdaptadorTutor;
import adaptador.myAdapatador;
import controlador.AnalizadorJSON;
import modelo.Alumno;
import modelo.Tutor;

public class TutoresActivity extends AppCompatActivity {

    private TextInputEditText txtConsulta;
    private RecyclerView rv;

    private AnalizadorJSON json = new AnalizadorJSON();
    private String url_servidor = "http://176.48.16.22/PruebasPHP/Sistema_ABCC_MSQL/";

    String campo = "tutores.nombreTutor";
    String valor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutores);

        configView();
    }

    private void configView() {
        txtConsulta = findViewById(R.id.txtConsultasTutor);
        rv = findViewById(R.id.lsFiltadoTutores);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        new BuscarTutor().execute("1","1");

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
                new FiltrarTutor().execute(campo,valor);
            }
        });
    }

    private class BuscarTutor extends AsyncTask<String, String, ArrayList<Tutor>> {
        @Override
        protected ArrayList<Tutor> doInBackground(String... strings) {
            ArrayList<Tutor> lista = null;

            String url = url_servidor+"tutores.php";
            JSONObject jsonObject = json.buscarHTTP(url,strings[0],strings[1]);

            try {
                if (jsonObject.getInt("exito") == 1){
                    lista = new ArrayList<>();
                    JSONArray jesonArray = jsonObject.getJSONArray("tutores");

                    for (int i = 0; i < jesonArray.length(); i++){

                        lista.add(new Tutor(
                                jesonArray.getJSONObject(i).getString("nombreTutor"),
                                jesonArray.getJSONObject(i).getString("apellidoTutor"),
                                jesonArray.getJSONObject(i).getString("nombreAlumno"),
                                jesonArray.getJSONObject(i).getString("apellidoAlumno")
                        ));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return lista;
        }

        @Override
        protected void onPostExecute(ArrayList<Tutor> tutores) {
            if (tutores != null && tutores.size() > 0){
                AdaptadorTutor adaptador = new AdaptadorTutor(tutores);
                rv.setAdapter(adaptador);
            } else {
                Snackbar.make(rv, "No se ha econtrado tutores registrados!.", Snackbar.LENGTH_SHORT)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .show();
            }
        }
    }

    private class FiltrarTutor extends AsyncTask<String, String, ArrayList<Tutor>> {
        @Override
        protected ArrayList<Tutor> doInBackground(String... strings) {
            ArrayList<Tutor> lista = null;

            String url = url_servidor+"tutores_patrones.php";
            JSONObject jsonObject = json.buscarHTTP(url,strings[0],strings[1]);

            try {
                if (jsonObject.getInt("exito") == 1){
                    lista = new ArrayList<>();
                    JSONArray jesonArray = jsonObject.getJSONArray("tutores");

                    for (int i = 0; i < jesonArray.length(); i++){

                        lista.add(new Tutor(
                                jesonArray.getJSONObject(i).getString("nombreTutor"),
                                jesonArray.getJSONObject(i).getString("apellidoTutor"),
                                jesonArray.getJSONObject(i).getString("nombreAlumno"),
                                jesonArray.getJSONObject(i).getString("apellidoAlumno")
                        ));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return lista;
        }

        @Override
        protected void onPostExecute(ArrayList<Tutor> tutores) {
            if (tutores != null && tutores.size() > 0){
                AdaptadorTutor adaptador = new AdaptadorTutor(tutores);
                rv.setAdapter(adaptador);
            } else {
                Snackbar.make(rv, "No se ha econtrado tutores registrados!.", Snackbar.LENGTH_SHORT)
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
