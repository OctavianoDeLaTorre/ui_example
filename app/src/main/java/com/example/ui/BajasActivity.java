package com.example.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
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

public class BajasActivity extends AppCompatActivity {
    private RecyclerView rv;
    private ArrayList<Alumno> alumnos;
    private Alumno alumno;

    private MaterialButton btnBuscar;
    private MaterialButton btnEliminar;
    private TextInputEditText txtBNumControl;

    private String nc;
    private AnalizadorJSON json = new AnalizadorJSON();
    private String url_servidor = "http://10.0.2.2/PruebasPHP/Sistema_ABCC_MSQL/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bajas);

        configView();
    }

    private void configView() {
        rv = findViewById(R.id.lsBajas);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);


        mostrarAlumnos("1","1");

        txtBNumControl = findViewById(R.id.txtBNumControl);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnEliminar = findViewById(R.id.btnEliminar);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nc = txtBNumControl.getText().toString();
                mostrarAlumnos("num_control",nc);

            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    nc = txtBNumControl.getText().toString();
                    boolean resultado = new EliminarAlumno().execute(nc).get();
                    if (resultado){
                        Snackbar.make(rv, "Alumno eliminado con exito!.", Snackbar.LENGTH_SHORT)
                                .setAction("Ok", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                })
                                .show();
                    }else{
                        Snackbar.make(rv, "No fue posible eliminar al alumno eliminado!.", Snackbar.LENGTH_SHORT)
                                .setAction("Ok", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                    }
                                })
                                .show();
                    }

                    mostrarAlumnos("1","1");
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void mostrarAlumnos(String campo, String valor){
        try {
            alumnos = new BuscarAlumno().execute(campo, valor).get();
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
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class BuscarAlumno extends AsyncTask<String, String, ArrayList<Alumno>> {
        @Override
        protected ArrayList<Alumno> doInBackground(String... strings) {
            ArrayList<Alumno> lista = null;

            String url = url_servidor+"consultas.php";
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
    }

    private class EliminarAlumno extends AsyncTask<String, String, Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {
            String url = url_servidor+"bajas_alumnos.php";
            JSONObject jsonObject = json.eliminarHTTP(url,strings[0]);

            try {
                if (jsonObject.getInt("exito") == 1) {
                    return true;
                }else
                    return false;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return false;
        }
    }
}
