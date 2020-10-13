package com.example.masknotifier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomePage extends AppCompatActivity implements View.OnClickListener {

    private Button historyButton, aboutButton, editGeoFenceButton;
    private double latitude, longitude, radius;
    private static String TAG = "HomePage";

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        historyButton = findViewById(R.id.see_history_button);
        aboutButton = findViewById(R.id.about_mask_notifier_button);
        editGeoFenceButton = findViewById(R.id.edit_geo_fence_button);

        historyButton.setOnClickListener(this);
        aboutButton.setOnClickListener(this);
        editGeoFenceButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.about_mask_notifier_button:
                startActivity(new Intent(this, About.class));
                break;
            case R.id.edit_geo_fence_button:
                startActivity(new Intent(this, MapsActivity.class));
                break;
            case R.id.see_history_button:
                startActivity(new Intent(this, HistoryPage.class));
                break;
        }
    }

}