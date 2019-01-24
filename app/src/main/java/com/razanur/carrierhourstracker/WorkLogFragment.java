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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WorkLogFragment extends Fragment {

    static final String TAG = "WORK_LOG_FRAGMENT";

    private DayListAdapter adapter;
    private DayViewModel mDayViewModel;

    public WorkLogFragment() {
        // Required empty constructor
    }

    static WorkLogFragment newInstance() {
        return new WorkLogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.date_recycler_view);
        adapter = new DayListAdapter(view.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mDayViewModel = ViewModelProviders.of(this).get(DayViewModel.class);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        if (fab.getVisibility() == View.INVISIBLE)
            fab.setVisibility(View.VISIBLE);

        mDayViewModel.getAllDays().observe(this, new Observer<List<Day>>() {
            @Override
            public void onChanged(@Nullable List<Day> days) {
                adapter.setDays(days);
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        DayViewModel.setActiveFragment(TAG);
    }
}
