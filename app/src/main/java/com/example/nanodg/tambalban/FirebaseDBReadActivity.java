package com.example.nanodg.tambalban;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.nanodg.tambalban.Adapter.AdapterBarangRecyclerView;
import com.example.nanodg.tambalban.Model.Tambah;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * Created by Herdi_WORK on 18.06.17.
 */

public class FirebaseDBReadActivity extends AppCompatActivity implements AdapterBarangRecyclerView.FirebaseDataListener {

    /**
     * Mendefinisikan variable yang akan dipakai
     */
    private DatabaseReference database;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Tambah> daftarTambah;
    FirebaseAuth firebaseAuth;
    public String alias;
    EditText pencarian;
    ImageButton cari;
    public String hasil;
    private ActionBar actionBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginUserActivity.class));
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();
        alias = user.getEmail();
        Log.e("Data snapshot", "email" + alias);
        setContentView(R.layout.activity_db_read);
        pencarian = (EditText) findViewById(R.id.pencarian);
        cari = (ImageButton) findViewById(R.id.cari);
        rvView = (RecyclerView) findViewById(R.id.rv_main);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);
        hasil = pencarian.getText().toString();
        cari();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("List Tambal Ban");
        cari.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                hasil = pencarian.getText().toString();
                cari();
            }
        });

    }

    public void cari(){
        database = FirebaseDatabase.getInstance().getReference();
        database.child("tambah").orderByChild("nama").startAt(hasil).endAt(hasil + "\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                daftarTambah = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    final Tambah tambah = noteDataSnapshot.getValue(Tambah.class);
                    if(tambah.getPembuat().equals(alias)) {

                        tambah.setKey(noteDataSnapshot.getKey());


                        daftarTambah.add(tambah);
                        // Log.e("Data snapshot","barang5"+daftarBarang.add(barang));
                    }
                }

                adapter = new AdapterBarangRecyclerView(daftarTambah, FirebaseDBReadActivity.this);
                rvView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });
    }

    public static Intent getActIntent(Activity activity){
        return new Intent(activity, FirebaseDBReadActivity.class);
    }

    @Override
    public void onDeleteData(Tambah tambah, final int position) {
        /**
         * Kode ini akan dipanggil ketika method onDeleteData
         * dipanggil dari adapter lewat interface.
         * Yang kemudian akan mendelete data di Firebase Realtime DB
         * berdasarkan key barang.
         * Jika sukses akan memunculkan Toast
         */
        if(database!=null){
            database.child("tambah").child(tambah.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(FirebaseDBReadActivity.this,"success delete", Toast.LENGTH_LONG).show();
            }
        });

        }
    }
}