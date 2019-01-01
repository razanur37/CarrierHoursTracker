package com.razanur.carrierhourstracker;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "day_table")
public class Day {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "date")
    private String mDate;
    private double mStartTime;
    private double mEndTime;
    private boolean mNsDay;
    private double mHoursWorked;
    private double mStraightTime;
    private double mOvertime;
    private double mPenalty;

    public Day(@NonNull String date, double startTime, double endTime, boolean nsDay) {
        mDate = date;
        mStartTime = startTime;
        mEndTime = endTime;
        mNsDay = nsDay;

        setHoursWorked(calcHoursWorked());
        setStraightTime(calcStraightTime());
        setOvertime(calcOvertime());
        setPenalty(calcPenalty());
    }

    private Day(String date) {
        mDate = date;
    }

    static Day dateAsDay(String date) {
        return new Day(date);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof Day))
            return false;

        Day d = (Day) obj;

        return mDate.equals(d.getDate());
    }

    @Override
    public int hashCode() {
        int prime = 37;
        int result = 1;

        result = prime * result + ((mDate == null) ? 0 : mDate.hashCode());

        return result;
    }

    private double calcHoursWorked() {
        return determineLunch(mEndTime - mStartTime);
    }

    private double determineLunch(double hoursWorked) {
        if (hoursWorked >= 6.0)
            return (hoursWorked - 0.5);
        else
            return hoursWorked;
    }

    private double calcStraightTime() {
        if (!mNsDay)
            return Math.min(mHoursWorked, 8.0);
        else
            return 0.0;
    }

    private double calcOvertime() {
        if (!mNsDay)
            return Math.min(mHoursWorked - mStraightTime, 2.0);
        else
           return Math.min(mHoursWorked, 8.0);
    }

    private double calcPenalty() {
        return Math.max(mHoursWorked -(mStraightTime + mOvertime), 0.0);
    }

    public String getDate() {
        return mDate;
    }

    public boolean isNsDay() {
        return mNsDay;
    }

    public double getStartTime() {
        return mStartTime;
    }

    public double getEndTime() {
        return mEndTime;
    }

    public double getHoursWorked() {
        return mHoursWorked;
    }

    public void setHoursWorked(double hoursWorked) {
        mHoursWorked = hoursWorked;
    }

    public double getStraightTime() {
        return mStraightTime;
    }

    public void setStraightTime(double straightTime) {
        mStraightTime = straightTime;
    }

    public double getOvertime() {
        return mOvertime;
    }

    public void setOvertime(double overtime) {
        mOvertime = overtime;
    }

    public double getPenalty() {
        return mPenalty;
    }

    public void setPenalty(double penalty) {
        mPenalty = penalty;
    }
}
