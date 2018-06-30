package com.example.nanodg.tambalban;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nanodg.tambalban.Model.Aduan;
import com.example.nanodg.tambalban.Model.Tambah;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class AduanActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tgl,namafile,uri1,proses,pemilik;
    Spinner kategori;
    EditText keterangan,pembuat,hsl,nama;
    Button simpan,upload;
    String[] jeniskategori = {"Informasi Umum","Kritik dan Saran","lainya"};
    String hasil;
    private static final int PICK_IMAGE_REQUEST1 = 234;
    Uri filePath1;
    String FileName1;
    public String status = "0";
    ProgressBar progressBar;
    private DatabaseReference database;
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aduan);

        tgl = (TextView) findViewById(R.id.tgl);
        namafile = (TextView) findViewById(R.id.namafile);
        kategori = (Spinner) findViewById(R.id.kategori);
        keterangan = (EditText) findViewById(R.id.edket);
        pembuat = (EditText) findViewById(R.id.email);
        nama = (EditText) findViewById(R.id.nama);
        pemilik = (TextView) findViewById(R.id.pemilik);
        hsl = (EditText) findViewById(R.id.hsl);
        simpan = (Button) findViewById(R.id.simpan);
        upload = (Button) findViewById(R.id.file);
        uri1 = (TextView) findViewById(R.id.uri1);
        proses = (TextView) findViewById(R.id.proses);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        String tanggal_sekarang = getCurrentDate();
        tgl.setText(tanggal_sekarang);
        simpan.setOnClickListener(this);
        upload.setOnClickListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AduanActivity.this, R.layout.simple_kategori,R.id.test, jeniskategori);
        kategori.setAdapter(adapter);
        initToolbar();
        kategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (kategori.getSelectedItem()==("Informasi Umum")){
                    hsl.setText("1");
                }
                if (kategori.getSelectedItem()==("Kritik dan Saran")){
                    hsl.setText("2");
                }
                if (kategori.getSelectedItem()==("lainya")){
                    hsl.setText("3");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        database = FirebaseDatabase.getInstance().getReference();
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEmpty(pembuat.getText().toString()) && !isEmpty(hsl.getText().toString()) && !isEmpty(keterangan.getText().toString()))
                    submitBarang(new Aduan(pembuat.getText().toString(), tgl.getText().toString(), keterangan.getText().toString(), uri1.getText().toString(), hsl.getText().toString(), nama.getText().toString(),pemilik.getText().toString(),status.toString()));
                else
                    Snackbar.make(findViewById(R.id.simpan), "Data barang tidak boleh kosong", Snackbar.LENGTH_LONG).show();

                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(
                        pembuat.getWindowToken(), 0);
            }
        });

        Intent intent = getIntent();
        String hslnama = intent.getStringExtra(DtltambalActivity.DATA);
        //nama.setText(hslnama);
//        Log.e("Data snapshot","barang4"+hslnama);
        DatabaseReference mUserContactsRef = FirebaseDatabase.getInstance().getReference().child("tambah");
        mUserContactsRef.orderByChild("nama").equalTo(hslnama).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Log.e("Data snapshot","barang5"+dataSnapshot);
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    final Tambah tambah = s.getValue(Tambah.class);
                    pemilik.setText(tambah.getPembuat());
                    nama.setText(tambah.getNama());
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
        actionBar.setTitle("Aduan");
    }

    public String getCurrentDate(){
        final Calendar c = Calendar.getInstance();
        int year, month, day;
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DATE);
        return day + "/" + (month+1) + "/" + year;
    }

    private void showFileChooser1() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST1);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath1 = data.getData();
            FileName1 = data.getData().getLastPathSegment();
            namafile.setText(FileName1);
            uploadFile1();
        }

        //if there is not any file
        else {
            //you can display an error toast
        }

    }

    private void uploadFile1() {
        //if there is a file to upload
        uri1.setText(null);
        if (isEmpty(uri1.getText().toString()) ) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference riversRef = storage.getReferenceFromUrl("gs://tambalban-ecd4b.appspot.com").child("Images").child(FileName1);
            riversRef.putFile(filePath1)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot1) {
//                            progressDialog.dismiss();
                            String imageUploadInfo = (taskSnapshot1.getDownloadUrl().toString());
                            //Log.d("downloadUrl", "" + downloadUrl);

                            uri1.setText(imageUploadInfo);
                            namafile.setText(FileName1);

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded 1", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot1) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot1.getBytesTransferred()) / taskSnapshot1.getTotalByteCount();
                            progressBar.setProgress((int) progress);
                            proses.setText((int) progress + "%");

                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }

    private boolean isEmpty(String s){
        // Cek apakah ada fields yang kosong, sebelum disubmit
        return TextUtils.isEmpty(s);
    }



    public void onClick(View view) {
        //if the clicked button is choose
        if (view == upload) {
            showFileChooser1();
        }
    }

    private void submitBarang(Aduan aduan) {
        /**
         * Ini adalah kode yang digunakan untuk mengirimkan data ke Firebase Realtime Database
         * dan juga kita set onSuccessListener yang berisi kode yang akan dijalankan
         * ketika data berhasil ditambahkan
         */
        database.child("aduan").push().setValue(aduan).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pembuat.setText("");
                hsl.setText("");
                keterangan.setText("");
                uri1.setText("");
                nama.setText("");

                Snackbar.make(findViewById(R.id.simpan), "Data berhasil ditambahkan", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public static Intent getActIntent(Activity activity) {
        // kode untuk pengambilan Intent
        return new Intent(activity, AduanActivity.class);
    }
}
