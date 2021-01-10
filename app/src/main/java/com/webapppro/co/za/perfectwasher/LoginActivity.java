package com.webapppro.co.za.perfectwasher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText editEmail, editPassword;
    Button signInButton;
    ProgressBar progressBar;
    TextView textViewReg;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthlistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = (EditText) findViewById(R.id.fieldEmail);
        editPassword = (EditText) findViewById(R.id.fieldPassword);
        signInButton = (Button) findViewById(R.id.emailSignInButton);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        textViewReg = (TextView) findViewById(R.id.text_sign_up);

        mAuth = FirebaseAuth.getInstance();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    editEmail.setError("Email required!");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    editPassword.setError("Password required!");
                    return;
                }
                progressBarShow();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBarHide();
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            goToNextActivity();
                        } else {
                            progressBarHide();
                            Toast.makeText(getApplicationContext(), "Login Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });

        textViewReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentUp = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intentUp);
            }
        });

    }

    private void progressBarHide() {
        progressBar.setVisibility(View.GONE);
    }

    private void progressBarShow() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void goToNextActivity() {
        Intent intentNext = new Intent(LoginActivity.this, OrderActivity.class);
        startActivity(intentNext);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, OrderActivity.class));
            finish();
        }
    }
}