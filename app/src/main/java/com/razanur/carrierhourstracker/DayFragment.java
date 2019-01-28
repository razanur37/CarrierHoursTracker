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

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class DayFragment extends Fragment {

    static final String TAG = "DAY_FRAGMENT";

    private EditText startTime;
    private EditText endTime;
    private EditText dateText;
    private RadioGroup nsDayGroup;
    private List<Day> mDays;
    private Button button;

    private Day oldDay;

    private final Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener dateListener;
    private NewDayListener mListener;

    public interface NewDayListener {
        void onDaySet(Day day, boolean isEditing);
        void onButtonClick(View v);
    }

    public DayFragment() {
        // Required empty constructor
    }

    static DayFragment newInstance() {
        return new DayFragment();
    }
    static DayFragment newInstance(Day day) {
        Bundle args = new Bundle();
        args.putParcelable("day", day);
        DayFragment fragment = new DayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null)
            oldDay = args.getParcelable("day");
        View view = inflater.inflate(R.layout.content_day, container, false);

        startTime = view.findViewById(R.id.et_start_time);
        endTime = view.findViewById(R.id.et_end_time);
        dateText = view.findViewById(R.id.et_date);
        nsDayGroup = view.findViewById(R.id.ns_day_group);
        button = view.findViewById(R.id.button_submit);

        if (oldDay != null) {
            updateDayView(oldDay);
        }

        DayViewModel mDayViewModel = ViewModelProviders.of(this).get(DayViewModel.class);

        mDayViewModel.getAllDays().observe(this, new Observer<List<Day>>() {
            @Override
            public void onChanged(@Nullable List<Day> days) {
                mDays = days;
            }
        });

        createDateDialog();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Activity activity = getActivity();

        if (activity.findViewById(R.id.container) != null) {
            FloatingActionButton fab = activity.findViewById(R.id.fab);
            if (fab.getVisibility() == View.VISIBLE)
                fab.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        DayViewModel.setActiveFragment(TAG);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mListener = (NewDayListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
                if (getContext() == null)
                    return;
                new DatePickerDialog(getContext(), dateListener,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
    }

    void updateDayView(Day day) {
        oldDay = day;
        startTime.setText(String.format(Utils.LOCALE, Utils.DECIMAL_FORMAT, day.getStartTime()));
        endTime.setText(String.format(Utils.LOCALE, Utils.DECIMAL_FORMAT, day.getEndTime()));
        if (day.isNsDay())
            nsDayGroup.check(R.id.rb_ns_yes);
        Date date = day.getDate();
        dateText.setText(Utils.SHORT_SDF.format(date));
        button.setText(R.string.update);
    }

    private void updateLabel() {
        dateText.setText(Utils.SHORT_SDF.format(myCalendar.getTime()));
    }

    void onButtonClick(View v) {
        if (getContext() == null)
            return;
        InputMethodManager imm = (InputMethodManager) getContext()
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


        if (inputsVerified) {
            Day day;
            if (oldDay != null) {
                // We're editing
                day = new Day(oldDay, date, start, end, isNSDay);
            } else {
                day = new Day(date, start, end, isNSDay);
            }
            mListener.onDaySet(day, oldDay != null);
            if (getActivity() == null)
                return;
            getActivity().getSupportFragmentManager().popBackStack();
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
        Context context = getContext();
        int duration = Toast.LENGTH_SHORT;

        Toast.makeText(context, message, duration).show();
    }
}
