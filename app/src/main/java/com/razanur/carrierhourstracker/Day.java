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

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;


@Entity(tableName = "day_table")
public class Day {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "date")
    private Date mDate;
    private double mStartTime;
    private double mEndTime;
    private boolean mNsDay;
    private boolean mExcluded;
    private double mHoursWorked;
    private double mStraightTime;
    private double mOvertime;
    private double mPenalty;

    public Day(@NonNull Date date, double startTime, double endTime, boolean nsDay) {
        mDate = date;
        mStartTime = startTime;
        mEndTime = endTime;
        mNsDay = nsDay;

        mExcluded = determineIfExcluded(date);

        mHoursWorked = calcHoursWorked();
        mStraightTime = calcStraightTime();
        mOvertime = calcOvertime();
        mPenalty = calcPenalty();
    }

    private Day(@NonNull Date date) {
        mDate = date;
    }

    static Day dateAsDay(Date date) {
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
        double hoursWorked = determineLunch(mEndTime - mStartTime);
        if (!mNsDay && 7.92 <= hoursWorked && hoursWorked < 8.0)
            return 8.0;
        else
            return hoursWorked;
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

    private boolean determineIfExcluded(Date date) {
        String exclusionStart;
        String exclusionEnd;

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)-1);
                exclusionStart = Utils.SHORT_SDF.format(cal.getTime());
                break;
            case Calendar.MONDAY:
                cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)+5);
                exclusionStart = Utils.SHORT_SDF.format(cal.getTime());
                break;
            case Calendar.TUESDAY:
                cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)+4);
                exclusionStart = Utils.SHORT_SDF.format(cal.getTime());
                break;
            case Calendar.WEDNESDAY:
                cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)+3);
                exclusionStart = Utils.SHORT_SDF.format(cal.getTime());
                break;
            case Calendar.THURSDAY:
                cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)+2);
                exclusionStart = Utils.SHORT_SDF.format(cal.getTime());
                break;
            case Calendar.FRIDAY:
                cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)+1);
                exclusionStart = Utils.SHORT_SDF.format(cal.getTime());
                break;
            default: // Calendar.SATURDAY
                exclusionStart = Utils.SHORT_SDF.format(cal.getTime());
                break;
        }

        cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)+27);
        exclusionEnd = Utils.SHORT_SDF.format(cal.getTime());

        Date startDate;
        Date endDate;

        try {
            startDate = Utils.SHORT_SDF.parse(exclusionStart);
            endDate = Utils.SHORT_SDF.parse(exclusionEnd);

            if ((date.after(startDate) && date.before(endDate)) ||
                    (date.equals(startDate)) || date.equals(endDate)) {
                return true;
            }
        } catch (ParseException e) {
            // We only get here if the strings are badly formatted, but they are formatted by
            // sdf.format(), so we'll never get here.
        }
        return false;
    }

    public Date getDate() {
        return mDate;
    }

    boolean isNsDay() {
        return mNsDay;
    }

    double getStartTime() {
        return mStartTime;
    }

    double getEndTime() {
        return mEndTime;
    }

    boolean isExcluded() {
        return mExcluded;
    }

    void setExcluded(boolean excluded) {
        mExcluded = excluded;
    }

    double getHoursWorked() {
        return mHoursWorked;
    }

    void setHoursWorked(double hoursWorked) {
        mHoursWorked = hoursWorked;
    }

    double getStraightTime() {
        return mStraightTime;
    }

    void setStraightTime(double straightTime) {
        mStraightTime = straightTime;
    }

    double getOvertime() {
        return mOvertime;
    }

    void setOvertime(double overtime) {
        mOvertime = overtime;
    }

    double getPenalty() {
        return mPenalty;
    }

    void setPenalty(double penalty) {
        mPenalty = penalty;
    }
}
