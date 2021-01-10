package com.webapppro.co.za.perfectwasher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RequestActivity extends AppCompatActivity {
    Button btnBack, btnProfile;
    TextView txtOrderNumber, txtOrderName, txtOrderAddress, txtOrderComplex, txtOrderInstruction;

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private String cUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        txtOrderNumber = (TextView) findViewById(R.id.order_number);
        txtOrderName = (TextView) findViewById(R.id.order_name);
        txtOrderAddress = (TextView) findViewById(R.id.order_address);
        txtOrderComplex = (TextView) findViewById(R.id.order_complex);
        txtOrderInstruction = (TextView) findViewById(R.id.order_instruction);
        btnBack = (Button) findViewById(R.id.btn_backward);

        mAuth = FirebaseAuth.getInstance();
        cUserId = mAuth.getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference("Users");





        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });
    }
}