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
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DayListAdapter extends RecyclerView.Adapter<DayListAdapter.DayViewHolder> {

    public interface OnItemClickListener {
        void onItemClicked(Day day);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClicked(Day day);
    }

    class DayViewHolder extends RecyclerView.ViewHolder {
        private final TextView mDate;
        private final TextView mStartTime;
        private final TextView mEndTime;
        private final TextView mHoursWorked;
        private final TextView mOvertime;
        private final TextView mPenalty;

        DayViewHolder(View itemView) {
            super(itemView);
            mDate = itemView.findViewById(R.id.tv_item_date);
            mStartTime = itemView.findViewById(R.id.tv_item_start_time);
            mEndTime = itemView.findViewById(R.id.tv_item_end_time);
            mHoursWorked = itemView.findViewById(R.id.tv_item_hours);
            mOvertime = itemView.findViewById(R.id.tv_item_overtime);
            mPenalty = itemView.findViewById(R.id.tv_item_penalty);
        }
    }

    private final LayoutInflater mInflater;
    private List<Day> mDays;

    DayListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new DayViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, final int position) {
        if (mDays != null) {
            final Day current = mDays.get(position);

            holder.mDate.setText(Utils.LONG_SDF.format(current.getDate()));
            holder.mStartTime.setText(String.format(Utils.LOCALE, Utils.DECIMAL_FORMAT, current.getStartTime()));
            holder.mEndTime.setText(String.format(Utils.LOCALE, Utils.DECIMAL_FORMAT, current.getEndTime()));
            holder.mHoursWorked.setText(String.format(Utils.LOCALE, Utils.DECIMAL_FORMAT, current.getHoursWorked()));
            holder.mOvertime.setText(String.format(Utils.LOCALE, Utils.DECIMAL_FORMAT, current.getOvertime()));
            holder.mPenalty.setText(String.format(Utils.LOCALE, Utils.DECIMAL_FORMAT, current.getPenalty()));

            Log.d("ROWID_DEBUG", Integer.toString(current.getRowID()));

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

    void setDays(List<Day> days) {
        mDays = days;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mDays != null)
            return mDays.size();
        else
            return 0;
    }
}
