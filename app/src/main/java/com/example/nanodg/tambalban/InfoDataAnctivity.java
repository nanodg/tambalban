package com.example.nanodg.tambalban;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class InfoDataAnctivity extends AppCompatActivity {

    CardView btntambal,btnbengkel,btnaksesoris;
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_data_anctivity);
        btntambal = (CardView)findViewById(R.id.btntambal);
        btnbengkel = (CardView)findViewById(R.id.btnbengkel);
        btnaksesoris = (CardView)findViewById(R.id.btnaksesoris);
        initToolbar();
        btntambal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(FirebaseDBReadActivity.getActIntent(InfoDataAnctivity.this));
            }
        });
        btnbengkel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(FirebaseDBReadBengkelActivity.getActIntent(InfoDataAnctivity.this));
            }
        });
        btnaksesoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(FirebaseDBReadAksesorisActivity.getActIntent(InfoDataAnctivity.this));
            }
        });
    }
    public void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Data Informasi");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                onBackPressed();

            }
        });

    }
}
