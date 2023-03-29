package com.example.fitnesssocial;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DietFragment extends Fragment {
    public DietFragment() {
        // Required empty public constructor
    }

    private static final int PLAN_MAINTAIN = 1;
    private static final int PLAN_WEIGHT_LOSS = 1;
    private static final String app_id="672030e5";
    private static final String app_key="86230b9bbb865fbac3783a1b5f07f30c";
    private static final String type="public";
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference, userRef;
    RecyclerView recyclerBreakfast, recyclerLunch, recyclerDinner;
    float height;
    float weight;
    String uid, ingredientStr, gender;
    int plan, dobYear;
    AdapterFood adapterBreakfast, adapterLunch, adapterDinner;
    EditText editHeight, editWeight;
    RadioGroup groupPlan, groupIngredient;
    Button submitButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diet, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userRef = databaseReference.child("User");

        editHeight = view.findViewById(R.id.editTextHeight);
        editWeight = view.findViewById(R.id.editTextWeight);
        groupPlan = view.findViewById(R.id.groupPlan);
        groupIngredient = view.findViewById(R.id.groupIngredient);
        submitButton = view.findViewById(R.id.buttonSubmitDiet);
        LinearLayout linear1 = view.findViewById(R.id.linear1);
        LinearLayout linear2 = view.findViewById(R.id.linear2);
        LinearLayout linear3 = view.findViewById(R.id.linear3);
        LinearLayout linear4 = view.findViewById(R.id.linear4);
        LinearLayout linear5 = view.findViewById(R.id.linear5);
        LinearLayout linear6 = view.findViewById(R.id.linear6);
        LinearLayout linear7 = view.findViewById(R.id.linear7);

        editHeight.setOnFocusChangeListener((view1, hasFocus) -> {
            if(!hasFocus){
                hideKeyboard(view1);
            }
        });
        editWeight.setOnFocusChangeListener((view1, hasFocus) -> {
            if(!hasFocus){
                hideKeyboard(view1);
            }
        });

        recyclerBreakfast = view.findViewById(R.id.recycleBreakfast);
        recyclerBreakfast.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerBreakfast.setLayoutManager(layoutManager);

        recyclerLunch = view.findViewById(R.id.recycleLunch);
        recyclerLunch.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager2.setReverseLayout(true);
        layoutManager2.setStackFromEnd(true);
        recyclerLunch.setLayoutManager(layoutManager2);

        recyclerDinner = view.findViewById(R.id.recycleDinner);
        recyclerDinner.setHasFixedSize(true);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getActivity());
        layoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager3.setReverseLayout(true);
        layoutManager3.setStackFromEnd(true);
        recyclerDinner.setLayoutManager(layoutManager3);

        linear5.setVisibility(View.GONE);
        linear6.setVisibility(View.GONE);
        linear7.setVisibility(View.GONE);

        userRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dobYear = ((Long) snapshot.child("dob_year").getValue()).intValue();
                gender = "" + snapshot.child("gender").getValue();
                height = Float.parseFloat(snapshot.child("height").getValue().toString());
                weight = Float.parseFloat(snapshot.child("weight").getValue().toString());

                editHeight.setText(""+height);
                editWeight.setText(""+weight);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        submitButton.setOnClickListener(view12 -> {
            if(editHeight.getText().toString().equals("")){
                height = 0f;
                Toast.makeText(getContext(), "please input height", Toast.LENGTH_SHORT).show();
                editHeight.setFocusable(true);
            }
            else if(editHeight.getText().toString().equals("")){
                weight = 0f;
                Toast.makeText(getContext(), "please input weight", Toast.LENGTH_SHORT).show();
                editWeight.setFocusable(true);
            }
            else if(groupPlan.getCheckedRadioButtonId()==-1){
                Toast.makeText(getContext(), "please select a plan", Toast.LENGTH_SHORT).show();
                groupPlan.setFocusable(true);
            }
            else if(groupIngredient.getCheckedRadioButtonId()==-1){
                Toast.makeText(getContext(), "please select an ingredient", Toast.LENGTH_SHORT).show();
                groupIngredient.setFocusable(true);
            }
            else{
                height = Float.parseFloat(editHeight.getText().toString().trim());
                weight = Float.parseFloat(editWeight.getText().toString().trim());
                if(groupPlan.getCheckedRadioButtonId()==R.id.radioMaintain){
                    plan = PLAN_MAINTAIN;
                }
                else{
                    plan = PLAN_WEIGHT_LOSS;
                }
                if(groupIngredient.getCheckedRadioButtonId()==R.id.radioBeef){
                    ingredientStr = "beef";
                }
                else{
                    ingredientStr = "chicken";
                }

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);

                float BMR;
                if(gender.equals("female")){
                    BMR = (float) (655.1 + (9.563 * weight) + (1.850 * height) - (4.676 * (float) (year-dobYear)));
                }
                else{
                    BMR = (float) (66.47 + (13.75 * weight) + (5.003 * height) - (6.755 * (float) (year-dobYear)));
                }

                float calories;

                if(plan==PLAN_MAINTAIN){
                    calories = BMR*1.55f;
                }
                else{
                    calories = (BMR*1.55f) - 500;
                }
                linear1.setVisibility(View.GONE);
                linear2.setVisibility(View.GONE);
                linear3.setVisibility(View.GONE);
                linear4.setVisibility(View.GONE);
                submitButton.setVisibility(View.GONE);
                linear5.setVisibility(View.VISIBLE);
                linear6.setVisibility(View.VISIBLE);
                linear7.setVisibility(View.VISIBLE);

                getDiet(calories);
            }
        });

        return view;
    }

    private void getDiet(float calories) {
        float lowBreakfast, highBreakfast, lowLunch, highLunch, lowDinner, highDinner;
        lowBreakfast = 0.3f * calories;
        highBreakfast = 0.35f * calories;
        lowLunch = 0.35f * calories;
        highLunch = 0.4f * calories;
        lowDinner = 0.25f * calories;
        highDinner = 0.35f * calories;

        List<String> field = new ArrayList<>();
        field.add("uri");
        field.add("label");
        field.add("image");
        field.add("source");
        field.add("url");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.edamam.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FoodAPI foodAPI = retrofit.create(FoodAPI.class);
        Call<EdamamSearchResponse> call1 = foodAPI.getFood(app_id, app_key, type, ingredientStr, "Breakfast", lowBreakfast+"-"+highBreakfast, true, field);
        call1.enqueue(new Callback<EdamamSearchResponse>() {
            @Override
            public void onResponse(Call<EdamamSearchResponse> call, @NonNull Response<EdamamSearchResponse> response) {
                LoadData(response.body(), adapterBreakfast, recyclerBreakfast);
            }

            @Override
            public void onFailure(Call<EdamamSearchResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Unable to load breakfast", Toast.LENGTH_SHORT).show();
            }
        });

        Call<EdamamSearchResponse> call2 = foodAPI.getFood(app_id, app_key, type, ingredientStr, "Lunch", lowLunch+"-"+highLunch, true, field);
        call2.enqueue(new Callback<EdamamSearchResponse>() {
            @Override
            public void onResponse(Call<EdamamSearchResponse> call, @NonNull Response<EdamamSearchResponse> response) {
                LoadData(response.body(), adapterLunch, recyclerLunch);
            }

            @Override
            public void onFailure(Call<EdamamSearchResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Unable to load lunch", Toast.LENGTH_SHORT).show();
            }
        });

        Call<EdamamSearchResponse> call3 = foodAPI.getFood(app_id, app_key, type, ingredientStr, "Dinner", lowDinner+"-"+highDinner, true, field);
        call3.enqueue(new Callback<EdamamSearchResponse>() {
            @Override
            public void onResponse(Call<EdamamSearchResponse> call, @NonNull Response<EdamamSearchResponse> response) {
                LoadData(response.body(), adapterDinner, recyclerDinner);
            }

            @Override
            public void onFailure(Call<EdamamSearchResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Unable to load dinner", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void LoadData(EdamamSearchResponse body, AdapterFood adapter, RecyclerView recycler) {
        if(body!=null){
            adapter = new AdapterFood(getActivity(), body.getHits());
            recycler.setAdapter(adapter);
        }
        else{
            Toast.makeText(getContext(), "Unable to load", Toast.LENGTH_SHORT).show();
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}