/*
 * Copyright (c) 2019 Casey English
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.razanur.carrierhourstracker;

import android.app.Application;
import android.os.AsyncTask;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;

class DayRepository {

    private DayDao mDayDao;
    private LiveData<List<Day>> mAllDays;

    DayRepository(Application application) {
        DayRoomDatabase db = DayRoomDatabase.getDatabase(application);
        mDayDao = db.dayDao();
        mAllDays = mDayDao.getAllDays();
    }

    void refresh() {
        Day day = Day.dateAsDay(new Date(0));
        day.setRowID(-1);
        new refreshAsyncTask(mDayDao).execute(day);
    }

    LiveData<List<Day>> getAllDays() {
        return mAllDays;
    }

    void insert(Day day) {
        new insertAsyncTask(mDayDao).execute(day);
    }

    void update(Day day) {
        new updateAsyncTask(mDayDao).execute(day);
    }

    void delete(Day day) {
        new deleteSingleAsyncTask(mDayDao).execute(day);
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

    private static class updateAsyncTask extends AsyncTask<Day, Void, Void> {

        private DayDao mAsyncTaskDao;

        updateAsyncTask(DayDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Day... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class deleteSingleAsyncTask extends AsyncTask<Day, Void, Void> {

        private DayDao mAsyncTaskDao;
        deleteSingleAsyncTask(DayDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Day... params) {
            mAsyncTaskDao.delete(params[0]);
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

    private static class refreshAsyncTask extends AsyncTask<Day, Void, Void> {

        private DayDao mAsyncTaskDao;
        refreshAsyncTask(DayDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Day... params) {
            Day day = params[0];
            mAsyncTaskDao.insert(day);
            mAsyncTaskDao.delete(day);
            return null;
        }

    }

}
