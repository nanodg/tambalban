package com.example.nanodg.tambalban;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nanodg.tambalban.Model.Tambah;
import com.example.nanodg.tambalban.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

public class PnlPemilikActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvslmt;
    CardView tambah,btnlogout,btnlihat,btnaduan,btnpesan;
    FirebaseAuth firebaseAuth;
    String alias;
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnl_pemilik);

        tvslmt = (TextView)findViewById(R.id.tvslmt);
        tambah = (CardView) findViewById(R.id.btntambah) ;
        btnlogout = (CardView)findViewById(R.id.btnlogout);
        btnpesan = (CardView)findViewById(R.id.btnpesan);
        btnlihat = (CardView)findViewById(R.id.btnlihat);
        btnaduan = (CardView)findViewById(R.id.btnaduan);
        btnlogout.setOnClickListener(this);
        btnlihat.setOnClickListener(this);
        tambah.setOnClickListener(this);
        btnaduan.setOnClickListener(this);
        btnpesan.setOnClickListener(this);
        initToolbar();
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

        /**
         * Merubah Email ke Username
         */
        DatabaseReference mUserContactsRef =  FirebaseDatabase.getInstance().getReference().child("Users");
        mUserContactsRef.orderByChild("email").equalTo(alias).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                //Log.e("barang1", dataSnapshot.toString());
                for (DataSnapshot userContact : dataSnapshot.getChildren()) {

                    User user = userContact.getValue(User.class);
                   tvslmt.setText("Selamat Datang : "+ user.getUsername());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Panel Pemilik");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                onBackPressed();

            }
        });
    }

    @Override
    public void onClick(View view) {

        if(view == tambah){

            startActivity(new Intent(this, TambahActivity.class));
        }
        if(view == btnpesan){

            startActivity(new Intent(this, PesanPemilikActivity.class));
        }
        if(view == btnlihat){

            startActivity(new Intent(this, FirebaseDBReadActivity.class));
        }
        if(view == btnaduan){

            startActivity(new Intent(this, FirebaseDBReadAduanActivity.class));
        }
        if (view == btnlogout) {
            //logging out the user
            firebaseAuth.signOut();
            //closing activity

            //starting login activity
            startActivity(new Intent(this, LoginUserActivity.class));
            finish();
        }
    }
    public static Intent getActIntent(Activity activity) {
        // kode untuk pengambilan Intent
        return new Intent(activity, PnlPemilikActivity.class);
    }


}
