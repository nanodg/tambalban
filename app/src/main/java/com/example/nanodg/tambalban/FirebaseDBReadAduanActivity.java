package com.example.nanodg.tambalban;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.nanodg.tambalban.Adapter.AdapterAduanPemilikRecyclerView;
import com.example.nanodg.tambalban.Adapter.AdapterBarangRecyclerView;
import com.example.nanodg.tambalban.Model.Aduan;
import com.example.nanodg.tambalban.Model.Tambah;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by NanoDG on 23-Jun-18.
 */

public class FirebaseDBReadAduanActivity extends AppCompatActivity implements AdapterAduanPemilikRecyclerView.FirebaseDataListener {

    private DatabaseReference database;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Aduan> daftarAduan;
    FirebaseAuth firebaseAuth;
    String alias;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_read);

        rvView = (RecyclerView) findViewById(R.id.rv_main);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);

        /**
         * FIREBASE LOGIN
         */
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginUserActivity.class));
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();
        alias = user.getEmail();
        Log.e("Data snapshot", "email" + alias);

        database = FirebaseDatabase.getInstance().getReference();
        database.child("aduan").orderByChild("kategori").equalTo("2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                daftarAduan = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                  final  Aduan aduan = noteDataSnapshot.getValue(Aduan.class);
                    if(aduan.getPemilik().equals(alias)) {
                        if(aduan.getStatus().equals("0")) {
                            aduan.setKey(aduan.getKey());
                            daftarAduan.add(aduan);
                            Log.e("Data snapshot", "data" + aduan);
                        }
                    }
                    // Log.e("Data snapshot","barang5"+daftarBarang.add(barang));
                }


                adapter = new AdapterAduanPemilikRecyclerView(daftarAduan, FirebaseDBReadAduanActivity.this);
                rvView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });
    }

    public static Intent getActIntent(Activity activity){
        return new Intent(activity, FirebaseDBReadAduanActivity.class);
    }
    @Override
    public void onDeleteData(Aduan aduan, final int position) {


    }
}