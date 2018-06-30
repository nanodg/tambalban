package com.example.nanodg.tambalban;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.nanodg.tambalban.Adapter.AdapterTambalRecyclerView;
import com.example.nanodg.tambalban.Adapter.AdapterUserRecyclerView;
import com.example.nanodg.tambalban.Model.Tambah;
import com.example.nanodg.tambalban.Model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListUserActivity extends AppCompatActivity implements AdapterUserRecyclerView.FirebaseDataListener {

    private DatabaseReference database;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<User> daftarUser;
    FirebaseAuth firebaseAuth;
    public String alias;
    EditText pencarian;
    ImageButton cari;
    public String hasil;
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);
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
        actionBar.setTitle("List Data User");
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
        database.child("Users").orderByChild("email").startAt(hasil).endAt(hasil + "\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                daftarUser = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    final User user = noteDataSnapshot.getValue(User.class);

                    user.setKey(noteDataSnapshot.getKey());
                    daftarUser.add(user);
                    // Log.e("Data snapshot","barang5"+daftarBarang.add(barang));

                }

                adapter = new AdapterUserRecyclerView(daftarUser, ListUserActivity.this);
                rvView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });
    }

    public static Intent getActIntent(Activity activity){
        return new Intent(activity, ListUserActivity.class);
    }

    @Override
    public void onDeleteData(User user, final int position) {
        /**
         * Kode ini akan dipanggil ketika method onDeleteData
         * dipanggil dari adapter lewat interface.
         * Yang kemudian akan mendelete data di Firebase Realtime DB
         * berdasarkan key barang.
         * Jika sukses akan memunculkan Toast
         */
        if(database!=null){
            database.child("Users").child(user.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ListUserActivity.this,"success delete", Toast.LENGTH_LONG).show();
                }
            });

        }
    }


}
