package com.razanur.carrierhourstracker;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


@Entity(tableName = "day_table")
public class Day {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "date")
    private String mDate;
    private double mStartTime;
    private double mEndTime;
    private boolean mNsDay;
    private boolean mExcluded;
    private double mHoursWorked;
    private double mStraightTime;
    private double mOvertime;
    private double mPenalty;

    public Day(@NonNull String date, double startTime, double endTime, boolean nsDay) {
        mDate = date;
        mStartTime = startTime;
        mEndTime = endTime;
        mNsDay = nsDay;

        mExcluded = determineIfExcluded(CarrierUtils.unReverseDate(date));

        mHoursWorked = calcHoursWorked();
        mStraightTime = calcStraightTime();
        mOvertime = calcOvertime();
        mPenalty = calcPenalty();
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

        result = prime * result +  mDate.hashCode();

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
        if (!mNsDay) {
            if (!mExcluded)
                return Math.min(mHoursWorked - mStraightTime, 2.0);
            else
                return mHoursWorked-mStraightTime;
        }
        else
           return Math.min(mHoursWorked, 8.0);
    }

    private double calcPenalty() {
        if (!mExcluded)
            return Math.max(mHoursWorked -(mStraightTime + mOvertime), 0.0);
        else
            return 0;
    }

    private boolean determineIfExcluded(String date) {
        String format = "MM/dd/yyyy";
        String exclusionStart;
        String exclusionEnd;
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.YEAR, Integer.valueOf(date.substring(6)));
        cal.set(Calendar.DAY_OF_MONTH, 1);

        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)-1);
                exclusionStart = sdf.format(cal.getTime());
                break;
            case 2:
                cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)+5);
                exclusionStart = sdf.format(cal.getTime());
                break;
            case 3:
                cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)+4);
                exclusionStart = sdf.format(cal.getTime());
                break;
            case 4:
                cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)+3);
                exclusionStart = sdf.format(cal.getTime());
                break;
            case 5:
                cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)+2);
                exclusionStart = sdf.format(cal.getTime());
                break;
            case 6:
                cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)+1);
                exclusionStart = sdf.format(cal.getTime());
                break;
            default:
                exclusionStart = sdf.format(cal.getTime());
                break;
        }

        cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)+27);
        exclusionEnd = sdf.format(cal.getTime());

        Date startDate;
        Date endDate;
        Date currentDate;

        try {
            startDate = sdf.parse(exclusionStart);
            endDate = sdf.parse(exclusionEnd);
            currentDate = sdf.parse(date);

            if ((currentDate.after(startDate) && currentDate.before(endDate)) ||
                    (date.equals(exclusionStart)) || date.equals(exclusionEnd)) {
                return true;
            }
        } catch (ParseException e) {
            // We only get here if the strings are badly formatted, but they are formatted by
            // sdf.format(), so we'll never get here.
        }
        return false;
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

    public boolean isExcluded() {
        return mExcluded;
    }

    public void setExcluded(boolean excluded) {
        mExcluded = excluded;
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
