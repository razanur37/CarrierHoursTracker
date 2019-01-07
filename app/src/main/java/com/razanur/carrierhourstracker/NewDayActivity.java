/*
 * Copyright (c) 2019 Casey English
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.razanur.carrierhourstracker;

import android.app.DatePickerDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewDayActivity extends AppCompatActivity {

    private EditText startTime;
    private EditText endTime;
    private EditText dateText;
    private RadioGroup nsDayGroup;
    private List<Day> mDays;

    private Day oldDay;

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_new_day);

        startTime = findViewById(R.id.et_start_time);
        endTime = findViewById(R.id.et_end_time);
        dateText = findViewById(R.id.et_date);
        nsDayGroup = findViewById(R.id.ns_day_group);

        oldDay = intent.getParcelableExtra("day");
        if (oldDay != null) {
            startTime.setText(String.format(Utils.LOCALE, Utils.DECIMAL_FORMAT, oldDay.getStartTime()));
            endTime.setText(String.format(Utils.LOCALE, Utils.DECIMAL_FORMAT, oldDay.getEndTime()));
            if (oldDay.isNsDay())
                nsDayGroup.check(R.id.rb_ns_yes);
            Date date = oldDay.getDate();
            dateText.setText(Utils.SHORT_SDF.format(date));
            Button button = findViewById(R.id.button_submit);
            button.setText(R.string.update);
        }

        DayViewModel mDayViewModel = ViewModelProviders.of(this).get(DayViewModel.class);

        mDayViewModel.getAllDays().observe(this, new Observer<List<Day>>() {
            @Override
            public void onChanged(@Nullable List<Day> days) {
                mDays = days;
            }
        });

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
        dateText.setText(Utils.SHORT_SDF.format(myCalendar.getTime()));
    }

    public void setTotals(View v) {
        InputMethodManager imm = (InputMethodManager) getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        double start;
        double end;
        String dateString;
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

        dateString = dateText.getText().toString();
        if (dateString.equals("")) {
            showToast("Enter a Date");
            return;
        }

        Date date;
        try {
            date = Utils.SHORT_SDF.parse(dateString);
        } catch (ParseException e) {
            // Never here
            date = new Date();
        }

        inputsVerified = verifyInputs(start, end, date);

        Intent replyIntent = new Intent();

        if (inputsVerified) {
            Day day;
            if (oldDay != null) {
                // We're editing
                day = new Day(oldDay, date, start, end, isNSDay);
            } else {
                day = new Day(date, start, end, isNSDay);
            }
            replyIntent.putExtra("day", day);
            setResult(RESULT_OK, replyIntent);
            finish();
        }
    }

    private boolean verifyInputs(double start, double end, Date date) {
        // Verify inputs
        if (mDays.contains(Day.dateAsDay(date))) {
            // Date exists, check if we're editing or adding
            if (oldDay != null) {
                // We're editing, check if the date matches an existing date because that's the
                // date we're editing.
                if (!date.equals((oldDay.getDate()))) {
                    showToast("Enter a Different Date From Ones Already Entered");
                    return false;
                }
            } else {
                // We're adding, the date can't exist
                showToast("Enter a Different Date From Ones Already Entered");
                return false;
            }
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
