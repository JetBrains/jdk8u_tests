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

package org.apache.harmony.test.func.api.java.util.TimeZone;

import java.util.Date;
import java.util.TimeZone;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/*
 * Mar 20, 2006
 */
public class TimeZoneTest extends MultiCase {

    private class MyTimeZone extends TimeZone {

        public MyTimeZone() {
            super();
        }

        public int getOffset(int arg0, int arg1, int arg2, int arg3, int arg4,
                int arg5) {
            return 0;
        }

        public void setRawOffset(int arg0) {
        }

        public int getRawOffset() {
            return 0;
        }

        public boolean useDaylightTime() {
            return false;
        }

        public boolean inDaylightTime(Date arg0) {
            return false;
        }

    }

    public Result testConstructor() {
        try {
            TimeZone tz = new MyTimeZone();
        } catch (Throwable e) {
            e.printStackTrace();
            return failed("Unexpected exception: " + e.getMessage());
        }
        return passed();
    }

    public static void main(String[] args) {
        System.exit(new TimeZoneTest().test(args));
    }

}
