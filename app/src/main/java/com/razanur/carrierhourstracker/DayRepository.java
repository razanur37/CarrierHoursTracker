package com.razanur.carrierhourstracker;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

class DayRepository {

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

    void insert (Day day) {
        new insertAsyncTask(mDayDao).execute(day);
    }

    void clear() {
        new deleteAsyncTask(mDayDao).execute();
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

    private static class deleteAsyncTask extends AsyncTask<Void, Void, Void> {

        private DayDao mAsyncTaskDao;
        deleteAsyncTask(DayDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
}
