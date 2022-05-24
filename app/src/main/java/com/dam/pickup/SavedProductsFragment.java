package com.dam.pickup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.DataCollectionDefaultChange;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SavedProductsFragment extends Fragment {
    private ListView lista;
    private ArrayList<Product> productos;
    private List_Adapter adap;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_fragment, container, false);
        lista = view.findViewById(R.id.savedproducts_list);
        productos = new ArrayList<Product>();
        db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .document(MainFragment.userName).collection("productos").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nombre = document.getData().get("Nombre").toString();
                                String descrip = document.getData().get("Detalle").toString();
                                String precio = document.getData().get("Precio").toString();
                                String prov = document.getData().get("Proveedor").toString();
                                String link = document.getData().get("URL").toString();
                                Product p = new Product(Integer.toString(i), nombre, precio, link, prov, descrip);
                                productos.add(p);
                                i++;
                            }
                            if(productos.size()!=0)
                                create_list();
                        }
                        else
                            Toast.makeText(getActivity().getApplicationContext()
                                    , "Se ha producido un error, intentelo de nuevo", Toast.LENGTH_SHORT).show();
                    }
                });

        return view;
    }

    private void create_list(){

        lista.setAdapter(adap = new List_Adapter(getActivity().getApplicationContext(), R.layout.producto, productos) {
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
                            String URL = ((Product) entrada).getLink();
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
                Button save_button = view.findViewById(R.id.save_button);
                save_button.setBackgroundColor(getResources().getColor(R.color.purple_700));
                save_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateList(((Product) entrada).getId(), ((Product) entrada).getLink());
                    }
                });
            }
        });
    }

    private void updateList(String id, String URL){
        AlertDialog.Builder adb= new AlertDialog.Builder(getActivity());
        adb.setTitle("Eliminar");
        adb.setMessage("¿Desea eliminar el producto?");
        adb.setNegativeButton("Cancelar", null);
        adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                db.collection("usuarios")
                        .document(MainFragment.userName).collection("productos")
                        .document(URL.replace("/", "+"))
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getActivity().getApplicationContext(),
                                            "Producto eliminado correctamente",
                                            Toast.LENGTH_LONG).show();
                                    adap.removeItem(Integer.parseInt(id));
                                    adap.notifyDataSetChanged();
                                }
                                else{
                                    Toast.makeText(getActivity().getApplicationContext(),
                                            "No ha sido posible eliminar el producto, intentelo de nuevo",
                                            Toast.LENGTH_LONG).show();
                                }

                            }
                        });
            }});
        adb.show();
    }
}