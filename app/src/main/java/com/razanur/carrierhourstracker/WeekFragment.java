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
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeekFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekFragment extends Fragment {

    private DayViewModel mDayViewModel;
    private TextView totalStraightHours;

    public WeekFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WeekFragment.
     */
    public static WeekFragment newInstance() {
        return new WeekFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_week, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.week_recycler_view);

        final WeekListAdapter adapter = new WeekListAdapter(view.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mDayViewModel = ViewModelProviders.of(this).get(DayViewModel.class);

        mDayViewModel.getAllDays().observe(this, new Observer<List<Day>>() {
            @Override
            public void onChanged(@Nullable List<Day> days) {
                Function<Day, String> weekNum = new Function<Day, String>() {
                    @Override
                    public String apply(Day input) {
                        return input.getWeekYear();
                    }
                };

                Map<String, List<Day>> weekMap;

                if (days != null) {
                    weekMap = days.stream().collect(Collectors.groupingBy(weekNum));
                } else
                    weekMap = null;
                adapter.setWeeks(weekMap);
            }
        });

        return view;
    }

    private int calcWeek(Day day) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.SATURDAY);
        cal.setTime(day.getDate());
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    private Date getSatOfWeek(Day day) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.SATURDAY);
        cal.setTime(day.getDate());
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        return cal.getTime();
    }

    private Date getFriOfWeek(Day day) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.SATURDAY);
        cal.setTime(day.getDate());
        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        return cal.getTime();
    }
}
