package com.example.masknotifier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.masknotifier.model.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LogIn extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private TextView email, password;
    private Button signUp, logIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.login_email_editText);
        password = findViewById(R.id.login_password_editText);
        signUp = findViewById(R.id.login_sign_up_buton);
        logIn = findViewById(R.id.login_login_button);

        signUp.setOnClickListener(this);
        logIn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.login_login_button) {
            mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                UserDetails.getUserInstance().setUid(mAuth.getCurrentUser().getUid());
                                Toast.makeText(LogIn.this, "Loged In", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LogIn.this, HomePage.class));
                            } else {
                                Toast.makeText(LogIn.this, (CharSequence) task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LogIn.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            startActivity(new Intent(LogIn.this, SignUp.class));
        }
    }
}