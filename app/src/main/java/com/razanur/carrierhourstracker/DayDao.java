package com.razanur.carrierhourstracker;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DayDao {
    @Insert
    void insert(Day day);

    @Query("DELETE FROM day_table")
    void deleteAll();

    @Query("SELECT * from day_table ORDER BY date ASC")
    LiveData<List<Day>> getAllDays();

    @Query("SELECT * from day_table WHERE date LIKE :dateToCheck")
    LiveData<Day> getDayWithDate(String dateToCheck);
}
