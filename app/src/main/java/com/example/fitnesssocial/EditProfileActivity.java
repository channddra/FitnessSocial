package com.example.fitnesssocial;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class EditProfileActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private Uri filePath;
    StorageReference storageReference;
    ImageView profileImage;
    TextView editProfile, editPassword, uploadProfile, weightInput, heightInput;
    Button saveWeightHeightButton;
    private final int PICK_IMAGE_REQUEST = 22;
    String cameraPermission[];
    String storagePermission[];
    Uri imageURI;
    String profilePhoto, downloadUri, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editProfile = findViewById(R.id.editProfilepic);
        profileImage = findViewById(R.id.profileImage);
        uploadProfile = findViewById(R.id.uploadProfile);
        editPassword = findViewById(R.id.editPassword);
        weightInput = findViewById(R.id.inputProfileWeight);
        heightInput = findViewById(R.id.inputProfileHeight);
        saveWeightHeightButton = findViewById(R.id.saveWeightHeightButton);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        uid = firebaseUser.getUid();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = firebaseDatabase.getReference("User");
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                for (DataSnapshot currDataSnapshot : dataSnapshot.getChildren()){
                    String image = "" + currDataSnapshot.child("image").getValue();
                    try {
                        Glide.with(EditProfileActivity.this).load(image).into(profileImage);
                    }catch (Exception e){

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){

            }
        });

        editPassword.setOnClickListener(v -> showPasswordChangeDailog());

        editProfile.setOnClickListener(v -> {
            profilePhoto = null;
            selectImage();
        });

        uploadProfile.setOnClickListener(view -> uploadImage(filePath));
        saveWeightHeightButton.setOnClickListener(view -> saveWeightHeight());
    }

    private void saveWeightHeight(){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("weight", weightInput.getText());
        hashMap.put("height", heightInput.getText());
        databaseReference.child(uid).updateChildren(hashMap).addOnCompleteListener(
                task1 -> {
                    if(task1.isSuccessful()){
                        Toast.makeText(EditProfileActivity.this, "Height and weight saved", Toast.LENGTH_SHORT).show();
                    }
                }
        ).addOnFailureListener(e -> Toast.makeText(EditProfileActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,
                resultCode,
                data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                profileImage.setImageBitmap(bitmap);
                profileImage.setVisibility(View.VISIBLE);
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(Uri FP) {
        if (FP != null) {
            StorageReference ref1
                    = storageReference
                    .child(
                            UUID.randomUUID().toString());

            UploadTask uploadTask1 = ref1.putFile(FP);

            Task<Uri> urlTask1 = uploadTask1.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return ref1.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    imageURI = task.getResult();
                    Toast.makeText(EditProfileActivity.this, "Upload Success", Toast.LENGTH_SHORT).show();
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("image", imageURI.toString());
                    databaseReference.child(uid).updateChildren(hashMap).addOnCompleteListener(
                            task1 -> {
                                if(task1.isSuccessful()){
                                    Toast.makeText(EditProfileActivity.this, "profile picture Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            }
                    ).addOnFailureListener(e -> Toast.makeText(EditProfileActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show());
                } else {
                    Toast.makeText(EditProfileActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                }

            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String image = "" + dataSnapshot1.child("image").getValue();
                    try {
                        Glide.with(EditProfileActivity.this).load(image).into(profileImage);
                    } catch (Exception e){

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        editPassword.setOnClickListener(v -> showPasswordChangeDailog());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String image = "" + dataSnapshot1.child("image").getValue();
                    try {
                        Glide.with(EditProfileActivity.this).load(image).into(profileImage);
                    }catch (Exception e){

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){

            }
        });
        editPassword.setOnClickListener(v -> showPasswordChangeDailog());
    }


    private void showPasswordChangeDailog(){
        View view = LayoutInflater.from(this).inflate(R.layout.edit_password, null);
        final EditText oldPass = view.findViewById(R.id.oldpassinp);
        final EditText newPass = view.findViewById(R.id.newpassinp);
        Button editPass = view.findViewById(R.id.updatepass);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        editPass.setOnClickListener(v -> {
            String oldp = oldPass.getText().toString().trim();
            String newp = newPass.getText().toString().trim();
            if (TextUtils.isEmpty(oldp)){
                Toast.makeText(EditProfileActivity.this, "Current Password must be filled", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(newp)){
                Toast.makeText(EditProfileActivity.this, "New Password must be filled", Toast.LENGTH_LONG).show();
                return;
            }
            if (newp.length()<8){
                Toast.makeText(EditProfileActivity.this, "New Password must be at least 8 characters", Toast.LENGTH_LONG).show();
                return;
            }
            dialog.dismiss();
            updPass(oldp, newp);
        });
    }

    private void updPass(String oldp, String newp){
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), oldp);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(v1 -> user.updatePassword(newp)
                        .addOnSuccessListener(v2 -> Toast.makeText(EditProfileActivity.this, "Password has been changed", Toast.LENGTH_LONG).show()).addOnFailureListener(e -> Toast.makeText(EditProfileActivity.this, "Failed", Toast.LENGTH_LONG).show())).addOnFailureListener(e -> Toast.makeText(EditProfileActivity.this, "Failed", Toast.LENGTH_LONG).show());
    }
}