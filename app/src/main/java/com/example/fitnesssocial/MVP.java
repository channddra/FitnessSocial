package com.example.fitnesssocial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

class Ui {
    String formattedPace;
    String formattedDistance;
    LatLng currentLocation;
    List<LatLng> userPath;

    public Ui(){
        formattedPace = "";
        formattedDistance = "";
        currentLocation = null;
        userPath = new ArrayList<>();
    }
}

class MapPresenter{
    AppCompatActivity activity;
    MutableLiveData<Ui> ui = new MutableLiveData<>();
    final LocationProvider locationProvider;
    final StepCounter stepCounter;
    final PermissionsManager permissionsManager;

    public MapPresenter(AppCompatActivity activity){
        this.activity = activity;
        this.locationProvider = new LocationProvider();
        this.stepCounter = new StepCounter(activity);
        this.permissionsManager = new PermissionsManager(activity, locationProvider, stepCounter);
        Ui temp = new Ui();
        this.ui.postValue(temp);
    }

    public void onViewCreated(){
        locationProvider.liveLocations.observe(this.activity, locations -> {
                Ui current = ui.getValue();
                current.userPath = locations;
                this.ui.postValue(current);
        });

        locationProvider.liveLocation.observe(this.activity, currentLocation -> {
            Ui current = ui.getValue();
            current.currentLocation = currentLocation;
            this.ui.postValue(current);
        });

        locationProvider.liveDistance.observe(this.activity, distance -> {
            Ui current = ui.getValue();
            current.formattedDistance = activity.getString(R.string.distance_value, distance);
            this.ui.postValue(current);
        });

        stepCounter.liveSteps.observe(this.activity, steps -> {
            Ui current = ui.getValue();
            current.formattedPace = String.valueOf(steps);
            this.ui.postValue(current);
        });

    }

    public void onMapLoaded(){
        permissionsManager.requestUserLocation();
    }

    public void startTracking(){
        locationProvider.trackUser();
        permissionsManager.requestActivityRecognition();
    }

    public void stopTracking(){
        locationProvider.stopTracking();
        stepCounter.unloadStepCounter();
    }

}