package com.example.masknotifier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SignUp";
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private User user;
    private EditText email,password,confirmPassword;
    private Button signUp;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email_editText);
        password = findViewById(R.id.new_password_editText);
        confirmPassword = findViewById(R.id.confirm_password_editText);
        signUp = findViewById(R.id.sign_up_buton);

        signUp.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            startActivity(new Intent(this, MapsActivity.class));
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_up_buton) {
            final String emailText = email.getText().toString().trim();
            final String passwordText = password.getText().toString().trim();
            if (emailText != null && passwordText != null
                    && passwordText.equals(confirmPassword.getText().toString().trim())) {
                mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Log.d(TAG, "onComplete: Sign Up successful");
                                    currentUser = mAuth.getCurrentUser();
                                    Map<String, Object> obj = new HashMap<>();
                                    obj.put("index",0);
                                    db.collection("userHistory")
                                            .document(currentUser.getUid())
                                            .set(obj)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    startActivity(new Intent(SignUp.this, MapsActivity.class));
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                                else{
                                    Log.d(TAG, "onComplete: " + task.getException());
                                }
                            }
                        });
            }
        }
    }
}