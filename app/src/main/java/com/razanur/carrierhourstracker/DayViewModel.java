package com.razanur.carrierhourstracker;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class DayViewModel extends AndroidViewModel {

    private DayRepository mRepository;
    private LiveData<List<Day>> mAllDays;

    public DayViewModel(Application application) {
        super(application);
        mRepository = new DayRepository(application);
        mAllDays = mRepository.getAllDays();
    }

    LiveData<List<Day>> getAllDays() {
        return mAllDays;
    }

    public void insert(Day day) {
        mRepository.insert(day);
    }

    boolean doesListContainDay(String date) {
        return mRepository.checkIfDayExists(date);
    }
}
