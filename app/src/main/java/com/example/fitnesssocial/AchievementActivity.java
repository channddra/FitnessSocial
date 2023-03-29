package com.example.fitnesssocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AchievementActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    float run_dist, run_cals;
    String uid;

    TextView textAchieve1, textAchieve2, textAchieve3, textAchieve4;
    ImageView imageAchieve1, imageAchieve2, imageAchieve3, imageAchieve4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        textAchieve1 = findViewById(R.id.textAchievement1);
        textAchieve2 = findViewById(R.id.textAchievement2);
        textAchieve3 = findViewById(R.id.textAchievement3);
        textAchieve4 = findViewById(R.id.textAchievement4);

        imageAchieve1 = findViewById(R.id.imageAchieve1);
        imageAchieve2 = findViewById(R.id.imageAchieve2);
        imageAchieve3 = findViewById(R.id.imageAchieve3);
        imageAchieve4 = findViewById(R.id.imageAchieve4);

        firebaseAuth = FirebaseAuth.getInstance();
        uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ModelUser modelUser = snapshot.getValue(ModelUser.class);
                run_dist = modelUser.getRun_dist();
                run_cals = modelUser.getRun_cals();
                checkAchievements();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkAchievements() {
        if(run_dist >=100){
            textAchieve1.setTextColor(getResources().getColor(R.color.blue_dark_cornflower));
            imageAchieve1.setImageResource(R.drawable.ic_achieve);
            imageAchieve1.setColorFilter(getResources().getColor(R.color.blue_dark_cornflower));
        }

        if(run_dist >=200){
            textAchieve2.setTextColor(getResources().getColor(R.color.blue_dark_cornflower));
            imageAchieve2.setImageResource(R.drawable.ic_achieve);
            imageAchieve2.setColorFilter(getResources().getColor(R.color.blue_dark_cornflower));
        }

        if(run_dist >=300){
            textAchieve3.setTextColor(getResources().getColor(R.color.blue_dark_cornflower));
            imageAchieve3.setImageResource(R.drawable.ic_achieve);
            imageAchieve3.setColorFilter(getResources().getColor(R.color.blue_dark_cornflower));
        }

        if(run_dist >=400){
            textAchieve4.setTextColor(getResources().getColor(R.color.blue_dark_cornflower));
            imageAchieve4.setImageResource(R.drawable.ic_achieve);
            imageAchieve4.setColorFilter(getResources().getColor(R.color.blue_dark_cornflower));
        }
    }
}