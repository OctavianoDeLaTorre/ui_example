package com.example.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class MenuAlumno extends AppCompatActivity {

    private Button btnAltas;
    private Button btnBajas;
    private Button btnCambios;
    private Button btnConsultas;

    private Animation animation1 ;
    private Animation animation2 ;
    private Animation animation3 ;
    private Animation animation4 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_alumno);

        animation1 = AnimationUtils.loadAnimation(this,R.anim.animation_home_button_2);
        animation2 = AnimationUtils.loadAnimation(this,R.anim.animation_home_button_4);
        animation3 = AnimationUtils.loadAnimation(this,R.anim.animation_home_button_5);
        animation4 = AnimationUtils.loadAnimation(this,R.anim.animacion_home);

        configViews();
    }

    private void configViews(){
        btnAltas = findViewById(R.id.btnAltas);
        btnBajas =  findViewById(R.id.btnBajas);
        btnCambios =  findViewById(R.id.btnCambios);
        btnConsultas =  findViewById(R.id.btnConsultas);



        btnAltas.startAnimation(animation1);
        btnCambios.startAnimation(animation1);
        btnBajas.startAnimation(animation2);
        btnConsultas.startAnimation(animation2);

        btnAltas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirActivity(AltasActivity.class);
            }
        });

        btnCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirActivity(CambiosActivity.class);
            }
        });

        btnBajas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirActivity(BajasActivity.class);
            }
        });

        btnConsultas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirActivity(ConsultasActivity.class);
            }
        });
    }

    private void abrirActivity(Class<?> cls){
        btnAltas.startAnimation(animation3);
        btnCambios.startAnimation(animation3);
        btnBajas.startAnimation(animation4);
        btnConsultas.startAnimation(animation4);

        Intent intent = new Intent(this,cls);

        startActivity(intent);

    }
}
