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
 * @version $Revision: 1.10 $
 */
package org.apache.harmony.harness;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

class MCActivityMonitor extends Thread {

    private final String    classID     = "MCActivityMonitor";

    static volatile boolean needRefresh = false;

    private Logging         log         = Main.getCurCore().getInternalLogger();

    private boolean         stop        = false;
    private Object          synObj      = new Object();

    protected ArrayList     mcIDArr     = new ArrayList();
    protected ArrayList     hangUp      = new ArrayList();

    MCActivityServer        server;
    HashMap                 socketPool  = new HashMap();

    // create list of agents to monitor and run monitor server
    MCActivityMonitor(int monitorPort, String[] expectedMCID) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tMCActivityMonitor(): ";
        boolean findOK = false;
        try {
            server = new MCActivityServer(monitorPort, this);
            server.start();
            log.add(Level.CONFIG, methodLogPrefix + Util.getCurrentTimeToLog()
                + " monitor server start OK");
        } catch (Exception e) {
            log.add(Level.INFO, methodLogPrefix
                + "unexpected exception while create MC monitor port listener "
                + e, e);
        }
        for (int i = 0; i < expectedMCID.length; i++) {
            findOK = false;
            //check if this record already defined (as result of MCore connect)
            synchronized (mcIDArr) {
                for (int y = 0; y < mcIDArr.size(); y++) {
                    MCRecord rec = (MCRecord)mcIDArr.get(y);
                    if (rec.idS.equalsIgnoreCase(expectedMCID[i])) {
                        findOK = true;
                        break;
                    }
                }
                //if not defined - define it
                if (!findOK) {
                    mcIDArr.add(new MCRecord(expectedMCID[i]));
                }
            }
        }
    }

    //check if some agents hang up
    public boolean checkStatus() {
        if (hangUp.size() != 0) {
            return false;
        } else {
            return true;
        }
    }

    //return ID's of hang up agents
    public ArrayList getHangUpID() {
        return (ArrayList)hangUp.clone();
    }

    //clear list of hang up agents
    public void clearHungUp() {
        hangUp.clear();
    }

    //check monitor counts for each record and set hangUp flag if needed
    protected void lookForHangUp() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tlookForHangUp(): ";
        for (int i = 0; i < mcIDArr.size(); i++) {
            MCRecord rec = (MCRecord)mcIDArr.get(i);
            if (rec.curState == MCRecord.INITIAL) {
                if (rec.hangUpCnt >= MCRecord.MAX_CNT) {
                    if (!rec.hangUpReported) {
                        hangUp.add(rec.idS);
                        rec.hangUpReported = true;
                        log.add(Level.INFO, methodLogPrefix
                            + Util.getCurrentTimeToLog()
                            + " Seems that MCore with id " + rec.idS
                            + " hang up.");
                    }
                } else {
                    rec.hangUpCnt++;
                    if (!rec.stopReported) {
                        rec.stopReported = true;
                        log.add(Level.FINEST, methodLogPrefix
                            + Util.getCurrentTimeToLog()
                            + " Reporting stopped for MCore with id " + rec.idS);
                    }
                }
            } else {
                rec.reset();
                log.add(Level.FINEST, methodLogPrefix + "Reset counts for: "
                    + rec.idS);
            }
        }
        updateMCRecords();
    }

    //update records list: remove elements if reporting was stopped for it
    protected void updateMCRecords() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tupdateMCRecords(): ";
        synchronized (mcIDArr) {
            int[] indexToRemove = new int[mcIDArr.size()];
            for (int i = 0; i < mcIDArr.size(); i++) {
                MCRecord rec = (MCRecord)mcIDArr.get(i);
                if (rec.hangUpReported) {
                    indexToRemove[i] = 1;
                }
            }
            for (int i = indexToRemove.length; i > 0; i--) {
                if (indexToRemove[i - 1] == 1) {
                    MCRecord rec = (MCRecord)mcIDArr.remove(i - 1);
                    log.add(Level.FINEST, methodLogPrefix
                        + Util.getCurrentTimeToLog()
                        + " Monitor record was removed for: " + rec.idS);
                }
            }
        }
    }

    protected void addSocketInfo(String key, Socket s) {
        int mcID = -1;
        try {
            mcID = Integer.parseInt(key);
            synchronized (mcIDArr) {
                for (int i = 0; i < mcIDArr.size(); i++) {
                    MCRecord rec = (MCRecord)mcIDArr.get(i);
                    if (rec.id == mcID) {
                        rec.s = s;
                        rec.in = s.getInputStream();
                        rec.out = s.getOutputStream();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            //do nothing
        }
    }

    protected void renewMCIDArr() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\trenewMCIDArr(): ";
        if (socketPool.isEmpty()) {
            return;
        }
        Iterator it = socketPool.keySet().iterator();
        boolean findOK = false;
        int cnt = socketPool.size();
        ArrayList toRemove = new ArrayList();
        for (int i = 0; i < cnt; i++) {
            String key = (String)it.next();
            try {
                //check if this record already defined and renew socket
                // settings only
                synchronized (mcIDArr) {
                    for (int y = 0; y < mcIDArr.size(); y++) {
                        MCRecord rec = (MCRecord)mcIDArr.get(y);
                        if (rec.idS.equalsIgnoreCase(key)) {
                            rec.s = (Socket)socketPool.get(key);
                            rec.in = rec.s.getInputStream();
                            rec.out = rec.s.getOutputStream();
                            findOK = true;
                            log.add(Level.CONFIG, methodLogPrefix
                                + "set socket settings");
                            break;
                        }
                    }
                    //if not defined - define it and set up socket settings
                    if (!findOK) {
                        MCRecord rec = new MCRecord(key);
                        rec.s = (Socket)socketPool.get(key);
                        rec.in = rec.s.getInputStream();
                        rec.out = rec.s.getOutputStream();
                        mcIDArr.add(rec);
                        log.add(Level.CONFIG, methodLogPrefix
                            + "create new record");
                        log.add(Level.FINER, methodLogPrefix
                            + Util.getCurrentTimeToLog() + " records count: "
                            + mcIDArr.size());
                    }
                }
                toRemove.add(key);
            } catch (Exception e) {
                log.add(Level.CONFIG, methodLogPrefix
                    + "unexpected exception while renewMCIDArr " + e);
            }
        }
        if (!toRemove.isEmpty()) {
            for (int i = 0; i < toRemove.size(); i++) {
                socketPool.remove(toRemove.get(i));
            }
        }
    }

    protected void addSocket(Socket s) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\taddSocket(): ";
        try {
            s.setSoTimeout(Constants.WAIT_TIME);
            String key = null;
            synchronized (mcIDArr) {
                byte[] data;
                InputStream is = s.getInputStream();
                int i;
                for (i = Constants.INTERNAL_TIMEOUT; i >= 0; i--) {
                    int size = is.available();
                    if (size >= 1) {
                        break;
                    } else {
                        mcIDArr.wait(Constants.INTERNAL_TIMEOUT);
                    }
                }
                data = new byte[is.available()];
                is.read(data, 0, data.length);
                String tmp = new String(data).trim();
                if (tmp != null && tmp.indexOf(":") != -1) {
                    key = tmp.substring(0, tmp.indexOf(":"));
                    socketPool.put(key, s);
                }
            }
            s.setSoTimeout(Constants.INTERNAL_TIMEOUT);
            log.add(Level.CONFIG, methodLogPrefix + Util.getCurrentTimeToLog()
                + " Add info to reporting pool: OK for " + key);
        } catch (Exception e) {
            log.add(Level.INFO, methodLogPrefix
                + "unexpected exception while setup report info:" + e, e);
        }
    }

    public void run() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\trun(): ";
        int cnt = 0;
        TimeThread tt = new TimeThread(synObj);
        tt.start();
        byte[] dataArr;
        log.add(Level.INFO, methodLogPrefix + Util.getCurrentTimeToLog()
            + " Start monitoring");
        while (!stop) {
            cnt++;
            if (!socketPool.isEmpty()) {
                renewMCIDArr();
            }
            synchronized (mcIDArr) {
                for (int i = 0; i < mcIDArr.size(); i++) {
                    try {
                        MCRecord rec = (MCRecord)mcIDArr.get(i);
                        if (rec.in != null) {
                            dataArr = new byte[rec.in.available()];
                            if (dataArr.length > 0) {
                                rec.in.read(dataArr, 0, dataArr.length);
                                String data = new String(dataArr);
                                if (data != null && data.indexOf(":") != -1) {
                                    rec.curState = MCRecord.ACTIVE;
                                }
                            }
                        }
                    } catch (SocketTimeoutException ste) {
                        if (cnt >= 100) {
                            log.add(Level.CONFIG, methodLogPrefix
                                + "Exception: " + ste);
                            cnt = 0;
                        }
                    } catch (Exception e) {
                        log.add(Level.INFO, methodLogPrefix
                            + "unexpected exception while receive info: " + e,
                            e);
                    }
                }
            }
            //needRefresh value updated by TimeThread
            if (needRefresh) {
                lookForHangUp();
                synchronized (synObj) {
                    needRefresh = false;
                }
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                //do nothing
            }
        }
        tt.stopIt();
    }

    public void stopIt() {
        stop = true;
    }
}

class MCRecord {

    public static final int ACTIVE         = 1;
    public static final int INITIAL        = 101;
    public static final int MAX_CNT        = 5;

    int                     id             = 0;
    String                  idS            = "";
    int                     curState       = INITIAL;
    int                     hangUpCnt      = 0;
    protected OutputStream  out            = null;
    protected InputStream   in             = null;
    protected Socket        s              = null;

    boolean                 stopReported   = false;
    boolean                 hangUpReported = false;

    MCRecord(String idS) {
        this.idS = idS;
        try {
            id = Integer.parseInt(idS);
        } catch (NumberFormatException nfe) {
            //do nothing
        }
    }

    void reset() {
        curState = INITIAL;
        hangUpCnt = 0;
        stopReported = false;
        hangUpReported = false;
    }
}

class TimeThread extends Thread {

    private Object  synObj = new Object();
    private boolean stop   = false;

    public TimeThread(Object synObj) {
        this.synObj = synObj;
    }

    public void run() {
        while (!stop) {
            try {
                synchronized (synObj) {
                    MCActivityMonitor.needRefresh = true;
                }
                Thread.sleep(Constants.WAIT_TIME);
            } catch (Exception e) {
                //do nothing
            }
        }
    }

    void stopIt() {
        stop = true;
    }
}

class MCActivityServer extends Thread {

    private boolean           stop        = false;
    private int               monitorPort = 10101;
    private Socket            tmpStore;
    private ServerSocket      ssock;
    private MCActivityMonitor reporter    = null;

    MCActivityServer(int monitorPort, MCActivityMonitor toAdd) {
        this.monitorPort = monitorPort;
        reporter = toAdd;
    }

    public void run() {
        try {
            ssock = new ServerSocket(monitorPort);
            while (!stop) {
                try {
                    tmpStore = ssock.accept();
                    reporter.addSocket(tmpStore);
                } catch (Exception e) {
                    //do nothing
                }
            }
        } catch (Exception e) {
            //do nothing
        }
    }

    void stopIt() {
        stop = true;
    }
}