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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WeekListAdapter extends RecyclerView.Adapter<WeekListAdapter.WeekViewHolder> {

    class WeekViewHolder extends RecyclerView.ViewHolder {
        private final TextView mWeek;
        private final TextView mStraight;
        private final TextView mOvertime;
        private final TextView mPenalty;

        WeekViewHolder(View itemView) {
            super(itemView);
            mWeek = itemView.findViewById(R.id.tv_item_week);
            mStraight = itemView.findViewById(R.id.tv_week_straight);
            mOvertime = itemView.findViewById(R.id.tv_week_overtime);
            mPenalty = itemView.findViewById(R.id.tv_week_penalty);
        }
    }

    private final LayoutInflater mInflater;
    private Map<String, List<Day>> mWeekMap;
    private List<String> mWeekYears;

    WeekListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public WeekListAdapter.WeekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new WeekListAdapter.WeekViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeekListAdapter.WeekViewHolder holder, final int position) {
        if (mWeekMap != null) {
            final List<Day> current = mWeekMap.get(mWeekYears.get(position));
            Date sat = getSatOfWeek(current.get(0));
            Date fri = getFriOfWeek(current.get(0));
            String week = Utils.SHORT_SDF.format(sat) + "-" + Utils.SHORT_SDF.format(fri);

            holder.mWeek.setText(week);
            holder.mStartTime.setText(String.format(Utils.LOCALE, Utils.DECIMAL_FORMAT, current.getStartTime()));
            holder.mEndTime.setText(String.format(Utils.LOCALE, Utils.DECIMAL_FORMAT, current.getEndTime()));
            holder.mHoursWorked.setText(String.format(Utils.LOCALE, Utils.DECIMAL_FORMAT, current.getHoursWorked()));
            holder.mOvertime.setText(String.format(Utils.LOCALE, Utils.DECIMAL_FORMAT, current.getOvertime()));
            holder.mPenalty.setText(String.format(Utils.LOCALE, Utils.DECIMAL_FORMAT, current.getPenalty()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) mInflater.getContext()).onItemClicked(current);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ((MainActivity) mInflater.getContext()).onItemLongClicked(current);
                    return true;
                }
            });
        } else {
            holder.mDate.setText("");
        }
    }

    void setDays(Map<String, List<Day>> weekMap) {
        mWeekMap = weekMap;
        TreeSet<String> weekYears = new TreeSet<>(mWeekMap.keySet());
        mWeekYears = new ArrayList<>(weekYears);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mDays != null)
            return mDays.size();
        else
            return 0;
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
