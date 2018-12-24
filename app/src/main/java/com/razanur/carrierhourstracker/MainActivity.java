package com.razanur.carrierhourstracker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText startTime;
    private EditText endTime;
    private EditText dateText;
    private TextView totalHours;
    private TextView straightHours;
    private TextView overtimeHours;
    private TextView penaltyHours;
    private RecyclerView dateRecyclerView;
    private RecyclerView.Adapter dateRecyclerAdapter;
    private RecyclerView.LayoutManager dateRecyclerManager;

    ArrayList<String> dateSet = new ArrayList<>();

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        startTime = findViewById(R.id.et_start_time);
        endTime = findViewById(R.id.et_end_time);
        dateText = findViewById(R.id.et_date);
        totalHours = findViewById(R.id.tv_total_hours);
        straightHours = findViewById(R.id.tv_total_straight_hours);
        overtimeHours = findViewById(R.id.tv_total_overtime_hours);
        penaltyHours = findViewById(R.id.tv_total_penalty_hours);

        dateRecyclerView = findViewById(R.id.date_recycler_view);
        dateRecyclerView.setHasFixedSize(true);

        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, dateListener,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        dateRecyclerManager = new LinearLayoutManager(this);
        dateRecyclerView.setLayoutManager(dateRecyclerManager);

        dateRecyclerAdapter = new DateAdapter(dateSet);
        dateRecyclerView.setAdapter(dateRecyclerAdapter);
    }

    private void updateLabel() {
        String format = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);

        dateText.setText(sdf.format(myCalendar.getTime()));
    }

    public void setTotals(View v) {
        InputMethodManager imm = (InputMethodManager) getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        Day day;
        Locale locale = Locale.US;
        String stringFormat = "%.2f";

        double start;
        double end;
        String date;
        boolean inputsVerified;

        // Make sure inputs were entered
        try {
            start = Double.parseDouble(startTime.getText().toString());
            end = Double.parseDouble(endTime.getText().toString());
        } catch (NumberFormatException e) {
            showToast("Enter a Start Time and an End Time");
            return;
        }

        date = dateText.getText().toString();
        if (date.equals("")) {
            showToast("Enter a Date");
            return;
        }

        inputsVerified = verifyInputs(start, end);

        if (inputsVerified)
            day = new Day(date, start, end);
        else
            return;

        totalHours.setText(String.format(locale, stringFormat, day.getHoursWorked()));
        straightHours.setText(String.format(locale, stringFormat, day.getStraightTime()));
        overtimeHours.setText(String.format(locale, stringFormat, day.getOvertime()));
        penaltyHours.setText(String.format(locale, stringFormat, day.getPenalty()));
        dateSet.add(day.getDate());

        ((DateAdapter) dateRecyclerAdapter).updateList(dateSet);
    }



    private boolean verifyInputs(double start, double end) {
        // Verify inputs
        if (start >= 24.0 || end >= 24.0) {
            showToast("Start and End Times Must be on a 24-hour Clock");
            return false;
        }

        if (start > end) {
            showToast("End Time Must be Later Than Start Time");
            return false;
        }

        return true;
    }

    private void showToast(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast.makeText(context, message, duration).show();
    }


}
