package com.example.masknotifier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

public class HomePage extends AppCompatActivity implements View.OnClickListener {

    private FusedLocationProviderClient mFusedLocationClient;
    private int locationRequestCode = 1000;
    private Button instructionsButton, historyButton, aboutButton, editGeoFenceButton;
    private double latitude, longitude;
    private static String TAG = "HomePage";
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);

        instructionsButton = findViewById(R.id.instructions_button);
        historyButton = findViewById(R.id.see_history_button);
        aboutButton = findViewById(R.id.about_us_button);
        editGeoFenceButton = findViewById(R.id.edit_geo_fence_button);

        instructionsButton.setOnClickListener(this);
        historyButton.setOnClickListener(this);
        aboutButton.setOnClickListener(this);
        editGeoFenceButton.setOnClickListener(this);

        locationCallback = new LocationCallback() {
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }
        };

//        getLocation();
    }


    private void getLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode);
        }
        else{
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this,
                    new OnSuccessListener<Location>() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onSuccess(Location location) {
                            if(location!=null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                Log.d(TAG, "onSuccess: " + latitude + " " + longitude);
                            }
                            else{
                                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                            }
                        }
                    });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.instructions_button:
                //Todo: goto to instructions page
                break;
            case R.id.about_us_button:
                //Todo: goto about us page
                break;
            case R.id.edit_geo_fence_button:
                startActivity(new Intent(this, MapsActivity.class));
                break;
            case R.id.see_history_button:
                //Todo: goto history page
                break;
        }
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
                                    }
                                    else{
                                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
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
}