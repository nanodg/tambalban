package com.example.nanodg.tambalban;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.nanodg.tambalban.Adapter.CustomInfoWindowAdapter;
import com.example.nanodg.tambalban.Model.Bengkel;
import com.example.nanodg.tambalban.Model.Tambah;
import com.example.nanodg.tambalban.Model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import static com.example.nanodg.tambalban.R.layout.toolbar;

public class BengkelActivity extends AppCompatActivity implements OnMapReadyCallback,LocationListener,GoogleMap.OnMarkerClickListener{

    public static final String DATA = "com.example.nanodg.tambalban";
    private GoogleMap mMap;
    DatabaseReference mTambah;
    private ArrayList<Bengkel> daftarBengkel = new ArrayList<> ();
    private Map<Marker, Integer> markersOrderNumbers = new HashMap<>();
    private ArrayList<Integer> list = new ArrayList<>();
    Marker marker;
    int markerIndex = 0;
    int position = 0 ;
    public double lng,lat;
    public ProgressBar progressBar;
    Button tampil;
    public float jarak=0;
    Switch semua;
    TextView kendaraan,numradius;
    Spinner spinner2;
    String[] jeniskendaraan = {"Motor","Mobil"};
    String jnkendaraan;
    String provider = null;
    Marker mCurrentPosition = null;
    LocationManager mLocationManager = null;
    private Location location;
    private Location saatIni = new Location("saatIni");
    private Location lokMarker = new Location("lokMarker");
    private float lingkaran = 0;
    private Circle circle;
    private CircleOptions mOptions;
    SeekBar radius;
    private ActionBar actionBar;
    private String view;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bengkel);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mTambah= FirebaseDatabase.getInstance().getReference();
        mTambah.push().setValue(marker);
        tampil = (Button)findViewById(R.id.tampil);
        semua = (Switch)findViewById(R.id.semua);
        spinner2 = (Spinner)findViewById(R.id.spinner2);
        kendaraan = (TextView)findViewById(R.id.jeniskendaraan);
        radius = (SeekBar)findViewById(R.id.radius);
        numradius = (TextView)findViewById(R.id.numradius);
        initToolbar();
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
//        getPermissions();

        progressBar.setVisibility(View.GONE);
        radius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                String radiusValue = String.valueOf(i);

                numradius.setText(radiusValue);
            }
            @Override
            public void onStartTrackingTouch(SeekBar radius) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar radius) {

            }
        });


        semua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(semua.isChecked()) {
                    mMap.clear();
                    mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(BengkelActivity.this));
                    final MarkerOptions mPosisi = new MarkerOptions();

                    DatabaseReference database;
                    database = FirebaseDatabase.getInstance().getReference();
                    database.child("bengkel").orderByChild("nama").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot s : dataSnapshot.getChildren()){
                                final Bengkel bengkel = s.getValue(Bengkel.class);
                                daftarBengkel.add(bengkel);
                                LatLng location = new LatLng(bengkel.getLat(), bengkel.getLongt());
                                mPosisi.position(location);
                                mPosisi.title(bengkel.getNama());
                                mPosisi.snippet("Alamat : " +bengkel.getAlamat() +"\n" +
                                        "Jam Operasional : " +bengkel.getBuka()+" s/d "+bengkel.getTutup()+"\n"+
                                        "Deskripsi : "+"\n"+bengkel.getInfo());
                                mMap.addMarker(mPosisi);
                                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                                    public void onInfoWindowClick(Marker marker) {
                                        String id = marker.getTitle().toString();
                                        Intent edit = new Intent(getApplicationContext(), DtltambalActivity.class);
                                        edit.putExtra(DATA, id);
                                        startActivity(edit);
                                    }

                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
                        }
                    });

                }
                else {

                    mMap.clear();
                }
            }
        });

        lingkaran = Float.valueOf(numradius.getText().toString());


        tampil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // kelas yang akan dijalankan ketika tombol Create/Insert Data diklik
                //refreshmap();
                progressBar.setVisibility(View.VISIBLE);
                lingkaran = Float.valueOf(numradius.getText().toString());
                refreshmap(lat,lng);

            }
        });


/**
 * =========================================SPINNER
 */


        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(BengkelActivity.this, R.layout.simple_list_item,R.id.test, jeniskendaraan);
        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner2.getSelectedItem().equals("Motor")){
                    jnkendaraan = ("1");
                }
                if (spinner2.getSelectedItem().equals("Mobil")){
                    jnkendaraan =("2");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
/**
 * ==============================================================SPINNER
 */

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setOnMarkerClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (isProviderAvailable() && (provider != null)) {
            locateCurrentPosition();
        }
    }
    private void locateCurrentPosition() {

        int status = getPackageManager().checkPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION,
                getPackageName());

        if (status == PackageManager.PERMISSION_GRANTED) {
            location = mLocationManager.getLastKnownLocation(provider);
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
        mMap.clear();
        if (location != null && provider != null) {
            lng = location.getLongitude();
            lat = location.getLatitude();

            //addBoundaryToCurrentPosition(lat, lng);


            CameraPosition camPosition = new CameraPosition.Builder()
                    .target(new LatLng(lat, lng)).zoom(getZoomLevel(circle)).build();

            if (mMap != null)
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(camPosition));
        } else {
            Log.d("Location error", "Something went wrong");
        }
    }

    public int getZoomLevel(Circle circle) {
        int zoomLevel = 11;
        if (circle != null) {
            double radius = circle.getRadius() + circle.getRadius() / 2;
            double scale = radius / 500;
            zoomLevel = (int) (16 - Math.log(scale) / Math.log(2));
        }
        return zoomLevel;
    }


    public void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Bengkel");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                onBackPressed();

            }
        });

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
    @Override
    public void onLocationChanged(Location location) {
        updateWithNewLocation(location);
    }
    @Override
    public void onProviderEnabled(String provider) {

    }
    @Override
    public void onProviderDisabled(String provider) {
        updateWithNewLocation(null);
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
    public static Intent getActIntent(Activity activity) {
        // kode untuk pengambilan Intent
        return new Intent(activity, BengkelActivity.class);
    }

    private void refreshmap(final double lat, final double lang){
        progressBar.setVisibility(View.INVISIBLE);
        mMap.clear();
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(BengkelActivity.this));
        final DecimalFormat formatDesimal = new DecimalFormat("#.##");
        saatIni.setLatitude(lat);
        saatIni.setLongitude(lang);
        final MarkerOptions mPosisi = new MarkerOptions();
        DatabaseReference  mUserContactsRef =  FirebaseDatabase.getInstance().getReference().child("bengkel");
        mUserContactsRef.orderByChild("kendaraan").equalTo(jnkendaraan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                //Log.e("barang1", dataSnapshot.toString());
                for (DataSnapshot s : dataSnapshot.getChildren()){
                    final Bengkel bengkel = s.getValue(Bengkel.class);
//                    if(bengkel.getKendaraan().equals(jnkendaraan)){

                    daftarBengkel.add(bengkel);
                    final Object row = (Object) bengkel.getNama();
//                    Log.e("Data snapshot","barang1"+daftarTambal);
                    LatLng location = new LatLng(bengkel.getLat(), bengkel.getLongt());
                    lokMarker.setLatitude(bengkel.getLat());
                    lokMarker.setLongitude(bengkel.getLongt());
                    jarak = saatIni.distanceTo(lokMarker);
                    if(lingkaran >= jarak){
                        jarak = saatIni.distanceTo(lokMarker) / 1000;
                        mPosisi.position(location);
                        mPosisi.anchor(0.3f, 0.3f);
                        mPosisi.title(bengkel.getNama());
                        mPosisi.snippet("Alamat : " +bengkel.getAlamat()+" - " + formatDesimal.format(jarak) + " km dari anda" +"\n" +
                                "Jam Operasional : " +bengkel.getBuka()+" s/d "+bengkel.getTutup()+"\n"+
                                "Deskripsi : "+"\n"+bengkel.getInfo());
                        mMap.addMarker(mPosisi);
                        //mMap.addMarker(new MarkerOptions().position(location).title(tambah.getNama())).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

                    }

                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                        public void onInfoWindowClick(Marker marker) {
                            String id = marker.getTitle().toString();
                            //Log.e("Data snapshot", "barang45" + id);
                            Intent edit = new Intent(getApplicationContext(), DtlBengkelActivity.class);
                            //String reference = mMarkerPlaceLink.get(id);
                            //daftarBarang.add(barang);
                            //Integer index = markersOrderNumbers.get(marker);
//                                edit.putExtra("data", daftarTambal.get(index));

                            edit.putExtra(DATA, id);


                            //Log.e("Data snapshot","barang1"+daftarBarang.get(position));
                            //Log.e("Data snapshot","barang2"+daftarBarang);
                            startActivity(edit);

                        }

                    });

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        progressBar.setVisibility(View.INVISIBLE);
        mUserContactsRef.orderByChild("kendaraan").equalTo("3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                //Log.e("barang1", dataSnapshot.toString());
                for (DataSnapshot s : dataSnapshot.getChildren()){
                    final Bengkel bengkel = s.getValue(Bengkel.class);
//                    if(bengkel.getKendaraan().equals("3")){

                    daftarBengkel.add(bengkel);
                    final Object row = (Object) bengkel.getNama();

//                    Log.e("Data snapshot","barang1"+daftarTambal);
                    LatLng location = new LatLng(bengkel.getLat(), bengkel.getLongt());
                    lokMarker.setLatitude(bengkel.getLat());
                    lokMarker.setLongitude(bengkel.getLongt());
                    jarak = saatIni.distanceTo(lokMarker);
                    if(lingkaran >= jarak){
                        jarak = saatIni.distanceTo(lokMarker) / 1000;
                        mPosisi.position(location);
                        mPosisi.anchor(0.3f, 0.3f);
                        mPosisi.title(bengkel.getNama());
                        mPosisi.snippet("Alamat : " +bengkel.getAlamat()+" - " + formatDesimal.format(jarak) + " km dari anda" +"\n" +
                                "Jam Operasional : " +bengkel.getBuka()+" s/d "+bengkel.getTutup()+"\n"+
                                "Deskripsi : "+"\n"+bengkel.getInfo());
                        mMap.addMarker(mPosisi);
                        // mMap.addMarker(new MarkerOptions().position(location).title(tambah.getNama())).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

                    }


                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                        public void onInfoWindowClick(Marker marker) {
                            String id = marker.getTitle().toString();
                            // Log.e("Data snapshot", "barang45" + id);
                            Intent edit = new Intent(getApplicationContext(), DtlBengkelActivity.class);
                            //String reference = mMarkerPlaceLink.get(id);
                            //daftarBarang.add(barang);
                            //Integer index = markersOrderNumbers.get(marker);
//                                edit.putExtra("data", daftarTambal.get(index));
                            edit.putExtra(DATA, id);


                            //Log.e("Data snapshot","barang1"+daftarBarang.get(position));
                            //Log.e("Data snapshot","barang2"+daftarBarang);
                            startActivity(edit);

                        }

                    });

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

}