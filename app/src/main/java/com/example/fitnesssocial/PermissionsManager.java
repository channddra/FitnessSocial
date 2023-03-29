package com.example.fitnesssocial;

import android.Manifest;
import android.os.Build;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class PermissionsManager{
    private final LocationProvider locationProvider;
    private StepCounter stepCounter;
    private final ActivityResultLauncher<String> locationPermissionProvider;
    private final ActivityResultLauncher<String> activityRecognitionPermissionProvider;

    public PermissionsManager(AppCompatActivity activity, LocationProvider locationProvider, StepCounter stepCounter){
        this.stepCounter = stepCounter;
        this.locationProvider = locationProvider;
        this.activityRecognitionPermissionProvider = activity.registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if(result){
                this.stepCounter.setupStepCounter();
            }
        });
        this.locationPermissionProvider = activity.registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if(result){
                this.locationProvider.getUserLocation(activity);
            }
        });
    }

    public void requestActivityRecognition(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activityRecognitionPermissionProvider.launch(Manifest.permission.ACTIVITY_RECOGNITION);
        } else {
            stepCounter.setupStepCounter();
        }
    }

    public void requestUserLocation(){
        locationPermissionProvider.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }
}
