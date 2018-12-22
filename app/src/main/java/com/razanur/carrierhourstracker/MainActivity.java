package com.razanur.carrierhourstracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText startTime;
    private EditText endTime;
    private TextView totalHours;
    private Button calcButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startTime = findViewById(R.id.et_start_time);
        endTime = findViewById(R.id.et_end_time);
        totalHours = findViewById(R.id.tv_total_hours);
        calcButton = findViewById(R.id.button_calculate);

        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double start = Double.parseDouble(startTime.getText().toString());
                double end = Double.parseDouble(endTime.getText().toString());
                double total = end - start;
                totalHours.setText(Double.toString(total));
            }
        });
    }
}
