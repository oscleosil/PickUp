package com.dam.pickup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class HistoryFragment extends Fragment {
    private ListView lista;
    private ArrayList<String> items;
    private Button erase_history_button;
    private List_Adapter adap;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment,container,false);
        lista = view.findViewById(R.id.history_list);
        erase_history_button = view.findViewById(R.id.erase_history_button);
        items = new ArrayList<String>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .document(MainFragment.userName).collection("historial").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Iterator<QueryDocumentSnapshot> iter = task.getResult().iterator();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nombre = document.getData().get("Producto").toString();
                                items.add(nombre);
                            }
                            if(items.size()!=0) {
                                erase_history_button.setClickable(true);
                                erase_history_button.setEnabled(true);
                                Collections.reverse(items);
                                create_list();
                            }
                            else {
                                erase_history_button.setClickable(false);
                                erase_history_button.setEnabled(false);
                            }
                        }
                        else
                            Toast.makeText(getActivity().getApplicationContext()
                                    , "Se ha producido un error, intentelo de nuevo", Toast.LENGTH_SHORT).show();
                    }
                });

        erase_history_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> l = new ArrayList<String>();
                l.addAll(items);
                for(String s: l) {
                    db.collection("usuarios")
                            .document(MainFragment.userName)
                            .collection("historial").document(s).delete();
                    adap.removeItem(0);
                }
                adap.notifyDataSetChanged();
                Toast.makeText(getActivity().getApplicationContext()
                        , "El historial se ha eliminado correctamente", Toast.LENGTH_SHORT).show();
                erase_history_button.setClickable(false);
                erase_history_button.setEnabled(false);
            }
        });

        return view;
    }


    private void create_list(){

        lista.setAdapter(adap = new List_Adapter(getActivity().getApplicationContext(), R.layout.history_row, items) {
            @Override
            public void onEntry(Object entrada, View view) {
                TextView product_name = view.findViewById(R.id.history_row_txtView);
                if (product_name!=null){
                    product_name.setText((String) entrada);
                    product_name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String URL = "https://serpapi.com/search.json?q="+((String) entrada).replace(" ", "+")
                                    +"&api_key="+getString(R.string.serpapi_key)+"&location=Spain&google_domain=google.es&hl=es&gl=es";
                            Bundle b = new Bundle();
                            b.putString("URL", URL);
                            b.putString("query", ((String) entrada));
                            Intent act = new Intent(getActivity(), SearchActivity.class);
                            act.putExtras(b);
                            startActivity(act);
                        }
                    });
                }
            }
        });

    }
}