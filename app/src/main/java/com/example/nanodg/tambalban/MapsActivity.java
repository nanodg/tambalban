package com.example.nanodg.tambalban;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nanodg.tambalban.Model.Tambah;
import com.example.nanodg.tambalban.Model.User;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    TextView ban,kendaraan;
    Spinner spinner1,spinner2;
    String[] jenisban = {"Biasa","Tubles"};
    String[] jeniskendaraan = {"Motor","Mobil"};
    String jnban,jnkendaraan;
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
               refreshmap();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setOnMarkerClickListener(this);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

/**
 * =========================================SPINNER
 */
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MapsActivity.this, android.R.layout.simple_list_item_1, jenisban);
        spinner1.setAdapter(adapter);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(MapsActivity.this, android.R.layout.simple_list_item_1, jeniskendaraan);
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
    @Override
    public void onLocationChanged(Location location) {
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    @Override
    public void onProviderEnabled(String provider) {
    }
    @Override
    public void onProviderDisabled(String provider) {
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
    public static Intent getActIntent(Activity activity) {
        // kode untuk pengambilan Intent
        return new Intent(activity, MapsActivity.class);
    }

    private void refreshmap(){
        mMap.clear();
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
                        mMap.addMarker(new MarkerOptions().position(location).title(tambah.getNama())).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
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
                        mMap.addMarker(new MarkerOptions().position(location).title(tambah.getNama())).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
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

}