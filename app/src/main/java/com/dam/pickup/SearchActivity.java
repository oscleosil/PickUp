package com.dam.pickup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = this.getIntent().getExtras();
        if (b !=null) {
            String URL = b.getString("URL");
            init_search(URL);
            setContentView(R.layout.activity_search);
        }
    }

    private void init_search(String URL){
        if(!URL.equals("")) {
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
                            create_list(product_result);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Se ha producido un error, intentelo de nuevo", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "No ha sido posible obtener los datos", Toast.LENGTH_SHORT).show();
                        finish();
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


    private void create_list(JSONObject json) throws JSONException {
        ListView lista = findViewById(R.id.product_list);
        JSONArray data = json.getJSONArray("pricing");
        ArrayList<Product> productos = new ArrayList<Product>();
        for (int i = 0; i < data.length();i++){
            JSONObject j = data.getJSONObject(i);
            Product p = new Product(json.getString("title"),j.getString("price"), j.getString("link"),
                    j.getString("name"), j.getString("description"));
            productos.add(p);
        }
        lista.setAdapter(new List_Adapter(getApplicationContext(), R.layout.producto, productos) {
            @Override
            public void onEntry(Object entrada, View view) {
                TextView product_name = view.findViewById(R.id.textView_producto);
                if (product_name!=null){
                    product_name.setText(((Product) entrada).getTitle());
                }
                TextView precio = view.findViewById(R.id.textView_precio);
                if (precio!=null){
                    precio.setText(((Product) entrada).getPrice());
                }
                TextView link = view.findViewById(R.id.textView_link);
                if (link!=null){
                    link.setText(((Product) entrada).getLink());
                }
                TextView vendedor = view.findViewById(R.id.textView_vendedor);
                if (vendedor!=null){
                    vendedor.setText(((Product) entrada).getProvider_name());
                }
                TextView descripcion = view.findViewById(R.id.textView_descripcion);
                if (descripcion!=null){
                    descripcion.setText(((Product) entrada).getDescription());
                }
            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView_URL = view.findViewById(R.id.textView_link);
                String URL = textView_URL.getText().toString();
                Intent intent_web = new Intent(Intent.ACTION_VIEW);
                intent_web.setData(Uri.parse(URL));
                startActivity(intent_web);
            }
        });
    }
}