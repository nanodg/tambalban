package com.example.nanodg.tambalban;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nanodg.tambalban.Model.Tambah;
import com.example.nanodg.tambalban.Model.User;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.List;

import ir.apend.slider.model.Slide;
import ir.apend.slider.ui.Slider;
import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;
import com.example.nanodg.tambalban.network.ApiServices;
import com.example.nanodg.tambalban.network.InitLibrary;
import com.example.nanodg.tambalban.response.Distance;
import com.example.nanodg.tambalban.response.Duration;
import com.example.nanodg.tambalban.response.LegsItem;
import com.example.nanodg.tambalban.response.ReponseRoute;
import com.sdsmdg.tastytoast.TastyToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class DtltambalActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener,View.OnClickListener  {

//    private Button bttlpn,btsms;
    TextView nama,tlp,jmbuka,jmtutup,hsl1,hsl2,alamat,tvbiasa,tvtubles,tvmotor,tvmobil,tvverif,hsl3,status,status1;
    EditText lat,lon;
    private TextView uri1,uri2,uri3;
    private Slider slider;;
    private GoogleMap mMap;
    LocationManager mLocationManager = null;
    String provider = null;
    Marker mCurrentPosition = null;
    ScrollView mScrollView;
    String hasil,verif;
    ImageView imgverif,imgbiasa,imgtub,imgmotor,imgmobil,stbuka,sttutup;
    ImageButton bttlpn,btsms;
    Button aduan;


    private String API_KEY = "AIzaSyBu1ueAsgh5rVX5GNxjogBa3J_afkCuXxw";


    private TextView tvStartAddress, tvEndAddress, tvDuration, tvDistance;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dtltambal);
        final ProgressDialog loading = ProgressDialog.show(this,"Mengambil data","Tunggu Sebentar...",false,false);
        nama = (TextView) findViewById(R.id.et_namatambal);
        tlp = (TextView) findViewById(R.id.et_nohp);
        jmbuka = (TextView) findViewById(R.id.et_jambuka);
        hsl2 = (TextView) findViewById(R.id.et_hsl2);
        alamat = (TextView) findViewById(R.id.et_alamat);
        lat = (EditText) findViewById(R.id.et_lat);
        aduan = (Button) findViewById(R.id.aduan);
        lon = (EditText) findViewById(R.id.et_long);
        uri1 = (TextView) findViewById(R.id.uri1);
        uri2 = (TextView) findViewById(R.id.uri2);
        uri3 = (TextView) findViewById(R.id.uri3);
        tvbiasa = (TextView) findViewById(R.id.tvbiasa);
        tvtubles = (TextView) findViewById(R.id.tvtubles);
        tvmotor = (TextView) findViewById(R.id.tvmotor);
        tvmobil = (TextView) findViewById(R.id.tvmobil);
        tvverif = (TextView) findViewById(R.id.tvverif);
        stbuka = (ImageView) findViewById(R.id.stbuka);
        sttutup= (ImageView) findViewById(R.id.sttutup);
        bttlpn = (ImageButton) findViewById(R.id.tlpn);
        btsms = (ImageButton) findViewById(R.id.sms);
        slider = (Slider) findViewById(R.id.slider);
        imgverif = (ImageView) findViewById(R.id.imgverif);
        imgbiasa = (ImageView) findViewById(R.id.imgbiasa);
        imgtub = (ImageView) findViewById(R.id.imgtub);
        imgmotor = (ImageView) findViewById(R.id.imgmotor);
        imgmobil = (ImageView) findViewById(R.id.imgmobil);

        lat.setText("-7.248651474442163");
        lon.setText("112.62898944318295");

        aduan.setOnClickListener(this);

        //btSubmit.setVisibility(View.GONE);

        //Tambah tambah = (Tambah) getIntent().getSerializableExtra("data");
        Intent intent = getIntent();
        hasil = intent.getStringExtra(MapsActivity.DATA);
            DatabaseReference mUserContactsRef = FirebaseDatabase.getInstance().getReference().child("tambah");
            mUserContactsRef.orderByChild("nama").equalTo(hasil).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {

                    //Log.e("barang1", dataSnapshot.toString());
                    for (DataSnapshot userContact : dataSnapshot.getChildren()) {

                        Tambah tambah = userContact.getValue(Tambah.class);
                        loading.dismiss();
                        nama.setText(tambah.getNama());
                        tlp.setText(tambah.getNo());
                        jmbuka.setText(tambah.getBuka()+" s/d "+(tambah.getTutup()));
                        //jmtutup.setText(tambah.getTutup());
//                        hsl1.setText(tambah.getBan());
//                        hsl2.setText(tambah.getKendaraan());
                        alamat.setText(tambah.getAlamat());
                        lat.setText(Double.toString(tambah.getLat()));
                        lon.setText(Double.toString(tambah.getLongt()));
                        uri1.setText(tambah.getFoto1());
                        uri2.setText(tambah.getFoto2());
                        uri3.setText(tambah.getFoto3());
                        verif = tambah.getVerif();
                        if(tambah.getVerif().equals("0")){
                           imgverif.setVisibility(View.GONE);
                            tvverif.setVisibility(View.GONE);
                        } if(tambah.getVerif().equals("1")) {
                            imgverif.setVisibility(View.VISIBLE);
                            tvverif.setVisibility(View.VISIBLE);
                        }if(tambah.getBan().equals("1")){
                            imgtub.setVisibility(View.GONE);
                            tvtubles.setVisibility(View.GONE);
                            imgbiasa.setVisibility(View.VISIBLE);
                            tvbiasa.setVisibility(View.VISIBLE);
                        }if(tambah.getBan().equals("2")){
                            imgbiasa.setVisibility(View.GONE);
                            tvbiasa.setVisibility(View.GONE);
                            imgtub.setVisibility(View.VISIBLE);
                            tvtubles.setVisibility(View.VISIBLE);
                        }if(tambah.getBan().equals("3")){
                            imgbiasa.setVisibility(View.VISIBLE);
                            tvbiasa.setVisibility(View.VISIBLE);
                            imgtub.setVisibility(View.VISIBLE);
                            tvtubles.setVisibility(View.VISIBLE);
                        }if(tambah.getKendaraan().equals("1")){
                            imgmobil.setVisibility(View.GONE);
                            tvmobil.setVisibility(View.GONE);
                            imgmotor.setVisibility(View.VISIBLE);
                            tvmotor.setVisibility(View.VISIBLE);
                        }if(tambah.getKendaraan().equals("2")){
                            imgmotor.setVisibility(View.GONE);
                            tvmotor.setVisibility(View.GONE);
                            imgmobil.setVisibility(View.VISIBLE);
                            tvmobil.setVisibility(View.VISIBLE);
                        }if(tambah.getKendaraan().equals("3")){
                            imgmotor.setVisibility(View.VISIBLE);
                            tvmotor.setVisibility(View.VISIBLE);
                            imgmobil.setVisibility(View.VISIBLE);
                            tvmobil.setVisibility(View.VISIBLE);
                        }if(tambah.getStatus().equals("1")){
                            stbuka.setVisibility(View.VISIBLE);
                            sttutup.setVisibility(View.GONE);
                        }if(tambah.getStatus().equals("0")){
                            stbuka.setVisibility(View.GONE);
                            sttutup.setVisibility(View.VISIBLE);
                        }
                        //coba(la1,lo2);

                        final List<Slide> slideList = new ArrayList<>();
                        slideList.add(new Slide(0,tambah.getFoto1() , getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));
                        slideList.add(new Slide(1,tambah.getFoto2() , getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));
                        slideList.add(new Slide(2,tambah.getFoto3(), getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));

                        slider.setItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                //do what you want
                                Toast.makeText(getApplicationContext(), "you clicked image " + (i+1), Toast.LENGTH_LONG).show();
                            }
                        });
                        slider.addSlides(slideList);

                        //TLPN
                        bttlpn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String phoneNo = tlp.getText().toString();
                                if(!TextUtils.isEmpty(phoneNo)) {
                                    String dial = "tel:" + phoneNo;
                                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
                                }else {
                                    Toast.makeText(DtltambalActivity.this, "Enter a phone number", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        //SMS
                        btsms.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //String message = messagetEt.getText().toString();
                                String phoneNo = tlp.getText().toString();
                                if(!TextUtils.isEmpty(phoneNo)) {
                                    Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNo));
                                    smsIntent.putExtra("sms_body", "");
                                    startActivity(smsIntent);
                                }
                            }
                        });



                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });


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

        widgetInit();



    }

//    public void coba(String la2, String lo3){
//
//        la4 = la2;
//        lo5 = lo3;
//        Log.e("barang23", la4);
//        Log.e("barang23", lo5);
//    }


    private void widgetInit() {
        tvStartAddress = (TextView)findViewById(R.id.tvStartAddress);
        tvEndAddress =(TextView) findViewById(R.id.tvEndAddress);
        tvDuration =(TextView) findViewById(R.id.tvDuration);
        tvDistance = (TextView)findViewById(R.id.tvDistance);
    }

    private void actionRoute(double laat, double lang) {
        //final ProgressDialog loading = ProgressDialog.show(this,"Mengambil data Rute","Tunggu Sebentar...",false,false);

        double tujuanlat = Double.parseDouble(lat.getText().toString().trim());
        double tujuanlong = Double.parseDouble(lon.getText().toString().trim());
        final LatLng awal = new LatLng(laat, lang);
        final LatLng tujuan = new LatLng(tujuanlat,tujuanlong);
       // Log.e("barang23", Double.toString(tujuanlat));
        //Log.e("barang23",Double.toString(tujuanlong));

        String lokasiAwal = awal.latitude + "," + awal.longitude;
        String lokasiAkhir = tujuan.latitude + "," + tujuan.longitude;

        //Log.e("Data snapshot","barang1"+lokasiAwal);
        //Log.e("Data snapshot","barang2"+lokasiAkhir);

        // Panggil Retrofit
        ApiServices api = InitLibrary.getInstance();
        // Siapkan request
        Call<ReponseRoute> routeRequest = api.request_route(lokasiAwal, lokasiAkhir, API_KEY);
        // kirim request
        //Log.e("Data snapshot","barang3"+routeRequest);
        routeRequest.enqueue(new Callback<ReponseRoute>() {
            @Override
            public void onResponse(Call<ReponseRoute> call, Response<ReponseRoute> response) {
                mMap.clear();
                if (response.isSuccessful()){
                    // tampung response ke variable
                    ReponseRoute dataDirection = response.body();

                    LegsItem dataLegs = dataDirection.getRoutes().get(0).getLegs().get(0);

                    //loading.dismiss();
                    //Log.e("Data snapshot","barang4"+dataLegs);
                    // Dapatkan garis polyline
                    String polylinePoint = dataDirection.getRoutes().get(0).getOverviewPolyline().getPoints();
                    // Decode
                    List<LatLng> decodePath = PolyUtil.decode(polylinePoint);
                    // Gambar garis ke maps
                    mMap.addPolyline(new PolylineOptions().addAll(decodePath)
                            .width(8f).color(Color.argb(255, 56, 167, 252)))
                            .setGeodesic(true);

                    // Tambah Marker
                    mMap.addMarker(new MarkerOptions().position(awal).title("Lokasi Awal"));
                    mMap.addMarker(new MarkerOptions().position(tujuan).title("Lokasi Akhir"));
                    // Dapatkan jarak dan waktu
                    Distance dataDistance = dataLegs.getDistance();
                    Duration dataDuration = dataLegs.getDuration();

                    // Set Nilai Ke Widget
                    tvStartAddress.setText("Lokasi Awal : " + dataLegs.getStartAddress().toString());
                    tvEndAddress.setText("Lokasi Tujuan : " + dataLegs.getEndAddress().toString());

                    tvDistance.setText("Jarak : " + dataDistance.getText() + " (" + dataDistance.getValue() + ")");
                    tvDuration.setText("Waktu : " + dataDuration.getText() + " (" + dataDuration.getValue() + ")");
                    /** START
                     * Logic untuk membuat layar berada ditengah2 dua koordinat
                     */

                    LatLngBounds.Builder latLongBuilder = new LatLngBounds.Builder();
                    latLongBuilder.include(awal);
                    latLongBuilder.include(tujuan);

                    // Bounds Coordinata
                    LatLngBounds bounds = latLongBuilder.build();

                    int width = getResources().getDisplayMetrics().widthPixels;
                    int height = getResources().getDisplayMetrics().heightPixels;
                    int paddingMap = (int) (width * 0.2); //jarak dari
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, paddingMap);
                    mMap.animateCamera(cu);

                    /** END
                     * Logic untuk membuat layar berada ditengah2 dua koordinat
                     */

                }
            }

            @Override
            public void onFailure(Call<ReponseRoute> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

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
            mLocationManager.requestLocationUpdates(provider, minTime, minDist, this);
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
            double laat = location.getLatitude();

            //addBoundaryToCurrentPosition(lat, lng);
            actionRoute(laat,lng);

            CameraPosition camPosition = new CameraPosition.Builder()
                    .target(new LatLng(laat, lng)).zoom(15f).build();

            if (mMap != null)
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(camPosition));
        } else {
            Log.d("Location error", "Something went wrong");
        }
    }

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

    public void onClick(View view) {

        if(view == aduan){
            finish();
            startActivity(new Intent(this, AduanActivity.class));
        }
    }
    public static Intent getActIntent(Activity activity){
        return new Intent(activity, DtltambalActivity.class);
    }

}