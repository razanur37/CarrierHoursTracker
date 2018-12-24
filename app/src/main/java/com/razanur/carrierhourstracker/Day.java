package com.razanur.carrierhourstracker;

class Day {
    private String date;
    private double startTime;
    private double endTime;
    private double hoursWorked;
    private double straightTime;
    private double overtime;
    private double penalty;

    Day(String aDate, double start, double end) {
        date = aDate;

        startTime = start;
        endTime = end;

        hoursWorked = end - start;

        if (hoursWorked >= 6.0)
            hoursWorked -= 0.5;

        straightTime = Math.min(hoursWorked, 8.0);
        if (hoursWorked > 8.0)
            overtime = Math.min(hoursWorked-8.0, 2.0);
        penalty = Math.max(hoursWorked-10.0, 0.0);
    }

    double getStartTime() {
        return startTime;
    }

    double getEndTime() {
        return endTime;
    }

    double getHoursWorked() {
        return hoursWorked;
    }

    double getStraightTime() {
        return straightTime;
    }

    double getOvertime() {
        return overtime;
    }

    double getPenalty() {
        return penalty;
    }

    String getDate() {
        return date;
    }
}
