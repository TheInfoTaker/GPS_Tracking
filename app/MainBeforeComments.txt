package com.program.gpstracking;

//Start

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.task.OnSuccessListener;

import java.util.List;

//Finish

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Application;
import android.location.LocationRequest;


import java.util.List;

import android.app.Application;

// Completed: Set Up The UI, Install Fuse Location Client, Permissions and initial Location, Geocode the Street Address
// Could Add: Get Periodic Location Updates
// Add: Show a list of saved waypoints
// Current Video Timestamp: 30m
// YouTube link: https://www.youtube.com/watch?v=_xUcYfbtfsI&t=212s

public class MainActivity extends AppCompatActivity {
    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;
    public static final int PERMISSIONS_FINE_LOCATION = 99;

    TextView tv_lat, tv_lon, tv_altitude, tv_accuracy, tv_speed, tv_sensor, tv_updates, tv_address, tv_wayPointCounts;
    Button btn_newWayPoint, btn_showWayPointList, btn_showMap;
    Switch sw_locationupdates, sw_gps;


    boolean updateOn = False;

    Location currentLocation;

    List<Locations> SavedLocations;


    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_lat = findViewById(R.id.tv_lat);
        tv_lon = findViewById(R.id.tv_lon);
        tv_altitude = findViewById(R.id.tv_altitude);
        tv_accuracy = findViewById(R.id.tv_accuracy);
        tv_speed = findViewById(R.id.tv_speed);
        tv_sensor = findViewById(R.id.tv_sensor);
        tv_updates = findViewById(R.id.tv_updates);
        tv_address = findViewById(R.id.tv_address);
        sw_gps = findViewById(R.id.sw_gps);
        sw_locationupdates = findViewById(R.id.sw_locationsupdates);
        btn_newWayPoint = findViewById(R.id.btn_newWayPoint); // Check id
        btn_showWayPointList = findViewById(R.id.btn_showWayPoint); // Check id
        tv_wayPointCounts = findViewById(R.id.tv_CountOfCrumbs);
        btn_showMap = findViewById(R.id.btn_showMap)

        locationRequest = new LocationRequest();

        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);

        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);

        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

//        // Location updater currently not in use
//        locationsCallBack = (LocationCallBack) onLocationResult(locationResult) {
//            super.onLocationResult(locationResult);
//            // Save the location
//            updateUIValues(locationResult.getLastLocation());
//        }

        btn_newWayPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication my Application = (myApplication)getApplicationContext();
                savedLocations = myApplication.getMyLocations();
                savedLocations.add(currentLocation);
            }
        });

        btn_showWayPointList.setOnClickListener(new View.OnClickListener() {
            @Override
            Public void onClick(View v) {
                intent i = new Intent(MainActivity.this, ShowSavedLocationsList.class);
                startActivity(i);
            }
        });

        btn_showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(i);
            }

        sw_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_gps.isChecked()) {
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    tv_sensor.setText("Using GPS sensors");
                }
                else {
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    v_sensor.setText("Using Towers + Wifi");
                }
            }
        });

        // Location Updator currently not in use
//        sw_locationupdates.setOnClickListener((v) {
//            if (sw_locationupdates.isChecked()) {
//                startLocationUpdates();
//        }
//        else {
//            stopLocationUpdates();
//        }
//
        updateGPS();
    }

    public void onRequestPermissionResult(int request, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSION_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateGPS();
                }
                else {
                    Toast.makeText(this, "This app requires permission to be granted in order to work propperly", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }
    private void updateGPS() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        if (ActivityCompat.checkSelfPermission( this, Mainfest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListender(this, new OnSuccessListener<Location>() {

                updateUIValues(location);

                currentLocation = location;

            // Was Not in Tutorial Location. Move?
//                @Override
//                        public void onSuccess(Location location){
//                }
            });
        }
        else {
            if (Build.Version.SDK_INT >= Build.Version_CODES.M) {
                requestPermission(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }
    }
    private void updateUIValues(Location location) {

    }
    tv_lat.setText(String.ValueOf(location.getLatitude()));
    tv_lat.setText(String.ValueOf(location.getLongitude()));
    tv_accuracy.setText(String.valueOf(location.getAccuracy()));

    if(location.hasAltitude()){
        tv_altitude.setText(String.valueOf(location.getAltitude()));
    }
    else {
        tv_altitude.setText("Not available");
    }
    if (location.hasSpeed()) {
        tv_speed.setText(String.valueOf(location.getSpeed()));
    }
    else {
        tv_speed.setText("Not avaiable");
    }

    Geocoder geocoder = new Geocoder(MainActivity.this);

    try {
        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        tv_address.setText(address.get(0).getAddressLine(0));
    }
    catch (Exception e) {
        tv_address.setText("Unable to get street address");

    }

    MyApplication my Application = (myApplication)getApplicationContext();
    savedLocations = myApplication.getMyLocations();

    tv_wayPointCounts.setText(Integer.toString(savedLocations.size()));

    }

}