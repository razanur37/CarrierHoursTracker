package com.razanur.carrierhourstracker;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Represents a single, continuous block of work performed on a specific date.
 * @author Casey English
 * @version 1.0
 */
@Entity(tableName = "day_table")
public class Day {
    /**
     * The Date (in MM/dd/yyyy format) the work was performed.
     */
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "date")
    private String mDate;
    /**
     * The time (in 24-hour decimal format) that work began on mDate.
     */
    private double mStartTime;
    /**
     * The time (in 24-hour decimal format) that work ended on mDate.
     */
    private double mEndTime;
    /**
     * Marks whether the day worked was an NS day or not.
     */
    private boolean mNsDay;
    /**
     * The total hours worked on mDate.
     */
    private double mHoursWorked;
    /**
     * The amount of hours worked at straight time.
     */
    private double mStraightTime;
    /**
     * The amount of mOvertime hours worked.
     */
    private double mOvertime;
    /**
     * The amount of mPenalty mOvertime hours worked.
     */
    private double mPenalty;

    /**
     * Constructor
     *
     * <p>
     *     Constructor for {@code Day} class. Takes in the Date, Start, and End times and calls
     *     methods to calculate total, straight time, mOvertime, and mPenalty hours worked.
     * </p>
     *
     * @param date the date the work was performed.
     * @param startTime the Start Time for the work.
     * @param endTime the End Time for the work.
     * @param nsDay whether or not this was an NS day.
     */
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

    /**
     * Special Constructor
     * <p>
     *     Special Constructor designed to be used exclusively with {@link #dateAsDay(String)}.
     * </p>
     * @param date the date the work was performed.
     */
    private Day(String date) {
        mDate = date;
    }

    /**
     * Static method used to quickly convert a date into a {@code Day} object.
     * <p>
     *     Primarily used in conjunction with {@code List<T>.Contains()} to determine if a date
     *     has already been added to the List.
     * </p>
     * @param date the date the work was performed.
     * @return a new {@code Day} object.
     */
    static Day dateAsDay(String date) {
        return new Day(date);
    }

    /**
     * Indicates whether a {@code Day} object is "equal to" this one.
     * <p>
     *     Equality for a {@code Day} object only compares the {@link #mDate} fields between
     *     the two {@code Day} objects and returns {@code true} if the two {@code String} objects
     *     are the same and {@code false} otherwise.
     * </p>
     * @param obj the reference object with which to compare.
     * @return {@code true} if the {@link #mDate} fields are the same, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof Day))
            return false;

        Day d = (Day) obj;

        return mDate.equals(d.getDate());
    }

    /**
     * Returns a hash code value for the Day object. This method is supported for the benefit of
     * hash tables such as those provided by {@code HashMap}.
     * <p>
     *     Computes the hash code based off the hash code of the {@link #mDate}. This means
     *     two {@code Day} objects with the same {@link #mDate} will return the same hash code.
     * </p>
     * @return a hash code value for this Day object.
     */
    @Override
    public int hashCode() {
        int prime = 37;
        int result = 1;

        result = prime * result + ((mDate == null) ? 0 : mDate.hashCode());

        return result;
    }

    /**
     * Calculates the total hours worked.
     * <p>
     *     Subtracts the {@link #mStartTime} from the {@link #mEndTime} and updates
     *     {@link #mHoursWorked} with the result.
     * </p>
     */
    private double calcHoursWorked() {
        return determineLunch(mEndTime - mStartTime);
    }

    /**
     * Determines if a lunch was taken.
     * <p>
     *     Checks if {@link #mHoursWorked} is 6.0 or more, and subtracts a 30-minute lunch
     *     from {@link #mHoursWorked} if it is.
     * </p>
     */
    private double determineLunch(double hoursWorked) {
        if (hoursWorked >= 6.0)
            return (hoursWorked - 0.5);
        else
            return hoursWorked;
    }

    /**
     * Calculates the amount of hours worked at the straight time rate.
     * <p>
     *     Sets {@link #mStraightTime} to the lesser of {@link #mHoursWorked} and 8.0.
     * </p>
     */
    private double calcStraightTime() {
        if (!mNsDay)
            return Math.min(mHoursWorked, 8.0);
        else
            return 0.0;
    }

    /**
     * Calculates the amount of hours worked at the mOvertime rate.
     * <p>
     *     Sets {@link #mOvertime} to the lesser of {@link #mHoursWorked}-{@link #mStraightTime}
     *     and 2.0.
     * </p>
     */
    private double calcOvertime() {
        if (!mNsDay)
            return Math.min(mHoursWorked - mStraightTime, 2.0);
        else
           return Math.min(mHoursWorked, 8.0);
    }

    /**
     * Calculates the amount of hours worked at the mPenalty mOvertime rate.
     * <p>
     *     Sets {@link #mPenalty} to the greater of
     *     {@link #mHoursWorked}-({@link #mStraightTime}+{@link #mOvertime} and 0.0.
     * </p>
     */
    private double calcPenalty() {
        return Math.max(mHoursWorked -(mStraightTime + mOvertime), 0.0);
    }

    /**
     * @return {@link #mPenalty}
     */
    public String getDate() {
        return mDate;
    }

    /**
     * @return {@link #mNsDay}
     */
    public boolean isNsDay() {
        return mNsDay;
    }

    /**
     * @return {@link #mStartTime}
     */
    public double getStartTime() {
        return mStartTime;
    }

    /**
     * @return {@link #mEndTime}
     */
    public double getEndTime() {
        return mEndTime;
    }

    /**
     * @return {@link #mHoursWorked}
     */
    public double getHoursWorked() {
        return mHoursWorked;
    }

    public void setHoursWorked(double hoursWorked) {
        mHoursWorked = hoursWorked;
    }

    /**
     * @return {@link #mStraightTime}
     */
    public double getStraightTime() {
        return mStraightTime;
    }

    public void setStraightTime(double straightTime) {
        mStraightTime = straightTime;
    }

    /**
     * @return {@link #mOvertime}
     */
    public double getOvertime() {
        return mOvertime;
    }

    public void setOvertime(double overtime) {
        mOvertime = overtime;
    }

    /**
     * @return {@link #mPenalty}
     */
    public double getPenalty() {
        return mPenalty;
    }

    public void setPenalty(double penalty) {
        mPenalty = penalty;
    }
}
