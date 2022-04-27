package com.dam.pickup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.*;

public class SignInActivity extends AppCompatActivity {

    private EditText email;
    private EditText pass;
    private Button sign_in;
    private Button sign_in_google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        sign_in = findViewById(R.id.signin);
        sign_in_google = findViewById(R.id.signin_with_google);
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

        /*sign_in_google.setOnClickListener(new View.OnClickListener() {
        ToDo
            @Override
            public void onClick(View view) {
                final GoogleSignInOptions.Builder google_opt = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN);
            }
        });*/

    }
}