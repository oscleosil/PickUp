package com.dam.pickup;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;

import android.util.Log;
import android.app.ProgressDialog;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private EditText query;
    private Button search_button;
    private TextView signIn;
    private TextView logIn;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.main_fragment,container,false);
        query = view.findViewById(R.id.search_tool);
        search_button = view.findViewById(R.id.search_button);
        signIn = view.findViewById(R.id.IniciarSesion);
        logIn = view.findViewById(R.id.Registrarse);
        setUp();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //session();
    }

    private void session(){
        SharedPreferences prefs = this.getActivity().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String email = prefs.getString("email", null);
        String provider = prefs.getString("provider", null);
        if(!email.isEmpty() && !provider.isEmpty()){

        }
    }

    private void setUp(){
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query_txt = query.getText().toString();
                if(!query_txt.equals("")) {
                    String URL = "https://serpapi.com/search.json?q=" + query_txt.replace(" ", "+").toLowerCase()
                            +"&api_key="+getString(R.string.serpapi_key)+"&location=Spain&google_domain=google.es&hl=es&gl=es";
                    System.out.println(URL);
                    //final ProgressDialog dialog = ProgressDialog.show(getApplicationContext(), "Cargando","Espere, por favor",true);
                    Bundle b = new Bundle();
                    b.putString("URL", URL);
                    Intent act = new Intent(getActivity(), SearchActivity.class);
                    act.putExtras(b);
                    startActivity(act);
                }
                else
                    Toast.makeText(getContext(), "Indique un producto", Toast.LENGTH_SHORT).show();

            }
        });

        logIn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LogInActivity.class);
                startActivity(intent);
            }
        }));

        signIn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                startActivity(intent);
            }
        }));
    }


}
