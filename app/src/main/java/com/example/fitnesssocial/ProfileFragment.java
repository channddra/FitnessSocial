package com.example.fitnesssocial;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ImageView avatarProfile;
    TextView unameProfile, bioProfile;
    RecyclerView postRecycler;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");
        avatarProfile = view.findViewById(R.id.avatarProfile);
        unameProfile = view.findViewById(R.id.usernameProfile);
        bioProfile = view.findViewById(R.id.bioProfile);

        ImageView imageAchieve = view.findViewById(R.id.imageAchieveClick);
        ImageView imageProgress = view.findViewById(R.id.imageProgressClick);
        TextView textAchieve = view.findViewById(R.id.textAchieveClick);
        TextView textProgress = view.findViewById(R.id.textProgressClick);

        imageAchieve.setOnClickListener(view12 -> goToAchieve());
        imageProgress.setOnClickListener(view13 -> goToProgress());
        textAchieve.setOnClickListener(view14 -> goToAchieve());
        textProgress.setOnClickListener(view15 -> goToProgress());

        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);

        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot currDataSnapshot : snapshot.getChildren()){
                    String name = "" + currDataSnapshot.child("name").getValue();
                    String email = "" + currDataSnapshot.child("email").getValue();
                    String image = "" + currDataSnapshot.child("image").getValue();
                    unameProfile.setText(name);
                    try{
                        Glide.with(getActivity()).load(image).into(avatarProfile);
                    } catch (Exception e){

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        floatingActionButton.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), EditProfileActivity.class)));

        return view;
    }

    private void goToAchieve(){
        startActivity(new Intent(getContext(), AchievementActivity.class));
    }

    private void goToProgress(){
        startActivity(new Intent(getContext(), ProgressActivity.class));
    }
}