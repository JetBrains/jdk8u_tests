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
package org.apache.harmony.test.func.api.javax.management.share.framework;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

/**
 */
public class Test extends org.apache.harmony.share.Test {

    /**
     * Command line arguments.
     */
    protected String[] args;

    /**
     * Test results.
     */
    private Vector     results;

    /**
     * Test result.
     */
    private TestResult res;

    /**
     * Test results.
     */
    private Vector     testedMethods;

    /**
     * Finish the test execution.
     */
    private boolean    finish;

    /**
     * Logger.
     */
    protected Logger   log    = Logger.getLogger(this.getClass());

    /**
     * This thread is used to freeze the test execution for the specified number
     * of milliseconds.
     */
    protected Freeze   freeze = new Freeze();

    /**
     * Run the test
     * 
     * @param args command line arguments.
     * @return TestResult object instance.
     */
    public TestResult run(String[] args) {
        log.serve("Executing the test: " + this.getClass().getName() + " "
            + Utils.arrayToString(args, " "));
        this.args = args;
        String logLevel = getArg("-log");
        log.setLevel(logLevel);
        Logger.setDefaultLevel(logLevel);
        results = new Vector();
        res = new TestResult();
        testedMethods = new Vector();
        finish = false;
        Vector methods = new Vector();
        Vector skippedMethods = new Vector();
        Hashtable coverage = new Hashtable();
        int testCount = 0;
        try {
            testCount = getMethods(methods, skippedMethods, coverage);
        } catch (Throwable ex) {
            fail(ex);
            res.addTestResult(new TestResult(results, skippedMethods,
                testedMethods, testCount, 0));
            return res;
        }
        Object[] params = new Object[0];

        long time = System.currentTimeMillis();
        // Init.
        try {
            log.serve("Invoking: " + "init()");
            this.init();
        } catch (Throwable e) {
            // printStackTrace(e);
            addError(e, "init");
        }

        // Exec tests.
        int size = methods.size();
        for (int i = 0; ((i < size) && !finish); i++) {
            Method m = (Method) methods.get(i);
            String mname = m.getName();
            boolean isServeice = false;
            if (!mname.startsWith("test")) {
                isServeice = true;
            }
            // Setup.
            if (!isServeice) {
                try {
                    waitFor();
                    log.serve("Invoking: " + "setUp()");
                    this.setUp();
                } catch (Throwable e) {
                    // printStackTrace(e);
                    addError(e, "setUp");
                }
            }

            if (finish) {
                break;
            }

            // Exec test.
            try {
                waitFor();
                log.serve("Invoking: " + m);
                m.invoke(this, params);
            } catch (InvocationTargetException e) {
                Throwable ex = e.getCause();
                if (ex instanceof NotImplementedException) {
                    Object o = coverage.get(mname);
                    if (o != null) {
                        testedMethods.remove(o);
                    }

                    if (testCount > 0) {
                        testCount--;
                    }
                } else {
                    // log.error(ex);
                    // printStackTrace(ex);
                    addError(ex, mname);
                }
            } catch (Throwable e) {
                // log.error(e);
                // printStackTrace(e);
                addError(e, mname);
            }

            // Tear down.
            if (!isServeice) {
                try {
                    waitFor();
                    log.serve("Invoking: " + "tearDown()");
                    this.tearDown();
                } catch (Throwable e) {
                    // printStackTrace(e);
                    addError(e, "tearDown");
                }
            }
        }

        // Release.
        try {
            log.serve("Invoking: " + "release()");
            this.release();
        } catch (Throwable e) {
            printStackTrace(e);
            addError(e, "release");
        }
        time = System.currentTimeMillis() - time;

        res.addTestResult(new TestResult(results, skippedMethods,
            testedMethods, testCount, time));
        return res;
    }

    public int test() {
        return run(super.testArgs).getResult();
    }

    /**
     * Freeze the test execution for the specified number of milliseconds.
     * 
     * @param time rfeeze time
     */
    protected void freeze(long time) {
        if (!freeze.isAlive()) {
            freeze = new Freeze();
            freeze.time = time;
            freeze.start();
        }
    }

    /**
     * Proceed the test execution.
     */
    protected void proceed() {
        try {
            freeze.interrupt();
        } catch (Throwable ex) {
        }
    }

    /**
     * Finish the test execution.
     */
    protected void finish() {
        finish = true;
    }

    /**
     * Include another test in to this one.
     * 
     * @param c test to include.
     */
    protected void include(Class c) {
        try {
            include((Test) c.newInstance());
        } catch (Exception e) {
            printStackTrace(e);
        }
    }

    /**
     * Include another test in to this one.
     * 
     * @param test test to include.
     */
    protected void include(Test test) {
        res.addTestResult(test.run(args));
    }

    /**
     * Add tested method.
     * 
     * @param testedClass tested class.
     * @param methodName tested method name. null - means constructor.
     * @param types method types.
     */
    protected void addTestedMethod(Class testedClass, String methodName,
        Class[] types) {
        try {
            if (methodName != null) {
                testedMethods.add(testedClass.getMethod(methodName, types));
            } else {
                testedMethods.add(testedClass.getDeclaredConstructor(types));
            }
        } catch (Exception e) {
            printStackTrace(e);
        }
    }

    /**
     * This method is called before all tests are executed.
     * 
     * @throws Exception
     */
    protected void init() throws Exception {
    }

    /**
     * This method is called after all tests are executed.
     * 
     * @throws Exception
     */
    protected void release() throws Exception {
    }

    /**
     * This method is called before a test is executed.
     */
    protected void setUp() throws Exception {
    }

    /**
     * This method is called after a test is executed.
     */
    protected void tearDown() throws Exception {
    }

    /**
     * Fail a test.
     * 
     * @return Result object instance.
     */
    protected Result fail() {
        Result r = constructResult(Result.FAIL, "", 2);
        results.add(r);
        return r;
    }

    /**
     * Fail a test with the given message.
     * 
     * @param message message.
     * @return Result object instance.
     */
    public int fail(String message) {
        Result r = constructResult(Result.FAIL, addST(message, 2), 2);
        results.add(r);
        return Result.FAIL;
    }

    /**
     * Fail a test with the given message.
     * 
     * @param message message.
     * @return Result object instance.
     */
    protected Result fail(Throwable message) {
        Result r = constructResult(Result.FAIL, message, 2);
        results.add(r);
        return r;
    }

    /**
     * Fail a test with the given message.
     * 
     * @param message message.
     * @param ex exception.
     * @return Result object instance.
     */
    protected Result fail(String message, Throwable ex) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ex.printStackTrace(new PrintStream(baos));
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
        }

        message += "\n" + baos.toString();
        Result r = constructResult(Result.FAIL, message, 2);
        results.add(r);
        return r;
    }

    /**
     * Asserts that two ints are equal.
     * 
     * @param expected expected value.
     * @param actual actual value.
     * @return
     */
    protected Result assertEquals(int expected, int actual) {
        if (expected != actual) {
            return fail(equalsMessage(null, "int", "" + expected, "" + actual,
                false), 3);
        }

        return pass(3);
    }

    /**
     * Asserts that two ints are equal.
     * 
     * @param message message.
     * @param expected expected value.
     * @param actual actual value.
     * @return
     */
    protected Result assertEquals(String message, int expected, int actual) {
        if (expected != actual) {
            return fail(equalsMessage(message, "int", "" + expected, ""
                + actual, false), 3);
        }

        return pass(3);
    }

    /**
     * Asserts that two longs are equal.
     * 
     * @param expected expected value.
     * @param actual actual value.
     * @return
     */
    protected Result assertEquals(long expected, long actual) {
        if (expected != actual) {
            return fail(equalsMessage(null, "long", "" + expected, "" + actual,
                false), 3);
        }

        return pass(3);
    }

    /**
     * Asserts that two longs are equal.
     * 
     * @param message message.
     * @param expected expected value.
     * @param actual actual value.
     * @return
     */
    protected Result assertEquals(String message, long expected, long actual) {
        if (expected != actual) {
            return fail(equalsMessage(message, "long", "" + expected, ""
                + actual, false), 3);
        }

        return pass(3);
    }

    /**
     * Asserts that two objects are equal.
     * 
     * @param expected expected value.
     * @param actual actual value.
     * @return
     */
    protected Result assertEquals(Object expected, Object actual) {
        if (expected == null || actual == null) {
            return fail(equalsMessage(null, null, expected, actual, false), 3);
        } else if (expected.getClass().isArray() && actual.getClass().isArray()) {
            return Arrays.equals((Object[]) expected, (Object[]) actual)
                ? pass(3) : fail(equalsMessage(null, null, expected, actual,
                    false), 3);
        } else if (!expected.equals(actual)) {
            return fail(equalsMessage(null, null, expected, actual, false), 3);
        }

        return pass(3);
    }

    /**
     * Asserts that two objects are equal.
     * 
     * @param message message.
     * @param expected expected value.
     * @param actual actual value.
     * @return
     */
    protected Result assertEquals(String message, Object expected, Object actual) {
        if (expected == null || actual == null) {
            return fail(equalsMessage(message, null, expected, actual, false),
                3);
        } else if (expected.getClass().isArray() && actual.getClass().isArray()) {
            return Arrays.equals((Object[]) expected, (Object[]) actual)
                ? pass(3) : fail(equalsMessage(message, null, expected, actual,
                    false), 3);
        } else if (!expected.equals(actual)) {
            return fail(equalsMessage(message, null, expected, actual, false),
                3);
        }

        return pass(3);
    }

    /**
     * Asserts that two ints are equal.
     * 
     * @param expected expected value.
     * @param actual actual value.
     * @return
     */
    protected Result assertNotEquals(int expected, int actual) {
        if (expected == actual) {
            return fail(equalsMessage(null, "int", "" + expected, "" + actual,
                true), 3);
        }

        return pass(3);
    }

    /**
     * Asserts that two ints are equal.
     * 
     * @param message message.
     * @param expected expected value.
     * @param actual actual value.
     * @return
     */
    protected Result assertNotEquals(String message, int expected, int actual) {
        if (expected == actual) {
            return fail(equalsMessage(message, "int", "" + expected, ""
                + actual, true), 3);
        }

        return pass(3);
    }

    /**
     * Asserts that two longs are equal.
     * 
     * @param expected expected value.
     * @param actual actual value.
     * @return
     */
    protected Result assertNotEquals(long expected, long actual) {
        if (expected == actual) {
            return fail(equalsMessage(null, "long", "" + expected, "" + actual,
                true), 3);
        }

        return pass(3);
    }

    /**
     * Asserts that two longs are equal.
     * 
     * @param message message.
     * @param expected expected value.
     * @param actual actual value.
     * @return
     */
    protected Result assertNotEquals(String message, long expected, long actual) {
        if (expected == actual) {
            return fail(equalsMessage(message, "long", "" + expected, ""
                + actual, true), 3);
        }

        return pass(3);
    }

    /**
     * Asserts that two objects are equal.
     * 
     * @param expected expected value.
     * @param actual actual value.
     * @return
     */
    protected Result assertNotEquals(Object expected, Object actual) {
        if (expected == null || actual == null || expected.equals(actual)) {
            return fail(equalsMessage(null, null, expected, actual, true), 3);
        }

        return pass(3);
    }

    /**
     * Asserts that two objects are equal.
     * 
     * @param message message.
     * @param expected expected value.
     * @param actual actual value.
     * @return
     */
    protected Result assertNotEquals(String message, Object expected,
        Object actual) {
        if (expected == null || actual == null || expected.equals(actual)) {
            return fail(equalsMessage(message, null, expected, actual, true), 3);
        }

        return pass(3);
    }

    /**
     * Asserts that two objects refer to the same object.
     * 
     * @param message message.
     * @param expected expected value.
     * @param actual actual value.
     * @return
     */
    protected Result assertSame(String message, Object expected, Object actual) {
        if (expected != actual) {
            String msg = message != null ? message + "\n" : "";
            msg += "assertSame(Object, Object):\n    expected: "
                + parseMessage(expected, "    ") + "\n    actual: "
                + parseMessage(actual, "    ");
            return fail(msg, 3);
        }

        return pass(3);
    }

    /**
     * Asserts that two objects are equal.
     * 
     * @param expected expected value.
     * @param actual actual value.
     * @return
     */
    protected Result assertSame(Object expected, Object actual) {
        if (expected != actual) {
            String msg = "assertSame(Object, Object):\n    expected: "
                + parseMessage(expected, "    ") + "\n    actual: "
                + parseMessage(actual, "    ");
            return fail(msg, 3);
        }

        return pass(3);
    }

    /**
     * Asserts that two objects refer to the same object.
     * 
     * @param message message.
     * @param expected expected value.
     * @param actual actual value.
     * @return
     */
    protected Result assertNotSame(String message, Object expected,
        Object actual) {
        if (expected == actual) {
            String msg = message != null ? message + "\n" : "";
            msg += "assertNotSame(Object, Object):\n    expected: "
                + parseMessage(expected, "    ") + "\n    actual: "
                + parseMessage(actual, "    ");
            return fail(msg, 3);
        }

        return pass(3);
    }

    /**
     * Asserts that two objects are equal.
     * 
     * @param expected expected value.
     * @param actual actual value.
     * @return
     */
    protected Result assertNotSame(Object object1, Object object2) {
        if (object1 == object2) {
            String msg = "assertNotSame(Object, Object):\n    object1: "
                + parseMessage(object1, "    ") + "\n    object2: "
                + parseMessage(object2, "    ");
            return fail(msg, 3);
        }

        return pass(3);
    }

    /**
     * Asserts that the object is null.
     */
    protected Result assertNull(String message, Object obj) {
        if (obj != null) {
            String msg = message != null ? message + "\n" : "";
            msg += "assertNull(Object): " + parseMessage(obj, "    ");
            return fail(msg, 3);
        }

        return pass(3);
    }

    /**
     * Asserts that the object is null.
     */
    protected Result assertNull(Object obj) {
        if (obj != null) {
            String msg = "assertNull(Object): " + parseMessage(obj, "    ");
            return fail(msg, 3);
        }

        return pass(3);
    }

    /**
     * Asserts that the object is not null.
     */
    protected Result assertNotNull(String message, Object obj) {
        if (obj == null) {
            String msg = message != null ? message + "\n" : "";
            msg += "assertNotNull(Object): " + obj;
            return fail(msg, 3);
        }

        return pass(3);
    }

    /**
     * Asserts that the object is not null.
     */
    protected Result assertNotNull(Object obj) {
        if (obj == null) {
            String msg = "assertNotNull(Object): " + obj;
            return fail(msg, 3);
        }

        return pass(3);
    }

    /**
     * Asserts that the condition is true.
     */
    protected Result assertTrue(String message, boolean condition) {
        if (!condition) {
            String msg = message != null ? message + "\n" : "";
            msg += "assertTrue(boolean): " + condition;
            return fail(msg, 3);
        }

        return pass(3);
    }

    /**
     * Asserts that the condition is true.
     */
    protected Result assertTrue(boolean condition) {
        if (!condition) {
            String msg = "assertTrue(boolean): " + condition;
            return fail(msg, 3);
        }

        return pass(3);
    }

    /**
     * Asserts that the condition is false.
     */
    protected Result assertFalse(String message, boolean condition) {
        if (condition) {
            String msg = message != null ? message + "\n" : "";
            msg += "assertFalse(boolean): " + condition;
            return fail(msg, 3);
        }

        return pass(3);
    }

    /**
     * Asserts that the condition is false.
     */
    protected Result assertFalse(boolean condition) {
        if (condition) {
            String msg = "assertFalse(boolean): " + condition;
            return fail(msg, 3);
        }

        return pass(3);
    }

    /**
     * Returns the command line argument value.
     * 
     * @param name argument name.
     * @return the command line argument value.
     */
    protected String getArg(String name) {
        if (name == null) {
            return null;
        }
        for (int i = 0; i < (args.length - 1); i++) {
            try {
                if (name.equals(args[i])) {
                    return args[i + 1];
                }
            } catch (Exception e) {
                break;
            }
        }
        return null;
    }

    /**
     * Print stack trace.
     * 
     * @param ex exception.
     */
    protected void printStackTrace(Throwable ex) {
        if (ex == null) {
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ex.printStackTrace(new PrintStream(baos));
        log.log(baos.toString(), "[EXCEPTION]", 2);
    }

    /**
     * Test methods.
     * 
     * @return test methods.
     */
    private int getMethods(Vector methods, Vector skippedMethods,
        Hashtable coverage) {
        List exclude = Utils.getCommaSeparatedTokens(getArg("-skip"));
        Class c = this.getClass();
        if (!"false".equalsIgnoreCase(getArg("-coverage"))) {
            testedClassMethods(coverage);
        } else {
            coverage = null;
        }
        int testCount = 0;
        Method[] m = c.getMethods();
        for (int i = 0; i < m.length; i++) {
            String name = m[i].getName();
            if (exclude.contains(name)) {
                skippedMethods.add(m[i]);
                continue;
            }

            if (name.length() > 4 && name.startsWith("test")
                && m[i].getParameterTypes().length == 0
                && !Modifier.isPrivate(m[i].getModifiers())) {

                testCount++;

                // Find setUp+testName method.
                Method service = findMethod(c, name.replaceFirst("test",
                    "setUp"));
                if (service != null) {
                    methods.add(service);
                }

                // Add test method.
                methods.add(m[i]);

                // Find tearDown+testName method.
                service = findMethod(c, name.replaceFirst("test", "tearDown"));
                if (service != null) {
                    methods.add(service);
                }

                // Find tested method.
                if (coverage != null && coverage.containsKey(name)) {
                    testedMethods.add(coverage.get(name));
                }
            }
        }
        return testCount;
    }

    /**
     * The sufix of the name of the tested class.
     * 
     * @return the index of the name of the tested class.
     */
    private String testedClassPrefix() {
        return "org.apache.harmony.test.func.api.";
    }

    /**
     * Find tested methods.
     * 
     * @return tested methods.
     */
    private Hashtable testedClassMethods(Hashtable ht) {
        String cname = null;

        try {
            cname = (String) this.getClass().getField("testedClass").get(this);
        } catch (Exception e) {
            cname = this.getClass().getName();
        }

        cname = cname.replaceFirst(testedClassPrefix(), "");
        int ind = cname.lastIndexOf("Test");
        if (ind != -1) {
            cname = cname.substring(0, ind);
        }

        if (cname.equals(this.getClass().getName())) {
            return null;
        }

        Class c = null;
        try {
            c = Class.forName(cname);
        } catch (Exception e) {
            return null;
        }

        Member[] dc = c.getDeclaredConstructors();
        Member[] dm = c.getDeclaredMethods();
        Member[] m = new Member[dc.length + dm.length];
        System.arraycopy(dc, 0, m, 0, dc.length);
        System.arraycopy(dm, 0, m, dc.length, dm.length);
        for (int i = 0; i < m.length; i++) {
            int mod = m[i].getModifiers();
            if (Modifier.isPublic(mod) || Modifier.isProtected(mod)) {
                String mname = m[i].getName();
                // Check if there are more than 1 methods with the same name.
                int count = 0;
                for (int n = 0; n < m.length; n++) {
                    if (mname.equals(m[n].getName())) {
                        count++;
                    }
                    if (count > 1) {
                        break;
                    }
                }

                if (m[i] instanceof Constructor) {
                    ind = mname.lastIndexOf('.');
                    if (ind != -1) {
                        mname = mname.substring(ind + 1);
                    }
                }

                mname = "test" + mname.substring(0, 1).toUpperCase()
                    + mname.substring(1);

                if (count > 1) {
                    Class[] t = null;
                    if (m[i] instanceof Method) {
                        t = ((Method) m[i]).getParameterTypes();
                    } else {
                        t = ((Constructor) m[i]).getParameterTypes();
                    }

                    for (int n = 0; n < t.length; n++) {
                        String s = t[n].getName();
                        if (t[n].isArray()) {
                            s = Utils.classesToString(new Class[] { t[n] })
                                .replaceAll("\\[]", "Array");
                        }
                        ind = s.lastIndexOf('.');
                        if (ind != -1) {
                            s = s.substring(ind + 1);
                        }
                        mname += s;
                    }
                }

                ht.put(mname, m[i]);
            }
        }

        return ht;
    }

    /**
     * Retrieves the method with specified name.
     * 
     * @param c class.
     * @param methodName method name.
     * @return the method with specified name.
     */
    private Method findMethod(Class c, String methodName) {
        try {
            Method m = c.getMethod(methodName, new Class[0]);
            int mod = m.getModifiers();
            return (Modifier.isPublic(mod) || Modifier.isProtected(mod)) ? m
                : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Construct Result object instnce.
     * 
     * @param result test result.
     * @param message message.
     * @param methodInd position in the stacktrace.
     * @return Result object instance.
     */
    private Result constructResult(int result, String message, int methodInd) {
        StackTraceElement ste = new Throwable().getStackTrace()[methodInd];
        return new Result(result, message, ste);
    }

    /**
     * Construct Result object instnce.
     * 
     * @param result test result.
     * @param message message.
     * @param methodInd position in the stacktrace.
     * @return Result object instance.
     */
    private Result constructResult(int result, Throwable message, int methodInd) {
        StackTraceElement ste = new Throwable().getStackTrace()[methodInd];
        return new Result(result, message, ste);
    }

    /**
     * Fail a test with the given message.
     * 
     * @param message message.
     * @param methodInd position in the stacktrace.
     * @return Result object instance.
     */
    private Result fail(String message, int methodInd) {
        Result r = constructResult(Result.FAIL, addST(message, methodInd),
            methodInd);
        results.add(r);
        return r;
    }

    /**
     * Ad stacktrace to the message.
     * 
     * @param message
     * @param ind
     * @return
     */
    private String addST(String message, int methodInd) {
        String str = "";
        final String tc = Test.class.getName() + ".run";
        StackTraceElement[] ste = new Throwable().getStackTrace();
        for (int i = methodInd; i < ste.length; i++) {
            String cn = ste[i].getClassName() + "." + ste[i].getMethodName();
            if (cn.equals(tc)) {
                break;
            }
            String fn = ste[i].getFileName();
            int ln = ste[i].getLineNumber();
            if (fn != null) {
                if (ln >= 0) {
                    fn += ":" + ln;
                }
            } else {
                fn = "";
            }

            str += "\n    " + cn + "(" + fn + ")";
        }
        message += "\nStacktrace:" + str;
        return message;
    }

    /**
     * Pass a test.
     * 
     * @param methodInd position in the stacktrace.
     * @return Result object instance.
     */
    private Result pass(int methodInd) {
        return constructResult(Result.PASS, (String) null, methodInd);
    }

    /**
     * @param message1
     * @return
     */
    private String parseMessage(Object message, String shift) {
        String m = "";
        if (message == null) {
            return "null";
        }

        if ((m = message.toString()).trim().equals("")) {
            return "";
        }

        if (m.indexOf("\n") > 80 || m.length() > 80) {
            m = "\n    " + m;
        }

        return m.replaceAll("\n", "\n" + shift);
    }

    /**
     * Returns assertEquals failed message.
     * 
     * @param message head message.
     * @param type objects type.
     * @param expected expected object.
     * @param actual actual object.
     * @param invert invert to failNotEquals.
     * @return assertEquals failed message.
     */
    private String equalsMessage(String message, String type, Object expected,
        Object actual, boolean invert) {

        String etype = type != null ? type : expected != null ? expected
            .getClass().getName() : "java.lang.Object";

        String atype = type != null ? type : actual != null ? actual.getClass()
            .getName() : "java.lang.Object";

        if (message == null || message.trim().equals("")) {
            message = "";
        } else {
            message += "\n";
        }

        String s = invert ? "assertNotEquals" : "assertEquals";

        message += s + "(" + etype + ", " + atype + "):\n   expected: "
            + expected + "\n   actual: " + actual;

        return message;
    }

    /**
     * Add error.
     * 
     * @param ex exception.
     * @param mName method name.
     */
    private void addError(Throwable ex, String mName) {
        StackTraceElement[] ste = ex.getStackTrace();
        String cn = this.getClass().getName();
        for (int i = 0; i < ste.length; i++) {
            if (ste[i].getClassName().equals(cn)
                && ste[i].getMethodName().equals(mName)) {
                results.add(new Result(Result.ERROR, ex, ste[i]));
                return;
            }
        }

        results.add(new Result(Result.ERROR, ex, cn, mName));
    }

    private void waitFor() {
        while (freeze.isAlive()) {
        }
    }

    /**
     * This thread is used to freeze the test execution for the specified number
     * of milliseconds.
     * 
     */
    private class Freeze extends Thread {

        /**
         * Freeze time.
         */
        public long time;

        /**
         * Freeze the thread.
         */
        public void run() {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
            }
        }
    }
}
