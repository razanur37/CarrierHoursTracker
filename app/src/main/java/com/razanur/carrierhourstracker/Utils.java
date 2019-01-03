package com.razanur.carrierhourstracker;

import java.text.SimpleDateFormat;
import java.util.Locale;

class Utils {
    static final String DECIMAL_FORMAT = "%.2f";
    private static final String DATE_FORMAT_SHORT = "MM/dd/yyyy";
    private static final String DATE_FORMAT_LONG = "EEE, MMM d, yyyy";
    static final Locale LOCALE = Locale.US;
    static final SimpleDateFormat SHORT_SDF = new SimpleDateFormat(DATE_FORMAT_SHORT, LOCALE);
    static final SimpleDateFormat LONG_SDF = new SimpleDateFormat(DATE_FORMAT_LONG, LOCALE);
}
