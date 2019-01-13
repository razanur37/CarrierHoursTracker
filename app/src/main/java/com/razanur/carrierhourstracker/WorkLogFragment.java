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

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WorkLogFragment extends Fragment {
    WorkLogFragmentListener mListener;
    private DayViewModel mDayViewModel;
    private TextView totalStraightHours;
    private TextView totalOvertimeHours;
    private TextView totalPenaltyHours;

    public interface WorkLogFragmentListener {
        //public void onDaySelected(int position);
    }

    public static WorkLogFragment newInstance() {
        return new WorkLogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.date_recycler_view);
        totalStraightHours = view.findViewById(R.id.total_st_hours);
        totalOvertimeHours = view.findViewById(R.id.total_ot_hours);
        totalPenaltyHours = view.findViewById(R.id.total_vt_hours);
        final DayListAdapter adapter = new DayListAdapter(view.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

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

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WorkLogFragment.WorkLogFragmentListener) {
            mListener = (WorkLogFragment.WorkLogFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement WorkLogFragmentListener");
        }
    }
}
