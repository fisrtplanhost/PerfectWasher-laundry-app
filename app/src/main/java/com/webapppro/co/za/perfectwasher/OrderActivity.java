package com.webapppro.co.za.perfectwasher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    EditText txtDate, txtTime, txtStreet, txtSuburb, txtTel, txtComplex, txtInstruction;
    Button btnDatePicker, btnTimePicker, btnSendRequest;
    ProgressBar progressBar;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private FirebaseAuth mAuth;


    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        txtStreet = (EditText) findViewById(R.id.field_street);
        txtSuburb = (EditText) findViewById(R.id.field_suburb);
        txtComplex = (EditText) findViewById(R.id.field_complex);
        txtInstruction = (EditText) findViewById(R.id.field_instruction);
        txtDate = (EditText) findViewById(R.id.field_date_collect);
        txtTime = (EditText) findViewById(R.id.field_time_collect);
        btnDatePicker = (Button) findViewById(R.id.btn_date_select);
        btnTimePicker = (Button) findViewById(R.id.btn_time_select);
        btnSendRequest = (Button) findViewById(R.id.btn_send_request);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnSendRequest.setOnClickListener(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();

        mAuth = FirebaseAuth.getInstance();

    }

    private void fetchLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(OrderActivity.this);
                }
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        googleMap.addMarker(markerOptions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if(v == btnDatePicker){
            //Get Current Date
            final Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    txtDate.setText(dayOfMonth + "-" + (month + 1 ) + "-" + year);
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker){
            //Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            //Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    txtTime.setText(hourOfDay + ":" + minute);
                }
            }, mHour, mMinute, false);
            timePickerDialog.show();
        }if (v == btnSendRequest){


            final String street = txtStreet.getText().toString();
            final String suburb = txtSuburb.getText().toString();
            final String complex = txtComplex.getText().toString();
            final String instruction = txtInstruction.getText().toString();
            final String dateP = txtDate.getText().toString();
            final String timeP = txtTime.getText().toString();

           if (TextUtils.isEmpty(street)){
               txtStreet.setError("Street address required!");
               return;
           }
            if (TextUtils.isEmpty(suburb)){
                txtSuburb.setError("Suburb is required!");
                return;
            }
            progressBarShow();

            String userId = mAuth.getCurrentUser().getUid();
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(userId).child("Orders");
            Map orderInfo = new HashMap();
            orderInfo.put("Street", street);
            orderInfo.put("Suburb", suburb);
            orderInfo.put("Complex", complex);
            orderInfo.put("Instruction", instruction);
            orderInfo.put("Date", dateP);
            orderInfo.put("Time", timeP);

            dbRef.setValue(orderInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressBarHide();
                    Toast.makeText(OrderActivity.this, "Order successful placed, we will contact you soon", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBarHide();
                    Toast.makeText(OrderActivity.this, "Order unsuccessful placed, Please Try Again!", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }

    private void goToNextActivity() {

        AlertDialog dialog = new AlertDialog.Builder(this).create();


        Intent intentNext = new Intent(OrderActivity.this, RequestActivity.class);
        startActivity(intentNext);
        finish();
    }

    private void progressBarHide() {
        progressBar.setVisibility(View.GONE);
    }

    private void progressBarShow() {
        progressBar.setVisibility(View.VISIBLE);
    }


    public void userLogout(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(OrderActivity.this, LoginActivity.class));
                finish();
    }

    public void phoneCall(View view) {
        String phoneNumber = "+27814722223";
        String numberFormat = String.format("tel: %s",phoneNumber);

        Intent dialNumber = new Intent(Intent.ACTION_DIAL);
        dialNumber.setData(Uri.parse(numberFormat));
        if (dialNumber.resolveActivity(getPackageManager())  != null) {
            startActivity(dialNumber);
        }else {
            Toast.makeText(getApplicationContext(), "Error Calling", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToProfile(View view){
        Intent proIntent = new Intent(OrderActivity.this, ProfileActivity.class);
        startActivity(proIntent);
    }

    public void goToAbout(View view) {
        startActivity(new Intent(OrderActivity.this, AboutActivity.class));

    }
}