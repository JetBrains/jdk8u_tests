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
 * Created on 18.10.2005 
 * Last modification G.Seryakova 
 * Last modified on 19.10.2005 
 * 
 * Tests methods getCause(), getException() of classes  
 * java.lang.ClassNotFoundException, java.lang.ExceptionInInitializerError. 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.F_ExceptionTest_04;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Tests methods getCause(), getException() of classes  
 * java.lang.ClassNotFoundException, java.lang.ExceptionInInitializerError.
 * 
 */

public class F_ExceptionTest_04 extends ScenarioTest {
    public static NullPointerException ex = new NullPointerException("this is test.");
    private ClassLoader loader = new  MyClassLoader(ex);
    private int stat = 0;
    
    public static void main(String[] args) {
        System.exit(new F_ExceptionTest_04().test(args));
    }

    public int test() {
        if (!task1()) {
            fail("FAIL in task1().");
            stat++;
        } else {
            log.info("task1 passed.");
        }
        if (!task2()) {
            fail("FAIL in task2().");
            stat++;
        } else {
            log.info("task2 passed.");
        }
        if (stat == 0) {
            return pass();
        } else {
            return fail("test failed.");
        }
    }
    
    private boolean task1() {
        try {
            loader.loadClass("null");
            System.out.println("FAIL");
        } catch (ClassNotFoundException e) {
            if (!equals(e.getCause(), ex)) {
                stat++;
                fail("FAIL: unexpected result of ClassNotFoundException.getCause()");
            }
            if (!equals(e.getException(), ex)) {
                stat++;
                fail("FAIL: unexpected result of ClassNotFoundException.getCause()");
            }
            e.getCause().printStackTrace();
            ex.printStackTrace();
        }
        return true;
    }
    
    private boolean task2() {
        try{
            F_ExceptionTest_04_Test1.test();
        } catch (ExceptionInInitializerError e) {
            if (!equals(e.getCause(), ex)) {
                stat++;
                fail("FAIL: unexpected result of ExceptionInInitializerError.getCause()");
            }
            if (!equals(e.getException(), ex)) {
                stat++;
                fail("FAIL: unexpected result of ExceptionInInitializerError.getException()");
            }
            e.getCause().printStackTrace();
            ex.printStackTrace();                     
        } 
        ex.setStackTrace(new Throwable().getStackTrace());
        ExceptionInInitializerError er = new ExceptionInInitializerError(ex);
        if (!equals(er.getCause(), ex)) {
            stat++;
            fail("FAIL: unexpected result of constructed ExceptionInInitializerError.getCause()");
        }
        if (!equals(er.getException(), ex)) {
            stat++;
            fail("FAIL: unexpected result of constructed ExceptionInInitializerError.getException()");
        }
        er.getCause().printStackTrace();
        ex.printStackTrace();  
        return true;
    }
    
    private boolean equals(Throwable ex1, Throwable ex2) {
        if (!ex1.getMessage().equals(ex2.getMessage())) {
            return false;
        }
        StackTraceElement elem1[] = ex1.getStackTrace();
        StackTraceElement elem2[] = ex2.getStackTrace();
        if (elem1.length != elem2.length) {
            return false;
        }
        for (int i = 0; i < elem1.length; i++) {
            if (!elem1[i].equals(elem2[i])) {
                return false;
            }
        }
        return true;
    }
}
class MyClassLoader extends ClassLoader {
    private NullPointerException ex;
    
    MyClassLoader(NullPointerException e) {
        super();
        ex = e;
    }
    
    public Class loadClass(String name) throws ClassNotFoundException {
        Class cls = null;
        try {
            cls = super.loadClass(name);
        } catch (ClassNotFoundException e) {
            if (name.equals("null")) {
                try {
                    ex.setStackTrace(null);
                } catch (NullPointerException e1) {
                    StackTraceElement elem[] = e1.getStackTrace();
                    elem[0] = null;
                    try {
                        ex.setStackTrace(elem);
                    } catch (NullPointerException e2) {                
                        ex.setStackTrace(e2.getStackTrace());
                    }
                }            
                throw new ClassNotFoundException("class null was not found.", ex);
            }
        }
        return cls;
    }
}
class F_ExceptionTest_04_Test1 {
    static NullPointerException ex;
    static Test1_class1 t = new Test1_class1();    

    static class Test1_class1 {
        Test1_class1() {
            ex = new NullPointerException("this is test.");
            F_ExceptionTest_04.ex.setStackTrace(ex.getStackTrace());
            throw ex;
        }
    }
    
    static void test() {        
    }
}

