package com.razanur.carrierhourstracker;

/**
 * Represents a single, continuous block of work performed on a specific date.
 * @author Casey English
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
     *     Constructor for Day class. Takes in the Date, Start, and End times and calls methods to
     *     calculate total, straight time, overtime, and penalty hours worked.
     * </p>
     *
     * @param date The date the work was performed.
     * @param start The Start Time for the work.
     * @param end The End Time for the work.
     */
    Day(String date, double start, double end) {
        dateWorked = date;
        startTime = start;
        endTime = end;

        calcHoursWorked();
        calcStraightTime();
        calcOvertime();
        calcPenalty();
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
        straightTime = Math.min(hoursWorked, 8.0);
    }

    /**
     * Calculates the amount of hours worked at the overtime rate.
     * <p>
     *     Sets {@link #overtime} to the lesser of {@link #hoursWorked}-{@link #straightTime}
     *     and 2.0.
     * </p>
     */
    private void calcOvertime() {
        if (hoursWorked > 8.0)
            overtime = Math.min(hoursWorked-straightTime, 2.0);
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
