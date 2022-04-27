package com.dam.pickup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void ir_Registrarse(View view){
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    public void ir_IniciarSesion(View view){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
/*    public void ir_Buscar(View view){
        Intent intent = new Intent(this, BusquedaActivity.class);
        startActivity(intent);
    }*/
}
