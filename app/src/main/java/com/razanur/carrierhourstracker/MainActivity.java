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

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity
        implements DayListAdapter.OnItemClickListener,
        DayListAdapter.OnItemLongClickListener,
        DayFragment.NewDayListener,
        DeleteDialogFragment.DeleteDialogListener {

    public static final long DELETE_ALL_CONFIRM_ID = -1;

    private DayViewModel mDayViewModel;
    private DayFragment newDayFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDayViewModel = ViewModelProviders.of(this).get(DayViewModel.class);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newDayFragment = DayFragment.newInstance();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, newDayFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, WorkLogFragment.newInstance())
                .commitNow();
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

        if (id == R.id.action_week_view) {
            Fragment weekView = WeekFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, weekView);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        newDayFragment.onButtonClick(v);
    }

    @Override
    public void onItemClicked(Day day) {
        newDayFragment = DayFragment.newInstance(day);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, newDayFragment)
                .addToBackStack(null)
                .commit();
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
        DialogFragment dialog = DeleteDialogFragment.newInstance(day);
        dialog.show(getSupportFragmentManager(), "DeleteDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, long id) {
        mDayViewModel.clear();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, Day day) {
        mDayViewModel.delete(day);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // Do nothing
    }
}
