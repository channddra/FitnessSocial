package com.example.fitnesssocial;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

public class StepCounter implements SensorEventListener {
    AppCompatActivity activity;
    MutableLiveData<Integer> liveSteps = new MutableLiveData<>();
    SensorManager sensorManager;
    Sensor stepCounterSensor;
    Integer initialSteps;
    public StepCounter(AppCompatActivity activity){
        this.activity = activity;
        this.initialSteps = -1;
    }

    public void setupStepCounter(){
        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(stepCounterSensor != null){
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    public void unloadStepCounter(){
        if(stepCounterSensor != null){
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int newSteps = (int) sensorEvent.values[0];
        if(initialSteps == -1){
            initialSteps = newSteps;
        }
        int currentSteps = newSteps-initialSteps;
        liveSteps.postValue(currentSteps);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
