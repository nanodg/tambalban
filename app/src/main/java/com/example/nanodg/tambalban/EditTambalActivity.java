package com.example.nanodg.tambalban;

import android.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.nanodg.tambalban.Adapter.AdapterTambalRecyclerView;
import com.example.nanodg.tambalban.Adapter.WorkaroundMapFragment;
import com.example.nanodg.tambalban.Model.Tambah;
import com.example.nanodg.tambalban.Model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.Calendar;

public class EditTambalActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, View.OnClickListener {

    //upload images
    private static final int PICK_IMAGE_REQUEST1 = 234;
    private static final int PICK_IMAGE_REQUEST2 = 235;
    private static final int PICK_IMAGE_REQUEST3 = 236;
    Uri filePath1,filePath2,filePath3;
    String FileName1,FileName2,FileName3;

    EditText pembuat, nama, no, alamat,image1,image2,image3,edinfo;
    TextView tvjambuka, tvjamtutup, lat, longt, useremail,uri1,uri2,uri3,hsl1,hsl2,proses1,proses2,proses3,pemilik,hsl3,verif,tvvrf;
    Button btbuka, bttutup, btlogout,simpan;
    TimePickerDialog timePickerDialog1,timePickerDialog2;
    CheckBox biasa, tubles, Motor, Mobil;
    ProgressBar androidProgressBar1,androidProgressBar2,androidProgressBar3;
    ImageButton pilih1,pilih2,pilih3;
    // variable yang merefers ke Firebase Realtime Database
    private DatabaseReference database;
    //maps
    GoogleMap mMap;
    LocationManager mLocationManager = null;
    String provider = null;
    Marker mCurrentPosition = null;
    ScrollView mScrollView;
    Switch sw_verif;
    String hasil;
    FirebaseAuth firebaseAuth;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tambal);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        pembuat = (EditText) findViewById(R.id.addusername);
        nama = (EditText) findViewById(R.id.addnama);
        no = (EditText) findViewById(R.id.addno_tlp);
        alamat = (EditText) findViewById(R.id.alamat);
        edinfo = (EditText) findViewById(R.id.edinfo);
        pemilik = (TextView) findViewById(R.id.tvpemilik);
        sw_verif = (Switch) findViewById(R.id.sw_verif);
        hsl3 = (TextView) findViewById(R.id.hsl3);
        verif = (TextView) findViewById(R.id.tvverif);
        tvvrf = (TextView) findViewById(R.id.tvvrf);
        // mengambil referensi ke Firebase Database
        database = FirebaseDatabase.getInstance().getReference();
        simpan = (Button) findViewById(R.id.simpan);
        //final Tambah tambah = (Tambah) getIntent().getSerializableExtra("data");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Edit Tambal Ban");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                onBackPressed();

            }
        });

        /**
         * Jam Operasional
         */
        tvjambuka = (TextView) findViewById(R.id.tv_jambuka);
        tvjamtutup = (TextView) findViewById(R.id.tv_jamtutup);
        btbuka = (Button) findViewById(R.id.jambuka);
        bttutup = (Button) findViewById(R.id.jamtutup);
        lat = (TextView) findViewById(R.id.edlat);
        longt = (TextView) findViewById(R.id.edlong);
        btbuka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jambuka();
            }
        });
        bttutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jamtutup();
            }
        });

        /**
         * Jenis Ban & Kendaraan
         */
        biasa = (CheckBox) findViewById(R.id.cb_biasa);
        tubles = (CheckBox) findViewById(R.id.cb_tubles);
        Motor = (CheckBox) findViewById(R.id.cb_sepedamotor);
        Mobil = (CheckBox) findViewById(R.id.cb_roda4);
        hsl1 = (TextView) findViewById(R.id.hsl1);
        hsl2 = (TextView) findViewById(R.id.hsl2);

        /**
         * MAPS ON SCROLL VIEW
         */
        if (mMap == null) {
            //mMap = ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            SupportMapFragment mMap = (WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mMap.getMapAsync(this);

            mScrollView = (ScrollView) findViewById(R.id.scrollView); //parent scrollview in xml, give your scrollview id value
            ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .setListener(new WorkaroundMapFragment.OnTouchListener() {
                        @Override
                        public void onTouch() {
                            mScrollView.requestDisallowInterceptTouchEvent(true);
                        }
                    });

        }


        /**
         * Upload Images
         */
        pilih1 = (ImageButton) findViewById(R.id.buttonChoose1);
        uri1 = (TextView) findViewById(R.id.uri1);
        proses1 = (TextView) findViewById(R.id.proses1);
        pilih2 = (ImageButton) findViewById(R.id.buttonChoose2);
        uri2 = (TextView) findViewById(R.id.uri2);
        proses2 = (TextView) findViewById(R.id.proses2);
        pilih3 = (ImageButton) findViewById(R.id.buttonChoose3);
        uri3 = (TextView) findViewById(R.id.uri3);
        proses3 = (TextView) findViewById(R.id.proses3);
        image1 = (EditText) findViewById(R.id.image1);
        image2 = (EditText) findViewById(R.id.image2);
        image3 = (EditText) findViewById(R.id.image3);
        androidProgressBar1 = (ProgressBar) findViewById(R.id.horizontal_progress_bar1);
        androidProgressBar2 = (ProgressBar) findViewById(R.id.horizontal_progress_bar2);
        androidProgressBar3 = (ProgressBar) findViewById(R.id.horizontal_progress_bar3);


        //attaching listener
        pilih1.setOnClickListener(this);
        pilih2.setOnClickListener(this);
        pilih3.setOnClickListener(this);
        //simpan.setOnClickListener(this);

        Intent intent = getIntent();
        hasil = intent.getStringExtra(AdapterTambalRecyclerView.DATA);
        DatabaseReference mUserContactsRef = FirebaseDatabase.getInstance().getReference().child("tambah");
        mUserContactsRef.orderByChild("nama").equalTo(hasil).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                //Log.e("barang1", dataSnapshot.toString());
                for (DataSnapshot userContact : dataSnapshot.getChildren()) {

                    final Tambah tambah = userContact.getValue(Tambah.class);
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("tambah").child(userContact.getKey().toString());
                    mDatabase.child("key").setValue(userContact.getKey());
                    pembuat.setText(tambah.getPembuat());
                    nama.setText(tambah.getNama());
                    alamat.setText(tambah.getAlamat());
                    no.setText(tambah.getNo());
                    tvjambuka.setText(tambah.getBuka());
                    tvjamtutup.setText(tambah.getTutup());
                    hsl1.setText(tambah.getBan());
                    hsl2.setText(tambah.getKendaraan());
                    uri1.setText(tambah.getFoto1());
                    uri2.setText(tambah.getFoto2());
                    uri3.setText(tambah.getFoto3());
                    lat.setText(tambah.getLat().toString());
                    longt.setText(tambah.getLongt().toString());
                    pemilik.setText(tambah.getPemilik());
                    hsl3.setText(tambah.getVerif());
                    edinfo.setText(tambah.getInfo());
                    simpan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tambah.setPembuat(pembuat.getText().toString());
                            tambah.setNama(nama.getText().toString());
                            tambah.setNo(no.getText().toString());
                            tambah.setBuka(tvjambuka.getText().toString());
                            tambah.setTutup(tvjamtutup.getText().toString());
                            tambah.setBan(hsl1.getText().toString());
                            tambah.setKendaraan(hsl2.getText().toString());
                            tambah.setAlamat(alamat.getText().toString());
                            tambah.setLat(Double.parseDouble(lat.getText().toString()));
                            tambah.setLongt(Double.parseDouble(longt.getText().toString()));
                            tambah.setFoto1(uri1.getText().toString());
                            tambah.setFoto2(uri2.getText().toString());
                            tambah.setFoto3(uri3.getText().toString());
                            tambah.setPemilik(pemilik.getText().toString());
                            tambah.setVerif(hsl3.getText().toString());
                            tambah.setInfo(edinfo.getText().toString());


                            updateTambah(tambah);
                        }
                    });


                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        sw_verif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sw_verif.isChecked()) {
                    hsl3.setText("1");
                }
                else {
                    hsl3.setText("0");

                }
            }
        });

        if(hsl1.getText().equals("1")){
            biasa.setChecked(true);
        }
        if(hsl1.getText().equals("2")){
            tubles.setChecked(true);
        }
        if(hsl1.getText().equals("3")){
            biasa.setChecked(true);
            tubles.setChecked(true);
        }
        if(hsl2.getText().equals("1")){
            Motor.setChecked(true);
        }
        if(hsl2.getText().equals("2")){
            Mobil.setChecked(true);
        }
        if(hsl2.getText().equals("3")){
            Motor.setChecked(true);
            Mobil.setChecked(true);
        }
        if(hsl3.getText().equals("1")){
            sw_verif.setChecked(true);
        }
        if(hsl3.getText().equals("0")){
            sw_verif.setChecked(false);
        }
    }

    /**
     * UPDATE
     */

    private void updateTambah(Tambah tambah) {
        /**
         * Baris kode yang digunakan untuk mengupdate data barang
         * yang sudah dimasukkan di Firebase Realtime Database
         */
        database.child("tambah") //akses parent index, ibaratnya seperti nama tabel
                .child(tambah.getKey()) //select barang berdasarkan key
                .setValue(tambah) //set value barang yang baru
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        /**
                         * Baris kode yang akan dipanggil apabila proses update tambah sukses
                         */
                        Snackbar.make(findViewById(R.id.simpan), "Data berhasil diupdatekan", Snackbar.LENGTH_LONG).setAction("Oke", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        }).show();
                    }
                });
    }

    /**
     * Edit CheckBox
     */
    public void cekcb(){

    }


    /**
     * CheckBox
     */
    public void itemClicked(View v) {
        //code to check if this checkbox is checked!
        //CheckBox checkBox = (CheckBox)v;
        String msg="";
        if(biasa.isChecked()){
            msg="1";
        }
        if(tubles.isChecked()){
            msg="2";
        }
        if(biasa.isChecked() && tubles.isChecked() ){
            msg="3";
        }
        //showTextNotification(msg);
        String msg1="";
        if(Motor.isChecked()){
            msg1="1";
        }
        if(Mobil.isChecked()){
            msg1="2";
        }
        if(Motor.isChecked() && Mobil.isChecked() ){
            msg1="3";
        }

        showTextNotification(msg,msg1);
    }

    public void showTextNotification(String msgToDisplay, String msg1ToDisplay)
    {
        hsl1.setText(msgToDisplay);
        hsl2.setText(msg1ToDisplay);
    }

    /**
     * Upload Images
     */
    //method to show file chooser
    private void showFileChooser1() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST1);
    }
    private void showFileChooser2() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST2);
    }
    private void showFileChooser3() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST3);
    }

    //handling the image chooser activity result

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath1 = data.getData();
            FileName1 = data.getData().getLastPathSegment();
            image1.setText(FileName1);
            uploadFile1();
        }

        if (requestCode == PICK_IMAGE_REQUEST2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath2 = data.getData();
            FileName2 = data.getData().getLastPathSegment();
            image2.setText(FileName2);
            uploadFile2();
        }
        if (requestCode == PICK_IMAGE_REQUEST3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath3 = data.getData();
            FileName3 = data.getData().getLastPathSegment();
            image3.setText(FileName3);
            uploadFile3();

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
                            image1.setText(FileName1);

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
                            androidProgressBar1.setProgress((int) progress);
                            proses1.setText((int) progress + "%");

                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }

    private void uploadFile2() {
        //if there is a file to upload
        uri2.setText(null);
        if (isEmpty(uri2.getText().toString())) {
            FirebaseStorage storage2 = FirebaseStorage.getInstance();
            StorageReference riversRef2 = storage2.getReferenceFromUrl("gs://tambalban-ecd4b.appspot.com").child("Images").child(FileName2);
            riversRef2.putFile(filePath2)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot2) {
//                            progressDialog.dismiss();
                            String imageUploadInfo2 = (taskSnapshot2.getDownloadUrl().toString());
                            //Log.d("downloadUrl", "" + downloadUrl);
                            uri2.setText(imageUploadInfo2);
                            image2.setText(FileName2);

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded 2 ", Toast.LENGTH_LONG).show();
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
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot2) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot2.getBytesTransferred()) / taskSnapshot2.getTotalByteCount();
                            androidProgressBar2.setProgress((int) progress);
                            proses2.setText((int) progress + "%");

                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }
    private void uploadFile3() {
        //if there is a file to upload
        uri3.setText(null);
        if(isEmpty(uri3.getText().toString())) {
            FirebaseStorage storage3 = FirebaseStorage.getInstance();
            StorageReference riversRef3 = storage3.getReferenceFromUrl("gs://tambalban-ecd4b.appspot.com").child("Images").child(FileName3);
            riversRef3.putFile(filePath3)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot3) {
//                            progressDialog.dismiss();
                            String imageUploadInfo3 = (taskSnapshot3.getDownloadUrl().toString());
                            //Log.d("downloadUrl", "" + downloadUrl);
                            uri3.setText(imageUploadInfo3);
                            image3.setText(FileName3);

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded 3 ", Toast.LENGTH_LONG).show();
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
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot3) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot3.getBytesTransferred()) / taskSnapshot3.getTotalByteCount();
                            androidProgressBar3.setProgress((int) progress);
                            proses3.setText((int) progress + "%");

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

    public static Intent getActIntent(Activity activity) {
        // kode untuk pengambilan Intent
        return new Intent(activity, EditTambalActivity.class);
    }
    @Override
    public void onClick(View view) {
        //if the clicked button is choose
        if (view == pilih1) {
            showFileChooser1();
        }
        if (view == pilih2) {
            showFileChooser2();
        }
        if (view == pilih3) {
            showFileChooser3();
        }
        //if the clicked button is upload
    }


    /**
     * MAPS
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                MarkerOptions options = new MarkerOptions();
                options.position(latLng);

                mMap.addMarker(options);

                lat.setText(Double.toString(latLng.latitude));
                longt.setText(Double.toString(latLng.longitude));
            }
        });

        if (isProviderAvailable() && (provider != null)) {
            locateCurrentPosition();
        }

    }

    private void locateCurrentPosition() {

        int status = getPackageManager().checkPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION,
                getPackageName());

        if (status == PackageManager.PERMISSION_GRANTED) {
            Location location = mLocationManager.getLastKnownLocation(provider);
            updateWithNewLocation(location);

            mMap.setMyLocationEnabled(true);
            //  mLocationManager.addGpsStatusListener(this);
            long minTime = 5000;// ms
            float minDist = 5.0f;// meter
            mLocationManager.requestLocationUpdates(provider, minTime, minDist,
                    this);
        }
    }


    private boolean isProviderAvailable() {
        mLocationManager = (LocationManager) getSystemService(
                Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);

        provider = mLocationManager.getBestProvider(criteria, true);
        if (mLocationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;

            return true;
        }

        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
            return true;
        }

        if (provider != null) {
            return true;
        }
        return false;
    }

    private void updateWithNewLocation(Location location) {

        if (location != null && provider != null) {
            double lng = location.getLongitude();
            double lat = location.getLatitude();

//            addBoundaryToCurrentPosition(lat, lng);

            CameraPosition camPosition = new CameraPosition.Builder()
                    .target(new LatLng(lat, lng)).zoom(15f).build();

            if (mMap != null)
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(camPosition));
        } else {
            Log.d("Location error", "Something went wrong");
        }
    }

//    private void addBoundaryToCurrentPosition(double lat, double lang) {
//
//        MarkerOptions mMarkerOptions = new MarkerOptions();
//        mMarkerOptions.position(new LatLng(lat, lang));
//        mMarkerOptions.icon(BitmapDescriptorFactory
//                .fromResource(R.drawable.ic_place_black_24dp));
//        mMarkerOptions.anchor(0.5f, 0.5f);
//
//        CircleOptions mOptions = new CircleOptions()
//                .center(new LatLng(lat, lang)).radius(1000)
//                .strokeColor(0x110000FF).strokeWidth(1).fillColor(0x110000FF);
//        mMap.addCircle(mOptions);
//        if (mCurrentPosition != null)
//            mCurrentPosition.remove();
//        mCurrentPosition = mMap.addMarker(mMarkerOptions);
//    }


    @Override
    public void onLocationChanged(Location location) {

        updateWithNewLocation(location);
    }

    @Override
    public void onProviderDisabled(String provider) {

        updateWithNewLocation(null);
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                break;
            case LocationProvider.AVAILABLE:
                break;
        }
    }

    /**
     * JAM
     */
    private void jambuka() {

        /**
         * Calendar untuk mendapatkan waktu saat ini
         */
        Calendar calendar = Calendar.getInstance();

        /**
         * Initialize TimePicker Dialog
         */
        //JAM BUKA
        timePickerDialog1 = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                /**
                 * Method ini dipanggil saat kita selesai memilih waktu di DatePicker
                 */
                tvjambuka.setText(hourOfDay+":"+minute);

            }
        },
                /**
                 * Tampilkan jam saat ini ketika TimePicker pertama kali dibuka
                 */
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),

                /**
                 * Cek apakah format waktu menggunakan 24-hour format
                 */
                DateFormat.is24HourFormat(this));
        timePickerDialog1.show();
    }

    private void jamtutup() {

        /**
         * Calendar untuk mendapatkan waktu saat ini
         */
        Calendar calendar = Calendar.getInstance();

        /**
         * Initialize TimePicker Dialog
         */
        //JAM BUKA
        timePickerDialog2 = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                /**
                 * Method ini dipanggil saat kita selesai memilih waktu di DatePicker
                 */
                tvjamtutup.setText(hourOfDay+":"+minute);

            }
        },
                /**
                 * Tampilkan jam saat ini ketika TimePicker pertama kali dibuka
                 */
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),

                /**
                 * Cek apakah format waktu menggunakan 24-hour format
                 */
                DateFormat.is24HourFormat(this));
        timePickerDialog2.show();
    }

}

