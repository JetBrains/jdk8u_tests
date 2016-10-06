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
 * @version $Revision: 1.23 $
 */
package org.apache.harmony.harness.MCore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.apache.harmony.harness.ConfigIR;
import org.apache.harmony.harness.Constants;
import org.apache.harmony.harness.MessageInfo;
import org.apache.harmony.harness.ProcDestroy;
import org.apache.harmony.harness.RRunner;
import org.apache.harmony.harness.TResIR;
import org.apache.harmony.harness.TestIR;

public class Main {

    private final String          classID               = "MCMain";

    public static final boolean   SERVER                = true;
    public static final boolean   CLIENT                = false;
    static final int              STATUS_PASSED         = 100;
    static final int              STATUS_FAILED         = 101;
    public static final int       TESTS_UNLIMITED       = 0;

    /* values to return as execution status of received command execution */
    static final Integer          OK                    = new Integer(
                                                            STATUS_PASSED);
    static final Integer          FAILED                = new Integer(
                                                            STATUS_FAILED);

    /* variables to synchronized with the test execution Thread */
    protected static volatile int curSynStatus          = 0;
    static final int              SYN_STATUS_STARTED    = 100;
    static final int              SYN_STATUS_STARTED_OK = 101;
    static final int              SYN_STATUS_WORK       = 200;
    static final int              SYN_STATUS_WORK_OK    = 201;
    static final int              SYN_STATUS_FINISH     = 300;
    static final int              SYN_STATUS_FINISH_OK  = 301;

    static final String[]         MAP_FILE_COMMENTS     = { "//", "*", " " };

    /* the default logger to work in the same vm mode */
    public static final String    SAME_MODE_LOGGER_NAME = "org.apache.harmony.harness.MCore.MCOutLogger";
    //"org.apache.harmony.harness.MCore.MCConsLogger";
    //"org.apache.harmony.harness.MCore.MCFileLogger";

    private static Main           curMain;

    /* the unique ID of this mcore */
    private int                   mcoreID               = 0;

    /*
     * timeout for current test. After the timeout the harness will try to stop
     * the test execution Thread
     */
    protected int                 curTimeout            = 0;
    protected int                 testNumberBeforeExit  = TESTS_UNLIMITED;
    protected int                 currentTestNumber     = 0;
    protected MCOutStream         myOut                 = new MCOutStream();
    protected MCOutStream         myErr                 = new MCOutStream();

    private ConfigIR              cfg;
    private ArrayList             extProcs              = new ArrayList();

    private Logger                log;
    private FileHandler           logH                  = null;
    private ActivityReporter      activReporter;

    /*
     * info = 1, warning = 2, severe = 3 Note, if level > 0 this class may hang
     */
    private int                   logLevel              = 0;

    private Socket                sock;
    private ServerSocket          ssock;
    private InputStream           inS;
    private OutputStream          outS;
    private ObjectInputStream     ois;
    private ObjectOutputStream    oos;
    private int                   port                  = 5678;

    // null is equivalent of "localhost";
    private String                hostName              = null;
    private boolean               mode                  = CLIENT;

    private volatile boolean      shutdown;
    private volatile boolean      shutdownEnable        = true;
    private ArrayList             tmpList               = new ArrayList();
    private ArrayList             mapList               = new ArrayList();

    private InetAddress           rInetAddr             = null;

    /*
     * open the connection to the root harness
     */
    boolean open() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\topen(): ";
        try {
            if (mode == CLIENT) {
                log.config(methodLogPrefix + "before socket create: OK");
                logFlush();
                while (sock == null) {
                    try {
                        sock = new Socket(hostName, port);
                    } catch (Exception e) {
                        log.info(methodLogPrefix
                            + "Can not connect to server: " + e);
                        logFlush();
                    }
                }
            } else {
                log.config(methodLogPrefix + "before server socket create: OK");
                logFlush();
                ssock = new ServerSocket(port);
                sock = ssock.accept();
            }
            inS = sock.getInputStream();
            outS = sock.getOutputStream();
            oos = new ObjectOutputStream(outS);
            oos.flush();
            ois = new ObjectInputStream(inS);
            log.config(methodLogPrefix + "End open: OK");
            logFlush();
            rInetAddr = sock.getInetAddress();
            if (rInetAddr.isLoopbackAddress()) {
                rInetAddr = InetAddress.getLocalHost();
            }
            log.log(Level.INFO, methodLogPrefix + "Host to report: "
                + rInetAddr.toString());
            return true;
        } catch (Exception e) {
            log.log(Level.SEVERE, methodLogPrefix + MessageInfo.UNEX_EXCEPTION,
                e);
            logFlush();
            return false;
        }
    }

    /*
     * close all outputs and exit
     */
    void close() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tclose(): ";
        if (sock != null) {
            try {
                sock.shutdownInput();
                sock.shutdownOutput();
                sock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (ssock != null) {
            try {
                ssock.close();
            } catch (IOException e) {
                log.log(Level.SEVERE, methodLogPrefix
                    + MessageInfo.UNEX_EXCEPTION, e);
                logFlush();
            }
        }
        for (int i = 0; i < extProcs.size(); i++) {
            Process proc = (Process)(extProcs.get(i));
            InputStream err = proc.getErrorStream();
            InputStream in = proc.getInputStream();
            try {
                byte[] tmpIn = new byte[in.available()];
                in.read(tmpIn, 0, tmpIn.length);
                byte[] tmpErr = new byte[err.available()];
                err.read(tmpErr, 0, tmpErr.length);
                new ProcDestroy((Process)extProcs.get(i)).start();
            } catch (Exception e) {
                log.log(Level.SEVERE, methodLogPrefix
                    + MessageInfo.UNEX_EXCEPTION, e);
                logFlush();
            }
        }
        if (logH != null) {
            logH.flush();
            logH.close();
        }
    }

    /*
     * return the status of socket
     */
    boolean isOpen() {
        if (sock != null) {
            return sock.isConnected();
        }
        return false;
    }

    public static Main getCurCore() {
        return curMain;
    }

    public ConfigIR getConfig() {
        return cfg;
    }

    public Logger getCurOut() {
        return log;
    }

    public void addExtProcs(Process proc) {
        extProcs.add(proc);
    }

    public int getMCoreID() {
        return mcoreID;
    }

    public String getSameModeLoggerName() {
        return SAME_MODE_LOGGER_NAME;
    }

    private void logFlush() {
        if (log != null) {
            Handler[] handl = log.getHandlers();
            for (int i = 0; i < handl.length; i++) {
                handl[i].flush();
            }
        }
    }

    /*
     * wait for data forever. Note, the timeout is implemented in the 'big'
     * harness.
     */
    private void waitForReplay() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\twaitForReplay(): ";

        log.info(methodLogPrefix + "wait for replay: OK");
        logFlush();
        try {
            while (inS.available() == 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    //do nothing
                }
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, methodLogPrefix + MessageInfo.UNEX_EXCEPTION,
                e);
            logFlush();
        }
    }

    /*
     * convert the ArrayList of Strings to one String (to add to log for
     * example)
     */
    private String convertArrarListtoString(ArrayList data) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tconvertArrarListtoString(): ";
        String tmpStr = "";
        try {
            for (int i = 0; i < data.size(); i++) {
                tmpStr = tmpStr + data.get(i).toString() + "\n";
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, methodLogPrefix + MessageInfo.UNEX_EXCEPTION,
                e);
            logFlush();
        }
        return tmpStr;
    }

    /*
     * send data to harness
     */
    private boolean send(ArrayList data) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tsend(): ";
        log.config(methodLogPrefix + "OK");
        log.config(convertArrarListtoString(data));
        logFlush();
        if (sock != null) {
            try {
                oos.writeUnshared(data);
                oos.flush();
                return true;
            } catch (IOException e) {
                log.log(Level.SEVERE, methodLogPrefix
                    + MessageInfo.UNEX_EXCEPTION, e);
                logFlush();
            }
        }
        return false;
    }

    /*
     * receive data from harness
     */
    private ArrayList receive() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\treceive(): ";
        log.config(methodLogPrefix + "OK");
        logFlush();
        if (sock != null) {
            try {
                return (ArrayList)ois.readObject();
            } catch (IOException e) {
                log.log(Level.SEVERE, methodLogPrefix
                    + MessageInfo.UNEX_EXCEPTION, e);
                logFlush();
            } catch (ClassNotFoundException e) {
                log.log(Level.SEVERE, methodLogPrefix
                    + MessageInfo.UNEX_EXCEPTION, e);
                logFlush();
            }
        }
        return null;
    }

    public int runCmdOther(String[] cmdarray) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\trunCmdOther(): ";
        log.config(methodLogPrefix + "OK");
        logFlush();
        int execStat;
        Process proc;
        try {
            proc = Runtime.getRuntime().exec(cmdarray);
            addExtProcs(proc);
            return STATUS_PASSED;
        } catch (IOException e) {
            log.log(Level.SEVERE, methodLogPrefix + MessageInfo.UNEX_EXCEPTION,
                e);
            logFlush();
            return STATUS_FAILED;
        }
    }

    /**
     * Calculate time for execution in the time slices (100 ms == 1 slice)
     * 
     * @param testTime - tests timeout factor
     */
    protected void calcTimeout(float testTime) {
        int time = (int)(testTime * cfg.getGenTimeout() * 10);
        if (time == 0) {
            curTimeout = cfg.getGenTimeout() * 10; //testTime == 1 by default
        } else {
            curTimeout = time;
        }
    }

    public TResIR execTest(String clas, TestIR test) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\texecTest(): ";
        int cnt;
        byte[] testOut;
        String hostInetAddr;
        log.config(methodLogPrefix + "OK");
        logFlush();
        TResIR result = null;
        Object synchronizedObj = new Object();

        //create thread to execute the test in the proper thread group
        ThreadGroup testThGroup = new ThreadGroup(test.getTestID());
        RunOneTest rt = new RunOneTest(clas, test, synchronizedObj,
            testThGroup, log);

        //wait for test execution
        calcTimeout(test.getTestTimeout());
        synchronized (synchronizedObj) {
            curSynStatus = 0;
            rt.start();
        }
        log.config(methodLogPrefix + "Test ran: " + test.getTestID()
            + "\ttest timeout: " + test.getTestTimeout() + "\trunner: " + clas
            + "\tparameters: " + test.getRunnerParam());
        //the execution unit will wait test not more than 1 timeout
        //reserve the second timeout for killing thread/ process in
        //emergency case
        for (cnt = (int)(curTimeout * 1.3f); cnt > 0; cnt--) {
            if (curSynStatus != SYN_STATUS_FINISH) {
                try {
                    Thread.sleep(100);//synchronizedObj.wait(100);
                } catch (InterruptedException e) {
                    //do nothing
                }
            } else {
                break;
            }
        }
        synchronized (synchronizedObj) {
            result = rt.getResult();
            curSynStatus = SYN_STATUS_FINISH_OK;
            log.config(methodLogPrefix + "Result is: " + result
                + "\tcurTimeout = " + curTimeout + "\tcnt = " + cnt);
        }
        log.config(methodLogPrefix + "Test: " + test.getTestID() + " run OK");
        //analyze result of test run
        if (result != null) {
            log.config(methodLogPrefix + "status is " + result.getExecStat());
            //to drop all thread variable to common memory
            synchronized (synchronizedObj) {
                curSynStatus = SYN_STATUS_FINISH_OK;
                synchronizedObj.notifyAll();
            }
            //we have a result of execution.
            //just wait some time to execution thread is terminated
            try {
                //it is a part of the GenTimout (10%, in slices)
                cnt = cfg.getGenTimeout();
                while (cnt-- != 0) {
                    if (rt.isAlive()) {
                        rt.join(100);
                    } else {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                //do nothing
            }
        } else {
            //the test is hang. Try to terminate the test thread
            log.info(methodLogPrefix + "result is null");
            curSynStatus = SYN_STATUS_FINISH_OK;
            rt.interrupt();
            Thread.yield();
            testThGroup.interrupt();
            Thread.yield();
            testThGroup.interrupt();
            Thread.yield();
            // testThGroup.destroy(); //But BEA crashed with
            // NoSuchMethodError from java.lang.Thread.destroy()V(Unknown
            // Source) ?!?
            try {
                hostInetAddr = ", host "
                    + InetAddress.getLocalHost().toString();
            } catch (UnknownHostException e) {
                hostInetAddr = "";
            }
            result = new TResIR(System.getProperty("os.name") + " (version "
                + System.getProperty("os.version") + ")", System
                .getProperty("os.arch")
                + hostInetAddr, System.getProperty("java.vm.name"), test
                .getTestID());
            result.setOutMsg(methodLogPrefix
                + "timeout!!!. The test hung (not finished in " + curTimeout
                / 10 + " second).\n");
            result.setExecStat(cfg.getRepError()[0]);
            result.setRepFile(test.getTestID());
        }
        //result must be non null. Store the test error stream to the result
        if (result != null && myErr != null) {
            cnt = myErr.available();
            if (cnt > 0) {
                testOut = new byte[cnt];
                for (int i = 0; i < testOut.length; i++) {
                    testOut[i] = myErr.read();
                }
                try {
                    result.setOutMsg(new String(testOut, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    result.setOutMsg(methodLogPrefix
                        + "Test output is not UTF-8 compatible");
                    result.setOutMsg(new String(testOut));
                }
            }
        }
        //result must be non null. Store the test output stream to the result
        if (result != null && myOut != null) {
            cnt = myOut.available();
            if (cnt > 0) {
                testOut = new byte[cnt];
                for (int i = 0; i < testOut.length; i++) {
                    testOut[i] = myOut.read();
                }
                try {
                    result.setTestSpecificInfo(new String(testOut, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    result.setTestSpecificInfo(methodLogPrefix
                        + "Test output is not UTF-8 compatible");
                    result.setTestSpecificInfo(new String(testOut));
                }
            }
        }
        return result;
    }

    ArrayList processData(ArrayList data) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tprocessData(): ";
        if (data == null || data.size() == 0) {
            return null;
        }
        String cmd = (String)data.get(0);
        int execStat = STATUS_FAILED;
        tmpList.clear();
        tmpList.add("Status");
        log.info(methodLogPrefix + "cmd size is " + data.size());
        logFlush();
        log.info("cmd is " + convertArrarListtoString(data));
        logFlush();
        if ("isalive".equalsIgnoreCase(cmd)) {
            log.info(methodLogPrefix + "isAlive cmd: OK");
            logFlush();
            tmpList.add("isAlive");
        } else if ("terminate".equalsIgnoreCase(cmd)) {
            log.info("terminate cmd: OK");
            logFlush();
            tmpList.add(OK);
            if (shutdownEnable == true) {
                shutdown = true;
                if (activReporter != null) {
                    activReporter.stopIt();
                }
            }
        } else if ("getID".equalsIgnoreCase(cmd)) {
            log.info(methodLogPrefix + "OK");
            logFlush();
            tmpList.add(OK);
            tmpList.add(new Integer(mcoreID));
        } else if ("setcfg".equalsIgnoreCase(cmd)) {
            log.info(methodLogPrefix + "setcfg cmd: OK");
            logFlush();
            if (data.size() < 2) {
                tmpList.add(FAILED);
            } else {
                cfg = (ConfigIR)data.get(1);
                if (mapList.size() > 0) {
                    cfg.setMapsList(mapList);
                }
                tmpList.add(OK);
                log.info(methodLogPrefix + "setcfg cmd: cfg is " + cfg);
            }
            System.setProperty("java.class.path", cfg.getTestSuiteClassRoot()
                + File.pathSeparator + System.getProperty("java.class.path"));
            int monitorPort = RRunner.MONITOR_PORT;
            try {
                monitorPort = Integer.parseInt(cfg.getMCMonitorPort());
            } catch (Exception e) {
                log.config(methodLogPrefix
                    + "Can't read monitor port settings. Use default port: "
                    + RRunner.MONITOR_PORT);
            }
            activReporter = new ActivityReporter(rInetAddr, monitorPort,
                mcoreID, log);
            log.info(methodLogPrefix + "Start reporting for " + mcoreID
                + ", to " + rInetAddr + ":" + monitorPort);
            activReporter.start();
        } else if ("exec".equalsIgnoreCase(cmd)) {
            log.config(methodLogPrefix + "exec cmd: OK");
            logFlush();
            if (data.size() < 2) {
                tmpList.add(FAILED);
            } else {
                String[] param = null;
                param = new String[data.size() - 1];
                for (int i = 2; i < data.size(); i++) {
                    param[i - 2] = (String)data.get(i);
                }
                execStat = runCmdOther(param);
                tmpList.add(OK);
                tmpList.add(new Integer(execStat));
            }
        } else if ("test".equalsIgnoreCase(cmd)) {
            log.config(methodLogPrefix + "test cmd: OK");
            logFlush();
            if (data.size() < 3) {
                tmpList.add(FAILED);
            } else {
                TResIR result = execTest((String)data.get(1), (TestIR)data
                    .get(2));
                tmpList.add(OK);
                tmpList.add(result);
            }
            currentTestNumber++;
            if (testNumberBeforeExit != TESTS_UNLIMITED) {
                if (currentTestNumber >= testNumberBeforeExit) {
                    shutdown = true;
                    if (activReporter != null) {
                        activReporter.stopIt();
                    }
                }
            }
        }
        log.config(methodLogPrefix + " end: OK");
        return tmpList;
    }

    /**
     * Checks that the file with the given name exist in the file system.
     * 
     * @param value - directory name
     * @return - true if file exist, otherwise false
     */
    public static boolean checkExistFile(String value) {
        try {
            if ((new File(value)).isFile()) {
                return true;
            }
        } catch (Exception e) {
            //do nothing
        }
        return false;
    }

    boolean startwithComments(String data) {
        for (int i = 0; i < MAP_FILE_COMMENTS.length; i++) {
            if (data.startsWith(MAP_FILE_COMMENTS[i])) {
                return true;
            }
        }
        return false;
    }

    ArrayList readMapFromFile(String fileName) {
        ArrayList tmpStore = new ArrayList();
        try {
            FileInputStream in = new FileInputStream(fileName);
            int size = in.available();
            int index = 0;
            byte[] data = new byte[size];
            in.read(data);
            String tmpData = new String(data);
            StringTokenizer st = new StringTokenizer(tmpData, System
                .getProperty("line.separator"));
            int iter = st.countTokens();
            for (int cnt = 0; cnt < iter; cnt++) {
                String tmp = st.nextToken();
                if (tmp.length() > 1 && startwithComments(tmp) != true) {
                    StringTokenizer mapSt = new StringTokenizer(tmp);
                    int mapIter = mapSt.countTokens();
                    if (mapIter >= 2) {
                        tmpStore.add(mapSt.nextToken()); //from
                        tmpStore.add(mapSt.nextToken()); //to
                    }
                }
            }
        } catch (IOException e) {
            //do nothing
        }
        return tmpStore;
    } /*
       * convert the string "value1 value2 ..." to ArrayList of string
       * {"value1", "value2"}
       */

    ArrayList processMapsValues(String data) {
        ArrayList tmpValue = new ArrayList();
        String tmp;
        if (data == null) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(data);
        int iter = st.countTokens();
        for (int cnt = 0; cnt < iter; cnt++) {
            tmp = st.nextToken();
            if (checkExistFile(tmp) != true) {
                tmpValue.add(tmp); //add by pairs only and first may be file !
                tmpValue.add(st.nextToken());
                cnt++;
            } else {
                ArrayList tmpArr = readMapFromFile(tmp);
                for (int i = 0; i < tmpArr.size(); i++) {
                    tmpValue.add(tmpArr.get(i));
                }
            }
        }
        return tmpValue;
    }

    void parseArgs(Logger log, String[] params) {
        final String helpMsg = "Usage:\n-server\twait for connection\n"
            + "-client\ttry to connect (default)\n"
            + "-port <int>\t\tset port number (default is 5678)\n"
            + "-host <name>\thost name\n"
            + "-id <int>\tunique id to communicate with harness\n"
            + "-noshutdown <name>\tignore the request to shutdown from harness\n"
            + "-testnumber <int>\tnumbers of tests to run before exit. Value '0' - unlimited\n"
            + "-help\tthis message\n"
            + "-map \"value newvalue ...\"\tlist of files or pairs to replace in the paths (host -> mcore)\n";

        try {
            for (int i = 0; i < params.length; i++) {
                if ("-port".equals(params[i])) {
                    try {
                        port = Integer.parseInt(params[++i]);
                    } catch (NumberFormatException e) {
                        log.info("Invalid port number. Use default " + port);
                    }
                } else if ("-server".equalsIgnoreCase(params[i])) {
                    mode = SERVER;
                } else if ("-client".equalsIgnoreCase(params[i])) {
                    mode = CLIENT;
                } else if ("-host".equalsIgnoreCase(params[i])) {
                    hostName = params[++i];
                } else if ("-logl".equalsIgnoreCase(params[i])) {
                    logLevel = Integer.parseInt(params[++i]);
                } else if ("-id".equalsIgnoreCase(params[i])) {
                    mcoreID = Integer.parseInt(params[++i]);
                } else if ("-noshutdown".equalsIgnoreCase(params[i])) {
                    shutdownEnable = false;
                } else if ("-testnumber".equalsIgnoreCase(params[i])) {
                    try {
                        testNumberBeforeExit = Integer.parseInt(params[++i]);
                        if (testNumberBeforeExit <= 0) {
                            testNumberBeforeExit = TESTS_UNLIMITED;
                        }
                    } catch (NumberFormatException nfe) {
                        testNumberBeforeExit = TESTS_UNLIMITED;
                    }
                } else if ("-map".equalsIgnoreCase(params[i])) {
                    mapList = processMapsValues(params[++i]);
                } else {
                    System.out.println(helpMsg);
                }
            }
        } catch (Exception e) {
            System.out.println("Please, check parameters.\n" + helpMsg);
            System.exit(1);
        }
    }

    int run(Logger curout, String[] args) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\trun(): ";
        curMain = this;
        log = curout;
        PrintStream mOut = new PrintStream(myOut);
        PrintStream mErr = new PrintStream(myErr);
        PrintStream copyOfSystemOut = System.out;
        PrintStream copyOfSystemErr = System.err;
        try {
            parseArgs(curout, args);
            System.setErr(mErr);
            System.setOut(mOut);
            ArrayList data = new ArrayList();
            ArrayList toSend = new ArrayList();
            log.setUseParentHandlers(false);
            if (logLevel > 0) {
                try {
                    logH = new FileHandler(System.getProperty("user.dir")
                        + File.separator + "mcore" + mcoreID + "log");
                    logH.setFormatter(new MCFormatter());
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                log.addHandler(logH);
                switch (logLevel) {
                case 1:
                    log.setLevel(Level.CONFIG);
                    logH.setLevel(Level.CONFIG);
                    break;
                case 2:
                    log.setLevel(Level.INFO);
                    logH.setLevel(Level.INFO);
                    break;
                case 3:
                    log.setLevel(Level.WARNING);
                    logH.setLevel(Level.WARNING);
                    break;
                case 4:
                    log.setLevel(Level.SEVERE);
                    logH.setLevel(Level.SEVERE);
                default:
                    log.setLevel(Level.OFF);
                    logH.setLevel(Level.OFF);
                }
            }

            if (!open()) {
                log.log(Level.SEVERE, methodLogPrefix
                    + "can not open connection");
                return -1;
            }
            log.info("run MCore: OK");
            while (shutdown != true) {
                waitForReplay();
                data = receive();
                log.info(methodLogPrefix + "cmd received "
                    + convertArrarListtoString(data));
                logFlush();
                toSend = processData(data);
                send(toSend);
                data.clear();
                toSend.clear();
            }
            log.log(Level.INFO, methodLogPrefix + "shutdown OK");
        } catch (Throwable th) {
            log.log(Level.SEVERE, methodLogPrefix + MessageInfo.UNEX_EXCEPTION,
                th);
            logFlush();
        } finally {
            System.setErr(copyOfSystemErr);
            System.setOut(copyOfSystemOut);
            close();
        }
        return 0;
    }

    public static void main(String[] args) {
        System.exit(new Main().run(Logger.getAnonymousLogger(), args));
    }
}

class RunOneTest extends Thread {

    private final String classID = "RunOneTest";

    private String       className;
    private TestIR       curTest;
    private TResIR       curRes;
    private Object       synObj;
    private Logger       log;

    public void run() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\trun(): ";
        if (synObj != null) {
            synchronized (synObj) {
                Main.curSynStatus = Main.SYN_STATUS_WORK;
                synObj.notifyAll();
            }
            try {
                Class cl = Class.forName(className);
                Class[] param = new Class[1];
                param[0] = TestIR.class;
                Method m = cl.getMethod("execTest", param);
                Method sm = cl.getMethod("setCurTest", param);
                Object[] p = new Object[1];
                p[0] = curTest;
                Object wInstance = cl.newInstance();
                sm.invoke(wInstance, p);
                curRes = (TResIR)m.invoke(wInstance, p);
            } catch (InvocationTargetException e) {
                log.info(methodLogPrefix + "unexpected exception: "
                    + e.getTargetException());
            } catch (Exception e) {
                log.info(methodLogPrefix + "unexpected exception: " + e);
            }
            synchronized (synObj) {
                Main.curSynStatus = Main.SYN_STATUS_FINISH;
                synObj.notifyAll();
                try {
                    while (Main.curSynStatus != Main.SYN_STATUS_FINISH_OK) {
                        synObj.wait(100);
                    }
                } catch (InterruptedException e) {
                    //do nothing
                }
            }
        }
    }

    /*
     * return result by request
     */
    public TResIR getResult() {
        synchronized (synObj) {
            return curRes;
        }
    }

    /*
     * set up the test to run
     */
    public RunOneTest(String clas, TestIR test, Object synchronizedObj,
        ThreadGroup tgrp, Logger toLog) {
        super(tgrp, test.getTestID());
        className = clas;
        curRes = null;
        synObj = synchronizedObj;
        curTest = test;
        log = toLog;
    }
}

class MCFormatter extends Formatter {

    public static final String MSG_DELIMITER = "| ";

    /*
     * (non-Javadoc)
     * 
     * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
     */
    public String format(LogRecord record) {
        return record.getMillis() + MSG_DELIMITER + record.getMessage() + "\n";
    }
}

class ActivityReporter extends Thread {

    private final String   classID     = "ActivityReporter";

    protected Socket       monitorSock = null;
    protected String       data        = "";
    protected OutputStream out         = null;

    protected boolean      stop        = false;

    private Logger         log;

    public ActivityReporter(InetAddress sAddr, int portNumber, int mcID,
        Logger toLog) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tActivityReporter(): ";
        log = toLog;
        try {
            monitorSock = new Socket(sAddr, portNumber);
            out = monitorSock.getOutputStream();
            data = mcID + ": I'am OK";
            out.write(data.getBytes());
            log.info(methodLogPrefix + "Create socket OK " + monitorSock);
        } catch (Exception e) {
            monitorSock = null;
            log.warning(methodLogPrefix + MessageInfo.UNEX_EXCEPTION
                + "while create reporter: " + e);
        }
    }

    public void run() {
        while (!stop) {
            try {
                if (out != null && data != null) {
                    out.write(data.getBytes());
                }
                Thread.sleep(Constants.WAIT_TIME);
            } catch (Exception e) {
                //do nothing
            }
        }
    }

    public void stopIt() {
        stop = true;
    }
}