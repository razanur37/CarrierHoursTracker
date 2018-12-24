package com.razanur.carrierhourstracker;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {
    public ArrayList<String> dateSet;

    public static class DateViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public DateViewHolder(View v) {
            super(v);
            date = v.findViewById(R.id.tv_item_date);
        }
    }

    public DateAdapter(ArrayList<String> dates) {
        dateSet = dates;
    }

    @NonNull
    @Override
    public DateAdapter.DateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =LayoutInflater.from(parent.getContext()).inflate(R.layout.date_list_item, parent, false);
        DateViewHolder vh = new DateViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(DateViewHolder holder, int position) {
        holder.date.setText(dateSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dateSet.size();
    }

    public void updateList(ArrayList<String> dates) {
        dateSet = dates;
        notifyDataSetChanged();
    }
}
