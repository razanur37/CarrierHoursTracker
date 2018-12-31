package com.razanur.carrierhourstracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NewDayActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.razanur.carrierhourstracker.daylistsql.REPLY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_day);
    }
}
