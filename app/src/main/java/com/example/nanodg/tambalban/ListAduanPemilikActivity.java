package com.example.nanodg.tambalban;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationListener;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nanodg.tambalban.Model.Aduan;
import com.example.nanodg.tambalban.Model.Tambah;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListAduanPemilikActivity extends AppCompatActivity{

    EditText edemail, nama, tgl,keterangan;
    Button selesai;
    TextView sts,hslsts;
    public String ststus="1";
    DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_aduan_pemilik);
        edemail = (EditText) findViewById(R.id.email);
        nama = (EditText) findViewById(R.id.nama);
        tgl = (EditText) findViewById(R.id.tgl);
        keterangan = (EditText) findViewById(R.id.keterangan);
        selesai = (Button) findViewById(R.id.selesai);
        sts = (TextView) findViewById(R.id.sts);
        hslsts = (TextView) findViewById(R.id.hslsts);


        final Aduan aduan = (Aduan) getIntent().getSerializableExtra("data");
        if (aduan != null) {
            edemail.setText(aduan.getPembuat());
            nama.setText(aduan.getNamatambal());
            tgl.setText(aduan.getTanggal());
            keterangan.setText(aduan.getKeterangan());
            hslsts.setText(aduan.getStatus());
            selesai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    aduan.setStatus(sts.getText().toString());

                    updateAduan(aduan);
                }
            });
        }
    }

    public void updateAduan(Aduan aduan) {
        Log.e("Data snapshot", "email" + aduan);
        database.child("aduan") //akses parent index, ibaratnya seperti nama tabel
                .child(aduan.getKey()) //select barang berdasarkan key
                .setValue(aduan) //set value barang yang baru
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Snackbar.make(findViewById(R.id.simpan), "Data berhasil diupdatekan", Snackbar.LENGTH_LONG).setAction("Oke", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        }).show();
                    }
                });
    }

    public static Intent getActIntent(Activity activity){
        return new Intent(activity, ListAduanPemilikActivity.class);
    }
}
