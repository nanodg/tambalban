package com.example.nanodg.tambalban;

import android.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener,GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    DatabaseReference mTambah;
    private ArrayList<Tambah> daftarTambal = new ArrayList<> ();
    private Map<Marker, Integer> markersOrderNumbers = new HashMap<>();
    private ArrayList<Integer> list = new ArrayList<>();
    Marker marker;
    int markerIndex = 0;
    int position = 0 ;
    Button tambah,tampil;
    public float jarak=0;
    TextView ban,kendaraan,numradius;
    Spinner spinner1,spinner2;
    String[] jenisban = {"Biasa","Tubles"};
    String[] jeniskendaraan = {"Motor","Mobil"};
    String jnban,jnkendaraan;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mTambah= FirebaseDatabase.getInstance().getReference();
        mTambah.push().setValue(marker);
        tambah = (Button) findViewById(R.id.tambah);
        tampil = (Button)findViewById(R.id.tampil);
        spinner1 = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner)findViewById(R.id.spinner2);
        ban = (TextView)findViewById(R.id.jenisban);
        kendaraan = (TextView)findViewById(R.id.jeniskendaraan);
        radius = (SeekBar)findViewById(R.id.radius);
        numradius = (TextView)findViewById(R.id.numradius);

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


    lingkaran = Float.valueOf(numradius.getText().toString());
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // kelas yang akan dijalankan ketika tombol Create/Insert Data diklik
                startActivity(LoginUserActivity.getActIntent(MapsActivity.this));
            }
        });

       tampil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // kelas yang akan dijalankan ketika tombol Create/Insert Data diklik
               //refreshmap();
                lingkaran = Float.valueOf(numradius.getText().toString());
                updateWithNewLocation(location);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setOnMarkerClickListener(this);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (isProviderAvailable() && (provider != null)) {
            locateCurrentPosition();
        }
/**
 * =========================================SPINNER
 */
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MapsActivity.this, R.layout.simple_list_item, jenisban);
        spinner1.setAdapter(adapter);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(MapsActivity.this, R.layout.simple_list_item, jeniskendaraan);
        spinner2.setAdapter(adapter2);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner1.getSelectedItem()==("Biasa")){
                   jnban = ("1");
                }
                if (spinner1.getSelectedItem()==("Tubles")){
                    jnban = ("2");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

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
            double lng = location.getLongitude();
            double lat = location.getLatitude();

            addBoundaryToCurrentPosition(lat, lng);
            refreshmap(lat,lng);

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

    private void addBoundaryToCurrentPosition(double lat, double lang) {

        MarkerOptions mMarkerOptions = new MarkerOptions();
        mMarkerOptions.position(new LatLng(lat, lang));
        mMarkerOptions.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.ic_place_black_24dp));
        mMarkerOptions.anchor(0.5f, 0.5f);
        mMarkerOptions.title("ini lokasi anda");
        mMarkerOptions.snippet("anda sekarang ada disini");

        mOptions = new CircleOptions()
                .center(new LatLng(lat, lang)).radius(lingkaran)
                .strokeColor(0x110000FF).strokeWidth(1).fillColor(0x110000FF);
      circle = mMap.addCircle(mOptions);
        mMap.addCircle(mOptions);
        if (mCurrentPosition != null)
            mCurrentPosition.remove();
        mCurrentPosition = mMap.addMarker(mMarkerOptions);
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
        return new Intent(activity, MapsActivity.class);
    }

    private void refreshmap(final double lat, final double lang){
        mMap.clear();
        final DecimalFormat formatDesimal = new DecimalFormat("#.##");
        saatIni.setLatitude(lat);
        saatIni.setLongitude(lang);
        final MarkerOptions mPosisi = new MarkerOptions();
        DatabaseReference  mUserContactsRef =  FirebaseDatabase.getInstance().getReference().child("tambah");
        mUserContactsRef.orderByChild("ban").equalTo(jnban).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Log.e("barang1", dataSnapshot.toString());
                for (DataSnapshot s : dataSnapshot.getChildren()){
                    final Tambah tambah = s.getValue(Tambah.class);
                    if(tambah.getKendaraan().equals(jnkendaraan)){

                        daftarTambal.add(tambah);
                        final Object row = (Object) tambah.getNama();

//                    Log.e("Data snapshot","barang1"+daftarTambal);
                        LatLng location = new LatLng(tambah.getLat(), tambah.getLongt());
                        lokMarker.setLatitude(tambah.getLat());
                        lokMarker.setLongitude(tambah.getLongt());
                        jarak = saatIni.distanceTo(lokMarker);
                        if(lingkaran >= jarak){
                            jarak = saatIni.distanceTo(lokMarker) / 1000;
                            mPosisi.position(location);
                            mPosisi.anchor(0.3f, 0.3f);
                            mPosisi.title(tambah.getNama());
                            mPosisi.snippet("Berjarak = " +formatDesimal.format(jarak)+ " km dari anda");
                            mMap.addMarker(mPosisi);
                            //mMap.addMarker(new MarkerOptions().position(location).title(tambah.getNama())).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

                        }
                       markersOrderNumbers.put(marker, position);
                        position++;

                        final Integer index = markersOrderNumbers.get(marker);
//                    list.add(index);
//                    final String name = daftarTambal.get(index).getNama();
                        Log.e("Data snapshot", "barang4" + tambah);

                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                            public void onInfoWindowClick(Marker marker) {
                                Log.e("Data snapshot", "barang4" + tambah);
                                Intent edit = new Intent(getApplicationContext(), DtltambalActivity.class);
                                //String reference = mMarkerPlaceLink.get(id);
                                //daftarBarang.add(barang);
                                //Integer index = markersOrderNumbers.get(marker);
                                edit.putExtra("data", daftarTambal.get(index));


                                //Log.e("Data snapshot","barang1"+daftarBarang.get(position));
                                //Log.e("Data snapshot","barang2"+daftarBarang);
                                startActivity(edit);

                            }

                        });
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mUserContactsRef.orderByChild("ban").equalTo("3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Log.e("barang1", dataSnapshot.toString());
                for (DataSnapshot s : dataSnapshot.getChildren()){
                    final Tambah tambah = s.getValue(Tambah.class);
                    if(tambah.getKendaraan().equals("3")){

                        daftarTambal.add(tambah);
                        final Object row = (Object) tambah.getNama();

//                    Log.e("Data snapshot","barang1"+daftarTambal);
                        LatLng location = new LatLng(tambah.getLat(), tambah.getLongt());
                        lokMarker.setLatitude(tambah.getLat());
                        lokMarker.setLongitude(tambah.getLongt());
                        jarak = saatIni.distanceTo(lokMarker);
                        if(lingkaran >= jarak){
                            jarak = saatIni.distanceTo(lokMarker) / 1000;
                            mPosisi.position(location);
                            mPosisi.anchor(0.3f, 0.3f);
                            mPosisi.title(tambah.getNama());
                            mPosisi.snippet("Berjarak = " +formatDesimal.format(jarak)+ " km dari anda");
                            mMap.addMarker(mPosisi);
                           // mMap.addMarker(new MarkerOptions().position(location).title(tambah.getNama())).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

                        }
                        markersOrderNumbers.put(marker, position);
                        position++;

                        final Integer index = markersOrderNumbers.get(marker);
//                    list.add(index);
//                    final String name = daftarTambal.get(index).getNama();
                        Log.e("Data snapshot", "barang4" + tambah);

                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                            public void onInfoWindowClick(Marker marker) {
                                Log.e("Data snapshot", "barang4" + tambah);
                                Intent edit = new Intent(getApplicationContext(), DtltambalActivity.class);
                                //String reference = mMarkerPlaceLink.get(id);
                                //daftarBarang.add(barang);
                                //Integer index = markersOrderNumbers.get(marker);
                                edit.putExtra("data", daftarTambal.get(index));


                                //Log.e("Data snapshot","barang1"+daftarBarang.get(position));
                                //Log.e("Data snapshot","barang2"+daftarBarang);
                                startActivity(edit);

                            }

                        });
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    /**
    * MAPS
     */


}