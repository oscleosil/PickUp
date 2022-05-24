package com.dam.pickup;

import static android.graphics.Color.GRAY;
import static android.graphics.Color.GREEN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private ListView lista;
    private int control = 0;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = this.getIntent().getExtras();
        if (b !=null) {
            String URL = b.getString("URL");
            String query = b.getString("query");
            init_search(URL, query);
            setContentView(R.layout.activity_search);
        }
    }

    private void init_search(String URL, String query){
        if(!URL.equals("")) {
            ProgressDialog dialog = ProgressDialog.show(SearchActivity.this, "Cargando","Espere, por favor",true);
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println("OBTENIENDO RESPUESTA");
                    Log.d("Response", response.toString());
                    dialog.dismiss();
                    try {
                        JSONObject json = response.getJSONObject("search_metadata");
                        JSONObject product_result = response.getJSONObject("product_result");
                        String status = json.getString("status");
                        if (status.equals("Success")) {
                            if(MainFragment.sessionActive){
                                Map<String, Object> p = new HashMap<>();
                                p.put("Producto", query);
                                p.put("URL", URL);
                                db.collection("usuarios").document(MainFragment.userName)
                                            .collection("historial").document(query).set(p);
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
        else {
            Toast.makeText(getApplicationContext(), "Indique un producto", Toast.LENGTH_SHORT).show();
            finish();
        }

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
                    product_name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String URL = ((Product) entrada).getLink();;
                            Intent intent_web = new Intent(Intent.ACTION_VIEW);
                            intent_web.setData(Uri.parse(URL));
                            startActivity(intent_web);
                        }
                    });
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
                    save_button.setBackgroundColor(getResources().getColor(R.color.purple_700));
                    save_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Map<String, Object> p = new HashMap<>();
                            p.put("Nombre", ((Product) entrada).getTitle());
                            p.put("Precio", ((Product) entrada).getPrice());
                            p.put("Detalle", ((Product) entrada).getDescription());
                            p.put("Proveedor", ((Product) entrada).getProvider_name());
                            p.put("URL", ((Product) entrada).getLink());
                            db.collection("usuarios").document(MainFragment.userName)
                                    .collection("productos")
                                    .document(((Product) entrada).getLink().replace("/", "+"))
                                    .set(p)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(),
                                                        "Producto guardado correctamente",
                                                        Toast.LENGTH_LONG).show();
                                                save_button.setClickable(false);
                                                save_button.setBackgroundColor(getResources().getColor(R.color.purple_200));
                                            } else {
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

    }
}