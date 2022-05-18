package com.dam.pickup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
                            if(MainFragment.sessionActive){
                                //Guardamos la busqueda en el historial
                            }
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
            Product p = new Product(Integer.toString(i),json.getString("title"),j.getString("price"), j.getString("link"),
                    j.getString("name"), j.getString("description"));
            productos.add(p);
        }
        lista.setAdapter(new List_Adapter(getApplicationContext(), R.layout.producto, productos) {
            @Override
            public void onEntry(Object entrada, View view) {
                TextView position = view.findViewById(R.id.position);
                if (position!=null){
                    position.setText(((Product) entrada).getId());
                }
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
                if(MainFragment.sessionActive) {
                    Button save_button = view.findViewById(R.id.save_button);
                    save_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Map<String, Object> p = new HashMap<>();
                            p.put("Nombre",((Product) entrada).getTitle());
                            p.put("Precio",((Product) entrada).getPrice());
                            p.put("Detalle",((Product) entrada).getDescription());
                            p.put("Proveedor",((Product) entrada).getProvider_name());
                            p.put("URL",((Product) entrada).getLink());
                            CollectionReference productDB = FirebaseFirestore.getInstance().collection("Producto");
                            productDB.add(p).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),
                                                "Producto guardado correctamente",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),
                                                "No ha sido posible guardar el producto",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });
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