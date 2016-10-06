/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
/**
 */
/*
 * Created on 14.03.2006
 */
package org.apache.harmony.test.func.api.java.util.GregorianCalendar;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

class MyCalendar extends Calendar {

    public MyCalendar(TimeZone tz, Locale loc) {
        super(tz, loc);
    }

    public MyCalendar() {
        super();
    }

    protected void computeFields() {
    }

    protected void computeTime() {
    }

    public int getGreatestMinimum(int arg0) {
        return 0;
    }

    public int getLeastMaximum(int arg0) {
        return 0;
    }

    public int getMaximum(int arg0) {
        return 0;
    }

    public int getMinimum(int arg0) {
        return 0;
    }

    public void add(int arg0, int arg1) {
    }

    public void roll(int arg0, boolean arg1) {
    }

    public boolean isAllFieldsNotSet() {
        boolean res = false;
        for (int i = 0; i < FIELD_COUNT; i++) {
            res = res || isSet(i);
        }
        return !res;
    }

    public boolean isOnlyThisFields(int j) {
        boolean res = false;
        for (int i = 0; i < FIELD_COUNT; i++) {
            if (i == j) {
                continue;
            }
            res = res || isSet(i);
        }
        return !res && isSet(j);
    }

}

class MyGregorianCalendar extends GregorianCalendar {
    public boolean isAllFieldsNotSet() {
        boolean res = false;
        for (int i = 0; i < FIELD_COUNT; i++) {
            res = res || isSet(i);
        }
        return !res;
    }

    public boolean isAllFieldsSet() {
        boolean res = true;
        for (int i = 0; i < FIELD_COUNT; i++) {
            res = res && isSet(i);
        }
        return res;
    }

    public void setMyTime(long t) {
        time = t;
    }

    public long getMyTime() {
        return time;
    }

    public String getMyFields() {
        String st = "";
        for (int i = 0; i < FIELD_COUNT; i++) {
            st += fields[i];
            if (i < FIELD_COUNT - 1) {
                st += " ";
            }
        }
        return st;
    }

    public void complete() {
        super.complete();
    }

    public void computeTime() {
        super.computeTime();
    }

    public void computeFields() {
        super.computeFields();
    }

    public MyGregorianCalendar() {
        clear();
    }

}

public class GregorianCalendarTest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new GregorianCalendarTest().test(args));
    }

    public Result testComputeFields() throws Throwable {

        MyGregorianCalendar mgc = new MyGregorianCalendar();

        if (!mgc.isAllFieldsNotSet()) {
            return failed("1");
        }

        mgc.computeFields();
        if (!mgc.isAllFieldsSet()) {
            return failed("2");
        }

        mgc = new MyGregorianCalendar();

        mgc.getMyFields();

        long time = (long) 60 * 60 * 24 * 1000 * (long) 10000;

        mgc.setMyTime(time);

        mgc.computeFields();

        if (!mgc.getMyFields().equals(
                "1 1997 4 21 4 19 139 2 3 0 4 4 0 0 0 10800000 3600000")) {
            return failed("3");
        }

        return passed();
    }

    public Result testComputeTime() throws Throwable {

        MyGregorianCalendar mgc = new MyGregorianCalendar();

        mgc.set(0, 1);
        mgc.set(1, 1997);
        mgc.set(2, 4);
        mgc.set(3, 21);
        mgc.set(4, 4);
        mgc.set(5, 19);
        mgc.set(6, 139);
        mgc.set(7, 2);
        mgc.set(8, 3);
        mgc.set(9, 0);
        mgc.set(10, 4);
        mgc.set(11, 4);
        mgc.set(12, 0);
        mgc.set(13, 0);
        mgc.set(14, 0);
        mgc.set(15, 10800000);
        mgc.set(16, 3600000);

        mgc.computeTime();

        long time = (long) 60 * 60 * 24 * 1000 * (long) 10000;
        if (mgc.getMyTime() != time) {
            return failed("1");
        }

        mgc.complete();

        if (mgc.getMyTime() != time) {
            return failed("2");
        }

        if (!mgc.getMyFields().equals(
                "1 1997 4 21 4 19 139 2 3 0 4 4 0 0 0 10800000 3600000")) {
            return failed("3");
        }

        return passed();
    }

    public Result testCalendarConstructor() throws Throwable {
        TimeZone tz = new SimpleTimeZone(0, "");
        Locale loc = Locale.US;
        MyCalendar mc = new MyCalendar(tz, loc);

        if (!mc.getTimeZone().equals(tz)) {
            return failed("1");
        }

        return passed();
    }

    public Result testIsSet() throws Throwable {
        MyCalendar mc = new MyCalendar();

        if (!mc.isAllFieldsNotSet()) {
            return failed("1");
        }

        for (int i = 0; i < Calendar.FIELD_COUNT; i++) {
            mc.clear();
            mc.set(i, 1);
            if (!mc.isOnlyThisFields(i)) {
                return failed("2_" + i);
            }

        }

        return passed();
    }
}