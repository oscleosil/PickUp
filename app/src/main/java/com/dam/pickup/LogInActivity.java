package com.dam.pickup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.*;
import com.google.firebase.auth.GoogleAuthProvider;

public class LogInActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText confirm_password;
    private Button log_in;
    private Button log_in_with_google;
    private enum provider{BASIC, GOOGLE};
    private final int GOOGLE_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);
        log_in = findViewById(R.id.login);
        log_in_with_google = findViewById(R.id.login_with_google);
        setup();
    }

    private boolean check_fields(){
        boolean control = true;
        String email_txt = email.getText().toString();
        String pass_txt = password.getText().toString();
        String conf_pass_txt = confirm_password.getText().toString();
        if((email_txt.equals("")) || (pass_txt.equals("")) || (conf_pass_txt.equals(""))){
            control = false;
            Toast.makeText(getApplicationContext(),
                    "Debe completar todos los campos para poder registrarse",
                    Toast.LENGTH_LONG).show();
        }
        else if(!pass_txt.equals(conf_pass_txt)){
            control = false;
            Toast.makeText(getApplicationContext(),
                    "Los campos 'Password' y 'Confirm password' deben coincidir",
                    Toast.LENGTH_LONG).show();
        }
        else if(!email_txt.contains("@gmail") && !email_txt.contains("@hotmail")){
            control = false;
            Toast.makeText(getApplicationContext(),
                    "El correo introducido no es valido",
                    Toast.LENGTH_LONG).show();
        }
        else if(pass_txt.length()<6){
            control = false;
            Toast.makeText(getApplicationContext(),
                    "La contraseña debe tener al menos 6 caracteres",
                    Toast.LENGTH_LONG).show();
        }

        return control;
    }

    private void setup(){
        log_in.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String email_txt = email.getText().toString();
                String pass_txt = password.getText().toString();
                if(check_fields()){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email_txt,
                            pass_txt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(),
                                                "Usuario registrado correctamente",
                                                Toast.LENGTH_LONG).show();
                                        SharedPreferences.Editor prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit();
                                        prefs.putString("email", email_txt);
                                        prefs.putString("provider", provider.BASIC.toString());
                                        prefs.apply();
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "Se ha producido un error, por favor, intentelo de nuevo",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }

                            });
                        }

                    }
        });

        log_in_with_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 GoogleSignInOptions google_conf = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                         .requestIdToken(getString(R.string.default_web_client_id))
                         .requestEmail()
                         .build();
                 GoogleSignInClient google_client = GoogleSignIn.getClient(getApplicationContext(), google_conf);
                 google_client.signOut();
                 startActivityForResult(google_client.getSignInIntent(), GOOGLE_SIGN_IN);
            }
        });
    }

    @Override
    public void onActivityResult(int request_code, int result_code, Intent data) {
        super.onActivityResult(request_code, result_code, data);
        if(request_code== GOOGLE_SIGN_IN){
            Task<GoogleSignInAccount> sign_in= GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = sign_in.getResult(ApiException.class);
                if(account != null){
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),
                                        "Usuario registrado correctamente",
                                        Toast.LENGTH_LONG).show();
                                        SharedPreferences.Editor prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit();
                                        prefs.putString("email", account.getEmail());
                                        prefs.putString("provider", provider.GOOGLE.toString());
                                        prefs.apply();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Se ha producido un error, por favor, intentelo de nuevo",
                                        Toast.LENGTH_LONG).show();
                            }
                        }

                    });;
                }
                else
                    Toast.makeText(getApplicationContext(),
                            "La cuenta utilizada no existe",
                            Toast.LENGTH_LONG).show();
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }
}