package com.example.nanodg.tambalban;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nanodg.tambalban.Model.User;
import com.example.nanodg.tambalban.data.SettingsAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseDatabase database;
    DatabaseReference users;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    EditText edemail,edpswd;
    Button btnlogin,btnsignup;
    SettingsAPI set;
    public String hslnama;
    public static final String DATA = "com.example.nanodg.tambalban";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        edemail = (EditText) findViewById(R.id.edemail);
        edpswd = (EditText) findViewById(R.id.edpswd);
        progressDialog = new ProgressDialog(this);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(this);
        btnsignup = (Button) findViewById(R.id.btnsignup);
        btnsignup.setOnClickListener(this);
        set = new SettingsAPI(this);
        Intent intent = getIntent();
        final String hslnama = intent.getStringExtra(DtltambalActivity.DATA);

        if(firebaseAuth.getCurrentUser() != null){
            FirebaseUser user = firebaseAuth.getCurrentUser();
            String tvemail;
            tvemail=user.getEmail();
            //Log.e("barang1", tvemail.toString());
            DatabaseReference  mUserContactsRef =  FirebaseDatabase.getInstance().getReference().child("Users");
            mUserContactsRef.orderByChild("email").equalTo(tvemail).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    set.deleteAllSettings();
                    Log.e("barang1", dataSnapshot.toString());
                    //Log.e("barang1", dataSnapshot.toString());
                    for (DataSnapshot userContact : dataSnapshot.getChildren()) {
                        User user = userContact.getValue(User.class);

                        final String usrNm = user.getUsername();
                        final String usrId = user.getKey();
                        set.addUpdateSettings("myid", usrId);
                        set.addUpdateSettings("myname", usrNm);

                        String email2 = hslnama;
                        Intent edit = new Intent(getApplicationContext(), ActivitySelectFriend.class);
                        edit.putExtra(DATA, email2);
                        startActivity(edit);
                        finish();

                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    //method for user login
    private void userLogin(){
        final String email = edemail.getText().toString().trim();
        String password  = edpswd.getText().toString().trim();


        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            TastyToast.makeText(getApplicationContext(), "Masukan Email", TastyToast.LENGTH_LONG, TastyToast.INFO);
            return;
        }

        if(TextUtils.isEmpty(password)){
            TastyToast.makeText(getApplicationContext(), "Masukan Password", TastyToast.LENGTH_LONG, TastyToast.INFO);
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Login Please Wait...");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful()){

                            DatabaseReference  mUserContactsRef =  FirebaseDatabase.getInstance().getReference().child("Users");
                            mUserContactsRef.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(final DataSnapshot dataSnapshot) {
                                    set.deleteAllSettings();
                                    //Log.e("barang1", dataSnapshot.toString());
                                    for (DataSnapshot userContact : dataSnapshot.getChildren()) {
                                        User user = userContact.getValue(User.class);
                                        final String usrNm = user.getUsername();
                                        final String usrId = user.getKey();
                                        Log.e("barang2", usrNm);
                                        Log.e("barang2", usrId);
                                        set.addUpdateSettings("myid", usrId);
                                        set.addUpdateSettings("myname", usrNm);

                                        String email2 = hslnama;
                                        Intent edit = new Intent(getApplicationContext(), ActivitySelectFriend.class);
                                        edit.putExtra(DATA, email2);
                                        startActivity(edit);
                                        finish();
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }else{
                            //display some message here
                            TastyToast.makeText(getApplicationContext(), "Login Gagal", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if(view == btnlogin){
            userLogin();
        }

        if(view == btnsignup){
            finish();
            startActivity(new Intent(this, RegisUserActivity.class));
        }
    }
    public static Intent getActIntent(Activity activity) {
        // kode untuk pengambilan Intent
        return new Intent(activity, LoginUserActivity.class);
    }
}
