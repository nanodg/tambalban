package com.example.nanodg.tambalban;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import com.example.nanodg.tambalban.Adapter.AdapterBengkelRecyclerView;
import com.example.nanodg.tambalban.Adapter.AdapterTambalRecyclerView;
import com.example.nanodg.tambalban.Adapter.WorkaroundMapFragment;
import com.example.nanodg.tambalban.Model.Bengkel;
import com.example.nanodg.tambalban.Model.Tambah;
import com.example.nanodg.tambalban.ir.apend.slider.model.Slide;
import com.example.nanodg.tambalban.ir.apend.slider.ui.Slider;
import com.example.nanodg.tambalban.network.ApiServices;
import com.example.nanodg.tambalban.network.InitLibrary;
import com.example.nanodg.tambalban.response.Distance;
import com.example.nanodg.tambalban.response.Duration;
import com.example.nanodg.tambalban.response.LegsItem;
import com.example.nanodg.tambalban.response.ReponseRoute;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;


public class FirebaseDBReadSingleBengkelActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener,View.OnClickListener  {

    //    private Button bttlpn,btsms;
    public static final String DATA = "com.example.nanodg.tambalban";
    TextView nama,tlp,jmbuka,hsl2,alamat,tvmotor,tvmobil,tvverif,hsl3,status,status1,info;
    EditText lat,lon;
    private TextView uri1,uri2,uri3;
    private Slider slider;
    private GoogleMap mMap;
    public String idkey,email;
    LocationManager mLocationManager = null;
    String provider = null;
    Marker mCurrentPosition = null;
    ScrollView mScrollView;
    String hasil,verif;

    ImageView imgverif,imgbiasa,imgtub,imgmotor,imgmobil,stbuka,sttutup;
    ImageButton bttlpn,btsms;
    Button aduan,chat;
    private ActionBar actionBar;
    double lng=0,laat=0;
    private String API_KEY = "AIzaSyBu1ueAsgh5rVX5GNxjogBa3J_afkCuXxw";


    private TextView tvStartAddress, tvEndAddress, tvDuration, tvDistance;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dtl_bengkel);

        nama = (TextView) findViewById(R.id.et_namatambal);
        tlp = (TextView) findViewById(R.id.et_nohp);
        jmbuka = (TextView) findViewById(R.id.et_jambuka);
        hsl2 = (TextView) findViewById(R.id.et_hsl2);
        alamat = (TextView) findViewById(R.id.et_alamat);
        lat = (EditText) findViewById(R.id.et_lat);
        aduan = (Button) findViewById(R.id.aduan);
        chat = (Button) findViewById(R.id.chat);
        info = (TextView) findViewById(R.id.info);
        lon = (EditText) findViewById(R.id.et_long);
        uri1 = (TextView) findViewById(R.id.uri1);
        uri2 = (TextView) findViewById(R.id.uri2);
        uri3 = (TextView) findViewById(R.id.uri3);
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
        initToolbar();

//        lat.setText("-7.248651474442163");
//        lon.setText("112.62898944318295");

        aduan.setOnClickListener(this);
        chat.setOnClickListener(this);

        //btSubmit.setVisibility(View.GONE);

        //Tambah tambah = (Tambah) getIntent().getSerializableExtra("data");
        Intent intent = getIntent();
        hasil = intent.getStringExtra(AdapterBengkelRecyclerView.DATA);
        DatabaseReference mUserContactsRef = FirebaseDatabase.getInstance().getReference().child("bengkel");
        mUserContactsRef.orderByChild("nama").equalTo(hasil).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                //Log.e("barang1", dataSnapshot.toString());
                for (DataSnapshot userContact : dataSnapshot.getChildren()) {

                    Bengkel bengkel = userContact.getValue(Bengkel.class);

                    idkey = userContact.getKey();
                    nama.setText(bengkel.getNama());
                    email = bengkel.getPembuat();
                    tlp.setText(bengkel.getNo());
                    jmbuka.setText(bengkel.getBuka()+" s/d "+(bengkel.getTutup()));
                    //jmtutup.setText(tambah.getTutup());
//                        hsl1.setText(tambah.getBan());
//                        hsl2.setText(tambah.getKendaraan());
                    alamat.setText(bengkel.getAlamat());

                    lat.setText(Double.toString(bengkel.getLat()));
                    lon.setText(Double.toString(bengkel.getLongt()));

                    if(lat.getText() != null && lon.getText() !=null){
                        actionRoute(laat,lng);

                    }
                    uri1.setText(bengkel.getFoto1());
                    uri2.setText(bengkel.getFoto2());
                    uri3.setText(bengkel.getFoto3());

                    verif = bengkel.getVerif();
                    if(bengkel.getVerif().equals("0")){
                        imgverif.setVisibility(View.GONE);
                        tvverif.setVisibility(View.GONE);
                    } if(bengkel.getVerif().equals("1")) {
                        imgverif.setVisibility(View.VISIBLE);
                        tvverif.setVisibility(View.VISIBLE);
                    }if(bengkel.getKendaraan().equals("1")){
                        imgmobil.setVisibility(View.GONE);
                        tvmobil.setVisibility(View.GONE);
                        imgmotor.setVisibility(View.VISIBLE);
                        tvmotor.setVisibility(View.VISIBLE);
                    }if(bengkel.getKendaraan().equals("2")){
                        imgmotor.setVisibility(View.GONE);
                        tvmotor.setVisibility(View.GONE);
                        imgmobil.setVisibility(View.VISIBLE);
                        tvmobil.setVisibility(View.VISIBLE);
                    }if(bengkel.getKendaraan().equals("3")){
                        imgmotor.setVisibility(View.VISIBLE);
                        tvmotor.setVisibility(View.VISIBLE);
                        imgmobil.setVisibility(View.VISIBLE);
                        tvmobil.setVisibility(View.VISIBLE);
                    }if(bengkel.getStatus().equals("1")){
                        stbuka.setVisibility(View.VISIBLE);
                        sttutup.setVisibility(View.INVISIBLE);
                    }if(bengkel.getStatus().equals("0")){
                        stbuka.setVisibility(View.INVISIBLE);
                        sttutup.setVisibility(View.VISIBLE);
                    }
                    info.setText(bengkel.getInfo());
                    //coba(la1,lo2);
                    if(bengkel.getPemilik().equals("1")){
                        chat.setVisibility(View.VISIBLE);
                    }
                    if(bengkel.getPemilik().equals("0")){
                        chat.setVisibility(View.GONE);
                    }

                    final List<Slide> slideList = new ArrayList<>();
                    final ArrayList<Slide> imageList = new ArrayList<>();
                    imageList.add(new Slide(0,bengkel.getFoto1() , getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));
                    imageList.add(new Slide(1,bengkel.getFoto2() , getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));
                    imageList.add(new Slide(2,bengkel.getFoto3(), getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));

                    slideList.add(new Slide(0,bengkel.getFoto1() , getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));
                    slideList.add(new Slide(1,bengkel.getFoto2() , getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));
                    slideList.add(new Slide(2,bengkel.getFoto3(), getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));

                    slider.setItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //do what you want
                            Toast.makeText(getApplicationContext(), "you clicked image " + (i+1), Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(FirebaseDBReadSingleBengkelActivity.this,
                                    DtlImgActivity.class);
                            intent.putExtra(DtlImgActivity.IMAGE_LIST,
                                    imageList);
                            intent.putExtra(DtlImgActivity.CURRENT_ITEM, 3);
                            startActivity(intent);

                        }
                    });
                    slider.addSlides(slideList);

                    //TLPN
                    bttlpn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String phoneNo = tlp.getText().toString();
                            if (isEmpty(tlp.getText().toString())) {
                                Toast.makeText(FirebaseDBReadSingleBengkelActivity.this, "Tidak Dapat Melakukan Panggilan", Toast.LENGTH_SHORT).show();
                            } else {
                                String dial = "tel:" + phoneNo;
                                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
                            }
                        }
                    });
                    //SMS
                    btsms.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //String message = messagetEt.getText().toString();
                            String phoneNo = tlp.getText().toString();
                            if (isEmpty(tlp.getText().toString())) {
                                Toast.makeText(FirebaseDBReadSingleBengkelActivity.this, "Tidak Dapat Melakukan Sms", Toast.LENGTH_SHORT).show();
                            }else{
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
    private void widgetInit() {
        tvStartAddress = (TextView)findViewById(R.id.tvStartAddress);
        tvEndAddress =(TextView) findViewById(R.id.tvEndAddress);
        tvDuration =(TextView) findViewById(R.id.tvDuration);
        tvDistance = (TextView)findViewById(R.id.tvDistance);
    }

    private void refresh(){

    }

    private void actionRoute(double laat, double lang) {

        // progressDialog.dismiss();
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
                    LegsItem dataLegs = null;

                    try {
                        dataLegs = dataDirection.getRoutes().get(0).getLegs().get(0);
                    }catch (Exception e) {
                        Log.d("Data snapshot", "barang3" + e.toString());
                    }
                    Log.e("Data snapshot", "barang4" + dataLegs);
                    if (dataLegs == null){
                        return;
                    }

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
            lng = location.getLongitude();
            laat = location.getLatitude();
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

            String id = nama.getText().toString();
            Intent edit = new Intent(getApplicationContext(), AduanActivity.class);
            edit.putExtra(DATA, id);
            startActivity(edit);

        }
        if(view == chat){

            String email2 = email;
            Intent edit = new Intent(getApplicationContext(), LoginActivity.class);
            edit.putExtra(DATA, email2);
            startActivity(edit);

        }

    }
    public static Intent getActIntent(Activity activity){
        return new Intent(activity, FirebaseDBReadSingleBengkelActivity.class);
    }

}