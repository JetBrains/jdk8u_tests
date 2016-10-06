/*
    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and
    limitations under the License.
*/
/**
 * @author Vladimir A. Ivanov
 * @author Yerlan Tokpanov
 * @version $Revision: 1.13 $
 */
package org.apache.harmony.share;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.harmony.share.Test;
import org.apache.harmony.share.Result;

/**
 * This is a base class for API tests. It is assumed that common API test
 * contains many simple test cases to check general functionality of each class
 * method.
 */
public class MultiCase extends Test implements JUnitConverter {

    public static final String ERROR_CONST_MSG  = "ERROR";
    public static final String FAILED_CONST_MSG = "FAILED";
    public static final String PASSED_CONST_MSG = "PASSED";
    public static final String TRUE_CONST_MSG   = "TRUE";
    public static final String FALSE_CONST_MSG  = "FALSE";

    //the check statuses for each assert check into test case
    protected List             assertStatuses   = new ArrayList();

    protected String[]         toExclude;
    protected String[]         toInclude;
    protected boolean          createNewInstance;
    protected boolean          zeroTestCaseIsError;

    private String             formatedOut      = "";

    /**
     * Return numbers of all defined test cases in this test. For statistics
     * purpose only.
     */
    public int countTestCases() {
        try {
            //if some enemies set exclude/include, skip it
            toExclude = null;
            toInclude = null;
            return defineMethods(getClass()).size();
        } catch (Exception e) {
            log.info("MultiCase: countTestCases. Unexpected exception " + e);
        }
        return 0;
    }

    /**
     * Return numbers of test cases in this test that will be run in the current
     * configuration For statistics purpose only.
     * 
     * @param args - array of arguments (based on current configuration)
     * @return
     */
    public int getTestNumber(String[] args) {
        try {
            log.setLevel(getLogLevel(args));
            removeLogLevel(args);
            parseArgs(testArgs);
            return defineMethods(getClass()).size();
        } catch (Exception e) {
            log.info("MultiCase: getTestNumber. Unexpected exception " + e);
        }
        return 0;
    }

    protected void parseArgs(String[] params) {
        if (params == null || params.length == 0) {
            return;
        }
        try {
            for (int i = 0; i < params.length; i++) {
                if ("-exclude".equalsIgnoreCase(params[i])) {
                    toExclude = params[++i].split(" ");
                    log.fine("MultiCase: Test cases to exclude: "
                        + toExclude.length);
                } else if ("-createnewinstance".equalsIgnoreCase(params[i])) {
                    createNewInstance = true;
                } else if ("-include".equalsIgnoreCase(params[i])) {
                    toInclude = params[++i].split(" ");
                    log.fine("MultiCase: Test cases to run: "
                        + toInclude.length);
                } else if ("-zerotcerr".equalsIgnoreCase(params[i])) {
                    zeroTestCaseIsError = true;
                }
            }
        } catch (Exception e) {
            log
                .info("Unexpected exception while parse a command line parameters: "
                    + e);
        }
    }

    protected List defineMethods(Class cl) {
        ArrayList result = new ArrayList();
        HashMap methodNames = new HashMap();

        Method[] allMethods = cl.getDeclaredMethods();
        for (int i = 0; i < allMethods.length; i++) {
            if (allMethods[i].getName().startsWith("test")
                && allMethods[i].getReturnType().isAssignableFrom(Result.class)
                && allMethods[i].getParameterTypes().length == 0) {
                result.add(allMethods[i]);
                methodNames.put(allMethods[i].getName(), allMethods[i]);
            }
        }
        if (toExclude == null && toInclude == null) {
            return result;
        }
        if (toInclude != null) {
            //include only pointed testcases. Exclude does not work here
            result.clear();
            for (int i = 0; i < toInclude.length; i++) {
                if (methodNames.containsKey(toInclude[i])) {
                    result.add(methodNames.get(toInclude[i]));
                }
            }
        } else {// (toExclude != null) {
            for (int i = 0; i < toExclude.length; i++) {
                if (methodNames.containsKey(toExclude[i])) {
                    result.remove(methodNames.get(toExclude[i]));
                }
            }
        }
        return result;
    }

    // calculate test result. If any testcase report ERROR -> total status ERROR
    // or total status first non PASSED
    protected int calcExecStatus(int[] execStatus) {
        int toRet = Integer.MIN_VALUE;
        boolean passed = true;
        for (int i = execStatus.length - 1; i >= 0; i--) {
            if (Result.PASS != execStatus[i]) {
                if (Result.ERROR == execStatus[i]) {
                    return Result.ERROR;
                }
                passed = false;
                toRet = execStatus[i];
            }
        }
        if (!passed) {
            return toRet;
        }
        return Result.PASS;
    }

    public int test() {
        long startTime = System.currentTimeMillis();
        int[] execStatus = { Result.ERROR };
        try {
            Result retType = new Result(Result.ERROR, "no message");
            Object[] params = new Object[0];
            List toCall = defineMethods(getClass());
            formatedOut = formatedOut + "<multicase id=\"" + this.getClass()
                + "\">\n";
            execStatus = new int[toCall.size()];
            log.info("Test: " + this.getClass() + "\n\t\ttest case(s) to run "
                + toCall.size());
            if (zeroTestCaseIsError && toCall.size() == 0) {
                log
                    .info("MultiCase: Zero testcases to run in this test. It set to interpret as error.");
                return Result.ERROR;
            }
            MultiCase mc = new MultiCase();
            for (int i = execStatus.length - 1; i >= 0; i--) {
                String logInfoMsg = "Testcase: @"
                    + ((Method)toCall.get(i)).getName() + "\tresult is: ";
                long caseStartTime = System.currentTimeMillis();
                try {
                    assertStatuses.clear();
                    setUp();
                    if (createNewInstance == true) {
                        mc = (MultiCase)this.getClass().newInstance();
                        mc.parseArgs(testArgs);
                        mc.setUp();
                        retType = (Result)(((Method)toCall.get(i)).invoke(mc,
                            params));
                    } else {
                        retType = (Result)(((Method)toCall.get(i)).invoke(this,
                            params));
                    }
                } catch (InvocationTargetException e) {
                    log.warning(logInfoMsg + ERROR_CONST_MSG + "("
                        + Result.ERROR + ")\n\t\tUnexpected exception: "
                        + e.getTargetException());
                    StackTraceElement[] st = e.getTargetException()
                        .getStackTrace();
                    String stmsg = "Stack trace for "
                        + e.getTargetException().getClass().getName() + "\n";
                    for (int stcnt = 0; stcnt < st.length; stcnt++) {
                        stmsg = stmsg + "\t" + st[stcnt] + "\n";
                    }
                    if (stmsg.length() > 0) {
                        log.info(stmsg);
                    }
                    retType = new Result(Result.ERROR, "Unexpected exception: "
                        + e.getTargetException());
                    return Result.ERROR;
                } catch (Throwable e) {
                    log.warning(logInfoMsg + ERROR_CONST_MSG + "("
                        + Result.ERROR + ")\n\t\tUnexpected exception: " + e);
                    StackTraceElement[] st = e.getStackTrace();
                    String stmsg = "Stack trace for " + e.getClass().getName()
                        + "\n";
                    for (int stcnt = 0; stcnt < st.length; stcnt++) {
                        stmsg = stmsg + "\t" + st[stcnt] + "\n";
                    }
                    if (stmsg.length() > 0) {
                        log.info(stmsg);
                    }
                    retType = new Result(Result.ERROR, "Unexpected exception: "
                        + e);
                    return Result.ERROR;
                } finally {
                    if (createNewInstance == true) {
                        mc.tearDown();
                    } else {
                        tearDown();
                    }
                    formatedOut = formatedOut + "<case id=\""
                        + ((Method)toCall.get(i)).getName() + "\">\n";
                    formatedOut = formatedOut + "<duration value=\""
                        + (System.currentTimeMillis() - caseStartTime)
                        + "\"/>\n";
                    formatedOut = formatedOut + "<status value=\""
                        + retType.getResult() + "\">\n\t<![CDATA["
                        + retType.getMessage() + "]]>\n</status>\n";
                    formatedOut = formatedOut + "</case>\n";
                }
                execStatus[i] = retType.getResult();
                log.info(logInfoMsg + retType.getMessage() + "("
                    + execStatus[i] + ")");
            }
            return calcExecStatus(execStatus);
        } catch (Exception e) {
            log.info("MultiCase: Unexpected exception " + e);
            return Result.ERROR;
        } finally {
            formatedOut = formatedOut + "<duration value=\""
                + (System.currentTimeMillis() - startTime) + "\"/>\n";
            formatedOut = formatedOut + "<status value=\""
                + calcExecStatus(execStatus) + "\"/>\n";
            formatedOut = formatedOut + "</multicase>\n";
            //note, it is one place to store all formated output.
            //should be commented for empty formatted output
            //storeUserData(formatedOut);
        }
    }

    public int test(String[] args) {
        parseArgs(args);
        //the super.test(args) also setup the testArgs array
        return super.test(args);
    }

    /**
     * Sets up the fixture, for example, open a network connection. This method
     * is called before a test is executed.
     */
    protected void setUp() throws Exception {
    }

    /**
     * Tears down the fixture, for example, close a network connection. This
     * method is called after a test is executed.
     */
    protected void tearDown() throws Exception {
    }

    public Result passed() {
        return new Result(Result.PASS, PASSED_CONST_MSG);
    }

    public Result passed(String msg) {
        return new Result(Result.PASS, msg + " " + PASSED_CONST_MSG);
    }

    public Result failed(String msg) {
        return new Result(Result.FAIL, msg + " " + FAILED_CONST_MSG);
    }

    public Result result() {
        int i = analyze();
        switch (i) {
        case Result.PASS:
            return new Result(i, PASSED_CONST_MSG);
        case Result.FAIL:
            return new Result(i, FAILED_CONST_MSG);
        default:
            return new Result(i, ERROR_CONST_MSG);
        }
    }

    private int analyze() {
        int res;
        if (assertStatuses.size() > 0) {
            if (assertStatuses.contains(ERROR_CONST_MSG)) {
                res = Result.ERROR;
            } else if (assertStatuses.contains(FAILED_CONST_MSG)) {
                res = Result.FAIL;
            } else if (assertStatuses.contains(PASSED_CONST_MSG)) {
                res = Result.PASS;
            } else {
                res = Result.ERROR;
            }
        } else {
            res = Result.PASS;
        }
        return res;
    }

    public void jufail() {
        jufail(FAILED_CONST_MSG);
    }

    public void jupass() {
        jupass(PASSED_CONST_MSG);
    }

    public void juerror() {
        juerror(ERROR_CONST_MSG);
    }

    public void jufail(String msg) {
        log.info(msg);
        assertStatuses.add(FAILED_CONST_MSG);
    }

    public void jupass(String msg) {
        log.info(msg);
        assertStatuses.add(PASSED_CONST_MSG);
    }

    public void juerror(String msg) {
        log.severe(msg);
        assertStatuses.add(ERROR_CONST_MSG);
    }

    public void assertTrue(boolean expr) {
        try {
            if (expr) {
                jupass();
            } else {
                jufail("assertTrue " + FALSE_CONST_MSG + " ");
            }
        } catch (Throwable e) {
            juerror(e.toString());
        }
    }

    public void assertTrue(String msg, boolean expr) {
        try {
            if (expr) {
                jupass(msg);
            } else {
                jufail("assertTrue " + FALSE_CONST_MSG + " " + msg);
            }
        } catch (Throwable e) {
            juerror(msg + ": " + e);
        }
    }

    public void assertFalse(boolean expr) {
        try {
            if (expr) {
                jufail("assertFalse " + TRUE_CONST_MSG + " ");
            } else {
                jupass();
            }
        } catch (Throwable e) {
            juerror(e.toString());
        }
    }

    public void assertFalse(String msg, boolean expr) {
        try {
            if (expr) {
                jufail("assertFalse " + TRUE_CONST_MSG + " " + msg);
            } else {
                jupass(msg);
            }
        } catch (Throwable e) {
            juerror(msg + ": " + e);
        }
    }

    public void assertNull(Object obj) {
        try {
            if (obj == null) {
                jupass();
            } else {
                jufail("assertNull " + FALSE_CONST_MSG + " ");
            }
        } catch (Throwable e) {
            juerror(e.toString());
        }
    }

    public void assertNull(String msg, Object obj) {
        try {
            if (obj == null) {
                jupass(msg);
            } else {
                jufail("assertNull " + FALSE_CONST_MSG + " " + msg);
            }
        } catch (Throwable e) {
            juerror(msg + ": " + e.toString());
        }
    }

    public void assertNotNull(Object obj) {
        try {
            if (obj != null) {
                jupass();
            } else {
                jufail("assertNotNull " + FALSE_CONST_MSG + " ");
            }
        } catch (Throwable e) {
            juerror(e.toString());
        }
    }

    public void assertNotNull(String msg, Object obj) {
        try {
            if (obj != null) {
                jupass(msg);
            } else {
                jufail("assertNotNull " + FALSE_CONST_MSG + " " + msg);
            }
        } catch (Throwable e) {
            juerror(msg + ": " + e);
        }
    }

    public void assertSame(Object expected, Object actual) {
        try {
            if (expected == actual) {
                jupass();
            } else {
                jufail("assertSame " + FALSE_CONST_MSG + " ");
            }
        } catch (Throwable e) {
            juerror(e.toString());
        }
    }

    public void assertSame(String msg, Object expected, Object actual) {
        try {
            if (expected == actual) {
                jupass(msg);
            } else {
                jufail("assertSame " + FALSE_CONST_MSG + " " + msg);
            }
        } catch (Throwable e) {
            juerror(msg + ": " + e.toString());
        }
    }

    public void assertNotSame(Object expected, Object actual) {
        try {
            if (expected != actual) {
                jupass();
            } else {
                jufail("assertNotSame " + FALSE_CONST_MSG + " ");
            }
        } catch (Throwable e) {
            juerror(e.toString());
        }
    }

    public void assertNotSame(String msg, Object expected, Object actual) {
        try {
            if (expected != actual) {
                jupass(msg);
            } else {
                jufail("assertNotSame " + FALSE_CONST_MSG + " " + msg);
            }
        } catch (Throwable e) {
            juerror(msg + ": " + e.toString());
        }
    }

    public void assertEquals(Object expected, Object actual) {
        try {
            if (expected.equals(actual)) {
                jupass();
            } else {
                jufail("assertEquals " + FALSE_CONST_MSG + " ");
            }
        } catch (Throwable e) {
            juerror(e.toString());
        }
    }

    public void assertEquals(String msg, Object expected, Object actual) {
        try {
            if (expected.equals(actual)) {
                jupass(msg);
            } else {
                jufail("assertEquals " + FALSE_CONST_MSG + " " + msg);
            }
        } catch (Throwable e) {
            juerror(msg + ": " + e.toString());
        }
    }

    public void assertEquals(boolean expected, boolean actual) {
        try {
            if (expected == actual) {
                jupass();
            } else {
                jufail("assertEquals_zz " + FALSE_CONST_MSG + " ");
            }
        } catch (Throwable e) {
            juerror(e.toString());
        }
    }

    public void assertEquals(String msg, boolean expected, boolean actual) {
        try {
            if (expected == actual) {
                jupass(msg);
            } else {
                jufail("assertEquals_zz " + FALSE_CONST_MSG + " " + msg);
            }
        } catch (Throwable e) {
            juerror(msg + ": " + e.toString());
        }
    }

    public void assertEquals(byte expected, byte actual) {
        try {
            if (expected == actual) {
                jupass();
            } else {
                jufail("assertEquals_bb " + FALSE_CONST_MSG + " ");
            }
        } catch (Throwable e) {
            juerror(e.toString());
        }
    }

    public void assertEquals(String msg, byte expected, byte actual) {
        try {
            if (expected == actual) {
                jupass(msg);
            } else {
                jufail("assertEquals_bb " + FALSE_CONST_MSG + " " + msg);
            }
        } catch (Throwable e) {
            juerror(msg + ": " + e.toString());
        }
    }

    public void assertEquals(char expected, char actual) {
        try {
            if (expected == actual) {
                jupass();
            } else {
                jufail("assertEquals_cc " + FALSE_CONST_MSG + " ");
            }
        } catch (Throwable e) {
            juerror(e.toString());
        }
    }

    public void assertEquals(String msg, char expected, char actual) {
        try {
            if (expected == actual) {
                jupass(msg);
            } else {
                jufail("assertEquals_cc " + FALSE_CONST_MSG + " " + msg);
            }
        } catch (Throwable e) {
            juerror(msg + ": " + e.toString());
        }
    }

    public void assertEquals(int expected, int actual) {
        try {
            if (expected == actual) {
                jupass();
            } else {
                jufail("assertEquals_ii " + FALSE_CONST_MSG + " ");
            }
        } catch (Throwable e) {
            juerror(e.toString());
        }
    }

    public void assertEquals(String msg, int expected, int actual) {
        try {
            if (expected == actual) {
                jupass(msg);
            } else {
                jufail("assertEquals_ii " + FALSE_CONST_MSG + " " + msg);
            }
        } catch (Throwable e) {
            juerror(msg + ": " + e.toString());
        }
    }

    public void assertEquals(short expected, short actual) {
        try {
            if (expected == actual) {
                jupass();
            } else {
                jufail("assertEquals_ss " + FALSE_CONST_MSG + " ");
            }
        } catch (Throwable e) {
            juerror(e.toString());
        }
    }

    public void assertEquals(String msg, short expected, short actual) {
        try {
            if (expected == actual) {
                jupass(msg);
            } else {
                jufail("assertEquals_ss " + FALSE_CONST_MSG + " " + msg);
            }
        } catch (Throwable e) {
            juerror(msg + ": " + e.toString());
        }
    }

    public void assertEquals(long expected, long actual) {
        try {
            if (expected == actual) {
                jupass();
            } else {
                jufail("assertEquals_jj " + FALSE_CONST_MSG + " ");
            }
        } catch (Throwable e) {
            juerror(e.toString());
        }
    }

    public void assertEquals(String msg, long expected, long actual) {
        try {
            if (expected == actual) {
                jupass(msg);
            } else {
                jufail("assertEquals_jj " + FALSE_CONST_MSG + " " + msg);
            }
        } catch (Throwable e) {
            juerror(msg + ": " + e.toString());
        }
    }

    public void assertEquals(double expected, double actual, double delta) {
        try {
            if (Double.isInfinite(expected)) {
                if (expected == actual) {
                    jupass();
                } else {
                    //only 2 param used -> _dd
                    jufail("assertEquals_dd " + FALSE_CONST_MSG + " ");
                }
            } else {
                if (Math.abs(expected - actual) <= delta) {
                    jupass();
                } else {
                    jufail("assertEquals_ddd " + FALSE_CONST_MSG + " ");
                }
            }
        } catch (Throwable e) {
            juerror(e.toString());
        }
    }

    public void assertEquals(String msg, double expected, double actual,
        double delta) {
        try {
            if (Double.isInfinite(expected)) {
                if (expected == actual) {
                    jupass(msg);
                } else {
                    jufail("assertEquals_dd " + FALSE_CONST_MSG + " " + msg);
                }
            } else {
                if (Math.abs(expected - actual) <= delta) {
                    jupass(msg);
                } else {
                    jufail("assertEquals_ddd " + FALSE_CONST_MSG + " " + msg);
                }
            }
        } catch (Throwable e) {
            juerror(msg + ": " + e.toString());
        }
    }

    public void assertEquals(float expected, float actual, float delta) {
        try {
            if (Float.isInfinite(expected)) {
                if (expected == actual) {
                    jupass();
                } else {
                    jufail("assertEquals_ff " + FALSE_CONST_MSG + " ");
                }
            } else {
                if (Math.abs(expected - actual) <= delta) {
                    jupass();
                } else {
                    jufail("assertEquals_fff " + FALSE_CONST_MSG + " ");
                }
            }
        } catch (Throwable e) {
            juerror(e.toString());
        }
    }

    public void assertEquals(String msg, float expected, float actual,
        float delta) {
        try {
            if (Float.isInfinite(expected)) {
                if (expected == actual) {
                    jupass(msg);
                } else {
                    jufail("assertEquals_ff " + FALSE_CONST_MSG + " " + msg);
                }
            } else {
                if (Math.abs(expected - actual) <= delta) {
                    jupass(msg);
                } else {
                    jufail("assertEquals_fff " + FALSE_CONST_MSG + " " + msg);
                }
            }
        } catch (Throwable e) {
            juerror(msg + ": " + e.toString());
        }
    }

    public void assertEquals(String expected, String actual) {
        try {
            if (expected.equals(actual)) {
                jupass();
            } else {
                jufail("assertEquals_ll " + FALSE_CONST_MSG + " ");
            }
        } catch (Throwable e) {
            juerror(e.toString());
        }
    }

    public void assertEquals(String msg, String expected, String actual) {
        try {
            if (expected.equals(actual)) {
                jupass(msg);
            } else {
                jufail("assertEquals_ll " + FALSE_CONST_MSG + " " + msg);
            }
        } catch (Throwable e) {
            juerror(e.toString());
        }
    }
}