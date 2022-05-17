package com.dam.pickup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class MainFragment extends Fragment implements View.OnClickListener{
    @Nullable
    private EditText query;
    private EditText registrarse;
    private EditText iniciarsesion;
    private Button search_button;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.main_fragment,container,false);
        query = view.findViewById(R.id.search_tool);
        search_button = view.findViewById(R.id.search_button);
        registrarse = view.findViewById(R.id.Registrarse);
        iniciarsesion = view.findViewById(R.id.IniciarSesion);

        registrarse.setOnClickListener((View.OnClickListener) this);
        iniciarsesion.setOnClickListener((View.OnClickListener)this);
        setUp();
        return view;
    }


    private void setUp(){
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "has picado", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(getContext(), "No ha sido posible obtener los datos", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "Indique un producto", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void create_list(JSONObject json){
        View v = getLayoutInflater().inflate(R.layout.activity_search, null);
        ListView lista = new ListView(getContext());
    }
    /*
    private void cargarFragment(Fragment fragment) {
        FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_fragment, fragment).addToBackStack(null).commit();
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Registrarse:
                cargarFragment(new historial_fragment());
                break;

            case R.id.IniciarSesion:
                cargarFragment(new historial_fragment());
                break;
        }
    }
    /*/
    public void ir_Registrarse(View view){
        Intent intent = new Intent(getActivity(), LogInActivity.class);
        getActivity().startActivity(intent);

    }

    public void ir_IniciarSesion(View view){
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        getActivity().startActivity(intent);
    }

     */
}
