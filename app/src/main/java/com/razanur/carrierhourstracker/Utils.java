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

import java.text.SimpleDateFormat;
import java.util.Locale;

class Utils {
    static final String DECIMAL_FORMAT = "%.2f";
    private static final String DATE_FORMAT_SHORT = "MM/dd/yyyy";
    private static final String DATE_FORMAT_LONG = "EEE, MMM d, yyyy";
    static final Locale LOCALE = Locale.US;
    static final SimpleDateFormat SHORT_SDF = new SimpleDateFormat(DATE_FORMAT_SHORT, LOCALE);
    static final SimpleDateFormat LONG_SDF = new SimpleDateFormat(DATE_FORMAT_LONG, LOCALE);
}
