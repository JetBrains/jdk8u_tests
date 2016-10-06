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

package org.apache.harmony.test.func.api.share;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.harmony.share.DRLLogging;
import org.apache.harmony.share.LogFactory;

public abstract class InteractiveTest extends AutonomousTest {
    private final String          parentPackage            = "api";
    private final String          menuCaption              = "Yes or No";
    public static final int       CENTER                   = 0;
    public static final int       TOP_LEFT                 = 1;
    public static final int       BOTTOM_LEFT              = 2;
    public static final int       TOP_RIGHT                = 4;
    public static final int       BOTTOM_RIGHT             = 3;
    public static final String    NO_SCREENSHOT            = "__NO_SCREENSHOT__";

    private static int            currentAlignment         = InteractiveTest.TOP_RIGHT;

    /* 24 hours */
    final private long            UPPER_WAITING_TIME_BOUND = 86400;

    protected String              testDesc                 = "Description not present.";
    protected String              screenshot               = NO_SCREENSHOT;

    protected static final String fileName                 = "./__tempFile.tmp";
    /* value in seconds */
    private long                  waitingTime              = 150;
    private int                   methodCallCounter        = 0;
    private String                currentCaller            = "";
    
    DRLLogging                    log                      = new LogFactory()
                                                                   .getLogger();

    private static void alignCenter(Window wnd) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        /* Determine the new location of the window */
        int w = wnd.getSize().width;
        int h = wnd.getSize().height;
        int x = (screenSize.width - w) / 2;
        int y = (screenSize.height - h) / 2;

        /* Move the window */
        wnd.setLocation(x, y);
    }

    private static void alignTopLeftCorner(Window wnd) {
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(
                wnd.getGraphicsConfiguration());
        int x = screenInsets.left;
        int y = screenInsets.top;
        /* Move the window */
        wnd.setLocation(x, y);
    }

    private static void alignBottomLeftCorner(Window wnd) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(
                wnd.getGraphicsConfiguration());
        /* Determine the new location of the window */
        int h = wnd.getSize().height;
        int x = screenInsets.left;
        int y = screenSize.height - h - screenInsets.bottom;

        /* Move the window */
        wnd.setLocation(x, y);
    }

    private static void alignTopRightCorner(Window wnd) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(
                wnd.getGraphicsConfiguration());
        /* Determine the new location of the window */
        int w = wnd.getSize().width;
        int x = screenSize.width - w - screenInsets.right;
        int y = screenInsets.top;

        /* Move the window */
        wnd.setLocation(x, y);
    }

    private static void alignBottomRightCorner(Window wnd) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(
                wnd.getGraphicsConfiguration());
        /* Determine the new location of the window */
        int w = wnd.getSize().width;
        int h = wnd.getSize().height;
        int x = screenSize.width - w - screenInsets.right;
        int y = screenSize.height - h - screenInsets.bottom;

        /* Move the window */
        wnd.setLocation(x, y);
    }

    public static void setWindowAlignment(Window wnd, int alignment) {
        switch (alignment) {
            case CENTER :
                alignCenter(wnd);
                break;
            case TOP_LEFT :
                alignTopLeftCorner(wnd);
                break;
            case TOP_RIGHT :
                alignTopRightCorner(wnd);
                break;
            case BOTTOM_LEFT :
                alignBottomLeftCorner(wnd);
                break;
            case BOTTOM_RIGHT :
                alignBottomRightCorner(wnd);
                break;
            default :
                alignCenter(wnd);
        }
    }

    public static void setIneractiveWindowAlignment(int alignment) {
        currentAlignment = alignment;
    }

    public static int getIneractiveWindowAlignment() {
        return currentAlignment;
    }

    public void setScreenshot(String ss) {
        int lastDot = ss.lastIndexOf(".");
        /* remove extension if any */
        if (lastDot > 0) {
            ss = ss.substring(0, lastDot);
        }
        screenshot = ss;
    }

    public void setDescription(String desc) {
        testDesc = new String(desc);
    }

    public String getDescription() {
        return testDesc;
    }

    public void setTimeoutSeconds(long time) {
        if (time < 0 || time > UPPER_WAITING_TIME_BOUND) {
            throw new IllegalArgumentException(
                    "\nsetTimeoutSeconds() failed time has incorrect value. "
                            + "It should be greater then zero and less then "
                            + UPPER_WAITING_TIME_BOUND);
        }
        waitingTime = time;
    }

    public long getWaitingTime() {
        return waitingTime;
    }

    public static void setFailureReason(String reason) {

        /* delete file with old failure reason */
        if (new File(fileName).exists()) {
            new File(fileName).delete();
        }

        File failureReasonFile = new File("./", fileName);

        FileOutputStream fos = null;

        /* Create and open stream */
        try {
            fos = new FileOutputStream(failureReasonFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /* Write data and close stream */
        try {
            fos.write(reason.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFailureReason() {
        String failureReason = null;
        if (new File(fileName).exists()) {
            File failureReasonFile = new File("./", fileName);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(failureReasonFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            /* Read failure reason */
            try {
                int availableBytes = fis.available();
                byte[] b = new byte[availableBytes];
                fis.read(b);
                failureReason = new String(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return failureReason;
    }
  

    /**
     * We override this method in order to save the command line arguments
     */
    public int test(String[] args) {
        super.parseArgs(args);
        return super.test(args);
    }

    public boolean Asserting() {

        /* if executed by target implementation (harness) */
        if (cmdArgs.containsKey("-reference")) {

            /* Change currentCaller if necessary */
            checkIfCallerMethodChanged();

            try {

                /* Run test with reference implementation */
                Process proc = null;
                try {

                    /* Compose parameters to launch interactive test */
                    String caption = "\"" + menuCaption + "\"";
                    String desc = "\"" + testDesc + "\"";
                    String[] packages = this.getClass().getName().split("\\.");
                    String testedClass = getTestedClass(packages);
                    String time = waitingTime + "";
                    String caller = currentCaller;
                    String callCounter = methodCallCounter + "";
                    String alignment = InteractiveTest
                            .getIneractiveWindowAlignment()
                            + "";
                    String ss = screenshot;

                    /*
                     * Make array of string and pass it into
                     * Runtime#exec(String[])
                     */
                    String[] params = new String[]{
                            (String) cmdArgs.get("-reference"),
                            "-cp",
                            (String) cmdArgs.get("-usedCP"),
                            "org.apache.harmony.test.func.api.share.DialogConfirm",
                            "-caption", caption, "-desc", desc, "-testedclass",
                            testedClass, "-time", time, "-caller", caller,
                            "-callcounter", callCounter, "-alignment",
                            alignment, "-screenshot", ss};

                    /* Run DialogConfirm under reference JVM */
                    proc = Runtime.getRuntime().exec(params);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /*
                 * Wait for DialogConfirm window to close, this can happen in
                 * two cases either after user closes it or after timeout
                 * expires
                 */
                try {
                    proc.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int exitVal = proc.exitValue();

                /* if no screenshot was provided decrement methodCallCounter */
                if ((DialogConfirm.NO_IMAGE & exitVal) == DialogConfirm.NO_IMAGE) {
                    --methodCallCounter;
                }

                /* reset screenshot image name */
                screenshot = InteractiveTest.NO_SCREENSHOT;

                /* if the test has failed return false, otherwise true */
                if ((DialogConfirm.RESULT_FAILURE & exitVal) == DialogConfirm.RESULT_FAILURE) {
                    return false;
                } else if ((exitVal & 1) == DialogConfirm.RESULT_SUCCESS) {
                    return true;
                }

            } finally {

            }
        }
        return false;
    }
    public String getTestedClass(String[] packages) {
        String testedClass = "";
        boolean flag = false;
        /*
         * search for the api package (we do not include the test itself, this
         * is why 1 is subtracted)
         */
        for (int i = 0; i < packages.length - 1; i++) {
            /* when found */
            if (packages[i].equals(parentPackage)) {
                flag = true;
                continue;
            }
            /* join them into the string */
            if (flag) {
                testedClass += packages[i];
                if ((i + 1) < packages.length - 1) {
                    testedClass += "/";
                }
            }
        }
        return testedClass;
    }
    /**
     * Check if caller method has chaged "currentCaller" is assigned the new
     * method name and assignes zero to "methodCallCounter", otherwise
     * increments "methodCallCounter"
     */
    public void checkIfCallerMethodChanged() {
        try {
            throw new Exception();
        } catch (Exception e) {
            /*
             * stackTrace[0] - this method stackTrace[1] - Asserting()
             * stackTrace[2] - initial test method
             */
            StackTraceElement[] stackTrace = e.getStackTrace();
            /* We take 2nd element for the reason indicated above */
            if (currentCaller.equals(stackTrace[2].getMethodName())) {
                ++methodCallCounter;
            } else {
                currentCaller = stackTrace[2].getMethodName();
                methodCallCounter = 0;
            }
        }
    }
}