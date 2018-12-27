package com.razanur.carrierhourstracker;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {
    private ArrayList<Day> daysList;

    static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView startTime;
        TextView endTime;
        TextView hoursWorked;
        TextView overtime;
        TextView penalty;
        DateViewHolder(View v) {
            super(v);
            date = v.findViewById(R.id.tv_item_date);
            startTime = v.findViewById(R.id.tv_item_start_time);
            endTime = v.findViewById(R.id.tv_item_end_time);
            hoursWorked = v.findViewById(R.id.tv_item_hours);
            overtime = v.findViewById(R.id.tv_item_overtime);
            penalty = v.findViewById(R.id.tv_item_penalty);
        }
    }

    DateAdapter(ArrayList<Day> days) {
        daysList = days;
    }

    @NonNull
    @Override
    public DateAdapter.DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_list_item, parent, false);
        return new DateViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        String format = "%.2f";
        Locale locale = Locale.US;

        holder.date.setText(daysList.get(position).getDateWorked());
        holder.startTime.setText(String.format(locale, format, daysList.get(position).getStartTime()));
        holder.endTime.setText(String.format(locale, format, daysList.get(position).getEndTime()));
        holder.hoursWorked.setText(String.format(locale, format, daysList.get(position).getHoursWorked()));
        holder.overtime.setText(String.format(locale, format, daysList.get(position).getOvertime()));
        holder.penalty.setText(String.format(locale, format, daysList.get(position).getPenalty()));
    }

    @Override
    public int getItemCount() {
        return daysList.size();
    }

    void updateList(ArrayList<Day> dates) {
        daysList = dates;
        notifyDataSetChanged();
    }
}
