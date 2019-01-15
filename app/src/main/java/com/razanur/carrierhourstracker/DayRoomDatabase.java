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

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Day.class}, version = 5)
@TypeConverters({Converters.class})
public abstract class DayRoomDatabase extends RoomDatabase {
    public abstract DayDao dayDao();

    private static volatile DayRoomDatabase INSTANCE;

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {

        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS day_table_copy " +
                    "(`rowID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "date INTEGER NOT NULL, " +
                    "mStartTime REAL NOT NULL, " +
                    "mEndTime REAL NOT NULL, " +
                    "mNsDay INTEGER NOT NULL, " +
                    "mExcluded INTEGER NOT NULL, " +
                    "mHoursWorked REAL NOT NULL, " +
                    "mStraightTime REAL NOT NULL, " +
                    "mOvertime REAL NOT NULL, " +
                    "mPenalty REAL NOT NULL);");

            database.execSQL("INSERT INTO day_table_copy (date, mStartTime, mEndTime, mNsDay, mExcluded, mHoursWorked, mStraightTime, mOvertime, mPenalty)" +
                    "SELECT date, mStartTime, mEndTime, mNsDay, mExcluded, mHoursWorked, mStraightTime, mOvertime, mPenalty FROM day_table;");

            database.execSQL("DROP TABLE day_table;");
            database.execSQL("ALTER TABLE day_table_copy RENAME TO day_table;");
        }
    };

    static final Migration MIGRATION_4_5 = new Migration(4, 5) {

        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS day_table_copy " +
                    "(`rowID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "date INTEGER NOT NULL, " +
                    "mStartTime REAL NOT NULL, " +
                    "mEndTime REAL NOT NULL, " +
                    "mNsDay INTEGER NOT NULL);");

            database.execSQL("INSERT INTO day_table_copy (date, mStartTime, mEndTime, mNsDay)" +
                    "SELECT date, mStartTime, mEndTime, mNsDay FROM day_table;");

            database.execSQL("DROP TABLE day_table;");
            database.execSQL("ALTER TABLE day_table_copy RENAME TO day_table;");
        }
    };

    static DayRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DayRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create Database
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DayRoomDatabase.class, "day_database")
                            .addCallback(sRoomDatabaseCallback)
                            .addMigrations(MIGRATION_3_4, MIGRATION_4_5)
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
        }
    };
}
