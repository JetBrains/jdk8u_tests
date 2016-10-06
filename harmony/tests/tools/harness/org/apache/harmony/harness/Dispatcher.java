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
 * @version $Revision: 1.38 $
 */
package org.apache.harmony.harness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

public class Dispatcher {

    private static final String classID             = "Dispatcher";

    public static final int     REPORTS_NUMBER      = 5;

    public static final int     TEST_UNDEFINED      = 1;
    public static final int     TEST_STARTED        = 10;
    public static final int     TEST_FINISHED       = 100;

    private Main                mMod                = Main.getCurCore();
    private Storage             store               = mMod.getStore();
    private Report              rp                  = mMod.getReporter();
    private Logging             log                 = mMod.getInternalLogger();
    /* the current configuration */
    protected ConfigIR          cfg                 = mMod.getConfigIR();
    protected ResourceManager   resourceMgr         = new ResourceManager();

    protected HashMap           currentlyExecuted   = new HashMap();

    private String              testResRoot         = cfg.getTestResultRoot();
    private int                 concurency          = cfg.getConcurency();
    private int                 allocNumber         = 0;

    private float               maxCurTimeoutFactor = 1.0f;
    private int                 curTimeoutCnt       = (int)(maxCurTimeoutFactor
                                                        * cfg.getGenTimeout() * Constants.INTERNAL_MULTIPLYER);

    private String              resExtension        = Main.getCurCore()
                                                        .getStore()
                                                        .getResultExtension();
    private StoreResThread      lastSRT;

    /**
     * Store the result of test run to external (file etc)
     * 
     * @param result - data to store
     */
    private synchronized void stoRes(TResIR result) {
        stoRes(result, true);
    }

    /**
     * Store the result of test run to external (file etc)
     * 
     * @param result - data to store
     * @param forceOverWrite - if true - always report result, if false - check
     *        for reported results and report only new results
     */
    private synchronized void stoRes(TResIR result, boolean forceOverWrite) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tstoRes(): ";
        log.add(Level.FINEST, methodLogPrefix + "Result is " + result);
        if (forceOverWrite) {
            lastSRT = new StoreResThread(store, result, testResRoot,
                resExtension);
            lastSRT.start();
            waitForStoRes(lastSRT);
        } else {
            HashMap resAll = rp.getResults();
            if (!resAll.containsKey(result.getTestID())) {
                lastSRT = new StoreResThread(store, result, testResRoot,
                    resExtension);
                lastSRT.start();
                waitForStoRes(lastSRT);
            }
        }
        Thread.yield();
    }

    private void waitForStoRes(StoreResThread lastSRT) {
        //wait until result will be stored. Note, dispatcher can store
        //results one-by-one
        if (lastSRT != null) {
            for (int i = (int)(mMod.getConfigIR().getGenTimeout() * 10); i != 0; i--) {
                try {
                    if (lastSRT.isFinished()) {
                        break;
                    }
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    //do nothing
                }
            }
        }

    }

    /**
     * Skip the test due to lack of resource to run the test or some other
     * reason. The report file not created.
     * 
     * @param test - test to skip
     */
    private synchronized void skipTest(TestIR test, String msg) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tskipTest(): ";
        log.add(Level.FINEST, methodLogPrefix + "Test to skip is " + test);
        if (test != null) {
            TResIR result = new TResIR(test.getTestID());
            result.setOutMsg(msg);
            result.setExecStat(cfg.getRepModErr()[0]);
            store.addToReportOnly(result);
        }
    }

    private synchronized boolean needSkipTest_Mode(TestIR test) {
        if (((mMod.getConfigIR().getExecM() == Main.OTHER) && (test
            .getSameVMOnly() == true))
            || ((mMod.getConfigIR().getExecM() == Main.SAME) && (test
                .getOtherVMOnly() == true))) {
            return true;
        }
        return false;
    }

    /**
     * Return the unique number for resource allocation
     * 
     * @return unique number
     */
    protected synchronized int getNextAllocNumber() {
        return allocNumber++;
    }

    /**
     * Delete the test from execution list, free resources and report status
     * 
     * @param repNumber - allocation resource id
     * @param result - data to report
     */
    synchronized void reportResult(int repNumber, TResIR result) {
        if (result != null) {
            synchronized (currentlyExecuted) {
                currentlyExecuted.remove(result.getTestID());
            }
            stoRes(result);
        }
        if (repNumber != Integer.MIN_VALUE) {
            resourceMgr.freeResources(repNumber);
        }
    }

    /**
     * Free resources with specified type name locked by executor with repNumber
     * 
     * @param repNumber - allocation resource id
     * @param type - name of resource type to free
     */
    synchronized void freeResourceByType(int repNumber, String type) {
        if (repNumber != Integer.MIN_VALUE) {
            resourceMgr.freeResources(repNumber, type);
        }
    }

    /**
     * Run tests on the local computer
     * 
     * @return true for successful run
     */
    public boolean runLocal() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\trunLocal(): ";
        ExecListIR eList = mMod.getExecList();
        int curTstCnt = 0;
        int resCntLogCurrent = 0;
        int reportTstCnt = eList.size() / REPORTS_NUMBER;
        int cnt;
        int grCnt;
        ExecThread runner;
        ExecUnit curRunner;
        log.add(Level.INFO, methodLogPrefix + Util.getCurrentTimeToLog()
            + " Run local. Test to execute: " + eList.size());
        if (reportTstCnt == 0) {
            reportTstCnt = 1;
        }
        while (eList.size() != 0) {
            if (curTimeoutCnt <= 0) {
                log
                    .add(
                        Level.WARNING,
                        methodLogPrefix
                            + "Test run was stopped: seems that all available resources locked."
                            + " Test remain to execute: " + eList.size());
                break;
            }
            cnt = getNextAllocNumber();
            curRunner = mMod.getRunner(eList.get().getRunnerID());
            grCnt = curRunner.groupCount(eList.get());
            String[] testResource = curRunner.needSpecialResource(eList.get());
            if (needSkipTest_Mode(eList.get())) {
                log
                    .add(
                        Level.INFO,
                        methodLogPrefix
                            + "According to the test description it does not support current "
                            + "configuration. Skipped.\n\t\t"
                            + eList.get().getTestID());
                skipTest(
                    eList.remove(),
                    methodLogPrefix
                        + "According to the test description it does not support current "
                        + "configuration. Skipped.");
            } else if (!resourceMgr.allocResourcePossible("",
                ResourceManager.EU_RESOURCE, 1)
                || !resourceMgr.allocResourcePossible(
                    eList.get().getRunnerID(), "", grCnt)
                || !resourceMgr.allocSpecialResourcePossible(testResource)) {
                log.add(Level.INFO, methodLogPrefix
                    + "Test was skipped due to lack of resources: "
                    + eList.get().getTestID());
                skipTest(eList.remove(),
                    "Test was skipped due to lack of resources");
            } else {
                if (cfg.getIntertestsTimeout() > 0) {
                    try {
                        Thread.sleep(cfg.getIntertestsTimeout());
                    } catch (InterruptedException e) {
                        //do nothing
                    }
                }
                if (resourceMgr.allocResources("", ResourceManager.EU_RESOURCE,
                    cnt, 1)
                    && resourceMgr.allocResources(eList.get().getRunnerID(),
                        "", cnt, grCnt)
                    && resourceMgr.allocSpecialResource(testResource, cnt)) {
                    TestIR test = curRunner
                        .updateTimeoutFromEnv(eList.remove());
                    maxCurTimeoutFactor = maxCurTimeoutFactor < test
                        .getTestTimeout() ? test.getTestTimeout()
                        : maxCurTimeoutFactor;
                    log.add(Level.FINEST, methodLogPrefix
                        + Util.getCurrentTimeToLog()
                        + " Run local. Test to run: "
                        + test
                        + ", thread to run "
                        + resourceMgr.getAllocResource("",
                            ResourceManager.EU_RESOURCE, cnt).runObjByClassName
                        + ", class to run "
                        + resourceMgr.getAllocResource(test.getRunnerID(), "",
                            cnt).runObjByName);
                    runner = (ExecThread)resourceMgr.getAllocResource("",
                        ResourceManager.EU_RESOURCE, cnt).runObjByClassName;
                    curTimeoutCnt = (int)(maxCurTimeoutFactor
                        * cfg.getGenTimeout() * Constants.INTERNAL_MULTIPLYER * 3);
                    if (grCnt > 1) {
                        //distributed run: need special handling
                        runner.setUp((ExecUnit)resourceMgr.getAllocResource(
                            test.getRunnerID(), "", cnt).runObjByName, test,
                            cnt, resourceMgr.getReservedResourceList(cnt, test
                                .getRunnerID(), ""));
                    } else {
                        runner.setUp((ExecUnit)resourceMgr.getAllocResource(
                            test.getRunnerID(), "", cnt).runObjByName, test,
                            cnt, null);
                    }
                    //remember test as currently executed (for cases, when
                    // VM crashed)
                    synchronized (currentlyExecuted) {
                        currentlyExecuted.put(test.getTestID(), test);
                    }
                    curTstCnt++;
                    if (curTstCnt % reportTstCnt == 0) {
                        log.add(Level.WARNING, methodLogPrefix
                            + Util.getCurrentTimeToLog()
                            + " Run local. Test were managed to run: "
                            + curTstCnt);
                    }
                    log.add(Level.FINE, methodLogPrefix
                        + Util.getCurrentTimeToLog()
                        + " Run local. Test were managed to run: "
                        + test.getTestID());
                } else {
                    curTimeoutCnt--;
                    resourceMgr.freeResources(cnt);
                    resCntLogCurrent++;
                    if (resCntLogCurrent >= 100) {
                        resCntLogCurrent = 0;
                        log.add(Level.FINEST, methodLogPrefix
                            + Util.getCurrentTimeToLog()
                            + " Run local. Tests to execute: "
                            + eList.get().getTestID()
                            + "\n\tTest remain to run: " + eList.size()
                            + "\tCan not allocate resources.");
                    }
                    try { //give a chance to test to work and finish
                        Thread.sleep(Constants.INTERNAL_TIMEOUT);
                    } catch (InterruptedException e) {
                        // do nothing
                    }
                }
            }
        }
        return true;
    }

    /**
     * Run test on the registered MCore
     * 
     * @return true for successful run
     */
    public boolean runRemote() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\trunRemote(): ";
        ExecListIR eList = mMod.getExecList();
        int curTstCnt = 0;
        int resCntLogCurrent = 0;
        int reportTstCnt = eList.size() / REPORTS_NUMBER;
        int cnt;
        ExecThread runner;
        ExecUnit curRunner;
        log.add(Level.INFO, methodLogPrefix + Util.getCurrentTimeToLog()
            + " Run remote. Test to execute: " + eList.size());
        if (reportTstCnt == 0) {
            reportTstCnt = 1;
        }
        while (eList.size() != 0) {
            if (curTimeoutCnt <= 0) {
                log
                    .add(
                        Level.WARNING,
                        methodLogPrefix
                            + "Test run was stopped: seems that all available resources locked."
                            + " Test remain to execute: " + eList.size());
                break;
            }
            cnt = getNextAllocNumber();
            curRunner = mMod.getRunner(eList.get().getRunnerID());
            int grCnt = curRunner.groupCount(eList.get());
            String[] testResource = curRunner.needSpecialResource(eList.get());
            if (needSkipTest_Mode(eList.get())) {
                log
                    .add(
                        Level.INFO,
                        methodLogPrefix
                            + "According to the test description it does not support current "
                            + "configuration. Skipped.\n\t\t"
                            + eList.get().getTestID());
                skipTest(
                    eList.remove(),
                    methodLogPrefix
                        + "According to the test description it does not support current "
                        + "configuration. Skipped.");
            } else if (!resourceMgr.allocResourcePossible("",
                ResourceManager.EU_RESOURCE, 1)
                || !resourceMgr.allocResourcePossible("",
                    ResourceManager.MC_RESOURCE, grCnt)
                || !resourceMgr.allocSpecialResourcePossible(testResource)) {
                log.add(Level.INFO, methodLogPrefix
                    + "Test was skipped due to lack of resources: "
                    + eList.get().getTestID());
                skipTest(eList.remove(),
                    "Test was skipped due to lack of resources");
            } else {
                if (cfg.getIntertestsTimeout() > 0) {
                    try {
                        Thread.sleep(cfg.getIntertestsTimeout());
                    } catch (InterruptedException e) {
                        //do nothing
                    }
                }
                if (resourceMgr.allocResources("", ResourceManager.EU_RESOURCE,
                    cnt, 1)
                    && resourceMgr.allocResources("",
                        ResourceManager.MC_RESOURCE, cnt, grCnt)
                    && resourceMgr.allocSpecialResource(testResource, cnt)) {
                    TestIR test = curRunner
                        .updateTimeoutFromEnv(eList.remove());
                    maxCurTimeoutFactor = maxCurTimeoutFactor < test
                        .getTestTimeout() ? test.getTestTimeout()
                        : maxCurTimeoutFactor;
                    runner = (ExecThread)resourceMgr.getAllocResource("",
                        ResourceManager.EU_RESOURCE, cnt).runObjByClassName;
                    curTimeoutCnt = (int)(maxCurTimeoutFactor
                        * cfg.getGenTimeout() * Constants.INTERNAL_MULTIPLYER * 3);
                    if (grCnt > 1) { //distributed run: need special handling
                        runner
                            .setUp(
                                (MCoreIR)resourceMgr.getAllocResource("",
                                    ResourceManager.MC_RESOURCE, cnt).runObjByClassName,
                                test, cnt, resourceMgr.getReservedResourceList(
                                    cnt, "", ResourceManager.MC_RESOURCE));
                    } else {
                        runner
                            .setUp(
                                (MCoreIR)resourceMgr.getAllocResource("",
                                    ResourceManager.MC_RESOURCE, cnt).runObjByClassName,
                                test, cnt, null);
                    }
                    //remember test as currently executed (for cases, when VM
                    //to execute is crashed)
                    synchronized (currentlyExecuted) {
                        currentlyExecuted.put(test.getTestID(), test);
                    }
                    curTstCnt++;
                    if (curTstCnt % reportTstCnt == 0) {
                        log.add(Level.WARNING, methodLogPrefix
                            + Util.getCurrentTimeToLog()
                            + " Run remote. Test were managed to run: "
                            + curTstCnt);
                    }
                    log.add(Level.FINE, methodLogPrefix
                        + Util.getCurrentTimeToLog()
                        + " Run remote. Test were managed to run: "
                        + test.getTestID());
                } else {
                    curTimeoutCnt--;
                    resourceMgr.freeResources(cnt);
                    resCntLogCurrent++;
                    if (resCntLogCurrent >= 100) {
                        resCntLogCurrent = 0;
                        log.add(Level.FINEST, methodLogPrefix
                            + Util.getCurrentTimeToLog()
                            + " Run local. Tests to execute: "
                            + eList.get().getTestID()
                            + "\n\tTest remain to run: " + eList.size()
                            + "\tCan not allocate resources.");
                    }
                    try {
                        Thread.sleep(Constants.INTERNAL_TIMEOUT);
                    } catch (InterruptedException e) {
                        //do nothing
                    }
                }
            }
        }
        return true;
    }

    public ResourceManager getResourceManager() {
        return resourceMgr;
    }

    /**
     * Analyze configuration and run tests according to one 
     * 
     * @return true for successful run
     */
    public boolean run() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\trun(): ";
        boolean retVal = false;
        int maxMCNum = 0;
        int execThCnt = 1;
        float waitFactor = 1.0f;
        if (mMod.getConfigIR().getLocalM() == Main.LOCAL
            && mMod.getConfigIR().getExecM() == Main.SAME) {
            concurency = 1; //it is a debug mode
        }
        //if the local run - special handling for execution units
        //need runners of each types multiply to concurency.
        //in general< the concurency for local/other mode is different
        //in other mode it restricted by MCore and each MCore run not more
        //than one test simultaneously
        if (concurency > 1 && mMod.getConfigIR().getLocalM() == Main.LOCAL) {
            Main.getCurCore().getExecUnitPool().multiplayToConcurency(
                concurency);
        }
        resourceMgr.createResourceList(concurency, this);
        if (mMod.getConfigIR().getLocalM() == Main.LOCAL) {
            //          && mode == Main.OTHER) { // ??? disable it ???
            retVal = runLocal();
        } else {
            retVal = runRemote();
        }
        log.add(Level.CONFIG, methodLogPrefix + Util.getCurrentTimeToLog()
            + " All tests were manage to run");

        synchronized (currentlyExecuted) {
            int cnt = currentlyExecuted.size();
            Iterator iter = currentlyExecuted.keySet().iterator();
            for (int i = 0; i < cnt; i++) {
                TestIR test = (TestIR)(currentlyExecuted.get(iter.next()));
                if (waitFactor < test.getTestTimeout()) {
                    waitFactor = test.getTestTimeout();
                }
            }
            //add time for communication etc as 80% from timeout
            waitFactor = waitFactor * 1.8f;
        }
        for (int i = (int)(mMod.getConfigIR().getGenTimeout() * 10 * waitFactor); i != 0; i--) {
            try {
                if (resourceMgr.lockResourceIsEmpty()) {
                    break;
                }
                Thread.sleep(100);
            } catch (InterruptedException e) {
                //do nothing
            }
        }
        log.add(Level.CONFIG, methodLogPrefix
            + "Wait when resources be free: OK");
        resourceMgr.waitToResourceRelease((int)(mMod.getConfigIR()
            .getGenTimeout() * 10 * waitFactor));
        try {
            if (!currentlyExecuted.isEmpty()) {
                reportHungTests();
            }
        } catch (Throwable e) {
            log.add(Level.FINE, methodLogPrefix
                + "Unexpected exception while report hung tests " + e, e);
        }
        log.add(Level.CONFIG, methodLogPrefix + "All tests were run OK");
        return retVal;
    }

    /**
     * Create the info for hang tests and pass this info to reporter
     */
    protected void reportHungTests() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\treportHungTests(): ";
        if (currentlyExecuted == null) {
            return;
        }
        TResIR res = new TResIR("N/A", "N/A", "N/A", "");
        TestIR test;
        synchronized (currentlyExecuted) {
            int cnt = currentlyExecuted.size();
            log.add(Level.INFO, methodLogPrefix + "Seems that up to " + cnt
                + " tests hang up");
            Iterator iter = currentlyExecuted.keySet().iterator();
            for (int i = 0; i < cnt; i++) {
                test = (TestIR)(currentlyExecuted.get(iter.next()));
                res.setTestID(test.getTestID());
                res.setRepFile(test.getTestID());
                if (mMod.getConfigIR().getLocalM() == Main.LOCAL) {
                    res
                        .setTestedOS(System.getProperty("os.name")
                            + " (version " + System.getProperty("os.version")
                            + ")");
                    res.setTestedPlatform(System.getProperty("os.arch"));
                }
                if (mMod.getConfigIR().getExecM() == Main.OTHER) {
                    try {
                        ExecUnit eu = mMod.getRunner(test.getRunnerID());
                        String[][] params = eu.getRunParameters(test);
                        for (int y = 0; y < params.length; y++) {
                            params[y] = eu.subsEnvToValue(params[y]);
                        }
                        ArrayList tmp = new ArrayList();
                        for (int y = 0; y < params.length; y++) {
                            tmp.add("block." + y);
                            for (int z = 0; z < params[y].length; z++) {
                                tmp.add(params[y][z]);
                            }
                        }
                        res.setExecCmd(tmp);
                    } catch (Exception e) {
                        res.setOutMsg(methodLogPrefix
                            + "Can't parse parameters for test: " + e);
                    }
                }
                res.setExecStat(mMod.getConfigIR().getRepError()[0]);
                res.setOutMsg(methodLogPrefix
                    + "No result for test. Seems that the test is hang up.");
                stoRes(res, false);
                log.add(Level.INFO, methodLogPrefix + "Seems that test "
                    + test.getTestID() + " hang up");
            }
        }
    }
}

class ExecThread extends Thread {

    private final String  classID   = "ExecThread";

    private ExecUnit      execU;
    private MCoreIR       mc;
    private TestIR        curTest;
    private RunTestThread rtt;
    private Dispatcher    reportTo;

    int                   curRepNum;

    //the execution units reserved for this test if it number > 1
    private ArrayList     testResources;

    Object                syncObj   = new Object();
    Object                syncObjCh = new Object();
    volatile boolean      stop      = false;
    private Logging       log       = Main.getCurCore().getInternalLogger();

    // define wait timeout as 3 general timeout by slices 1=100 ms
    // after it the test vm seems to hang and resource are stay busy
    // Note, after the 1 general timeout the runners should try to
    // destroy process/ thread. The MCore may execute test up to 2.1
    // times longer (plus time for communication)
    final int             cfgTime   = Main.getCurCore().getConfigIR()
                                        .getGenTimeout() * 3 * 10;

    void stopIt() {
        synchronized (syncObj) {
            stop = true;
        }
    }

    ExecThread(Dispatcher reportTo) {
        this.reportTo = reportTo;
    }

    void setUp(ExecUnit eu, TestIR test, int repNumber, ArrayList resources) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tsetUp(): ";
        synchronized (syncObj) {
            execU = eu;
            curTest = test;
            eu.setCurTest(test);
            curRepNum = repNumber;
            mc = null;
            testResources = resources;
        }
        log.add(Level.FINE, methodLogPrefix + "Test " + test.getTestID()
            + " was set up to execute");
    }

    void setUp(MCoreIR eu, TestIR test, int repNumber, ArrayList resources) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tsetUp(MCore): ";
        synchronized (syncObj) {
            execU = null;
            curTest = test;
            curRepNum = repNumber;
            mc = eu;
            testResources = resources;
        }
        log.add(Level.FINE, methodLogPrefix + "Test " + test.getTestID()
            + " was set up to execute");
    }

    //to call only from 'synchronized (syncObj)' blocks
    void runTest() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\trunTest(): ";
        // define wait timeout as 1.7 test timeout by slices 1=100 ms
        int timeout = (int)(cfgTime * curTest.getTestTimeout() * 1.7f);
        if (timeout == 0) {
            timeout = cfgTime;
        }
        //resources - reserved units for execution
        if (testResources == null) {
            //run test as one (all part one-by-one)
            TResIR curRes;
            try {
                if (execU != null) {
                    rtt = new RunTestThread(curTest, execU, syncObjCh);
                } else if (mc != null) {
                    rtt = new RunTestThread(curTest, mc, syncObjCh);
                } else {
                    log.add(methodLogPrefix
                        + "Incorrect internal params to run a test");
                    reportTo.reportResult(curRepNum, null);
                    mc = null;
                    execU = null;
                    curTest = null;
                    return;
                }
            } catch (Exception e) {
                mc = null;
                execU = null;
                curTest = null;
                log.add(Level.INFO, methodLogPrefix
                    + "Can't create thread to run the test.");
                return;
            }
            log.add(Level.FINEST, methodLogPrefix
                + "Run test and wait for finish " + curTest.getTestID());
            synchronized (syncObjCh) {
                rtt.start();
                try {
                    syncObjCh.wait(Constants.WAIT_TIME);
                } catch (Exception e) {
                    //do nothing
                }
            }
            log.add(Level.FINEST, methodLogPrefix + "Test start successful "
                + curTest.getTestID());
            for (int i = timeout; i >= 0; i--) {
                if (rtt.startFlag != Dispatcher.TEST_FINISHED) {
                    synchronized (syncObjCh) {
                        try {
                            syncObjCh.wait(100);
                        } catch (InterruptedException e) {
                            //do nothing
                        }
                    }
                } else {
                    break;
                }
            }
            log.add(Level.FINEST, methodLogPrefix + "End of test or timeout.");
            synchronized (syncObjCh) {
                curRes = rtt.getResult();
            }
            if (curRes != null) {
                log.add(Level.FINEST, methodLogPrefix + "Test finished "
                    + curTest.getTestID() + "\n\tResult is "
                    + curRes.getExecStat());
            } else {
                log.add(Level.FINEST, methodLogPrefix + "Test finished "
                    + curTest.getTestID() + "\n\tResult is null");
            }
            if (rtt.startFlag != Dispatcher.TEST_FINISHED) {
                // the test is hang: resources (except execution thread) are
                // stay busy
                new ThreadDestroy(rtt).start();
                reportTo.reportResult(Integer.MIN_VALUE, curRes);
                reportTo.freeResourceByType(curRepNum,
                    ResourceManager.EU_RESOURCE);
                log
                    .add(Level.FINEST, methodLogPrefix + "Result for test "
                        + curTest.getTestID()
                        + " reported. Resources stay locked.");
            } else {
                reportTo.reportResult(curRepNum, curRes);
                log.add(Level.FINEST, methodLogPrefix + "Result for test "
                    + curTest.getTestID() + " reported. Resources unlocked.");
            }
            mc = null;
            execU = null;
            curTest = null;
        } else {
            //run distributed test (some parts simultaneously)
            ExecUnit tstRunner = Main.getCurCore().getRunner(
                curTest.getRunnerID());
            TestIR[] testToExec = tstRunner.defineTestPart(curTest);
            TResIR[] resToMerge = new TResIR[testToExec.length];
            RunTestThread[] rtt = new RunTestThread[testToExec.length];
            int partCnt = testToExec.length;
            for (int i = 0; i < partCnt; i++) {
                Object runObj;
                if (((Resource)testResources.get(i)).runObjByName != null) {
                    runObj = ((Resource)testResources.get(i)).runObjByName;
                } else {
                    runObj = ((Resource)testResources.get(i)).runObjByClassName;
                }
                log.add(Level.FINE, methodLogPrefix + "Run test object is "
                    + runObj);
                if (runObj instanceof ExecUnit) {
                    rtt[i] = new RunTestThread(testToExec[i], (ExecUnit)runObj,
                        syncObjCh);
                } else if (runObj instanceof MCoreIR) {
                    rtt[i] = new RunTestThread(testToExec[i], (MCoreIR)runObj,
                        syncObjCh);
                } else {
                    log
                        .add(methodLogPrefix
                            + "Incorrect internal params to run a distributed test.");
                    reportTo.reportResult(curRepNum, null);
                    mc = null;
                    execU = null;
                    curTest = null;
                    return;
                }
            }
            log.add(Level.FINEST, methodLogPrefix
                + "Run distributed test and wait for finish "
                + curTest.getTestID());
            synchronized (syncObjCh) {
                for (int i = 0; i < partCnt; i++) {
                    rtt[i].start();
                }
                try {
                    syncObjCh.wait(Constants.WAIT_TIME);
                } catch (Exception e) {
                    //do nothing
                }
            }
            log.add(Level.FINEST, methodLogPrefix + "Test start successful "
                + curTest.getTestID());
            for (int i = timeout; i >= 0; i--) {
                int fineshed = 0;
                for (int y = 0; y < partCnt; y++) {
                    if (rtt[y].startFlag != Dispatcher.TEST_FINISHED) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            //do nothing
                        }
                    } else {
                        fineshed++;
                    }
                }
                if (fineshed == partCnt) {
                    break;
                }
            }
            log.add(Level.FINEST, methodLogPrefix + "Test finished "
                + curTest.getTestID());
            for (int i = 0; i < partCnt; i++) {
                resToMerge[i] = rtt[i].getResult();
                if (rtt[i].startFlag != Dispatcher.TEST_FINISHED) {
                    new ThreadDestroy(rtt[i]);
                    reportTo.freeResourceByType(curRepNum,
                        ResourceManager.EU_RESOURCE);
                    //the test is hang: resource are stay busy
                    curRepNum = Integer.MIN_VALUE;
                }
            }
            reportTo
                .reportResult(curRepNum, tstRunner.mergeResults(resToMerge));
            mc = null;
            execU = null;
            curTest = null;
        }
    }

    public void run() {
        synchronized (syncObj) {
            while (stop != true) {
                if (execU != null && curTest != null) {
                    runTest();
                } else if (mc != null && curTest != null) {
                    runTest();
                } else {
                    try {
                        syncObj.wait(Constants.INTERNAL_TIMEOUT);
                    } catch (InterruptedException e) {
                        //do nothing
                    }
                }
            }
        }
    }
}

class RunTestThread extends Thread {

    private final String classID = "RunTestThread";

    private TResIR       curRes;
    private TestIR       curTest;
    private ExecUnit     execU;
    private MCoreIR      mc;
    private Object       syncObj;

    volatile int         startFlag;

    public RunTestThread(TestIR test, ExecUnit executor, Object syncObj) {
        curTest = test;
        execU = executor;
        this.syncObj = syncObj;
        if (curTest == null || syncObj == null || execU == null) {
            throw new NullPointerException(MessageInfo.MSG_PREFIX + classID
                + "\tInvalid parameters to run a test");
        }
        startFlag = Dispatcher.TEST_UNDEFINED;
    }

    public RunTestThread(TestIR test, MCoreIR executor, Object syncObj) {
        curTest = test;
        mc = executor;
        this.syncObj = syncObj;
        if (curTest == null || syncObj == null || mc == null) {
            throw new NullPointerException(MessageInfo.MSG_PREFIX + classID
                + "Invalid parameters to run a test");
        }
        startFlag = Dispatcher.TEST_UNDEFINED;
    }

    public void run() {
        synchronized (syncObj) {
            startFlag = Dispatcher.TEST_STARTED;
            syncObj.notifyAll();
        }
        if (curTest != null) {
            if (execU != null) {
                curRes = execU.execTest(curTest);
            } else if (mc != null) {
                curRes = mc.execTest(curTest);
            }
        }
        synchronized (syncObj) {
            startFlag = Dispatcher.TEST_FINISHED;
            syncObj.notifyAll();
        }
    }

    public TResIR getResult() {
        return curRes;
    }
}

class StoreResThread extends Thread {

    private Storage store;
    private TResIR  result;
    private String  root;
    private String  extension;

    private boolean finish = false;

    public StoreResThread(Storage store, TResIR result, String root,
        String extension) {
        this.store = store;
        this.result = result;
        if (root == null) {
            this.root = "";
        } else {
            this.root = root;
        }
        if (extension == null) {
            this.extension = "";
        } else {
            this.extension = extension;
        }
    }

    public void run() {
        if (store != null && result != null) {
            if (store.init(root + Constants.INTERNAL_FILE_SEP
                + result.getRepFile() + extension)) {
                store.add(result);
                store.close();
            }
        }
        finish = true;
    }

    public boolean isFinished() {
        return finish;
    }
}