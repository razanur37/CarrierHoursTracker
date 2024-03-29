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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;

public class ConversionFragment extends Fragment {
    public static final String TAG = "CONVERSION_FRAGMENT";

    public ConversionFragment() {
        // Required empty public constructor
    }

    public static ConversionFragment newInstance() {
        return new ConversionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conversion, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);

        if (fab.getVisibility() == View.VISIBLE)
            fab.setVisibility(View.INVISIBLE);
    }
}
