package com.dam.pickup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SignInActivity extends AppCompatActivity {

    private EditText email;
    private EditText pass;
    private Button sign_in;
    private Button sign_in_with_google;
    private enum provider{BASIC, GOOGLE};
    private final int GOOGLE_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        sign_in = findViewById(R.id.signin);
        sign_in_with_google = findViewById(R.id.signin_with_google);
        setUp();
    }

    private void setUp(){
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_txt = email.getText().toString();
                String pass_txt = pass.getText().toString();
                if ((!email_txt.equals("")) && (!pass.equals(""))){
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email_txt,
                            pass_txt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),
                                        "Se ha iniciado sesion correctamente",
                                        Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);

                            }
                            else{
                                Toast.makeText(getApplicationContext(),
                                        "Se ha producido un error, por favor, intentelo de nuevo",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });


        sign_in_with_google.setOnClickListener(new View.OnClickListener() {
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
                                        /*SharedPreferences.Editor prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit();
                                        prefs.putString("email", email_txt);
                                        prefs.putString("provider", provider.BASIC.toString());
                                        prefs.apply(); ToDo*/
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Se ha producido un error, por favor, intentelo de nuevo",
                                        Toast.LENGTH_LONG).show();
                            }
                        }

                    });;
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

}