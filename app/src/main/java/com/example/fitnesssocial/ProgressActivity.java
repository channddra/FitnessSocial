package com.example.fitnesssocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ProgressActivity extends AppCompatActivity {
    private LineChart lineChart;
    FirebaseAuth firebaseAuth;
    DatabaseReference currUserRef;
    float total_cals, total_dist, total_steps;
    float monthCutoff, weekCutoff;
    TextView textEditCals, textEditDist, textEditSteps, textTotalCals, textTotalDist, textTotalSteps;
    RadioGroup groupTimeframe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        firebaseAuth = FirebaseAuth.getInstance();
        lineChart = findViewById(R.id.chart);
        textEditCals = findViewById(R.id.textEditCals);
        textEditDist = findViewById(R.id.textEditDist);
        textEditSteps = findViewById(R.id.textEditSteps);
        textTotalCals = findViewById(R.id.textTotalCals);
        textTotalDist = findViewById(R.id.textTotalDist);
        textTotalSteps = findViewById(R.id.textTotalSteps);
        groupTimeframe = findViewById(R.id.groupTimeframe);
        currUserRef = FirebaseDatabase.getInstance().getReference("User").child(firebaseAuth.getUid());
        monthCutoff = System.currentTimeMillis()-2629800000f;
        weekCutoff = System.currentTimeMillis()-604800000f;

        groupTimeframe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id){
                    case R.id.radioWeekly:
                        textTotalCals.setText("Total Calories (Weekly)");
                        textTotalDist.setText("Total Distance (Weekly)");
                        textTotalSteps.setText("Total Steps (Weekly)");
                        configureLineChart();
                        getDataMonthly(weekCutoff);
                        break;
                    case R.id.radioMonthly:
                        textTotalCals.setText("Total Calories (Monthly)");
                        textTotalDist.setText("Total Distance (Monthly)");
                        textTotalSteps.setText("Total Steps (Monthly)");
                        configureLineChart();
                        getDataMonthly(monthCutoff);
                        break;
                }
            }
        });

        RadioButton radioMonthly = findViewById(R.id.radioMonthly);
        radioMonthly.setChecked(true);

        configureLineChart();
        getDataMonthly(monthCutoff);
    }

    private void configureLineChart() {
        Description desc = new Description();
        desc.setText("History");
        desc.setTextSize(28);
        lineChart.setDescription(desc);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM", Locale.ENGLISH);

            @Override
            public String getFormattedValue(float value) {
                long millis = (long) value;
                return mFormat.format(new Date(millis));
            }
        });
    }

    private void getDataMonthly(float cutoff){
        ArrayList<Entry> history_cals = new ArrayList<>();
        ArrayList<Entry> history_dist = new ArrayList<>();
        ArrayList<Entry> history_steps = new ArrayList<>();
        currUserRef.child("history").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                total_cals=0;
                total_dist=0;
                total_steps=0;
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    ModelHistory modelHistory = snapshot1.getValue(ModelHistory.class);
                    float x = Float.parseFloat(modelHistory.getTimestamp());
                    if(x>cutoff){
                        float cals = Float.parseFloat(modelHistory.getTotal_cals());
                        float dist = Float.parseFloat(modelHistory.getTotal_dist());
                        float steps = Float.parseFloat(modelHistory.getTotal_steps());
                        total_cals+=cals;
                        total_dist+=dist;
                        total_steps+=steps;
                        if(cals>1e-9) history_cals.add(new Entry(x, cals));
                        if(dist>1e-9) history_dist.add(new Entry(x, dist));
                        if(steps>1e-9) history_steps.add(new Entry(x, steps));
                    }
                }

                textEditCals.setText(String.format("%.2f kcals", total_cals));
                textEditDist.setText(String.format("%.2f m", total_dist));
                textEditSteps.setText(String.format("%.2f steps", total_steps));
                setLineChartData(history_cals, history_dist, history_steps);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setLineChartData(ArrayList<Entry> history_cals, ArrayList<Entry> history_dist, ArrayList<Entry> history_steps) {
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        LineDataSet calsLineDataSet = new LineDataSet(history_cals, "Calories Burned");
        calsLineDataSet.setDrawCircles(true);
        calsLineDataSet.setCircleRadius(4);
        calsLineDataSet.setDrawValues(true);
        calsLineDataSet.setLineWidth(3);
        calsLineDataSet.setColor(Color.RED);
        calsLineDataSet.setCircleColor(Color.RED);
        dataSets.add(calsLineDataSet);

        LineDataSet distLineDataSet = new LineDataSet(history_dist, "Distance");
        distLineDataSet.setDrawCircles(true);
        distLineDataSet.setCircleRadius(4);
        distLineDataSet.setDrawValues(true);
        distLineDataSet.setLineWidth(3);
        distLineDataSet.setColor(Color.GREEN);
        distLineDataSet.setCircleColor(Color.GREEN);
        dataSets.add(distLineDataSet);

        LineDataSet stepsLineDataSet = new LineDataSet(history_steps, "Steps");
        stepsLineDataSet.setDrawCircles(true);
        stepsLineDataSet.setCircleRadius(4);
        stepsLineDataSet.setDrawValues(true);
        stepsLineDataSet.setLineWidth(3);
        stepsLineDataSet.setColor(Color.BLUE);
        stepsLineDataSet.setCircleColor(Color.BLUE);
        dataSets.add(stepsLineDataSet);

        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }
}