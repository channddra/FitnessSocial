package com.example.fitnesssocial;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPostFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    String uid;

    public AddPostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        ImageView imageWalking = view.findViewById(R.id.imageWalking);
        imageWalking.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), TrackerActivity.class);
            intent.putExtra("eType", "walking");
            startActivity(intent);
        });
        ImageView imageRunning = view.findViewById(R.id.imageRunning);
        imageRunning.setOnClickListener(view2 -> {
            Intent intent = new Intent(getContext(), TrackerActivity.class);
            intent.putExtra("eType", "running");
            startActivity(intent);
        });
        ImageView imageCycling = view.findViewById(R.id.imageCycling);
        imageCycling.setOnClickListener(view3 -> {
            Intent intent = new Intent(getContext(), TrackerActivity.class);
            intent.putExtra("eType", "cycling");
            startActivity(intent);
        });
        return view;
    }
}