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
/*
 * Created on 17.08.2005
 */
package org.apache.harmony.test.func.api.java.lang.F_ThrowableTest_02;

import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.share.Result;

public class F_ThrowableTest_02 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_ThrowableTest_02().test(args));
    }

    public int test() {
        try {
            if (task0() != Result.PASS || task1() != Result.PASS
                || task2() != Result.PASS || task3() != Result.PASS
                || task4() != Result.PASS || task5() != Result.PASS
                || task6() != Result.PASS || task7() != Result.PASS)
                return fail("test NOT passed");
        } catch (Exception e) {
            return error("test failed - " + e.getMessage());
        }
        log.info("************ TEST ***************");
        return pass();
    }

    public int task0() {
        Throwable cause0 = new Throwable();
        try {
            Throwable icause = cause0.initCause(cause0);
        } catch (IllegalArgumentException e) {
            log.info("************ task 0 ***************");
            return pass();
        } catch (Exception e) {
            return error("failed - " + e.getMessage());
        }
        return fail("test failed - IllegalArgumentException");
    }

    public int task1() {
        Throwable cause = new Throwable(new Throwable());
        try {
            Throwable icause = cause.initCause(new Throwable());
        } catch (IllegalStateException e) {
            log.info("************ task 1 ***************");
            return pass();
        } catch (Exception e) {
            return error("failed - " + e.getMessage());
        }
        return fail("test failed - IllegalStateException");
    }

    public int task2() {
        Throwable cause = new Throwable("Illegal cause", new Throwable());
        try {
            Throwable icause = cause.initCause(new Throwable());
        } catch (IllegalStateException e) {
            log.info("************ task 2 ***************");
            return pass();
        } catch (Exception e) {
            return error("failed - " + e.getMessage());
        }
        return fail("test failed - IllegalStateException");
    }

    public int task3() {
        Throwable cause = new Throwable();
        try {
            Throwable icause = cause.initCause(new Throwable());
            icause = cause.initCause(new Throwable());
        } catch (IllegalStateException e) {
            log.info("************ task 3 ***************");
            return pass();
        } catch (Exception e) {
            return error("failed - " + e.getMessage());
        }
        return fail("test failed - IllegalStateException");
    }

    public int task4() {
        Throwable cause = new Throwable();
        try {
            Throwable icause = cause.initCause(new Throwable());
            Throwable icause1 = cause.initCause(new Throwable());
        } catch (IllegalStateException e) {
            log.info("************ task 4 ***************");
            return pass();
        } catch (Exception e) {
            return error("failed - " + e.getMessage());
        }
        return fail("test failed - IllegalStateException");
    }

    public int task5() {
        Throwable cause = new myLocalException();
        if (((cause.initCause(new Throwable())).getLocalizedMessage())
            .equals("My localized message: null")) {
            log.info("************ task 5 ***************");
            return pass();
        } else {
            return fail("test failed - getLocalizedMessage()");
        }
    }

    public int task6() {
        Throwable cause = new myLocalException("Local exception");
        if (((cause.initCause(new Throwable())).getLocalizedMessage())
            .equals("My localized message: Local exception")) {
            log.info("************ task 6 ***************");
            return pass();
        } else {
            return fail("test failed - getLocalizedMessage(), String");
        }
    }

    public int task7() {
        Throwable cause = new newLocalException("New local exception");
        if (((cause.initCause(new Throwable())).getLocalizedMessage())
            .equals("New localized message: New message")) {
            log.info("************ task 7 ***************");
            return pass();
        } else {
            return fail("test failed - getLocalizedMessage(), new String");
        }
    }

    class myLocalException extends Exception {
        public myLocalException(String str) {
            super(str);
        }

        public myLocalException() {
            super();
        }

        public String getLocalizedMessage() {
            return "My localized message: " + super.getLocalizedMessage();
        }
    }

    class newLocalException extends Exception {
        private String str = "New message";

        public newLocalException(String str) {
            super(str);
        }

        public String getLocalizedMessage() {
            return "New localized message: " + str;
        }
    }
}