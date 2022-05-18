package com.dam.pickup;

import android.app.AlertDialog;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SavedProductsFragment extends Fragment {
    private ListView lista;
    private ArrayList<Product> productos;
    private List_Adapter adap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_fragment, container, false);
        lista = view.findViewById(R.id.savedproducts_list);
        //productos = datos firebase
        lista.setAdapter(adap = new List_Adapter(getActivity().getApplicationContext(), R.layout.producto, productos) {
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
                Button save_button = view.findViewById(R.id.save_button);
                //save_button.setText(Integer.toString());
                save_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
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

        return view;
    }

    private void updateList(){
        AlertDialog.Builder adb=new AlertDialog.Builder(getActivity());
        adb.setTitle("Delete?");
        //adb.setMessage("Are you sure you want to delete " + position);
        //final int positionToRemove = position;
        adb.setNegativeButton("Cancel", null);
        //adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
        //    public void onClick(DialogInterface dialog, int which) {
        //        adap.
        //        adap.remove(positionToRemove);
        //        adap.notifyDataSetChanged();
        //    }});
        //adb.show();
    }
}