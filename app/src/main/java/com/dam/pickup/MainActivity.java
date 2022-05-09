package com.dam.pickup;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import android.util.Log;
import android.app.ProgressDialog;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private EditText query;
    private Button search_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        query = findViewById(R.id.search_tool);
        search_button = findViewById(R.id.search_button);
        setUp();
    }

    private void setUp(){
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("ME HAS CLICKADO");
                String query_txt = query.getText().toString();
                if(!query_txt.equals("")) {
                    String URL = "https://serpapi.com/search.json?q=" + query_txt.replace(" ", "+").toLowerCase()
                            +"&api_key="+getString(R.string.serpapi_key)+"&location=Spain&google_domain=google.es&hl=es&gl=es";
                    System.out.println(URL);
                    //final ProgressDialog dialog = ProgressDialog.show(getApplicationContext(), "Cargando","Espere, por favor",true);
                    Bundle b = new Bundle();
                    b.putString("URL", URL);
                    Intent act = new Intent(getApplicationContext(), SearchActivity.class);
                    act.putExtras(b);
                    startActivity(act);
                }
                else
                    Toast.makeText(getApplicationContext(), "Indique un producto", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void ir_Registrarse(View view){
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    public void ir_IniciarSesion(View view){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
}
