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

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

/**
 */
public class TestResult {

    /**
     * Test results.
     */
    private Vector results;

    /**
     * Skipped tests.
     */
    private Vector skipped;

    /**
     * Tested Methods.
     */
    private Vector testedMethods;

    /**
     * Tests count.
     */
    private int    count;

    /**
     * Execution time.
     */
    private long   time;

    /**
     * Result.
     */
    private int    result = Result.PASS;

    /**
     * Construct new TestResult object.
     */
    public TestResult() {
        results = new Vector();
        skipped = new Vector();
        testedMethods = new Vector();
    }

    /**
     * Construct new TestResult object.
     * 
     * @param results results.
     * @param skipped skipped tests.
     * @param testedMethods tested methods.
     * @param count tests count.
     * @param time execution time.
     */
    public TestResult(Vector results, Vector skipped, Vector testedMethods,
        int count, long time) {
        this.results = results;
        this.skipped = skipped;
        this.testedMethods = testedMethods;
        this.count = count;
        this.time = time;
        result = getResults(Result.ERROR).size() > 0 ? Result.ERROR
            : getResults(Result.FAIL).size() > 0 ? Result.FAIL : Result.PASS;
    }

    /**
     * @return Returns the errors.
     */
    public Vector getResults() {
        return results;
    }

    /**
     * @return Returns the result.
     */
    public int getResult() {
        return result;
    }

    /**
     * @return Returns the skipped.
     */
    public Vector getSkipped() {
        return skipped;
    }

    /**
     * @return Returns the testedMethods.
     */
    public Vector getTestedMethods() {
        return testedMethods;
    }

    /**
     * @return Returns the count.
     */
    public int getCount() {
        return count;
    }

    /**
     * @return Returns the time.
     */
    public long getTime() {
        return time;
    }

    /**
     * Add test results.
     * 
     * @param res
     */
    public void addTestResult(TestResult res) {
        results.addAll(res.getResults());
        skipped.addAll(res.getSkipped());
        this.count += res.count;
        time += res.getTime();
        int r = res.getResult();
        if ((r != Result.PASS) && (result != Result.ERROR)) {
            result = r;
        }

        Vector tm = res.getTestedMethods();
        int size = tm.size();
        for (int i = 0; i < size; i++) {
            Object m = tm.get(i);
            if (!testedMethods.contains(m)) {
                testedMethods.add(m);
            }
        }
    }

    /**
     * Print report to the System.out.
     */
    public void printReport() {
        printReport(System.out);
    }

    /**
     * Print report to the <b>out</b>.
     */
    public void printReport(PrintStream out) {
        Vector errors = getResults(Result.ERROR);
        String nl = "\n";
        boolean printNL = false;

        // Print coverage.
        int msize = testedMethods.size();
        if (msize != 0) {
            printNL = true;
            out.println("Tested methods: " + msize + ":");
            Class[] c = getTestedClasses();
            Comparator comp = new Comparator() {
                public int compare(Object o1, Object o2) {
                    if ((o1 instanceof Constructor) && (o2 instanceof Method)) {
                        return -1;
                    } else if ((o2 instanceof Constructor)
                        && (o1 instanceof Method)) {
                        return 1;
                    }

                    Member m1 = (Member) o1;
                    Member m2 = (Member) o2;
                    String n1 = methodToString(m1);
                    String n2 = methodToString(m2);
                    n1 = n1.substring(n1.indexOf(m1.getName() + "("));
                    n2 = n2.substring(n2.indexOf(m2.getName() + "("));
                    return n1.compareTo(n2);
                }
            };
            for (int i = 0; i < c.length; i++) {
                Vector covered = new Vector();
                Vector notCovered = new Vector();
                int total = coverage(c[i], covered, notCovered);
                Member[] m = new Member[covered.size()];
                Member[] ncm = new Member[notCovered.size()];
                covered.toArray(m);
                notCovered.toArray(ncm);
                Arrays.sort(m, comp);
                Arrays.sort(ncm, comp);
                out.println("   " + (i + 1) + "). Class " + c[i].getName()
                    + ": Tested methods: " + m.length + ". Total: " + total
                    + ". Coverage: "
                    + (int) (100 * ((float) m.length / (float) total)) + "%.");

                if (total != m.length) {
                    out.println("       Covered methods: ");
                    for (int n = 0; n < m.length; n++) {
                        out.println("           " + (n + 1) + ").   "
                            + methodToString(m[n]));
                    }

                    out.println("       Not covered methods: ");
                    for (int n = 0; n < ncm.length; n++) {
                        out.println("           " + (n + 1) + ").   "
                            + methodToString(ncm[n]));
                    }
                }
            }
        }

        // Print errors.
        int esize = errors.size();
        if (esize != 0) {
            if (printNL) {
                out.print(nl);
            }
            out.println("There was " + esize + " errors:");
            printResults(errors, out);
            printNL = true;
        }

        // Print failures.
        Vector failures = getResults(Result.FAIL);
        int fsize = failures.size();
        if (fsize != 0) {
            if (printNL) {
                out.print(nl);
            }
            out.println("There was " + fsize + " failures:");
            printResults(failures, out);
            printNL = true;
        }

        // Print total.
        String total = "Test run: " + count;
        if (esize != 0) {
            total += ", Errors: " + esize;
        }

        if (fsize != 0) {
            total += ", Failures: " + fsize;
        }

        total += ".";

        int ssize = skipped.size();
        if (ssize != 0) {
            total += "\nSkipped: " + ssize;
        }

        String strRes = result == Result.FAIL ? "FAILED!!!"
            : result == Result.ERROR ? "ERROR!!!" : "PASSED";

        total += "\nTime: " + (float) time / 1000 + " c\nTest result: "
            + strRes;

        if (printNL) {
            out.print(nl);
        }
        out.println(total);
    }

    /**
     * Returns an array, containing the tested classses.
     * 
     * @return an array, containing the tested classses.
     */
    private Class[] getTestedClasses() {
        Vector v = new Vector();
        int size = testedMethods.size();
        for (int i = 0; i < size; i++) {
            Member m = (Member) testedMethods.get(i);
            Class c = m.getDeclaringClass();
            if (!v.contains(c)) {
                v.add(c);
            }
        }

        Class[] c = new Class[v.size()];
        v.toArray(c);
        Arrays.sort(c, new Comparator() {
            public int compare(Object o1, Object o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
        return c;
    }

    /**
     * Process coverage.
     * 
     * @param c tested class.
     * @param covered covered methods.
     * @param notCovered not covered methods.
     * @return total number of public or protected methods of the class <b>c</b>
     */
    private int coverage(Class c, Vector covered, Vector notCovered) {
        Member[] dc = c.getDeclaredConstructors();
        Member[] dm = c.getDeclaredMethods();
        Member[] m = new Member[dc.length + dm.length];
        System.arraycopy(dc, 0, m, 0, dc.length);
        System.arraycopy(dm, 0, m, dc.length, dm.length);
        int count = 0;
        for (int i = 0; i < m.length; i++) {
            int mod = m[i].getModifiers();
            if (Modifier.isPublic(mod) || Modifier.isProtected(mod)) {
                count++;
                if (testedMethods.contains(m[i])) {
                    covered.add(m[i]);
                } else {
                    notCovered.add(m[i]);
                }
            }
        }

        return count;
    }

    /**
     * String representation of the method or onstructor.
     * 
     * @param m
     * @return
     */
    String methodToString(Member m) {
        String s = "";
        s += Modifier.toString(m.getModifiers());
        Class[] t = null;
        Class[] ex = null;
        if (m instanceof Method) {
            Method dm = (Method) m;
            s += " " + dm.getReturnType().getName();
            t = dm.getParameterTypes();
            ex = dm.getExceptionTypes();
        } else {
            Constructor dc = (Constructor) m;
            t = dc.getParameterTypes();
            ex = dc.getExceptionTypes();
        }

        s += " " + m.getName() + "(" + classesToString(t) + ")";

        if (ex.length != 0) {
            s += " throws " + classesToString(ex);
        }

        return s.replaceAll("java.lang.", "").trim();
    }

    /**
     * Returns comma separated list of class names.
     * 
     * @param c
     * @return
     */
    private String classesToString(Class[] c) {
        String s = "";
        for (int i = 0; i < c.length; i++) {
            if (c[i].isArray()) {
                String str = c[i].getName();
                int n = 0;
                for (int ind = -1; (ind = str.indexOf('[')) != -1; n++) {
                    str = str.substring(ind + 1);
                }

                if (str.startsWith("L") && str.endsWith(";")) {
                    s += str.substring(1, str.length() - 1);
                } else {
                    s += str.equals("Z") ? "boolean" : str.equals("B") ? "byte"
                        : str.equals("C") ? "char" : str.equals("D") ? "double"
                            : str.equals("F") ? "float" : str.equals("I")
                                ? "int" : str.equals("J") ? "long" : "short";
                }

                for (; n > 0; n--) {
                    s += "[]";
                }
            } else {
                s += c[i].getName();
            }

            if (i != c.length - 1) {
                s += ", ";
            }
        }
        return s;
    }

    /**
     * Print results.
     * 
     * @param res list containing Result objects.
     */
    private void printResults(List res, PrintStream out) {
        for (int i = 0; i < res.size(); i++) {
            Result r = (Result) res.get(i);
            String fname = "";
            String msg = r.getMessage();

            if (r.getFileName() != null) {
                fname = r.getFileName();
                if (r.getLineNumber() >= 0) {
                    fname += ":" + r.getLineNumber();
                }
            }

            if (msg == null) {
                msg = "";
            } else {
                msg = "\n            " + msg.replaceAll("\n", "\n            ");
            }

            out.println("    " + (i + 1) + "). [" + r.getMethodName() + "] "
                + r.getClassName() + "." + r.getMethodName() + "(" + fname
                + "):" + msg);
        }
    }

    /**
     * Returns the Rresult objects, whose result is equal to <b>res</b>.
     * 
     * @param res result.
     * @return the Rresult objects, whose result is equal to <b>res</b>.
     */
    private Vector getResults(int res) {
        Vector v = new Vector();
        if (results == null) {
            return v;
        }

        int size = results.size();
        for (int i = 0; i < size; i++) {
            Result r = (Result) results.get(i);
            if (r.getResult() == res) {
                v.add(r);
            }
        }

        return v;
    }
}
