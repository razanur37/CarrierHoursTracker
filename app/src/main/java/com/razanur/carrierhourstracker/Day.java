package com.razanur.carrierhourstracker;

/**
 * Represents a single, continuous block of work performed on a specific date.
 * @author Casey English
 * @version 1.0
 */
class Day {
    /**
     * The Date (in MM/dd/yyyy format) the work was performed.
     */
    private String dateWorked;
    /**
     * The time (in 24-hour decimal format) that work began on dateWorked.
     */
    private double startTime;
    /**
     * The time (in 24-hour decimal format) that work ended on dateWorked.
     */
    private double endTime;
    /**
     * Marks whether the day worked was an NS day or not.
     */
    private boolean isNSDay;
    /**
     * The total hours worked on dateWorked.
     */
    private double hoursWorked;
    /**
     * The amount of hours worked at straight time.
     */
    private double straightTime;
    /**
     * The amount of overtime hours worked.
     */
    private double overtime;
    /**
     * The amount of penalty overtime hours worked.
     */
    private double penalty;

    /**
     * Constructor
     *
     * <p>
     *     Constructor for {@code Day} class. Takes in the Date, Start, and End times and calls
     *     methods to calculate total, straight time, overtime, and penalty hours worked.
     * </p>
     *
     * @param date the date the work was performed.
     * @param start the Start Time for the work.
     * @param end the End Time for the work.
     * @param nsDay whether or not this was an NS day.
     */
    Day(String date, double start, double end, boolean nsDay) {
        dateWorked = date;
        startTime = start;
        endTime = end;
        isNSDay = nsDay;

        calcHoursWorked();
        calcStraightTime();
        calcOvertime();
        calcPenalty();
    }

    /**
     * Special Constructor
     * <p>
     *     Special Constructor designed to be used exclusively with {@link #dateAsDay(String)}.
     * </p>
     * @param date the date the work was performed.
     */
    private Day(String date) {
        dateWorked = date;
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
     *     Equality for a {@code Day} object only compares the {@link #dateWorked} fields between
     *     the two {@code Day} objects and returns {@code true} if the two {@code String} objects
     *     are the same and {@code false} otherwise.
     * </p>
     * @param obj the reference object with which to compare.
     * @return {@code true} if the {@link #dateWorked} fields are the same, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof Day))
            return false;

        Day d = (Day) obj;

        return dateWorked.equals(d.getDateWorked());
    }

    /**
     * Returns a hash code value for the Day object. This method is supported for the benefit of
     * hash tables such as those provided by {@code HashMap}.
     * <p>
     *     Computes the hash code based off the hash code of the {@link #dateWorked}. This means
     *     two {@code Day} objects with the same {@link #dateWorked} will return the same hash code.
     * </p>
     * @return a hash code value for this Day object.
     */
    @Override
    public int hashCode() {
        int prime = 37;
        int result = 1;

        result = prime * result + ((dateWorked == null) ? 0 : dateWorked.hashCode());

        return result;
    }

    /**
     * Calculates the total hours worked.
     * <p>
     *     Subtracts the {@link #startTime} from the {@link #endTime} and updates
     *     {@link #hoursWorked} with the result.
     * </p>
     */
    private void calcHoursWorked() {
        hoursWorked = endTime - startTime;

        determineLunch();
    }

    /**
     * Determines if a lunch was taken.
     * <p>
     *     Checks if {@link #hoursWorked} is 6.0 or more, and subtracts a 30-minute lunch
     *     from {@link #hoursWorked} if it is.
     * </p>
     */
    private void determineLunch() {
        if (hoursWorked >= 6.0)
            hoursWorked -= 0.5;
    }

    /**
     * Calculates the amount of hours worked at the straight time rate.
     * <p>
     *     Sets {@link #straightTime} to the lesser of {@link #hoursWorked} and 8.0.
     * </p>
     */
    private void calcStraightTime() {
        if (!isNSDay)
            straightTime = Math.min(hoursWorked, 8.0);
        else
            straightTime = 0.0;
    }

    /**
     * Calculates the amount of hours worked at the overtime rate.
     * <p>
     *     Sets {@link #overtime} to the lesser of {@link #hoursWorked}-{@link #straightTime}
     *     and 2.0.
     * </p>
     */
    private void calcOvertime() {
        if (!isNSDay)
            overtime = Math.min(hoursWorked-straightTime, 2.0);
        else
            overtime = Math.min(hoursWorked, 8.0);
    }

    /**
     * Calculates the amount of hours worked at the penalty overtime rate.
     * <p>
     *     Sets {@link #penalty} to the greater of
     *     {@link #hoursWorked}-({@link #straightTime}+{@link #overtime} and 0.0.
     * </p>
     */
    private void calcPenalty() {
        penalty = Math.max(hoursWorked-(straightTime+overtime), 0.0);
    }

    /**
     * @return {@link #penalty}
     */
    String getDateWorked() {
        return dateWorked;
    }

    /**
     * @return {@link #startTime}
     */
    double getStartTime() {
        return startTime;
    }

    /**
     * @return {@link #endTime}
     */
    double getEndTime() {
        return endTime;
    }

    /**
     * @return {@link #hoursWorked}
     */
    double getHoursWorked() {
        return hoursWorked;
    }

    /**
     * @return {@link #straightTime}
     */
    double getStraightTime() {
        return straightTime;
    }

    /**
     * @return {@link #overtime}
     */
    double getOvertime() {
        return overtime;
    }

    /**
     * @return {@link #penalty}
     */
    double getPenalty() {
        return penalty;
    }
}
