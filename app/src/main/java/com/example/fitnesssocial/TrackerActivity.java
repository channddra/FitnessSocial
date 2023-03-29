package com.example.fitnesssocial;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TrackerActivity extends AppCompatActivity implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    GoogleMap map;

    private final MapPresenter mapPresenter = new MapPresenter(this);
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    Button startBtn;

    TextView txtPace, txtDistance, txtCals, labelPace, txtEType;
    Chronometer txtTime;
    ImageView imgPace, imgEType;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String uid, mapImgPath, eType;

    int dist, steps;
    float cals;
    float total_dist, total_steps, total_cals, weight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        Bundle extras = getIntent().getExtras();

        if(extras!=null){
            eType = extras.getString("eType");
        }

        startBtn = findViewById(R.id.startExerciseButton);
        txtPace = findViewById(R.id.txtPace);
        labelPace = findViewById(R.id.txtPaceLabel);
        imgPace = findViewById(R.id.imgPace);
        txtDistance = findViewById(R.id.txtDistance);
        txtTime = findViewById(R.id.txtTime);
        txtCals = findViewById(R.id.txtCal);
        txtEType = findViewById(R.id.txtExerciseType);
        imgEType = findViewById(R.id.imgExerciseType);
        mapFragment = SupportMapFragment.newInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_map, mapFragment).commit();

        View mapFragmentView = mapFragment.getView();
        if (mapFragmentView != null) {
            mapFragmentView.setClickable(false);
        }
        mapFragment.getMapAsync(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ModelUser modelUser = snapshot.getValue(ModelUser.class);
                if(eType.equals("walking")){
                    total_dist = modelUser.getWalk_dist();
                    total_cals = modelUser.getWalk_cals();
                    total_steps = modelUser.getWalk_steps();
                }
                else if(eType.equals("running")){
                    total_dist = modelUser.getRun_dist();
                    total_cals = modelUser.getRun_cals();
                }
                else if(eType.equals("cycling")){
                    total_dist = modelUser.getCycle_dist();
                    total_cals = modelUser.getCycle_cals();
                }
                weight = modelUser.getWeight();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        txtEType.setText("You are "+eType);
        if(eType.equals("walking")){
            imgEType.setImageResource(R.drawable.ic_walk);
        }
        else if(eType.equals("running")){
            imgEType.setImageResource(R.drawable.ic_run);
        }
        else{
            imgEType.setImageResource(R.drawable.ic_cycling);
        }

        if(eType.equals("running")||eType.equals("cycling")){
            txtPace.setVisibility(View.GONE);
            labelPace.setVisibility(View.GONE);
            imgPace.setVisibility(View.GONE);
        }

        startBtn.setOnClickListener(view -> {
            if(startBtn.getText() == getString(R.string.start_exercise_button_text)){
                startTracking();
                startBtn.setText(R.string.stop_exercise_button_text);
            }
            else{
                stopTracking();
                startBtn.setText(R.string.start_exercise_button_text);
            }
        });

        mapPresenter.onViewCreated();
    }

    private void startTracking() {
        txtPace.setText("");
        txtDistance.setText("");
        txtCals.setText("");
        txtTime.setBase(SystemClock.elapsedRealtime());
        txtTime.start();
        map.clear();
        mapPresenter.startTracking();
    }

    private void stopTracking() {
        dist = Integer.parseInt(String.valueOf(txtDistance.getText()));
        steps = Integer.parseInt((String) txtPace.getText());
        String timestamp = String.valueOf(System.currentTimeMillis());
        float elapsed_mins = (SystemClock.elapsedRealtime() - txtTime.getBase())/60000f;
        float calc_cals = 0.0f;
        if(eType.equals("walking")){
            calc_cals = 3.5f*weight*3.5f*elapsed_mins/200f;
        }
        if(eType.equals("running")){
            calc_cals = 9.8f*weight*3.5f*elapsed_mins/200f;
        }
        if(eType.equals("cycling")){
            calc_cals = 8.0f*weight*3.5f*elapsed_mins/200f;
        }

        total_dist += dist;
        total_steps += steps;
        total_cals += calc_cals;

        HashMap<String, Object> hashMap1 = new HashMap<>();
        if(eType.equals("walking")){
            hashMap1.put("walk_dist", total_dist);
            hashMap1.put("walk_steps", total_steps);
            hashMap1.put("walk_cals", total_cals);
        }
        else if(eType.equals("running")){
            steps = 0;
            hashMap1.put("run_dist", total_dist);
            hashMap1.put("run_cals", total_cals);
        }
        else if(eType.equals("cycling")){
            steps = 0;
            hashMap1.put("cycle_dist", total_dist);
            hashMap1.put("cycle_cals", total_cals);
        }
        databaseReference.child(uid).updateChildren(hashMap1);

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("total_dist", String.valueOf(dist));
        hashMap.put("total_steps", String.valueOf(steps));
        hashMap.put("total_cals", String.valueOf(cals));
        hashMap.put("timestamp", timestamp);

        databaseReference.child(uid).child("history").child(timestamp).updateChildren(hashMap);
        mapPresenter.stopTracking();
        txtTime.stop();

        if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
            map.snapshot(bitmap -> {
                mapImgPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "temp", "temp");
                Intent intent = new Intent(TrackerActivity.this, UploadActivity.class);
                intent.putExtra("mapImgPath", mapImgPath);
                intent.putExtra("eType", eType);
                intent.putExtra("total_dist", total_dist);
                intent.putExtra("total_cals", total_cals);
                startActivity(intent);
                finish();
            });
        }
    }

    @SuppressLint("MissingPermission")
    private void updateUi(Ui ui){
        if(ui.currentLocation!=null && ui.currentLocation != map.getCameraPosition().target){
            map.setMyLocationEnabled(true);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(ui.currentLocation, 16f));
        }
        txtDistance.setText(ui.formattedDistance);
        txtPace.setText(ui.formattedPace);
        float elapsed_mins = (SystemClock.elapsedRealtime() - txtTime.getBase())/60000f;
        float calc_cals = 0.0f;
        if(eType.equals("walking")){
            calc_cals = 3.5f*weight*3.5f*elapsed_mins/200f;
        }
        if(eType.equals("running")){
            calc_cals = 9.8f*weight*3.5f*elapsed_mins/200f;
        }
        if(eType.equals("cycling")){
            calc_cals = 8.0f*weight*3.5f*elapsed_mins/200f;
        }

        txtCals.setText(String.format("%.2f kcals", calc_cals));
        drawRoute(ui.userPath);
    }

    private void drawRoute(List<LatLng> locations){
        PolylineOptions polylineOptions = new PolylineOptions();
        map.clear();
        List<LatLng> points = polylineOptions.getPoints();
        points.addAll(locations);
        map.addPolyline(polylineOptions);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        mapPresenter.ui.observe(this, this::updateUi);
        mapPresenter.onMapLoaded();
    }

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    map.snapshot(bitmap -> {
                        mapImgPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "temp", "temp");
                        Intent intent = new Intent(TrackerActivity.this, UploadActivity.class);
                        intent.putExtra("mapImgPath", mapImgPath);
                        intent.putExtra("eType", eType);
                        intent.putExtra("total_dist", total_dist);
                        intent.putExtra("total_cals", total_cals);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    Toast.makeText(TrackerActivity.this, "Permission Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }
}