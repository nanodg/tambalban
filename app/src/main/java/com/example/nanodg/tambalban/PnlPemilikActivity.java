package com.example.nanodg.tambalban;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
    CardView tambah,btnlogout,btnlihat;
    FirebaseAuth firebaseAuth;
    String alias;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnl_pemilik);

        tvslmt = (TextView)findViewById(R.id.tvslmt);
        tambah = (CardView) findViewById(R.id.btntambah) ;
        btnlogout = (CardView)findViewById(R.id.btnlogout);
        btnlihat = (CardView)findViewById(R.id.btnlihat);
        btnlogout.setOnClickListener(this);
        btnlihat.setOnClickListener(this);
        tambah.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {

        if(view == tambah){
            finish();
            startActivity(new Intent(this, TambahActivity.class));
        }
        if(view == btnlihat){
            finish();
            startActivity(new Intent(this, FirebaseDBReadActivity.class));
        }
        if (view == btnlogout) {
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginUserActivity.class));
        }
    }
    public static Intent getActIntent(Activity activity) {
        // kode untuk pengambilan Intent
        return new Intent(activity, PnlPemilikActivity.class);
    }


}
