package com.webapppro.co.za.perfectwasher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AboutActivity extends AppCompatActivity {

    String PRICE_LIST[] = {"Wash only: R18", "1kg Wash and Dry: R22", "1kg Wash Dry and Iron: R27", "1kg Wash Dry and iron", "Blanket Single: R90", "Blanket Double: R110", "Blanket Queen: R140", "Blanket King: R160"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ListView priceList = (ListView) findViewById(R.id.price_list);

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.price_list_layout,  PRICE_LIST);
        priceList.setAdapter(adapter);
    }
}