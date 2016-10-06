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
 * @version $Revision: 1.19 $
 */
package org.apache.harmony.harness;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;

public class MCoreIR {

    private final String       classID    = "MCoreIR";
    /*
     * internal variable
     */
    private Socket             sock;
    private InputStream        inS;
    private OutputStream       outS;
    private ObjectInputStream  ois;
    private ObjectOutputStream oos;

    private ConfigIR           cfg        = Main.getCurCore().getConfigIR();
    private Storage            store      = Main.getCurCore().getStore();
    // this class always work as a part of "big" test harness
    private Logging            log        = Main.getCurCore()
                                              .getInternalLogger();

    // internal variable just to store the frequently used value
    private int                genTimeOut = cfg.getGenTimeout();

    private int                curTimeout;
    private TResIR             result;

    /*
     * temporary variable to send/ receive the data through the socket
     * connection
     */
    private ArrayList          tmpStore;

    /*
     * create the new MCoreIR object with associated input/ output streams,
     * object input/ output streams and set the configuration for the
     * corresponding MCore object
     */
    public MCoreIR(Socket sk) throws InstantiationException {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tMCoreIR(): ";
        if (sk == null) {
            throw new InstantiationException();
        }
        try {
            sock = sk;
            setOutS(null);
            setInS(null);
            oos = new ObjectOutputStream(outS);
            oos.flush();
            ois = new ObjectInputStream(inS);
            tmpStore = new ArrayList();
            tmpStore.add("setcfg");
            tmpStore.add(Main.getCurCore().getConfigIR());
            send(tmpStore);
            waitForReplay(Integer.MAX_VALUE);
            tmpStore = receive();
            log.add(Level.CONFIG, methodLogPrefix + "MCoreIR created: OK to "
                + sock.getRemoteSocketAddress().toString());
        } catch (Exception e) {
            log.add(Level.WARNING, methodLogPrefix + MessageInfo.UNEX_EXCEPTION
                + e, e);
            throw new InstantiationException(e.toString());
        }
    }

    /*
     * return the current input stream for socket connection
     */
    public InputStream getInS() {
        return inS;
    }

    /*
     * set the new input stream for socket connection
     */
    public InputStream setInS(InputStream value) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tsetInS(): ";
        InputStream tmpStore = value;
        if (value != null) {
            inS = value;
        } else if (sock != null) {
            try {
                inS = sock.getInputStream();
            } catch (IOException e) {
                log.add(Level.WARNING, methodLogPrefix
                    + MessageInfo.UNEX_EXCEPTION + e, e);
            }
        } else {
            inS = null;
        }
        return tmpStore;
    }

    /*
     * return the current output stream for socket connection
     */
    public OutputStream getOutS() {
        return outS;
    }

    /*
     * set the new output stream for socket connection
     */
    public OutputStream setOutS(OutputStream value) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tsetOutS(): ";
        OutputStream tmpStore = value;
        if (value != null) {
            outS = value;
        } else if (sock != null) {
            try {
                outS = sock.getOutputStream();
            } catch (IOException e) {
                log.add(Level.WARNING, methodLogPrefix
                    + MessageInfo.UNEX_EXCEPTION + e, e);
            }
        } else {
            outS = null;
        }
        return tmpStore;
    }

    /*
     * set new socket for this MCoreIR (the old will be correctly closed)
     */
    public Socket setSock(Socket value) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tsetSock(): ";
        Socket tmpStore = value;
        if (sock != null) {
            try {
                sock.shutdownInput();
                sock.shutdownOutput();
                sock.close();
            } catch (IOException e) {
                log.add(Level.WARNING, methodLogPrefix
                    + MessageInfo.UNEX_EXCEPTION + e, e);
            }
        }
        sock = value;
        return tmpStore;
    }

    /*
     * shutdown the corresponding MCore and close the connection
     */
    public void close() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tclose(): ";
        tmpStore = new ArrayList();
        tmpStore.add("terminate");
        try {
            send(tmpStore);
            if (waitForReplay(cfg.getGenTimeout()) == true) {
                receive();
            }
            log.add(Level.CONFIG, methodLogPrefix
                + "send 'terminate' command OK");
        } catch (Exception e) {
            log.add(Level.WARNING, methodLogPrefix + MessageInfo.UNEX_EXCEPTION
                + "while send 'terminate' command " + e, e);
        }
        try {
            sock.shutdownInput();
            sock.shutdownOutput();
            sock.close();
        } catch (IOException e) {
            log.add(Level.WARNING, methodLogPrefix + MessageInfo.UNEX_EXCEPTION
                + e, e);
        }
        inS = null;
        outS = null;
        ois = null;
        oos = null;
    }

    /**
     * wait for data less than timeout second
     * 
     * @param timeout seconds
     * @return true if timeout is finish and no data available
     */
    private boolean waitForReplay(int timeout) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\twaitForReplay(): ";
        int time = timeout;
        try {
            while (inS != null && inS.available() == 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    //do nothing
                }
                if (time-- == 0) {
                    break;
                }
            }
            if (time <= 0) {
                log.add(Level.CONFIG, methodLogPrefix
                    + "no response from the MCore");
                return true;
            }
        } catch (IOException e) {
            log.add(Level.WARNING, methodLogPrefix + MessageInfo.UNEX_EXCEPTION
                + e);
        }
        return false;
    }

    /*
     * send the command to the MCore ArrayList[0] package ID (command name), now
     * define the names "isAlive" - just return [Status, OK] "terminate"- ask to
     * MCore to finish the execution "getID" - request the unique ID of MCore
     * "setcfg" - set the configuration for this MCore (data is ConfigIR) "exec" -
     * run the command data as external process "test" - run the command data as
     * test (data is TestIR) ArrayList[1-n] command data
     */
    boolean send(ArrayList data) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tsend(): ";
        if (sock != null) {
            try {
                oos.writeUnshared(data);
                oos.flush();
                return true;
            } catch (IOException e) {
                log.add(Level.CONFIG, methodLogPrefix
                    + MessageInfo.UNEX_EXCEPTION + e, e);
            }
        }
        return false;
    }

    /*
     * receive the response from the MCore ArrayList[0] package ID (name), now
     * define the name "Status" only ArrayList[1] execution status for passed
     * command ArrayList[2-n] command execution data
     */
    ArrayList receive() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\treceive(): ";
        if (sock != null && ois != null) {
            try {
                ArrayList tmpVal = (ArrayList)ois.readObject();
                if (tmpVal != null) {
                    return tmpVal;
                } else {
                    return new ArrayList();
                }
            } catch (Exception e) {
                log.add(Level.CONFIG, methodLogPrefix
                    + MessageInfo.UNEX_EXCEPTION + e, e);
            }
        }
        return null;
    }

    /*
     * calculate the name of the report file (without extension)
     */
    String calcFileName(TestIR test) {
        return test.getTestID();
    }

    /*
     * calculate timeout for the current test in the slices (1 slice = 100ms)
     */
    protected void calcTimeout(float testTime) {
        int time = (int)(testTime * cfg.getGenTimeout() * 10);
        if (time == 0) {
            curTimeout = cfg.getGenTimeout() * 10; //testTime == 1 by default
        } else {
            curTimeout = time;
        }
    }

    /*
     * the start method: manage the test to run in the associated MCore
     */
    public TResIR execTest(TestIR test) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\texecTest(): ";
        try {
            if (test == null) {
                result = new TResIR("N/A", "N/A", "N/A", "");
                result.setOutMsg(methodLogPrefix + "no test to run");
                result.setExecStat(cfg.getRepError()[0]);
                return result;
            }
            tmpStore = new ArrayList();
            tmpStore.add("test");
            tmpStore.add(Main.getCurCore().getRunner(test.getRunnerID())
                .getClass().getName());
            tmpStore.add(test);
            send(tmpStore);
            tmpStore.clear();
            // Note, the real timeout for test execution is work in the MCore
            // if data available - read it, else report an error
            calcTimeout(test.getTestTimeout());
            if (waitForReplay(curTimeout * 2) == false) {
                tmpStore = receive();
                result = new TResIR("N/A", "N/A", "N/A", test.getTestID());
                result.setRepFile(calcFileName(test));
                result.setExecStat(cfg.getRepError()[0]);
                if (tmpStore == null) {
                    result
                        .setOutMsg(methodLogPrefix
                            + "No response from MCore. Seems, that test is hang up.");
                } else if (tmpStore.size() < 3) {
                    result.setOutMsg(methodLogPrefix
                        + "unexpected response from MCore. Incorrect size: "
                        + tmpStore.size());
                } else {
                    try {
                        //check that the package is [status, OK]
                        if ("Status".equalsIgnoreCase((String)tmpStore.get(0))) {
                            return (TResIR)(tmpStore.get(2));
                        } else {
                            result
                                .setOutMsg(methodLogPrefix
                                    + "unexpected response from MCore. Incorrect package ID (name): "
                                    + tmpStore.get(0));
                        }
                    } catch (Exception e) {
                        result
                            .setOutMsg(methodLogPrefix
                                + "unexpected response from MCore. Exception when parse a result: "
                                + e);
                        result.setOutMsg(e);
                    }
                }
            } else {
                //MCore for this MCoreIR is hang up. So hang up this MCoreIR
                //to mark it as invalid resource to use
                Thread.sleep(Long.MAX_VALUE);
            }
        } catch (Exception e) {
            result = new TResIR("N/A", "N/A", "N/A", test.getTestID());
            result.setRepFile(calcFileName(test));
            result.setExecStat(cfg.getRepError()[0]);
            result.setOutMsg(methodLogPrefix + MessageInfo.UNEX_EXCEPTION + e
                + "\nwhile execute test remotely");
            result.setOutMsg(e);
        }
        return result;
    }
}