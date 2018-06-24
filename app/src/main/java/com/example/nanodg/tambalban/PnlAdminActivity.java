package com.example.nanodg.tambalban;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.example.nanodg.tambalban.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PnlAdminActivity extends AppCompatActivity implements View.OnClickListener{

    CardView btntambah,btntambal,btnuser,btnaduan,btnlogout;
    FirebaseAuth firebaseAuth;
    TextView tvslmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnl_admin);

        btntambah = (CardView)findViewById(R.id.btntambah);
        btntambal = (CardView) findViewById(R.id.btntambal);
        btnuser = (CardView) findViewById(R.id.btnuser);
        btnaduan = (CardView) findViewById(R.id.btnaduan);
        btnlogout = (CardView) findViewById(R.id.btnlogout);
        tvslmt = (TextView) findViewById(R.id.tvslmt);

        btnlogout.setOnClickListener(this);
        btnaduan.setOnClickListener(this);
        btnuser.setOnClickListener(this);
        btntambah.setOnClickListener(this);
        btntambal.setOnClickListener(this);

        /**
         * FIREBASE LOGIN
         */
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginUserActivity.class));
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String alias = user.getEmail();

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

        if (view == btntambah) {
            startActivity(new Intent(this, TambahActivity.class));
            finish();
        }if (view == btnlogout) {
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginUserActivity.class));
        } if (view == btntambal) {
            finish();

        } if (view == btnaduan) {
            finish();

        } if (view == btnuser) {
            finish();

        }
    }
}
