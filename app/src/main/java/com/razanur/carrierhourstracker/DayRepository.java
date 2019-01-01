package com.razanur.carrierhourstracker;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class DayRepository {

    private DayDao mDayDao;
    private LiveData<List<Day>> mAllDays;

    DayRepository(Application application) {
        DayRoomDatabase db = DayRoomDatabase.getDatabase(application);
        mDayDao = db.dayDao();
        mAllDays = mDayDao.getAllDays();
    }

    LiveData<List<Day>> getAllDays() {
        return mAllDays;
    }

    public void insert (Day day) {
        new insertAsyncTask(mDayDao).execute(day);
    }

    private static class insertAsyncTask extends AsyncTask<Day, Void, Void> {

        private DayDao mAsyncTaskDao;

        insertAsyncTask(DayDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Day... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
