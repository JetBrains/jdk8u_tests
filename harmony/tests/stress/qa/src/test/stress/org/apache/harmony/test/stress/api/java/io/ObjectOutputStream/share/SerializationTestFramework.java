/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */    
/** 
 * @author Anton Luht
 * @version $Revision: 1.2 $
 */  
package org.apache.harmony.test.stress.api.java.io.ObjectOutputStream.share;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.harmony.share.Result;
import org.apache.harmony.share.Test;

public abstract class SerializationTestFramework extends Test {
    private static String REFERENCE_RUNTIME = "";

    private static String REFERENCE_RUNTIME_PARAMS = "";

    private static String TESTED_RUNTIME = "";

    private static String TESTED_RUNTIME_PARAMS = "";

    private static String REFERENCE_CLASSPATH = "";

    private static String TESTED_CLASSPATH = "";

    private static String TEMP_STORAGE = "";

    private static String VM_OPTION = "";

    private static int THREADS = 1; //can be set from config param or based on

    // command line arguments

    private static String MODE = "wrapper";

    private static ArrayList FILES = new ArrayList();

    private static int threadsAtBarrier = 0;

    private static Object barrier = new Object();

    private static final Random rnd = new Random();

    public static Random getRandom() {
        return rnd;
    }

    boolean isSerializationOnlyTest() {
        return false;
    }

    protected void parseArgs(String[] params) {
        if (params == null || params.length == 0) {
            return;
        }
        try {
            for (int i = 0; i < params.length; ++i) {
                if ("-reference".equalsIgnoreCase(params[i])) {
                    REFERENCE_RUNTIME = params[++i];
                } else if ("-tested".equalsIgnoreCase(params[i])) {
                    TESTED_RUNTIME = params[++i];
                } else if ("-temp".equalsIgnoreCase(params[i])) {
                    TEMP_STORAGE = params[++i];
                } else if ("-threads".equalsIgnoreCase(params[i])) {
                    try {
                        THREADS = Integer.parseInt(params[++i]);
                    } catch (Throwable e) {
                        THREADS = 1;
                    }
                } else if ("-usedCP".equalsIgnoreCase(params[i])) {
                    REFERENCE_CLASSPATH = TESTED_CLASSPATH = params[++i];
                } else if ("-bootCP".equalsIgnoreCase(params[i])) {
                    if (params[i + 1].startsWith("-X")) {
                        TESTED_RUNTIME_PARAMS = params[++i];
                    }
                } else if ("-generalVMOption".equalsIgnoreCase(params[i])) {
                    if (params[i + 1].startsWith("-")) {
                        VM_OPTION = params[++i];
                    }
                } else if ("-out".equalsIgnoreCase(params[i])) {
                    MODE = "out";
                    ++i;
                    for (; i < params.length && params[i].charAt(0) != '-'; ++i) {
                        FILES.add(params[i]);
                    }
                    if (i < params.length) {
                        --i;
                    }
                } else if ("-in".equalsIgnoreCase(params[i])) {
                    MODE = "in";
                    ++i;
                    for (; i < params.length && params[i].charAt(0) != '-'; ++i) {
                        FILES.add(params[i]);
                    }
                    if (i < params.length) {
                        --i;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Unexpected exception " + e);
        }
    }

    public int test(String[] args) {
        try {
            parseArgs(args);
            if ("out".equalsIgnoreCase(MODE)) {
                THREADS = FILES.size();
                if (THREADS == 1) {
                    return testOut(args[1]);
                }
                return new ThreadSerializationTestFramework(THREADS,
                        (String[]) FILES.toArray(new String[0]), this)
                        .testOut();

            }
            if ("in".equalsIgnoreCase(MODE)) {
                THREADS = FILES.size();
                if (THREADS == 1) {
                    return testIn(args[1]);
                }
                return new ThreadSerializationTestFramework(THREADS,
                        (String[]) FILES.toArray(new String[0]), this).testIn();
            }

            return testWrapper(args);
        } catch (Throwable e) {
            e.printStackTrace();
            return fail("unexpected exception in test execution: "
                    + e.getMessage());
        }
    }

    protected int testIn(String s) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(s);
        } catch (FileNotFoundException e) {
            return fail("can't open in file: " + e.getMessage());
        }

        int result = testIn(fis);
        try {
            fis.close();
        } catch (IOException e) {
            return fail("can't close in file: " + e.getMessage());
        }
        return result;
    }

    protected int testOut(String s) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(s);
        } catch (FileNotFoundException e) {
            return fail("can't open out file: " + e.getMessage());
        }
        int result = testOut(fos);
        try {
            fos.close();
        } catch (IOException e) {
            return fail("can't close out file: " + e.getMessage());
        }
        return result;
    }

    protected int testIn(InputStream is) {
        ObjectInputStream ois = null;
        Object o;
        try {
            ois = new ObjectInputStream(is);
            return testIn(ois);
        } catch (Throwable e) {
            //System.err.println("got exception");
            return fail("error reading object " + e.getMessage());
        } finally {
            try {
                ois.close();
            } catch (Throwable e) {
            }
        }
    }

    protected int testOut(OutputStream os) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(os);

            return testOut(oos);
        } catch (IOException e) {
            return fail("error writing string " + e.getMessage());
        } finally {
            try {
                oos.close();
            } catch (IOException e) {
            }
        }
    }

    protected abstract int testOut(ObjectOutputStream oos) throws IOException;

    protected abstract int testIn(ObjectInputStream ois) throws IOException,
            ClassNotFoundException;

    //calls this class's methods in other JVMS
    protected int testWrapper(String[] args) {
        File[] files = new File[THREADS];

        try {
            for (int i = 0; i < THREADS; i++) {
                files[i] = File.createTempFile("serialize", "", new File(
                        TEMP_STORAGE));
                files[i].deleteOnExit();
            }
        } catch (IOException e) {
            return fail("can't create tmp file in directory " + TEMP_STORAGE);
        }

        String returnText = "";

        try {
            ProcessResult pr = testSerialization(true, files);

            if (pr.exitCode != Result.PASS) {
                log.info("test VM serialization failed: " + pr.lastLine);
                return pr.exitCode;
            }

            returnText += "test serialization " + pr.lastLine + "\r\n";

            if (isSerializationOnlyTest()) {
                log.info(pr.lastLine);
                return pr.exitCode;
            }

            pr = testDeserialization(false, files);
            if (pr.exitCode != Result.PASS) {
                log.info("reference VM deserialization failed: " + pr.lastLine);
                return pr.exitCode;
            }

            returnText += "reference deserialization " + pr.lastLine + "\r\n";

            //don't run tests second time if VMs are equal
            if (!REFERENCE_CLASSPATH.equals(TESTED_CLASSPATH)
                    || !REFERENCE_RUNTIME.equals(TESTED_RUNTIME)
                    || !REFERENCE_RUNTIME_PARAMS.equals(TESTED_RUNTIME_PARAMS)) {
                pr = testSerialization(false, files);
                if (pr.exitCode != Result.PASS) {
                    log.info("reference VM serialization failed: "
                            + pr.lastLine);
                    return pr.exitCode;
                }
                returnText += "reference serialization " + pr.lastLine + "\r\n";

                pr = testDeserialization(true, files);
                if (pr.exitCode != Result.PASS) {
                    log.info("test VM deserialization failed: " + pr.lastLine);
                    return pr.exitCode;
                }
                returnText += "test deserialization " + pr.lastLine + "\r\n";
            }
        } finally {
            for (int i = 0; i < THREADS; i++) {
                files[i].delete();
            }
        }
        return pass(returnText);
    }

    protected ProcessResult testSerialization(boolean isTestedRuntime,
            File[] files) {
        return executeProcess(constructCommandArguments(isTestedRuntime,
                "-out", files));
    }

    protected ProcessResult testDeserialization(boolean isTestedRuntime,
            File[] files) {
        return executeProcess(constructCommandArguments(isTestedRuntime, "-in",
                files));
    }

    protected static ProcessResult executeProcess(String[] cmdLines) {
        Process p;
        String line, output = "";

        try {
            //System.err.println("going to execute " + joinArray(cmdLines));
            p = Runtime.getRuntime().exec(cmdLines);
            InputStream is = new SequenceInputStream(p.getErrorStream(), p
                    .getInputStream());
            BufferedReader input = new BufferedReader(new InputStreamReader(is));
            while ((line = input.readLine()) != null) {
                output += line + "\r\n";
            }
            input.close();
        } catch (IOException e) {
            return ProcessResult.fail("IOException in process execution");
        }

        try {
            p.waitFor();
        } catch (InterruptedException e) {
            return ProcessResult
                    .fail("InterruptedException in process execution");
        }

        //System.err.println("exit code: " + p.exitValue());

        return new ProcessResult(p.exitValue(), output);
    }

    //multi-threaded tests' threads should try to enter (de)serialization
    // section simultaneously
    //put a call of this method just before readObject() or writeObject() call.
    public static void waitAtBarrier() {
        if (THREADS <= 1)
            return;
        //        System.err.println(Thread.currentThread().getName() + " waiting at
        // the barrier, total threads to meet : " + THREADS);
        synchronized (barrier) {
            threadsAtBarrier++;
            if (threadsAtBarrier == THREADS) {
                barrier.notifyAll();
            } else
                while (threadsAtBarrier < THREADS) {
                    //                    System.err.println(Thread.currentThread().getName() + "
                    // here 1");
                    try {
                        //                        System.err.println(Thread.currentThread().getName() +
                        // " here 2");
                        barrier.wait();
                        //                        System.err.println(Thread.currentThread().getName() +
                        // " here 3");
                    } catch (InterruptedException e) {
                        System.err.println("thread interrupted"
                                + e.getMessage());
                    }
                    //                    System.err.println(Thread.currentThread().getName() + "
                    // here 4");
                }
        }
    }

    private static void addNonEmpty(Collection coll, String elem) {
        if (elem == null) {
            return;
        }
        elem = elem.trim();
        if (elem.length() == 0) {
            return;
        }
        coll.add(elem);
    }

    //constructs arguments to call this class in another runtime
    protected String[] constructCommandArguments(boolean isTestedRuntime,
            String switchParamValue, File[] files) {
        ArrayList cmdLines = new ArrayList();
        cmdLines.add(isTestedRuntime ? TESTED_RUNTIME : REFERENCE_RUNTIME);
        addNonEmpty(cmdLines, isTestedRuntime ? TESTED_RUNTIME_PARAMS
                : REFERENCE_RUNTIME_PARAMS);
        if (isTestedRuntime) { // add VM options to tested runtime
            StringTokenizer st = new StringTokenizer(VM_OPTION);
            while (st.hasMoreTokens()) {
                addNonEmpty(cmdLines, st.nextToken());
            }
        }
        cmdLines.add("-classpath");
        addNonEmpty(cmdLines, isTestedRuntime ? TESTED_CLASSPATH
                : REFERENCE_CLASSPATH);
        cmdLines.add(this.getClass().getName());
        cmdLines.add(switchParamValue);

        for (int i = 0; i < files.length; i++) {
            addNonEmpty(cmdLines, files[i].getAbsolutePath());
        }

        cmdLines.add("-threads");
        cmdLines.add("" + THREADS);

        return (String[]) cmdLines.toArray(new String[0]);
    }

    /**
     * @return random object that implements java.io.Serializable
     */
    public static Serializable generateRandomSerializableObject() {
        return generateRandomSerializableObject(getRandom().nextInt());
    }

    /**
     * @param i
     *            seed. consequent calls of this method with one seed return
     *            instances of one class (is convenient for filling comparable
     *            collections)
     * @return random object that implements java.io.Serializable
     */
    public static Serializable generateRandomSerializableObject(int i) {
        switch (Math.abs(i) % 3) {
        case 0:
            return new Long(getRandom().nextLong());
        case 1:
            return new java.util.Date(getRandom().nextLong() % 1000000000);
        case 2:
            return new Integer(getRandom().nextInt());
        }
        return null;
    }

    public int test() {
        log.info("this method shouldn't be called");
        return Result.FAIL;
    }

    private static String joinArray(String[] arr) {
        String s = "";

        for (int i = 0; i < arr.length; ++i) {
            if (i > 0) {
                s += '|';
            }
            s += arr[i];
        }
        return s;
    }
}