package com.dam.pickup;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

   // private EditText query;
   // private Button search_button;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout =findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment,new MainFragment());
        fragmentTransaction.commit();



       /* query = findViewById(R.id.search_tool);
        search_button = findViewById(R.id.search_button);
        setUp();

        */
    }
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //para cerrar automaticamente el menu
        drawerLayout.closeDrawer(GravityCompat.START);
        if(menuItem.getItemId() == R.id.home){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new MainFragment());
            fragmentTransaction.commit();
        }

        if(menuItem.getItemId() == R.id.historial){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new historial_fragment());
            fragmentTransaction.commit();
        }


        return false;
    }

    /*
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overflow, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        //imprmimen texto prueba, redireccionar
        if(id == R.id.item1){
            Toast.makeText(getApplicationContext(), "clicaste en Historial", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.item2){
            Toast.makeText(getApplicationContext(), "clicaste en Guardados", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.item3){
            Toast.makeText(getApplicationContext(), "clicaste en Cerrar Sesion", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);

    }

    private void show() {
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

        */
}
