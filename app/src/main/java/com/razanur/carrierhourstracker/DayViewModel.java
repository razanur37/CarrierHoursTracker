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

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

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

    void insert(Day day) {
        mRepository.insert(day);
    }

    void update(Day day) {
        mRepository.update(day);
    }

    void delete(Day day) {
        mRepository.delete(day);
    }

    void clear() {
        mRepository.clear();
    }
}
