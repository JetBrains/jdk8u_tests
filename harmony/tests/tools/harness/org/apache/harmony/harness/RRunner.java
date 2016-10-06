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
 * @version $Revision: 1.25 $
 */
package org.apache.harmony.harness;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

public class RRunner extends Thread {

    private final String       classID       = "RRunner";

    public static final String NEED_RUN      = "true";
    public static final String NEED_NOT_RUN  = "false";
    public static final String MC_CLASS_NAME = "org.apache.harmony.harness.MCore.Main";
    public static final int    MONITOR_PORT  = 20102;

    private ConfigIR           cfg           = Main.getCurCore().getConfigIR();
    private MCPool             clientPool    = Main.getCurCore().getMCPoll();
    private Logging            log           = Main.getCurCore()
                                                 .getInternalLogger();

    protected HashMap          mcCmdPool     = new HashMap();
    protected HashMap          mcRecordPool  = new HashMap();
    protected String[]         mcIDPool;

    private int                uniqueID      = 90;
    private boolean            stop          = false;

    // run micro core remote Parameters are decoded as for 'Runtime' exec unit
    private void runMC(String[][] hostList) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\trunMC(): ";
        String cpOpt = null;
        String bcpOpt = null;
        String[] genVm = null;
        String[] secOpt = null;
        log.add(Level.FINE, methodLogPrefix + "MCore records "
            + hostList.length);
        ExecUnit eunit = Main.getCurCore().getRunner("Runtime");
        if (eunit != null) {
            cpOpt = eunit.getCPOptions();
            bcpOpt = eunit.getBCPOptions();
            genVm = eunit.getGeneralVMOptions();
            secOpt = eunit.getSecurityOptions();
        }
        for (int i = 0; i < hostList.length; i++) {
            try {
                log.add(Level.FINEST, methodLogPrefix + "MCore records " + i
                    + ": " + hostList[i][0] + " - " + hostList[i][1] + " - "
                    + hostList[i][2] + " - " + hostList[i][3]);
                if (NEED_NOT_RUN.equalsIgnoreCase(hostList[i][3])
                    || hostList[i][3].length() < 1) {
                    log.add(Level.CONFIG, methodLogPrefix
                        + "The remote MCore for record " + i + " already run");
                } else if (NEED_RUN.equalsIgnoreCase(hostList[i][3])) {
                    if (hostList[i][0] == null) { //localhost
                        log.add(Level.CONFIG, methodLogPrefix
                            + "start MCore on local host with default params");
                        ArrayList cmdOptions = new ArrayList();
                        String runtime;
                        if (cfg.getExecM() == Main.SAME) {
                            runtime = cfg.getTestedRuntime();
                        } else {
                            runtime = cfg.getReferenceRuntime();
                        }
                        if (runtime == null || runtime.length() < 1) {
                            runtime = "java";
                        }
                        cmdOptions.add(runtime);
                        cmdOptions.add("-cp");
                        if (cpOpt != null) {
                            cmdOptions.add(cpOpt);
                        } else {
                            cmdOptions.add(cfg.getTestSuiteClassRoot()
                                + File.pathSeparator
                                + System.getProperty("java.class.path"));
                        }
                        if (bcpOpt != null) {
                            cmdOptions.add(bcpOpt);
                        }
                        if (genVm != null && genVm.length > 0) {
                            for (int y = 0; y < genVm.length; y++) {
                                if (genVm[y].equalsIgnoreCase("-cp")
                                    || genVm[y].equalsIgnoreCase("-classpath")) {
                                    y++; //miss it
                                } else {
                                    cmdOptions.add(genVm[y]);
                                }
                            }
                        }
                        if (secOpt != null && secOpt.length > 0) {
                            for (int y = 0; y < secOpt.length; y++) {
                                cmdOptions.add(secOpt[y]);
                            }
                        }
                        cmdOptions.add(MC_CLASS_NAME);
                        cmdOptions.add(hostList[i][2]);
                        cmdOptions.add("-port");
                        cmdOptions.add(hostList[i][1]);
                        cmdOptions.add("-id");
                        cmdOptions.add(new Integer(uniqueID++).toString());
                        if (cfg.getMaxTestsinSameCnt() != 0) {
                            cmdOptions.add("-testnumber");
                            cmdOptions.add("" + cfg.getMaxTestsinSameCnt());
                        }
                        String[] cmd = new String[cmdOptions.size()];
                        for (int y = 0; y < cmd.length; y++) {
                            cmd[y] = (String)cmdOptions.get(y);
                        }
                        String[] tmpStrArr = eunit.subsEnvToValue(cmd);
                        cmd = eunit.subsEnvToValue(cmd);
                        String tmp = "";
                        for (int y = 0; y < cmd.length; y++) {
                            log.add(Level.FINE, methodLogPrefix
                                + "run mcore param[" + y + "] " + cmd[y]);
                            tmp = tmp + " " + cmd[y];
                        }
                        Process proc;
                        if (cfg.getExecM() == Main.SAME) {
                            proc = java.lang.Runtime.getRuntime().exec(cmd,
                                eunit.getEnviroment());
                        } else {
                            proc = java.lang.Runtime.getRuntime().exec(cmd);
                        }
                        mcCmdPool.put("" + (uniqueID - 1), tmp);
                        mcRecordPool.put("" + (uniqueID - 1), hostList[i]);
                        Main.getCurCore().addExtProcs(proc);
                        log.add(Level.INFO, methodLogPrefix
                            + "start MCore on local host, port "
                            + hostList[i][1]);
                        log.add(Level.CONFIG, methodLogPrefix + "run process "
                            + proc);
                    } else {
                        log
                            .add(
                                Level.INFO,
                                methodLogPrefix
                                    + "can not automatically start MCore on remote host."
                                    + " Record number is " + i);
                    }
                } else {
                    // else remote run: the 'run' element of record is
                    // interpreted as full command to run
                    String[] cmd = new String[1];
                    cmd[0] = hostList[i][3];
                    String tmp = eunit.subsEnvToValue(cmd)[0];
                    int mcID = -1;
                    if (tmp.indexOf("-id") != -1) {
                        String[] tmpArr = Util.stringToArray(tmp);
                        for (int y = 0; y < tmpArr.length; y++) {
                            if (tmpArr[y].equalsIgnoreCase("-id")) {
                                try {
                                    mcID = Integer.parseInt(tmpArr[y + 1]);
                                } catch (NumberFormatException nfe) {
                                    log
                                        .add(
                                            Level.INFO,
                                            methodLogPrefix
                                                + "start MCore. Can't parse '-id' param.");
                                }
                                break;
                            }
                        }
                    } else {
                        mcID = uniqueID;
                        tmp = tmp + "-id " + uniqueID++;
                    }
                    // can't parse the params for ID? init it by default
                    if (mcID == -1) {
                        mcID = uniqueID;
                        uniqueID++;
                    }
                    Process proc;
                    if (cfg.getExecM() == Main.SAME) {
                        proc = java.lang.Runtime.getRuntime().exec(tmp,
                            eunit.getEnviroment());
                    } else {
                        proc = java.lang.Runtime.getRuntime().exec(tmp);
                    }
                    mcCmdPool.put("" + mcID, tmp);
                    mcRecordPool.put("" + mcID, hostList[i]);
                    Main.getCurCore().addExtProcs(proc);
                    log.add(Level.INFO, methodLogPrefix
                        + "start MCore. The command to start is\n\t" + tmp);
                    log.add(Level.CONFIG, methodLogPrefix + "run process "
                        + proc);
                }
            } catch (IOException e) {
                log.add(Level.INFO, methodLogPrefix
                    + MessageInfo.UNEX_EXCEPTION + e, e);
            }
        }
    }

    // run communication servers which are waits for connection for the remote
    // micro core
    private void runCS(ArrayList ports) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\trunCS(): ";
        int[] portArr;
        ComServer[] cs;
        // remove duplicates
        int num = ports.size();
        portArr = new int[num];
        int[] tmpStore = new int[num];
        int csCnt = 0;
        boolean needAd;
        for (int i = 0; i < num; i++) {
            needAd = true;
            portArr[i] = new Integer((String)ports.get(i)).intValue();
            for (int cnt = 0; cnt < i; cnt++) {
                if (tmpStore[cnt] == portArr[i]) {
                    needAd = false;
                }
            }
            if (needAd) {
                tmpStore[i] = portArr[i];
            }
        }
        // run communication servers on local host and different ports
        cs = new ComServer[num];
        for (int i = 0; i < num; i++) {
            if (tmpStore[i] != 0) {
                synchronized (Main.getCurCore().getSynObj()) {
                    cs[i] = new ComServer(tmpStore[i]);
                    cs[i].start();
                    csCnt++;
                    log.add(Level.INFO, methodLogPrefix
                        + "ComServer was run OK on port " + tmpStore[i]);
                    try {
                        Main.getCurCore().getSynObj().wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (csCnt == 0) {
            return;
        }
        ComServer[] tmpCS = new ComServer[csCnt];
        int val = 0;
        for (int i = 0; i < cs.length; i++) {
            if (cs[i] != null) {
                tmpCS[val++] = cs[i];
            }
        }
        Main.getCurCore().setComServers(tmpCS);
    }

    private void createConnection(String[][] hostList) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tcreateConnection(): ";
        Socket sock;
        int[] outRes = new int[hostList.length];
        boolean forceExit = false;

        while (forceExit == false) {
            forceExit = true;
            for (int i = 0; i < hostList.length; i++) {
                try {
                    if ("-server".equalsIgnoreCase(hostList[i][2])
                        && outRes[i] != Integer.MAX_VALUE) {
                        sock = new Socket(hostList[i][0], Integer
                            .parseInt(hostList[i][1]));
                        clientPool.add(new MCoreIR(sock));
                    }
                    outRes[i] = Integer.MAX_VALUE;
                } catch (IOException e) {
                    log.add(Level.INFO, methodLogPrefix
                        + MessageInfo.UNEX_EXCEPTION
                        + "while connect to the micro core (" + "host "
                        + hostList[i][0] + ", port "
                        + Integer.parseInt(hostList[i][1]) + "): " + e);
                    forceExit = false;
                    try {
                        Thread.sleep(Constants.INTERNAL_TIMEOUT);
                    } catch (InterruptedException ie) {
                        //do nothing
                    }
                } catch (InstantiationException e) {
                    log.add(Level.INFO, methodLogPrefix
                        + MessageInfo.UNEX_EXCEPTION
                        + "while connect to the micro core (" + "host "
                        + hostList[i][0] + ", port "
                        + Integer.parseInt(hostList[i][1]) + "): " + e);
                }
            }
        }

    }

    private String[][] parseRem(ArrayList args) {
        String mode;
        String[][] tmpStore = new String[args.size()][];
        for (int i = 0; i < args.size(); i++) {
            tmpStore[i] = new String[4];
            tmpStore[i][0] = (String)(((ArrayList)args.get(i)).get(0));
            tmpStore[i][1] = (String)(((ArrayList)args.get(i)).get(1));
            mode = (String)((((ArrayList)args.get(i)).get(2)));
            if ("client".equalsIgnoreCase(mode)) {
                tmpStore[i][2] = "-client";
            } else if ("server".equalsIgnoreCase(mode)) {
                tmpStore[i][2] = "-server";
            } else if ("active".equalsIgnoreCase(mode)) {
                tmpStore[i][2] = "-server";
            } else {
                tmpStore[i][2] = "-client";
            }
            tmpStore[i][3] = (String)(((ArrayList)args.get(i)).get(3));
            if ("localhost".equalsIgnoreCase(tmpStore[i][0])) {
                tmpStore[i][0] = null;
            }
        }
        return tmpStore;
    }

    void rerunMCore(ArrayList idList) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\trerunMCore(): ";
        for (int i = 0; i < idList.size(); i++) {
            try {
                if (mcCmdPool.containsKey(idList.get(i))) {
                    String cmd = mcCmdPool.get(idList.get(i)).toString();
                    if (cmd != null && cmd.length() > 0) {
                        Process proc = java.lang.Runtime.getRuntime().exec(
                            mcCmdPool.get(idList.get(i)).toString());
                        Main.getCurCore().addExtProcs(proc);
                        log.add(Level.INFO, methodLogPrefix + "MCore with id "
                            + idList.get(i) + " was rerun.\n\t\tCmd is " + cmd);
                    } else {
                        log.add(Level.INFO, methodLogPrefix + "MCore with id "
                            + idList.get(i)
                            + " can not be rerun. Empty command.");
                    }
                }
            } catch (IOException e) {
                log.add(Level.INFO, methodLogPrefix
                    + MessageInfo.UNEX_EXCEPTION + "while rerun MCore "
                    + idList.get(i) + "\t" + e);
            }
        }
        //set connection if MC run as server
        ArrayList hosts = new ArrayList();
        for (int i = 0; i < idList.size(); i++) {
            String[] tmp = (String[])mcRecordPool.get(idList.get(i));
            if (tmp != null) {
                hosts.add(tmp);
            }
        }
        String[][] hostList = new String[hosts.size()][];
        for (int i = 0; i < hostList.length; i++) {
            hostList[i] = (String[])hosts.get(i);
        }
        createConnection(hostList);
        log.add(Level.CONFIG, methodLogPrefix + "createConnection OK");
        Main.getCurCore().getDispatcher().getResourceManager()
            .updateMCoreResources();
        log.add(Level.CONFIG, methodLogPrefix + "update resources OK");
    }

    public void run() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\trun(): ";
        // if mode is local no MCore and servers needed
        if (Main.getCurCore().getConfigIR().getLocalM() == Main.LOCAL) {
            return;
        }
        //hostList - list of (host, port, mode, need_run)
        String[][] hostList;
        boolean needCS = false;
        boolean needR = false;
        ArrayList tmp = Main.getCurCore().getConfigIR().getRemoteRunner();
        if (tmp == null || tmp.size() == 0) {
            return;
        }
        // monitor all MCore's activity: each MCore should report 'alive' status
        // each WAIT_TIME interval. In case of 3 reports missed - rerun MCore if
        // it possible. Create and run monitor server
        int monitorPort = MONITOR_PORT;
        try {
            monitorPort = Integer.parseInt(cfg.getMCMonitorPort());
        } catch (Exception e) {
            log.add(Level.CONFIG, methodLogPrefix
                + "Can't read monitor port settings. Use default port: "
                + MONITOR_PORT);
        }
        mcIDPool = new String[mcCmdPool.size()];
        Iterator it = mcCmdPool.keySet().iterator();
        for (int i = 0; i < mcIDPool.length; i++) {
            mcIDPool[i] = it.next().toString();
        }
        MCActivityMonitor mcAct = new MCActivityMonitor(monitorPort, mcIDPool);
        mcAct.start();

        hostList = parseRem(tmp);
        ArrayList ports = new ArrayList();
        ArrayList runs = new ArrayList();
        log.add(Level.CONFIG, methodLogPrefix + "MCore records were defined "
            + hostList.length);
        for (int i = 0; i < hostList.length; i++) {
            //          run communication server
            if ("-client".equalsIgnoreCase(hostList[i][2])) {
                needCS = true;
                ports.add(hostList[i][1]);
            }
            if (!NEED_NOT_RUN.equalsIgnoreCase(hostList[i][3])) {
                needR = true;
            }
        }
        if (needCS) {
            log.add(Level.CONFIG, methodLogPrefix
                + "try to run communication server(s).");
            runCS(ports);
        } else {
            log.add(Level.CONFIG, methodLogPrefix
                + "no need to run communication server.");
        }
        if (needR) {
            log.add(Level.CONFIG, methodLogPrefix
                + "try to run specified micro core(s).");
            runMC(hostList);
        } else {
            log.add(Level.CONFIG, methodLogPrefix
                + "no need to run any micro core.");
        }
        createConnection(hostList);

        //monitor mcore activity
        while (!stop) {
            if (mcAct.checkStatus()) {
                try {
                    Thread.sleep(Constants.WAIT_TIME);
                } catch (InterruptedException e) {
                    //do nothing
                }
            } else {
                rerunMCore(mcAct.getHangUpID());
                mcAct.clearHungUp();
            }
        }
        mcAct.stopIt();
        mcAct.interrupt();
    }

    public void stopIt() {
        stop = true;
    }
}