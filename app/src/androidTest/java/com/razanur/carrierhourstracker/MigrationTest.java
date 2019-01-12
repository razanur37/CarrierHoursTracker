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

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Date;

import androidx.room.testing.MigrationTestHelper;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import static com.razanur.carrierhourstracker.DayRoomDatabase.MIGRATION_3_4;

@RunWith(AndroidJUnit4.class)
public class MigrationTest {
    private static final String TEST_DB = "migration-test";

    @Rule
    public MigrationTestHelper helper;



    public MigrationTest() {
        helper = new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
                DayRoomDatabase.class.getCanonicalName(),
                new FrameworkSQLiteOpenHelperFactory());
    }

    @Test
    public void migrate3To4() throws IOException {
        Day day1 = new Day(new Date(), 7.0, 18.0, false);
        Day day2 = new Day(new Date(day1.getDate().getTime() + 545454545), 6.0, 15.0, true);

        int day1ns = day1.isNsDay() ? 1 : 0;
        int day1ex = day1.isExcluded() ? 1 : 0;
        int day2ns = day2.isNsDay() ? 1 : 0;
        int day2ex = day2.isExcluded() ? 1 : 0;

        SupportSQLiteDatabase db = helper.createDatabase(TEST_DB, 3);
        db.execSQL("INSERT INTO day_table VALUES (" +
                Long.toString(day1.getDate().getTime()) + ", " +
                Double.toString(day1.getStartTime()) + ", " +
                Double.toString(day1.getEndTime()) + ", " +
                Integer.toString(day1ns) + ", " +
                Integer.toString(day1ex) + ", " +
                Double.toString(day1.getHoursWorked()) + ", " +
                Double.toString(day1.getStraightTime()) + ", " +
                Double.toString(day1.getOvertime()) + ", " +
                Double.toString(day1.getPenalty()) + ");");

        db.execSQL("INSERT INTO day_table VALUES (" +
                Long.toString(day2.getDate().getTime()) + ", " +
                Double.toString(day2.getStartTime()) + ", " +
                Double.toString(day2.getEndTime()) + ", " +
                Integer.toString(day2ns) + ", " +
                Integer.toString(day2ex) + ", " +
                Double.toString(day2.getHoursWorked()) + ", " +
                Double.toString(day2.getStraightTime()) + ", " +
                Double.toString(day2.getOvertime()) + ", " +
                Double.toString(day2.getPenalty()) + ");");

        db.close();

        db = helper.runMigrationsAndValidate(TEST_DB, 4, true, MIGRATION_3_4);
    }
}
