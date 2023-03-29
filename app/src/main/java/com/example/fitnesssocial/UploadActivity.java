package com.example.fitnesssocial;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {
    private ImageView imageView;
    TextView textEType, textDist, textCals;
    private Uri filePath, chosenUri, mapUri;
    String eType, mapImgPath, uid, title, desc, timestamp;
    DatabaseReference databaseReference;
    EditText editTextTitle, editTextDesc;
    FirebaseAuth firebaseAuth;
    int uploadCount;
    Button btnSelect, btnUpload;
    float total_dist, total_cals;

    private final int PICK_IMAGE_REQUEST = 22;

    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        databaseReference = FirebaseDatabase.getInstance().getReference("Post");
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDesc = findViewById(R.id.editTextDesc);
        textEType = findViewById(R.id.textEType);
        textDist = findViewById(R.id.textDist);
        textCals = findViewById(R.id.textCals);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            eType = extras.getString("eType");
            mapImgPath = extras.getString("mapImgPath");
            total_dist = extras.getFloat("total_dist");
            total_cals = extras.getFloat("total_cals");
        }

        textEType.setText(eType);
        textDist.setText(total_dist+" m");
        textCals.setText(String.format("%.2f kcals", total_cals));

        editTextDesc.setOnFocusChangeListener((view, hasFocus) -> {
            if(!hasFocus){
                hideKeyboard(view);
            }
        });
        editTextTitle.setOnFocusChangeListener((view, hasFocus) -> {
            if(!hasFocus){
                hideKeyboard(view);
            }
        });

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(
                Color.parseColor("#0F9D58"));
        actionBar.setBackgroundDrawable(colorDrawable);

        btnSelect = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        imageView = findViewById(R.id.imgView);

        btnSelect.setOnClickListener(v -> SelectImage());

        btnUpload.setOnClickListener(v -> uploadFun());
    }

    private void uploadPost() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("title", title);
        hashMap.put("description", desc);
        hashMap.put("ptime", timestamp);
        hashMap.put("plike", "0");
        hashMap.put("pcomments", "0");
        hashMap.put("total_cals", total_cals);
        hashMap.put("etype", eType);
        hashMap.put("mapImg", mapUri.toString());
        hashMap.put("chooseImg", chosenUri.toString());

        databaseReference.child(timestamp).updateChildren(hashMap).addOnCompleteListener(
                task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(UploadActivity.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UploadActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        ).addOnFailureListener(e -> Toast.makeText(UploadActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void uploadImage(Uri FP) {
        uploadCount = 0;
        if (FP != null) {
            StorageReference ref1
                    = storageReference
                    .child(
                            UUID.randomUUID().toString());

            UploadTask uploadTask1 = ref1.putFile(Uri.parse(mapImgPath));

            Task<Uri> urlTask1 = uploadTask1.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return ref1.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    mapUri = task.getResult();
                    Toast.makeText(UploadActivity.this, "Upload Success", Toast.LENGTH_SHORT).show();
                    uploadCount++;
                    if(uploadCount==2) uploadPost();
                } else {
                    Toast.makeText(UploadActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                }

            });

            StorageReference ref2
                    = storageReference
                    .child(
                            UUID.randomUUID().toString());

            UploadTask uploadTask2 = ref2.putFile(FP);

            Task<Uri> urlTask2 = uploadTask2.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return ref2.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    chosenUri = task.getResult();
                    Toast.makeText(UploadActivity.this, "Upload Success", Toast.LENGTH_SHORT).show();
                    uploadCount++;
                    if(uploadCount==2) uploadPost();
                } else {
                    Toast.makeText(UploadActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                }

            });
        }
    }

    private void SelectImage() {
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
                imageView.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFun() {
        title = "" + editTextTitle.getText().toString().trim();
        desc = "" + editTextDesc.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            editTextTitle.setError("Title Cant be empty");
            Toast.makeText(this, "Title can't be left empty", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(desc)) {
            editTextDesc.setError("Description Cant be empty");
            Toast.makeText(this, "Description can't be left empty", Toast.LENGTH_LONG).show();
        }
        else if (filePath == null) {
            Toast.makeText(this, "Please Select an Image", Toast.LENGTH_LONG).show();
        }
        else {
            btnUpload.setClickable(false);
            chosenUri = null;
            timestamp = String.valueOf(System.currentTimeMillis());
            uploadImage(filePath);
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}