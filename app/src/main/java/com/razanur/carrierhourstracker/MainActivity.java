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

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements DayListAdapter.OnItemLongClickListener,
        DeleteDialogFragment.DeleteDialogListener {

    public static final int NEW_DAY_ACTIVITY_REQUEST_CODE = 1;
    public static final long DELETE_ALL_CONFIRM_ID = -1;

    private DayViewModel mDayViewModel;
    private TextView totalStraightHours;
    private TextView totalOvertimeHours;
    private TextView totalPenaltyHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.date_recycler_view);
        totalStraightHours = findViewById(R.id.total_st_hours);
        totalOvertimeHours = findViewById(R.id.total_ot_hours);
        totalPenaltyHours = findViewById(R.id.total_vt_hours);
        final DayListAdapter adapter = new DayListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDayViewModel = ViewModelProviders.of(this).get(DayViewModel.class);

        mDayViewModel.getAllDays().observe(this, new Observer<List<Day>>() {
            @Override
            public void onChanged(@Nullable List<Day> days) {
                double totalStraight = 0.0;
                double totalOvertime = 0.0;
                double totalPenalty = 0.0;
                if (days != null) {
                    for (Day day : days) {
                        totalStraight += day.getStraightTime();
                        totalOvertime += day.getOvertime();
                        totalPenalty += day.getPenalty();
                    }
                }
                totalStraightHours.setText(String.format(Utils.LOCALE, Utils.DECIMAL_FORMAT, totalStraight));
                totalOvertimeHours.setText(String.format(Utils.LOCALE, Utils.DECIMAL_FORMAT, totalOvertime));
                totalPenaltyHours.setText(String.format(Utils.LOCALE, Utils.DECIMAL_FORMAT, totalPenalty));
                adapter.setDays(days);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewDayActivity.class);
                startActivityForResult(intent, NEW_DAY_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear) {
            showDeleteDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_DAY_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Date date;
            try {
                date = Utils.SHORT_SDF.parse(data.getStringExtra(NewDayActivity.DATE_REPLY));
            } catch (ParseException e) {
                // We'll never be here, so we don't need to worry, but just in case...
                date = new Date();
            }
            Day day = new Day(
                    date,
                    data.getDoubleExtra(NewDayActivity.START_REPLY, 0.0),
                    data.getDoubleExtra(NewDayActivity.END_REPLY,  0.0),
                    data.getBooleanExtra(NewDayActivity.NSDAY_REPLY, false));
            mDayViewModel.insert(day);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onItemLongClicked(Day day) {
        showDeleteDialog(day);
        return true;
    }

    public void showDeleteDialog() {
        DialogFragment dialog = DeleteDialogFragment.newInstance(DELETE_ALL_CONFIRM_ID);
        dialog.show(getSupportFragmentManager(), "DeleteDialogFragment");
    }

    public void showDeleteDialog(Day day) {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = DeleteDialogFragment.newInstance(Converters.dateToTimestamp(day.getDate()));
        dialog.show(getSupportFragmentManager(), "DeleteDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, long id) {
        Day day = Day.dateAsDay(Converters.fromTimestamp(id));
        if (id == DELETE_ALL_CONFIRM_ID)
            mDayViewModel.clear();
        else
            mDayViewModel.delete(day);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // Do nothing
    }
}
