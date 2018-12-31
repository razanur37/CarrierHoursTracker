package com.razanur.carrierhourstracker;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewDayActivity extends AppCompatActivity {

    public static final String DATE_REPLY = "com.razanur.carrierhourstracker.daylistsql.DATE";
    public static final String START_REPLY = "com.razanur.carrierhourstracker.daylistsql.START";
    public static final String END_REPLY = "com.razanur.carrierhourstracker.daylistsql.END";
    public static final String NSDAY_REPLY = "com.razanur.carrierhourstracker.daylistsql.NSDAY";

    private EditText startTime;
    private EditText endTime;
    private EditText dateText;
    private RadioGroup nsDayGroup;

    private DayViewModel mDayViewModel;

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_day);

        startTime = findViewById(R.id.et_start_time);
        endTime = findViewById(R.id.et_end_time);
        dateText = findViewById(R.id.et_date);
        nsDayGroup = findViewById(R.id.ns_day_group);

        mDayViewModel = ViewModelProviders.of(this).get(DayViewModel.class);

        createDateDialog();
    }

    private boolean checkNSDay(int checkedRadioButtonId) {
        return checkedRadioButtonId == R.id.rb_ns_yes;
    }

    private void createDateDialog() {
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
                new DatePickerDialog(NewDayActivity.this, dateListener,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
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

        double start;
        double end;
        String date;
        boolean inputsVerified;
        boolean isNSDay = checkNSDay(nsDayGroup.getCheckedRadioButtonId());

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

        inputsVerified = verifyInputs(start, end, date);

        Intent replyIntent = new Intent();

        if (inputsVerified) {
            replyIntent.putExtra(DATE_REPLY, date);
            replyIntent.putExtra(START_REPLY, start);
            replyIntent.putExtra(END_REPLY, end);
            replyIntent.putExtra(NSDAY_REPLY, isNSDay);
            setResult(RESULT_OK, replyIntent);
            finish();
        }
    }

    private boolean verifyInputs(double start, double end, String date) {
        // Verify inputs
        if (mDayViewModel.doesListContainDay(date)) {
            showToast("Enter a Different Date From Ones Already Entered");
            return false;
        }

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
