package com.razanur.carrierhourstracker;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

class HoursFunctions {
    static void calculateHours(Context context, EditText[] times, TextView[] totals) {
        EditText startTime = times[0];
        EditText endTime = times[1];

        TextView totalHours = totals[0];
        TextView straightHours = totals[1];
        TextView overtimeHours = totals[2];
        TextView penaltyHours = totals[3];

        int duration = Toast.LENGTH_SHORT;
        Locale locale = Locale.US;
        String stringFormat = "%.2f";

        double start;
        double end;
        double total;
        double totalStraight;
        double totalOvertime;
        double totalPenalty;

        // Make sure inputs were entered
        try {
            start = Double.parseDouble(startTime.getText().toString());
            end = Double.parseDouble(endTime.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(context, "Enter a Start Time and an End Time", duration).show();
            return;
        }

        // Verify inputs
        if (start >= 24.0 || end >= 24.0) {
            Toast.makeText(context, "Start and End Times Must be on 24-hour Clock", duration).show();
            return;
        }

        total = end - start;

        if (total < 0) {
            Toast.makeText(context, "End Time Must be Later Than Start Time", duration).show();
            return;
        }

        // Take out lunch if 6 or more hours
        if (total >= 6.0)
            total -= 0.5;

        String totalAsString = String.format(locale, stringFormat, total);

        totalHours.setText(totalAsString);

        if (total > 8.0)
            totalStraight = 8.0;
        else
            totalStraight = total;
        if (total > 8.0 && total <= 10.0)
            totalOvertime = total - 8.0;
        else
            totalOvertime = 0;

        if (total > 10.0)
            totalPenalty = total - 10.0;
        else
            totalPenalty = 0.0;

        String totalStraightAsString = String.format(locale, stringFormat, totalStraight);
        straightHours.setText(totalStraightAsString);

        String totalOvertimeAsString = String.format(locale,stringFormat,totalOvertime);
        overtimeHours.setText(totalOvertimeAsString);

        String totalPenaltyAsString = String.format(locale, stringFormat, totalPenalty);
        penaltyHours.setText(totalPenaltyAsString);
    }
}
