package com.razanur.carrierhourstracker;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Day.class}, version = 1)
public abstract class DayRoomDatabase extends RoomDatabase {
    public abstract DayDao dayDao();

    private static volatile DayRoomDatabase INSTANCE;

    static DayRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DayRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create Database
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DayRoomDatabase.class, "day_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final DayDao mDao;

        PopulateDbAsync(DayRoomDatabase db) {
            mDao = db.dayDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            return null;
        }
    }
}
