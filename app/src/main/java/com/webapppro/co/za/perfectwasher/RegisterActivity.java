package com.webapppro.co.za.perfectwasher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    TextView textViewIn;
    private EditText mEmail, mPassword, mPassConf, mName, mTel;
    Button buttonUp;
    ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName = (EditText) findViewById(R.id.btn_nam);
        mTel = (EditText) findViewById(R.id.btn_tel);
        mEmail = (EditText) findViewById(R.id.btn_user);
        mPassword = (EditText) findViewById(R.id.btn_pass);
        mPassConf = (EditText) findViewById(R.id.btn_pass_conf);
        textViewIn = (TextView) findViewById(R.id.text_sign_in);
        buttonUp = (Button) findViewById(R.id.btn_register);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = mName.getText().toString();
                final String tel = mTel.getText().toString();
                final String mail = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String pass_confirm = mPassConf.getText().toString();

                if (TextUtils.isEmpty(name)){
                    mName.setError("Name required");
                    return;
                }

                if (TextUtils.isEmpty(mail)){
                    mEmail.setError("Email required!");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password required!");
                }
                if (!password.equals(pass_confirm)){
                    Toast.makeText(RegisterActivity.this, "Both password field must match!", Toast.LENGTH_SHORT).show();
                }

                progressBarShow();

                firebaseAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            String userId = firebaseAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance()
                                    .getReference().child("Users").child(userId);

                            Map userInfo = new HashMap();
                            userInfo.put("username", mail);
                            userInfo.put("name", name);
                            userInfo.put("telephone", tel);

                            currentUserDb.updateChildren(userInfo);


                            goToNextActivity();
                            Toast.makeText(RegisterActivity.this, "User successfully created!", Toast.LENGTH_SHORT).show();
                            progressBarHide();
                        }else{
                            progressBarHide();
                            Toast.makeText(RegisterActivity.this, "Some error occurred Please try later", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        textViewIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentIn = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intentIn);
            }
        });

    }

    private void goToNextActivity() {
        Intent intent = new Intent(RegisterActivity.this, OrderActivity.class);
        startActivity(intent);
        finish();
    }
    private void progressBarHide() {
        progressBar.setVisibility(View.GONE);
    }

    private void progressBarShow() {
        progressBar.setVisibility(View.VISIBLE);
    }
}