package com.razanur.carrierhourstracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText startTime;
    private EditText endTime;
    private TextView totalHours;
    private TextView straightHours;
    private TextView overtimeHours;
    private TextView penaltyHours;
    private Button calcButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startTime = findViewById(R.id.et_start_time);
        endTime = findViewById(R.id.et_end_time);
        totalHours = findViewById(R.id.tv_total_hours);
        straightHours = findViewById(R.id.tv_total_straight_hours);
        overtimeHours = findViewById(R.id.tv_total_overtime_hours);
        penaltyHours = findViewById(R.id.tv_total_penalty_hours);
        calcButton = findViewById(R.id.button_calculate);
        final EditText[] times = {
                startTime,
                endTime
        };
        final TextView[] totals = {
                totalHours,
                straightHours,
                overtimeHours,
                penaltyHours};

        endTime.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                HoursFunctions.calculateHours(getApplicationContext(), times, totals);
                return true;
            }
        });
        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HoursFunctions.calculateHours(getApplicationContext(), times, totals);
            }
        });
    }


}
