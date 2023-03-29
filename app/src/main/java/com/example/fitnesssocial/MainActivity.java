package com.example.fitnesssocial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseUser currUser;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        if(auth!=null){
            currUser = auth.getCurrentUser();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = auth.getCurrentUser();
                if(user == null){
                    Intent intent1 = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent1);
                    finish();
                }
                else{
                    Intent intent2 = new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent2);
                    finish();
                }
            }
        }, 3000);
    }
}