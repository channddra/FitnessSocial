package com.example.fitnesssocial;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    private EditText nameEdit, email, pass, dob, weightEdit, heightEdit;
    private Button regbutt;
    private TextView signin;
    private RadioGroup groupGender;
    private DatePickerDialog picker;
    private FirebaseAuth auth;
    private int rday, rmonth, ryear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        nameEdit = findViewById(R.id.nameEdit);
        email = findViewById(R.id.emailEdit);
        pass = findViewById(R.id.passwordEdit);
        groupGender = findViewById(R.id.groupGender);
        groupGender.clearCheck();
        dob = findViewById(R.id.dobEdit);
        weightEdit = findViewById(R.id.weightEdit);
        heightEdit = findViewById(R.id.heightEdit);
        regbutt = findViewById(R.id.signupButton);
        signin = findViewById(R.id.signinText);
        auth = FirebaseAuth.getInstance();

        ConstraintLayout mainLayout = findViewById(R.id.mainLayout);

        mainLayout.setOnFocusChangeListener((view, hasFocus) -> {
            if(hasFocus){
                hideKeyboard(view);
            }
        });

        dob.setOnClickListener(view -> {
            final Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            picker = new DatePickerDialog(SignupActivity.this, (datePicker, year1, month1, dayofmonth) -> {
                dob.setText(dayofmonth + "/" + (month1 +1) + "/" + year1);
                rday = dayofmonth;
                rmonth = month1;
                ryear = year1;
            }, year, month, day);
            picker.show();
        });

        regbutt.setOnClickListener(view -> {
            String rname = nameEdit.getText().toString().trim();
            String remail = email.getText().toString().trim();
            String rpass = pass.getText().toString().trim();
            if(rname.length()<4){
                nameEdit.setError("username must be at least 4 characters");
                nameEdit.setFocusable(true);
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(remail).matches()){
                email.setError("Email is not valid");
                email.setFocusable(true);
            }
            else if(pass.length()<8){
                pass.setError("password must be at least 8 characters");
                pass.setFocusable(true);
            }
            else if(groupGender.getCheckedRadioButtonId()==-1){
                Toast.makeText(SignupActivity.this, "please choose your gender", Toast.LENGTH_SHORT).show();
                groupGender.setFocusable(true);
            }
            else if(dob.getText().toString().equals("")){
                dob.setError("please fill in your Date of Birth");
                dob.setFocusable(true);
            }
            else if(weightEdit.getText().toString().equals("")){
                weightEdit.setError("please fill in your weight");
            }
            else if(heightEdit.getText().toString().equals("")){
                heightEdit.setError("please fill in your height");
            }
            else{
                String rgender;
                if(groupGender.getCheckedRadioButtonId()==R.id.radioMale){
                    rgender = "male";
                }
                else{
                    rgender = "female";
                }
                float rweight = Float.valueOf(weightEdit.getText().toString());
                float rheight = Float.valueOf(heightEdit.getText().toString());
                regUser(rname, remail, rpass, rgender, rweight, rheight);
            }
        });
        signin.setOnClickListener(view -> startActivity(new Intent(SignupActivity.this, LoginActivity.class)));
    }

    private void regUser(String rname, String remail, String rpass, String rgender, float rweight, float rheight){
        auth.createUserWithEmailAndPassword(remail, rpass).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                FirebaseUser user = auth.getCurrentUser();
                String tempEmail = user.getEmail();
                String userID = user.getUid();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("email", tempEmail);
                hashMap.put("uid", userID);
                hashMap.put("name", rname);
                hashMap.put("gender", rgender);
                hashMap.put("dob_year", ryear);
                hashMap.put("dob_month", rmonth);
                hashMap.put("dob_day", rday);
                hashMap.put("weight", rweight);
                hashMap.put("height", rheight);
                hashMap.put("image", "noImage");
                hashMap.put("walk_dist", 0f);
                hashMap.put("walk_steps", 0f);
                hashMap.put("walk_cals", 0f);
                hashMap.put("run_dist", 0f);
                hashMap.put("run_cals", 0f);
                hashMap.put("cycle_dist", 0f);
                hashMap.put("cycle_cals", 0f);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("User");
                ref.child(userID).updateChildren(hashMap);
                Toast.makeText(SignupActivity.this, "User Registered with email " + tempEmail, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(SignupActivity.this, "User registration error", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(SignupActivity.this, "User registration error", Toast.LENGTH_LONG).show());
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}