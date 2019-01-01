package com.razanur.carrierhourstracker;

class CarrierUtils {
    static String reverseDate(String date) {
        String month = date.substring(0, 2);
        String day = date.substring(3,5);
        String year = date.substring(6);

        return year + "/" + month + "/" + day;
    }

    static String unReverseDate(String date) {
        String year = date.substring(0, 4);
        String month = date.substring(5,7);
        String day = date.substring(8);

        return month + "/" + day + "/" + year;
    }
}
