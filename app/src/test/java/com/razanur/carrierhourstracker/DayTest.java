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

import org.junit.Before;
import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;


import java.text.ParseException;
import java.util.Date;

public class DayTest {

    private static Date date;
    private static Date decDate;
    private static Date janDate;
    private static final double start = 7.0;
    private static final double end = 18.0;
    private static final boolean isNsDay = false;

    @Before
    public void setDate() {
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

    @Test
    public void test3HourDay() {
        Day day = new Day(date, start, 10.0, isNsDay);
        assertThat(day.getHoursWorked()).isEqualTo(3.0);
        assertThat(day.getStraightTime()).isEqualTo(3.0);
        assertThat(day.getOvertime()).isZero();
        assertThat(day.getPenalty()).isZero();
    }

    @Test
    public void test6HourDay() {
        Day day = new Day(date, start, 13.0, isNsDay);
        assertThat(day.getHoursWorked()).isEqualTo(5.5);
        assertThat(day.getStraightTime()).isEqualTo(5.5);
        assertThat(day.getOvertime()).isZero();
        assertThat(day.getPenalty()).isZero();
    }

    @Test
    public void test8HourDay() {
        Day day = new Day(date, start, 15.5, isNsDay);
        assertThat(day.getHoursWorked()).isEqualTo(8.0);
        assertThat(day.getStraightTime()).isEqualTo(8.0);
        assertThat(day.getOvertime()).isZero();
        assertThat(day.getPenalty()).isZero();
    }

    @Test
    public void test9HourDay() {
        Day day = new Day(date, start, 16.5, isNsDay);
        assertThat(day.getHoursWorked()).isEqualTo(9.0);
        assertThat(day.getStraightTime()).isEqualTo(8.0);
        assertThat(day.getOvertime()).isEqualTo(1.0);
        assertThat(day.getPenalty()).isZero();
    }

    @Test
    public void test10HourDay() {
        Day day = new Day(date, start, 17.5, isNsDay);
        assertThat(day.getHoursWorked()).isEqualTo(10.0);
        assertThat(day.getStraightTime()).isEqualTo(8.0);
        assertThat(day.getOvertime()).isEqualTo(2.0);
        assertThat(day.getPenalty()).isZero();
    }

    @Test
    public void test11HourDay() {
        Day day = new Day(date, start, 18.5, isNsDay);
        assertThat(day.getHoursWorked()).isEqualTo(11.0);
        assertThat(day.getStraightTime()).isEqualTo(8.0);
        assertThat(day.getOvertime()).isEqualTo(2.0);
        assertThat(day.getPenalty()).isEqualTo(1.0);
    }

    @Test
    public void test3HourNsDay() {
        Day day = new Day(date, start, 10.0, true);
        assertThat(day.getHoursWorked()).isEqualTo(3.0);
        assertThat(day.getStraightTime()).isZero();
        assertThat(day.getOvertime()).isEqualTo(3.0);
        assertThat(day.getPenalty()).isZero();
    }

    @Test
    public void test6HourNsDay() {
        Day day = new Day(date, start, 13.0, true);
        assertThat(day.getHoursWorked()).isEqualTo(5.5);
        assertThat(day.getStraightTime()).isZero();
        assertThat(day.getOvertime()).isEqualTo(5.5);
        assertThat(day.getPenalty()).isZero();
    }

    @Test
    public void test8HourNsDay() {
        Day day = new Day(date, start, 15.5, true);
        assertThat(day.getHoursWorked()).isEqualTo(8.0);
        assertThat(day.getStraightTime()).isZero();
        assertThat(day.getOvertime()).isEqualTo(8.0);
        assertThat(day.getPenalty()).isZero();
    }

    @Test
    public void test9HourNsDay() {
        Day day = new Day(date, start, 16.5, true);
        assertThat(day.getHoursWorked()).isEqualTo(9.0);
        assertThat(day.getStraightTime()).isZero();
        assertThat(day.getOvertime()).isEqualTo(8.0);
        assertThat(day.getPenalty()).isEqualTo(1.0);
    }

    @Test
    public void test11HourExcludedDay() {
        Day day = new Day(decDate, start, 18.5, isNsDay);
        assertThat(day.getHoursWorked()).isEqualTo(11.0);
        assertThat(day.getStraightTime()).isEqualTo(8.0);
        assertThat(day.getOvertime()).isEqualTo(3.0);
        assertThat(day.getPenalty()).isZero();
    }

    @Test
    public void test9HourNsExcludedDay() {
        Day day = new Day(decDate, start, 16.5, true);
        assertThat(day.getHoursWorked()).isEqualTo(9.0);
        assertThat(day.getStraightTime()).isZero();
        assertThat(day.getOvertime()).isEqualTo(9.0);
        assertThat(day.getPenalty()).isZero();
    }

    @Test
    public void testExclusionNotDecember() {
        Day day = new Day(date, start, end, isNsDay);
        assertThat(day.isExcluded()).isFalse();
    }

    @Test
    public void testExclusionDecember() {
        Day day = new Day(decDate, start, end, isNsDay);
        assertThat(day.isExcluded()).isTrue();
    }

    @Test
    public void testChangingDateChangesExclusionAndHours() {
        Day day = new Day(date, start, end, isNsDay);
        day.setDate(decDate);
        assertThat(day.isExcluded()).isTrue();
        assertThat(day.getOvertime()).isEqualTo(2.5);
        assertThat(day.getPenalty()).isZero();
    }

    @Test
    public void testCopyConstructor() {
        Day day = new Day(date, start, end, isNsDay);
        Day day2 = new Day(day);
        assertThat(day).isEqualTo(day2);
    }

    @Test
    public void testEqualToReflexive() {
        Day day = new Day(date, start, end, isNsDay);
        assertThat(day).isEqualTo(day);
        assertThat(day.hashCode()).isEqualTo(day.hashCode());
    }

    @Test
    public void testEqualToSymmetric() {
        Day day = new Day(date, start, end, isNsDay);
        Day day2 = new Day(date, 7.5, 16.0, true);

        assertThat(day).isEqualTo(day2);
        assertThat(day2).isEqualTo(day);

        assertThat(day.hashCode()).isEqualTo(day2.hashCode());
        assertThat(day2.hashCode()).isEqualTo(day.hashCode());
    }

    @Test
    public void testEqualToTransitive() {
        Day day = new Day(date, start, end, isNsDay);
        Day day2 = new Day(date, 7.5, 16.0, true);
        Day day3 = new Day(date, 8.0, 16.5, false);

        assertThat(day).isEqualTo(day2);
        assertThat(day2).isEqualTo(day3);
        assertThat(day).isEqualTo(day3);

        assertThat(day.hashCode()).isEqualTo(day2.hashCode());
        assertThat(day2.hashCode()).isEqualTo(day3.hashCode());
        assertThat(day.hashCode()).isEqualTo(day3.hashCode());
    }

    @Test
    public void testEqualToNull() {
        Day day = new Day(date, start, end, isNsDay);
        assertThat(day).isNotEqualTo(null);
    }

    @Test
    public void testNotEqualTo() {
        Day day = new Day(date, start, end, isNsDay);
        Day day2 = new Day(decDate, start, end, isNsDay);
        assertThat(day).isNotEqualTo(day2);
        assertThat(day.hashCode()).isNotEqualTo(day2.hashCode());
    }

    @Test
    public void testNotEqualToDifferentObject() {
        Day day = new Day(date, start, end, isNsDay);
        assertThat(day).isNotEqualTo(date);
    }

    @Test
    public void testToString() {
        Day day = new Day(date, start, end, isNsDay);
        String dateAsString = "Worked Sat, Jun 1, 2019, from 7.00 to 18.00.";
        assertThat(day.toString()).matches(dateAsString);
    }

    @Test
    public void testComparison() {
        Day day = new Day(janDate, start, end, isNsDay);
        Day day2 = new Day(date, start, end, isNsDay);
        Day day3 = new Day(decDate, start, end, isNsDay);
        Day day4 = new Day(janDate, 7.5, end, true);
        Day day5 = new Day(janDate, start, 15.5, isNsDay);


        assertThat(day.compareTo(day2)).isEqualTo(-1);
        assertThat(day2.compareTo(day)).isEqualTo(1);

        assertThat(day2.compareTo(day3)).isEqualTo(-1);
        assertThat(day.compareTo(day3)).isEqualTo(-1);

        assertThat(day.compareTo(day4)).isEqualTo(0);
        assertThat(day4.compareTo(day)).isEqualTo(0);

        assertThat(day.compareTo(day5)).isEqualTo(0);
        assertThat(day4.compareTo(day5)).isEqualTo(0);
    }
}
