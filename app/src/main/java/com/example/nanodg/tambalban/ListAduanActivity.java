package com.example.nanodg.tambalban;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nanodg.tambalban.Adapter.AdapterAduanRecyclerView;
import com.example.nanodg.tambalban.Model.Aduan;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;

public class ListAduanActivity extends AppCompatActivity implements AdapterAduanRecyclerView.FirebaseDataListener   {

    Spinner kategori;
    String[] jeniskategori = {"Informasi Umum","Kritik dan Saran","lainya"};
    private DatabaseReference database;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Aduan> daftarAduan;
    private RecyclerView.Adapter adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_aduan);
        kategori = (Spinner) findViewById(R.id.kategori);
        rvView = (RecyclerView) findViewById(R.id.rv_main);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListAduanActivity.this, R.layout.simple_kategori,R.id.test, jeniskategori);
        kategori.setAdapter(adapter);
        database = FirebaseDatabase.getInstance().getReference();
        kategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (kategori.getSelectedItem()==("Informasi Umum")){
                    database.child("aduan").orderByChild("kategori").equalTo("1").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.e("barang1", dataSnapshot.toString());
                            daftarAduan = new ArrayList<>();
                            for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

                                final Aduan aduan = noteDataSnapshot.getValue(Aduan.class);

                                aduan.setKey(noteDataSnapshot.getKey());
                                daftarAduan.add(aduan);
                                // Log.e("Data snapshot","barang5"+daftarBarang.add(barang));
                            }
                            adapter1 = new AdapterAduanRecyclerView(daftarAduan, ListAduanActivity.this);
                            rvView.setAdapter(adapter1);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
                        }
                    });
                }
                if (kategori.getSelectedItem()==("Kritik dan Saran")){
                    database.child("aduan").orderByChild("kategori").equalTo("2").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.e("barang1", dataSnapshot.toString());
                            daftarAduan = new ArrayList<>();
                            for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                                final Aduan aduan = noteDataSnapshot.getValue(Aduan.class);

                                aduan.setKey(noteDataSnapshot.getKey());
                                daftarAduan.add(aduan);
                                // Log.e("Data snapshot","barang5"+daftarBarang.add(barang));

                            }

                            adapter1 = new AdapterAduanRecyclerView(daftarAduan, ListAduanActivity.this);
                            rvView.setAdapter(adapter1);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
                        }
                    });
                }
                if (kategori.getSelectedItem()==("lainya")){
                    database.child("aduan").orderByChild("kategori").equalTo("3").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.e("barang1", dataSnapshot.toString());
                            daftarAduan = new ArrayList<>();
                            for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                                final Aduan aduan = noteDataSnapshot.getValue(Aduan.class);

                                aduan.setKey(noteDataSnapshot.getKey());
                                daftarAduan.add(aduan);
                                // Log.e("Data snapshot","barang5"+daftarBarang.add(barang));
                            }
                            adapter1 = new AdapterAduanRecyclerView(daftarAduan, ListAduanActivity.this);
                            rvView.setAdapter(adapter1);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
                        }
                    });
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }
    public static Intent getActIntent(Activity activity){
        return new Intent(activity, ListAduanActivity.class);
    }
    @Override
    public void onDeleteData(Aduan aduan, final int position) {
        /**
         * Kode ini akan dipanggil ketika method onDeleteData
         * dipanggil dari adapter lewat interface.
         * Yang kemudian akan mendelete data di Firebase Realtime DB
         * berdasarkan key barang.
         * Jika sukses akan memunculkan Toast
         */
        if(database!=null){
            database.child("aduan").child(aduan.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ListAduanActivity.this,"success delete", Toast.LENGTH_LONG).show();
                }
            });

        }
    }
}
