package com.example.nanodg.tambalban;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.nanodg.tambalban.Adapter.FriendsListAdapter;
import com.example.nanodg.tambalban.data.ParseFirebaseData;
import com.example.nanodg.tambalban.data.Tools;
import com.example.nanodg.tambalban.Model.User;
import com.example.nanodg.tambalban.widget.DividerItemDecoration;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivitySelectFriend extends AppCompatActivity {

    private ActionBar actionBar;
    private RecyclerView recyclerView;
    private FriendsListAdapter mAdapter;
    List<User> friendList;
    FirebaseAuth firebaseAuth;
    public static final String USERS_CHILD = "Users";
    ParseFirebaseData pfbd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_chat);

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
//                    tvslmt.setText("Selamat Datang : "+ user.getUsername());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        initComponent();
        friendList=new ArrayList<>();
        pfbd = new ParseFirebaseData(this);

        Intent intent = getIntent();
        String hslnama = intent.getStringExtra(LoginActivity.DATA);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(USERS_CHILD);
        ref.orderByChild("email").equalTo(hslnama).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String totalData = dataSnapshot.getValue().toString();
                // TODO: 25-05-2017 if number of items is 0 then show something else
                mAdapter = new FriendsListAdapter(ActivitySelectFriend.this, pfbd.getUserList(totalData));
                recyclerView.setAdapter(mAdapter);

                mAdapter.setOnItemClickListener(new FriendsListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, User obj, int position) {
                        ActivityChatDetails.navigate((ActivitySelectFriend) ActivitySelectFriend.this, findViewById(R.id.lyt_parent), obj);
                    }
                });

                bindView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                    Snackbar.make(getWindow().getDecorView(), "Could not connect", Snackbar.LENGTH_LONG).show();
            }
        });

        // for system bar in lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Tools.systemBarLolipop(this);
        }
    }

    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
		recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }



    public void bindView() {
        try {
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
        }
    }
}
