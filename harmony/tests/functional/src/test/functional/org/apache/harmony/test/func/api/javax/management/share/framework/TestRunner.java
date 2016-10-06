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

import java.util.List;
import java.util.Vector;

/**
 */
public class TestRunner {

    /**
     * Run the test.
     * 
     * @param c Test class.
     * @return the constant, indicating the test execution result.
     * @throws Exception
     */
    public static int run(Class c) throws Exception {
        return run(c, new String[0]);
    }

    /**
     * Run the test.
     * 
     * @param t Test object instance.
     * @return the constant, indicating the test execution result.
     */
    public static int run(Test t) {
        return run(t, new String[0]);
    }

    /**
     * Run the test.
     * 
     * @param c Test class.
     * @param args command line arguments.
     * @return the constant, indicating the test execution result.
     * @throws Exception
     */
    public static int run(Class c, String[] args) {
        try {
            Test t = (Test) c.newInstance();
            return run(t, args);
        } catch (Throwable ex) {
            ex.printStackTrace();
            Vector v = new Vector();
            v.add(new Result(Result.ERROR, ex,
                new Throwable().getStackTrace()[0]));
            TestResult res = new TestResult(v, new Vector(), new Vector(), 0, 0);
            res.printReport();
            return res.getResult();
        }
    }

    /**
     * Run the test.
     * 
     * @param t Test object instance.
     * @param args command line arguments.
     * @return the constant, indicating the test execution result.
     */
    public static int run(Test t, String[] args) {
        TestResult result = t.run(args);
        result.printReport();
        return result.getResult();
    }

    /**
     * Run the tests.
     * 
     * @param c test classes.
     * @param args args command line arguments.
     * @return the constant, indicating the test execution result.
     */
    public static int run(Class[] c, String[] args) {
        TestResult result = new TestResult();
        for (int i = 0; i < c.length; i++) {
            try {
                Object o = c[i].newInstance();
                if (o instanceof Test) {
                    TestResult r = ((Test) o).run(args);
                    result.addTestResult(r);
                }
            } catch (Throwable ex) {
            }
        }

        result.printReport();
        return result.getResult();
    }

    /**
     * Run the tests.
     * 
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        String pkg = getArg("-p", args);
        String classes = getArg("-c", args);
        String skip = getArg("-skip", args);
        Class[] c = new Class[0];
        if (pkg != null) {
            c = Utils.getClasses(pkg);
            args = removeValues(removeValues(args, "-p"), pkg);
        } else if (classes != null) {
            List cl = Utils.getCommaSeparatedTokens(classes);
            Vector v = new Vector();
            int size = cl.size();
            for (int i = 0; i < size; i++) {
                try {
                    v.add(Class.forName(cl.get(i).toString()));
                } catch (ClassNotFoundException e) {
                }
            }
            c = new Class[v.size()];
            v.toArray(c);
        }

        if (skip != null) {
            List s = Utils.getCommaSeparatedTokens(skip);
            Vector v = new Vector();
            int size = s.size();
            for (int i = 0; i < c.length; i++) {
                String cname = c[i].getName();
                boolean b = true;
                for (int n = 0; n < size; n++) {
                    if (cname.startsWith(s.get(n).toString())) {
                        b = false;
                        break;
                    }
                }
                if (b) {
                    v.add(c[i]);
                }
            }
            c = new Class[v.size()];
            v.toArray(c);
        }
        run(c, args);
    }

    private static String getArg(String name, String[] args) {
        if (name == null) {
            return null;
        }
        for (int i = 0; i < args.length - 1; i++) {
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

    private static String[] removeValues(String[] args, String value) {
        Vector v = new Vector();
        for (int i = 0; i < args.length; i++) {
            if (!value.equals(args[i])) {
                v.add(args[i]);
            }
        }

        args = new String[v.size()];
        v.toArray(args);
        return args;
    }
}
