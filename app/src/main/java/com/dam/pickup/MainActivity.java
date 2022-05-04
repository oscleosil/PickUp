package com.dam.pickup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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
                    final String URL = "https://serpapi.com/search.json?q=" + query.getText().toString()
                            +"&api_key="+getString(R.string.serpapi_key)+"&location=Spain&google_domain=google.es&hl=es&gl=es";
                    System.out.println(URL);
                    //final ProgressDialog dialog = ProgressDialog.show(getApplicationContext(), "Cargando","Espere, por favor",true);
                    JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("OBTENIENDO RESPUESTA");
                            Log.d("Response", response.toString());
                            //dialog.dismiss();
                            try {
                                JSONObject json = response.getJSONObject("search_metadata");
                                JSONObject product_result = response.getJSONObject("product_result");
                                String status = json.getString("status");
                                if (status.equals("Success")) {
                                    //create_list(product_result);
                                } else
                                    Toast.makeText(getApplicationContext(), "No ha sido posible obtener los datos", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            VolleyLog.v("Response:%n %s", response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", error.toString());
                        }
                    });
                    SearchApplication.getInstance().getRequestQueue().add(req);
                }
                else
                    Toast.makeText(getApplicationContext(), "Indique un producto", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void create_list(JSONObject json){
      View v = getLayoutInflater().inflate(R.layout.activity_search, null);
      ListView lista = new ListView(getApplicationContext());
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
