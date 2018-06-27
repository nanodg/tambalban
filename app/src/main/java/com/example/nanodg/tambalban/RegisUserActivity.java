package com.example.nanodg.tambalban;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nanodg.tambalban.Model.User;
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

public class RegisUserActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase database;
    DatabaseReference users;
    ProgressBar progressBar;
    EditText edemail,edusername,edpswd;
    Button btnsignup,btnlogin;
    TextView tvpemilik;
    FirebaseAuth mAuth;
    CheckBox pemilik;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis_user);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");
        pemilik = (CheckBox)findViewById(R.id.pemilik);
        edemail = (EditText)findViewById(R.id.edemail);
        edusername = (EditText)findViewById(R.id.edusername);
        edpswd = (EditText)findViewById(R.id.edpswd);
        tvpemilik = (TextView)findViewById(R.id.tvpemilik);
        btnsignup = (Button)findViewById(R.id.btnsignup);
        btnsignup.setOnClickListener(this);
        btnlogin = (Button)findViewById(R.id.btnlogin);
        tvpemilik.setText("0");
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        progressBar.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();

        btnlogin.setOnClickListener(new View.OnClickListener(){
            @Override
                    public void onClick( View view) {
                Intent s = new Intent(getApplicationContext(), LoginUserActivity.class);
                startActivity(s);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            //handle the already login user
        }
    }
    private void registerUser() {
        final String username = edusername.getText().toString().trim();
        final String email = edemail.getText().toString().trim();
        String password = edpswd.getText().toString().trim();
        final String pemilik = tvpemilik.getText().toString().trim();
        final String key = "-";

        if (email.isEmpty()) {
            edemail.setError(getString(R.string.input_error_email));
            edemail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edemail.setError(getString(R.string.input_error_email_invalid));
            edemail.requestFocus();
            return;
        }

        if (username.isEmpty()) {
            edusername.setError(getString(R.string.input_error_name));
            edusername.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            edpswd.setError(getString(R.string.input_error_password));
            edpswd.requestFocus();
            return;
        }

        if (password.length() < 8) {
            edpswd.setError(getString(R.string.input_error_password_length));
            edpswd.requestFocus();
            return;
        }



        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            User user = new User(
                                    key,email,username,pemilik

                            );

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        TastyToast.makeText(getApplicationContext(), getString(R.string.registration_success), TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                                        final DatabaseReference  mUserContactsRef =  FirebaseDatabase.getInstance().getReference().child("Users");
                                        mUserContactsRef.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(final DataSnapshot dataSnapshot) {

                                               // Log.e("barang1", dataSnapshot.toString());
                                                for (DataSnapshot userContact : dataSnapshot.getChildren()) {

                                                    User user = userContact.getValue(User.class);
                                                    if(user.getPemilik().equals("0")){
//                                                        mUserContactsRef.child("key").setValue(userContact.getKey());
                                                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(userContact.getKey().toString());
                                                        mDatabase.child("key").setValue(userContact.getKey());
                                                        TastyToast.makeText(getApplicationContext(), "User", TastyToast.LENGTH_LONG, TastyToast.INFO);
                                                        startActivity(new Intent(getApplicationContext(), PnlUserActivity.class));
                                                        finish();
                                                    } if(user.getPemilik().equals("1")) {
                                                        //mUserContactsRef.child("key").setValue(userContact.getKey());
                                                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(userContact.getKey().toString());
                                                        mDatabase.child("key").setValue(userContact.getKey());
                                                        user.setKey(userContact.getKey());
                                                        TastyToast.makeText(getApplicationContext(), "Pemilik", TastyToast.LENGTH_LONG, TastyToast.INFO);
                                                        startActivity(new Intent(getApplicationContext(), PnlPemilikActivity.class));
                                                        //Log.e("Data snapshot", "barang1" + user.getPemilik());
                                                        finish();
                                                    }
                                                }
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });
                                    } else {
                                        //display a failure message
                                    }
                                }
                            });

                        } else {

                            progressBar.setVisibility(View.GONE);
                            TastyToast.makeText(getApplicationContext(), task.getException().getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnsignup:
                registerUser();
                break;
        }
    }


    public static Intent getActIntent(Activity activity) {
        // kode untuk pengambilan Intent
        return new Intent(activity, LoginUserActivity.class);
    }

    public void itemClicked(View v) {
        //code to check if this checkbox is checked!
        //CheckBox checkBox = (CheckBox)v;
        String msg="0";

        if(pemilik.isChecked()){
            msg="1";
        }
        showTextNotification(msg);
    }


    public void showTextNotification(String msgToDisplay)
    {
        tvpemilik.setText(msgToDisplay);

    }

}
