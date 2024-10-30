package com.well_sync.presentation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.well_sync.R;
import com.well_sync.logic.DailyLogHandler;
import com.well_sync.logic.IDailyLogHandler;
import com.well_sync.logic.IPatientHandler;
import com.well_sync.logic.PatientHandler;
import com.well_sync.objects.Patient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UserProgressActivity extends AppCompatActivity {
    private String patientEmail, doctorEmail, date;
    private LineChart lineChart;
    AnyChartView symptomsChart;
    private Map<String, Float> symptomScores;
    private final String MOOD="mood";
    private final String SLEEP="sleep hours";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_progress);
        // retrieve the patient email and date from the intent
        Intent intent = getIntent();
        patientEmail = intent.getStringExtra("patientEmail");
        doctorEmail = intent.getStringExtra("doctorEmail");
        date = intent.getStringExtra("date");

        //get the handler to call the methods
        IPatientHandler patientHandler = new PatientHandler();
        Patient patient = patientHandler.getDetails(patientEmail);
        IDailyLogHandler dailyLogHandler = new DailyLogHandler();

        //Names
        List<String> moodNames = Arrays.asList(getResources().getStringArray(R.array.moods));
        int totalSymptoms= getResources().getIntArray(R.array.symptoms).length;
        //get the data for x and y coordinates
        List<String> dates = dailyLogHandler.getAllDatesAsString(patient);
        float[] moodScores = dailyLogHandler.getAllMoodScores(patient);
        float[] sleepHours = dailyLogHandler.getAllSleepHours(patient);
        double avgSleep = dailyLogHandler.getAverageSleep(patient);
        symptomScores= dailyLogHandler.getAverageSymptoms(patient);
        List<Entry> entriesMood =getEntries(moodScores);
        List<Entry> entriesSleep =getEntries(sleepHours);
        TextView average= findViewById(R.id.avg_sleep);
        average.setText("Sleep Average: "+avgSleep+" hrs.");

        //create the charts
        createLineChart(R.id.mood_chart,MOOD, dates, moodNames,entriesMood);
        createLineChart2(R.id.sleep_chart,SLEEP, dates,totalSymptoms,entriesSleep);
        createPieChart(R.id.symptoms_chart);

        // Set click listeners or any other event listeners as needed
        ImageView  closeImageView = findViewById(R.id.close);
        closeImageView.setOnClickListener(view -> {
            // Handle close button click
            Intent closeIntent = new Intent(UserProgressActivity.this, PatientInfoActivity.class);
            closeIntent.putExtra("doctorEmail",doctorEmail);
            closeIntent.putExtra("patientEmail",patientEmail);
            closeIntent.putExtra("date",date);
            UserProgressActivity.this.startActivity(closeIntent);
        });

    }
    private void createPieChart(int idView) {
        symptomsChart = findViewById(idView);
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();
        for (Map.Entry<String, Float> entry : symptomScores.entrySet()) {
            dataEntries.add(new ValueDataEntry(entry.getKey(), entry.getValue()));
        }
        pie.data(dataEntries);
        symptomsChart.setChart(pie);
    }

    private void createLineChart(int idView, String label, List<String> xList,List<String> yList,List<Entry> entries){
        lineChart = findViewById(idView);
        lineChart.getAxisRight().setDrawLabels(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xList));
        xAxis.setLabelCount(xList.size());
        xAxis.setAxisLineWidth(5f);
        xAxis.setAxisLineColor(Color.BLACK);
        xAxis.setGranularity(1f);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        if(yList!=null){
            yAxis.setValueFormatter(new IndexAxisValueFormatter(yList));
            yAxis.setAxisMaximum(yList.size()-1);
            yAxis.setLabelCount(yList.size());
        }else{
            yAxis.setAxisMaximum(16);
            yAxis.setLabelCount(8);
        }
        yAxis.setAxisLineWidth(5f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setGranularity(1f);
        LineDataSet dataSet = new LineDataSet(entries,label);
        dataSet.setColor(Color.BLUE);
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    private void createLineChart2(int idView, String label, List<String> xList, int ySize,List<Entry> entries){
        lineChart = findViewById(idView);
        lineChart.getAxisRight().setDrawLabels(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xList));
        xAxis.setLabelCount(xList.size());
        xAxis.setAxisLineWidth(5f);
        xAxis.setAxisLineColor(Color.BLACK);
        xAxis.setGranularity(1f);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);

        yAxis.setAxisMaximum(ySize-1);
        if(ySize>10)
            yAxis.setLabelCount(ySize/2);
        else
            yAxis.setLabelCount(ySize);
        yAxis.setAxisLineWidth(5f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setGranularity(1f);
        LineDataSet dataSet = new LineDataSet(entries,label);
        dataSet.setColor(Color.BLUE);
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }
    public List<Entry> getEntries(float[] array){
        List<Entry> entries = new ArrayList<>();
        for(int i=0;i<array.length;i++){
            entries.add(new Entry(i, array[i]));
        }
        return entries;
    }

}
