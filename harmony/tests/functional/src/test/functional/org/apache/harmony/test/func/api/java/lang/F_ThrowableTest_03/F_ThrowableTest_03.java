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
package org.apache.harmony.test.func.api.java.lang.F_ThrowableTest_03;

import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.share.Result;

/**
 * Tests Throwable.getCause() and StackTraceElement.isNativeMethod()
 * 
 */

public class F_ThrowableTest_03 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_ThrowableTest_03().test(args));
    }

    public int test() {
        try {
            if (task1() != Result.PASS || task2() != Result.PASS)
                return fail("test NOT passed");
        } catch (Exception e) {
            return error("test failed - " + e.getMessage());
        }
        log.info("");
        log.info("");
        log.info("************ TEST ***************");
        return pass();
    }

    public int task1() {
        log.info("");
        try {
            A a = new A();
            a.f();
        } catch (Throwable e) {
            Throwable ee = new Throwable(e);
            log.info("Exception: " + e);
            log.info("Exception: " + ee);
            log.info("***************************************");
            if(ee.getCause() instanceof NullPointerException) {
//            StackTraceElement ste[] = e.getStackTrace();
            StackTraceElement ste[] = ((NullPointerException)ee.getCause()).getStackTrace();
            boolean isNotNative = false;
            for (int i = 0; i < ste.length; i++) {
                if(!ste[i].isNativeMethod()) isNotNative = true;  
                log.info("filename = " + ste[i].getFileName());
                log.info("line number = " + ste[i].getLineNumber());
                log.info("class name = " + ste[i].getClassName());
                log.info("method name = " + ste[i].getMethodName());
                log.info("is native method = " + ste[i].isNativeMethod());
                log.info("");
            }
            log.info("************ task 1 ***************");
            if(isNotNative) return pass();
            }
        }
        return fail("test failed - not native method (throw NullPointerException)");
    }

    public int task2() {
        log.info("");
        try {
            new C();
        } catch (Throwable e) {
            log.info("Exception: " + e);
            log.info("***************************************");
            StackTraceElement ste[] = e.getStackTrace();
            boolean isNotNative = false;
            for (int i = 0; i < ste.length; i++) {
                if(!ste[i].isNativeMethod()) isNotNative = true;  
                log.info("filename = " + ste[i].getFileName());
                log.info("line number = " + ste[i].getLineNumber());
                log.info("class name = " + ste[i].getClassName());
                log.info("method name = " + ste[i].getMethodName());
                log.info("is native method = " + ste[i].isNativeMethod());
                log.info("");
            }
            log.info("************ task 2 ***************");
            if(isNotNative) return pass();
        }
        return fail("test failed - not native method (throw initial NullPointerException)");
    }

    class A {
        B b;
        void f() {
            b.g();
        }
    }
    
    class B {
        void g() {}
    }
    
    class C {
        String str;
        int len = str.length();
    }
}