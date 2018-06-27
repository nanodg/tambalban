package com.example.nanodg.tambalban;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.nanodg.tambalban.R;
import com.example.nanodg.tambalban.Fragment.ChatsFragment;

public class PesanPemilikActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_pemilik);
        initComponent();
    }
    private void initComponent() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ChatsFragment ctf = new ChatsFragment();
        //icf.setRetainInstance(true);
        fragmentTransaction.add(R.id.main_container, ctf, "Chat History");
        fragmentTransaction.commit();

    }
}
