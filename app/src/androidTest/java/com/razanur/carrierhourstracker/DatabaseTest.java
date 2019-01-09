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

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private DayDao mDayDao;
    private DayRoomDatabase mDb;

    private static Date date;
    private static Date decDate;
    private static Date janDate;
    private static final double start = 7.0;
    private static final double end = 18.0;
    private static final boolean isNsDay = false;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        mDb = Room.inMemoryDatabaseBuilder(context, DayRoomDatabase.class)
                .allowMainThreadQueries()
                .build();
        mDayDao = mDb.dayDao();
    }

    @Before
    public void setDates() {
        try {
            String dateString = "06/01/2019";
            String decDateString = "12/15/2019";
            String janDateString = "01/15/2019";
            date = Utils.SHORT_SDF.parse(dateString);
            decDate = Utils.SHORT_SDF.parse(decDateString);
            janDate = Utils.SHORT_SDF.parse(janDateString);
        } catch (ParseException e) {
            // Never here
        }
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void readEmptyTable() throws Exception {
        List<Day> days = LiveDataTestUtil.getValue(mDayDao.getAllDays());
        assertThat(days).isEmpty();
    }

    @Test
    public void writeDayAndCheckSize() throws Exception {
        Day day = new Day(date, start, end, isNsDay);
        mDayDao.insert(day);
        List<Day> days = LiveDataTestUtil.getValue(mDayDao.getAllDays());
        assertThat(days).hasSize(1);
    }

    @Test
    public void writeDayAndReadFromList() throws Exception {
        Day day = new Day(date, start, end, isNsDay);
        mDayDao.insert(day);
        List<Day> days = LiveDataTestUtil.getValue(mDayDao.getAllDays());
        assertThat(days.get(0)).isEqualTo(day);
    }

    @Test
    public void updateDay() throws Exception {
        Day day = new Day(date, start, end, isNsDay);
        mDayDao.insert(day);
        List<Day> days = LiveDataTestUtil.getValue(mDayDao.getAllDays());
        day = days.get(0);
        day.setDate(decDate);
        mDayDao.update(day);
        days = LiveDataTestUtil.getValue(mDayDao.getAllDays());
        assertThat(days.get(0)).isEqualTo(day);
    }

    @Test
    public void deleteDay() throws Exception {
        Day day = new Day(date, start, end, isNsDay);
        mDayDao.insert(day);
        List<Day> days = LiveDataTestUtil.getValue(mDayDao.getAllDays());
        day = days.get(0);
        mDayDao.delete(day);
        days = LiveDataTestUtil.getValue(mDayDao.getAllDays());
        assertThat(days).isEmpty();
    }

    @Test
    public void deleteAllDays() throws Exception {
        Day day = new Day(date, start, end, isNsDay);
        Day day2 = new Day(decDate, start, end, isNsDay);
        mDayDao.insert(day);
        mDayDao.insert(day2);
        List<Day> days = LiveDataTestUtil.getValue(mDayDao.getAllDays());
        assertThat(days).hasSize(2);
        mDayDao.deleteAll();
        days = LiveDataTestUtil.getValue(mDayDao.getAllDays());
        assertThat(days.isEmpty());
    }
}
