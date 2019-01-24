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

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.razanur.carrierhourstracker.settings.SettingsActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity
        implements DayListAdapter.OnItemClickListener,
        DayListAdapter.OnItemLongClickListener,
        DayFragment.NewDayListener,
        DeleteDialogFragment.DeleteDialogListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    final String WORK_LOG_FRAGMENT_TAG =  WorkLogFragment.TAG;
    final String DAY_FRAGMENT_TAG = DayFragment.TAG;
    final String WEEK_FRAGMENT_TAG = WeekFragment.TAG;
    final String CONVERSION_FRAGMENT_TAG = ConversionFragment.TAG;

    private DayViewModel mDayViewModel;
    private DayFragment dayFragment;
    private FragmentManager fragmentManager = getSupportFragmentManager();

    static boolean isRoundingEnabled;
    String activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getBoolean(R.bool.isTablet))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDayViewModel = ViewModelProviders.of(this).get(DayViewModel.class);

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        isRoundingEnabled = sharedPreferences.getBoolean("rounding", true);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (findViewById(R.id.container) != null) {

            FloatingActionButton fab = findViewById(R.id.fab);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dayFragment = DayFragment.newInstance();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, dayFragment, DAY_FRAGMENT_TAG)
                            .addToBackStack(null)
                            .commit();
                }
            });

            if (activeFragment == null)
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, WorkLogFragment.newInstance(), WORK_LOG_FRAGMENT_TAG)
                        .commit();
            else {
                Fragment fragment = fragmentManager.findFragmentByTag(activeFragment);
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, fragment, activeFragment)
                        .commit();
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        activeFragment = DayViewModel.getActiveFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        /*if (findViewById(R.id.container) == null)
            findViewById(R.id.action_week_view).setVisibility(View.INVISIBLE);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.action_week_view:
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, WeekFragment.newInstance(), WEEK_FRAGMENT_TAG)
                        .addToBackStack(null)
                        .commit();
                return true;

            case R.id.action_conversion:
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, ConversionFragment.newInstance(), CONVERSION_FRAGMENT_TAG)
                        .addToBackStack(null)
                        .commit();
                return true;

            case R.id.action_clear:
                showDeleteDialog();
                return true;

            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onDaySet(Day day, boolean isEditing) {

        if (!isEditing)
            mDayViewModel.insert(day);
        else
            mDayViewModel.update(day);
    }

    @Override
    public void onButtonClick(View v) {

        if (dayFragment == null) {
            dayFragment = (DayFragment) fragmentManager.findFragmentById(R.id.day_fragment);
            dayFragment.onButtonClick(v);
        } else {
            dayFragment.onButtonClick(v);
        }
    }

    @Override
    public void onItemClicked(Day day) {
        dayFragment = (DayFragment) fragmentManager.findFragmentById(R.id.day_fragment);

        if (dayFragment != null) {
            dayFragment.updateDayView(day);
        } else {
            dayFragment = DayFragment.newInstance(day);
            fragmentManager.beginTransaction()
                    .replace(R.id.container, dayFragment, DAY_FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onItemLongClicked(Day day) {
        showDeleteDialog(day);
    }

    public void showDeleteDialog() {
        DialogFragment dialog = DeleteDialogFragment.newInstance();
        dialog.show(fragmentManager, "DeleteDialogFragment");
    }

    public void showDeleteDialog(Day day) {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = DeleteDialogFragment.newInstance(day);
        dialog.show(fragmentManager, "DeleteDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, Day day) {
        if (day == null)
            mDayViewModel.clear();
        else
            mDayViewModel.delete(day);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // Do nothing
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
        if (key.equals("rounding")) {
            isRoundingEnabled = preferences.getBoolean(key, true);
            mDayViewModel.refresh();
        }
    }
}
