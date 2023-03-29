package com.example.fitnesssocial;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("MissingPermission")
public class LocationProvider {
    private FusedLocationProviderClient client;
    private ArrayList<LatLng> locations = new ArrayList<>();
    private Integer distance;
    public MutableLiveData<LatLng> liveLocation = new MutableLiveData<>();
    public MutableLiveData<List<LatLng>> liveLocations = new MutableLiveData<>();
    public MutableLiveData<Integer> liveDistance = new MutableLiveData<>();

    public LocationProvider(){
        distance = 0;
    }

    public void getUserLocation(AppCompatActivity activity){
        this.client = LocationServices.getFusedLocationProviderClient(activity);
        client.getLastLocation().addOnSuccessListener(location -> {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            locations.add(latLng);
            liveLocation.postValue(latLng);
        });
    }

    public void trackUser(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    public void stopTracking(){
        client.removeLocationUpdates(locationCallback);
        locations.clear();
        distance = 0;
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location currentLocation = locationResult.getLastLocation();
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

            LatLng lastLocation;
            if(locations.size()>0){
                lastLocation = locations.get(locations.size()-1);
            }
            else{
                lastLocation = null;
            }

            if(lastLocation!=null){
                distance += (int) Math.round(SphericalUtil.computeDistanceBetween(lastLocation, latLng));
                liveDistance.postValue(distance);
            }

            locations.add(latLng);
            liveLocations.postValue(locations);
        }
    };
}
