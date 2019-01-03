package com.razanur.carrierhourstracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DayListAdapter extends RecyclerView.Adapter<DayListAdapter.DayViewHolder> {

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
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        if (mDays != null) {
            Day current = mDays.get(position);
            String format = "%.2f";
            Locale locale = Locale.US;
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy", locale);

            holder.mDate.setText(sdf.format(current.getDate()));
            holder.mStartTime.setText(String.format(locale, format, current.getStartTime()));
            holder.mEndTime.setText(String.format(locale, format, current.getEndTime()));
            holder.mHoursWorked.setText(String.format(locale, format, current.getHoursWorked()));
            holder.mOvertime.setText(String.format(locale, format, current.getOvertime()));
            holder.mPenalty.setText(String.format(locale, format, current.getPenalty()));
        } else {
            holder.mDate.setText("No Days");
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
