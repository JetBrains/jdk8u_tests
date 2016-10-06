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
 * @version $Revision: 1.13 $
 */
package org.apache.harmony.harness;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;

/*
 * This is a communication server that used to establish a connection between
 * harness core and harness micro core
 */
public class ComServer extends Thread {

    private volatile boolean shutdown   = false;
    private MCPool           clientPool = Main.getCurCore().getMCPoll();
    private Logging          log        = Main.getCurCore().getInternalLogger();
    private int              port       = 5678;

    private Socket           tmpStore;
    private ServerSocket     ssock;

    private final String     classID    = "ComServer";

    /**
     * Set the port number to run the server
     */
    public ComServer(int i) {
        port = i;
    }

    /**
     * This method accept all clients and create the communication channel
     */
    public void run() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\trun(): ";
        MCoreIR mc;
        try {
            ssock = new ServerSocket(port);
            synchronized (Main.getCurCore().getSynObj()) {
                Main.getCurCore().getSynObj().notifyAll();
            }
            while (!shutdown) {
                log.add(Level.FINE, methodLogPrefix
                    + "waiting for MCore on port " + port);
                tmpStore = ssock.accept();
                log.add(Level.FINE, methodLogPrefix
                    + "waiting for MCore OK on port " + port);
                clientPool.add(new MCoreIR(tmpStore));
                log.add(Level.INFO, methodLogPrefix
                    + "MCore was added to the mc list. Numbers of MCore "
                    + clientPool.size());
            }
        } catch (SocketException e) {
            if (shutdown) {
                log.add(Level.INFO, methodLogPrefix + "server for port " + port
                    + " shutdown successfully");
            } else {
                log.add(Level.INFO, methodLogPrefix
                    + MessageInfo.UNEX_EXCEPTION + e, e);
            }
        } catch (Exception e) {
            log.add(Level.INFO, methodLogPrefix + MessageInfo.UNEX_EXCEPTION
                + e, e);
        }
    }

    /**
     * Set the shutdown flag and tries to close the socket
     */
    public void close() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tclose(): ";
        shutdown = true;
        if (ssock != null && !ssock.isClosed()) {
            try {
                ssock.close();
            } catch (IOException e) {
                log.add(Level.CONFIG, methodLogPrefix
                    + MessageInfo.UNEX_EXCEPTION + "while close server socket "
                    + e, e);
            }
        }
    }
}