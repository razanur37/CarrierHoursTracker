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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private DayDao mDayDao;
    private DayRoomDatabase mDb;

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

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void readEmptyTable() throws Exception {
        List<Day> days = LiveDataTestUtil.getValue(mDayDao.getAllDays());
        assertTrue(days.isEmpty());
    }

    @Test
    public void writeDayAndCheckSize() throws Exception {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        Day day = new Day(date, 7.0, 18.0, false);
        mDayDao.insert(day);
        List<Day> days = LiveDataTestUtil.getValue(mDayDao.getAllDays());
        assertThat(days.size(), equalTo(1));
    }

    @Test
    public void writeDayAndReadFromList() throws Exception {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        Day day = new Day(date, 7.0, 18.0, false);
        mDayDao.insert(day);
        List<Day> days = LiveDataTestUtil.getValue(mDayDao.getAllDays());
        assertThat(days.get(0), equalTo(day));
    }

    @Test
    public void updateDay() throws Exception {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        Day day = new Day(date, 7.0, 18.0, false);
        mDayDao.insert(day);
        cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)-1);
        date = cal.getTime();
        day = LiveDataTestUtil.getValue(mDayDao.getAllDays()).get(0);
        day.setDate(date);
        mDayDao.update(day);
        List<Day> days = LiveDataTestUtil.getValue(mDayDao.getAllDays());
        assertThat(days.get(0), equalTo(day));
    }
}
