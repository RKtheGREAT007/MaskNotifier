package com.example.masknotifier;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMapClickListener, OnMapReadyCallback, View.OnClickListener {

    private static final String TAG = "MapsActivity";
    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleMap mMap;
    private Marker marker;
    private SeekBar seekBar;
    private Circle circle;
    private TextView radiusTextView;
    private double radius;
    private double latitude, longitude;
    private Button continueButton;
    private int locationRequestCode = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        seekBar = findViewById(R.id.seekBar);
        radiusTextView = findViewById(R.id.radius_textView);
        continueButton = findViewById(R.id.continue_button);

        continueButton.setOnClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode);
        }
        else{
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this,
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location!=null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                addMarker(new LatLng(latitude, longitude));
                            }
                        }
                    });
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            private int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                radius = (double)progress*1.5;
                circle.setRadius((double)progress*1.5);
                radiusTextView.setText("Radius: " + radius);
            }
        });
    }

    private void addMarker(LatLng point){
        this.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 16.0f));
        circle.setCenter(point);
        marker.setPosition(point);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.mMap = map;

        this.mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        this.mMap.setBuildingsEnabled(false);
        this.mMap.setIndoorEnabled(true);
        this.mMap.setTrafficEnabled(false);
        this.mMap.setOnMapClickListener(this);
        this.mMap.getUiSettings().setZoomControlsEnabled(true);
        this.mMap.getUiSettings().setCompassEnabled(true);
        this.mMap.getUiSettings().setMyLocationButtonEnabled(true);
        this.mMap.getUiSettings().setMapToolbarEnabled(true);
        this.mMap.getUiSettings().setZoomGesturesEnabled(true);
        this.mMap.getUiSettings().setScrollGesturesEnabled(true);
        this.mMap.getUiSettings().setTiltGesturesEnabled(true);
        this.mMap.getUiSettings().setRotateGesturesEnabled(true);
        Log.d(TAG, "onMapReady: " + latitude + " " + longitude);
        marker = this.mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(240));
        circle = this.mMap.addCircle(new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(0)
                .strokeColor(Color.parseColor("#71cce7"))
                .fillColor(Color.parseColor("#6071cce7"))
                .strokeWidth(5));
    }

    @Override
    public void onMapClick(LatLng point) {
        Log.d(TAG, "tapped, point=" + point);
        addMarker(point);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.getLastLocation().addOnSuccessListener(this,
                            new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        latitude = location.getLatitude();
                                        longitude = location.getLongitude();
                                        addMarker(new LatLng(latitude, longitude));
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(this, "Permission is required for the app to work.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.continue_button:
                Intent intent = new Intent(this, HomePage.class);
                intent.putExtra("radius", radius);
                startActivity(intent);
                break;
        }
    }
}