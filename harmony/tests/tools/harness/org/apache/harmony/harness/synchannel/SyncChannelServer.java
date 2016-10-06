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
 * @author Kim, Khen Gi
 * @version $Revision: 1.11 $
 */
package org.apache.harmony.harness.synchannel;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

import org.apache.harmony.harness.Logging;
import org.apache.harmony.harness.Main;

/**
 * SyncChannelServer is a ServerSocket, which listens specified port for
 * connections of clients. Port number should be specified in the argc[]
 * parameter of the main method or class constructor. At the current
 * implementation argc[0] should specify port number. If port number is not
 * specified, SyncChannelServer will not be started. It can be started through
 * main method as java application or through start method of class Thread.
 * SyncChannelServer generates unique id for all connected clients. This Id is
 * generates on the base of the counter of all connected clients, during current
 * session. So, the number of connected clients is limited by MAX INT value. To
 * null this counter we recommend to use disconnectAll() method.
 */
public class SyncChannelServer extends Thread {

    private static int            port     = 21001;

    private volatile boolean      shutdown = false;
    private volatile int          genCnt   = 0;
    private SyncChannelClientPool pool;

    Logging                       log      = Main.getCurCore()
                                               .getInternalLogger();

    /*
     * Starts Synchronization Channel Server as application. @param argc Array
     * of strings to initialize Synchronization Channel Server. At the current
     * implementation argc[0] should specify port number listened by
     * Synchronization Channel Server.
     */

    /**
     * Class constructor. Creates Synchronization Channel Server.
     * 
     * @param argc Array of strings to initialize Synchronization Channel
     *        Server. At the current implementation argc[0] should specify port
     *        number listened by Synchronization Channel Server.
     */
    public SyncChannelServer(String[] argc) {
        if (argc.length > 0) {
            try {
                port = Integer.parseInt(argc[0]);
            } catch (NumberFormatException e) {
                log.add(Level.FINE,
                    "SyncChannelServer. Unexpected exception while parse port number: "
                        + e);
            }
        }
        pool = new SyncChannelClientPool();
    }

    /**
     * Run method for Thread of Synchronization Channel Server.
     */
    public void run() {
        Socket connected_socket;
        String group;
        int id;
        Object synObj = Main.getCurCore().getSynObj();
        try {
            ServerSocket socket = new ServerSocket(port);
            synchronized (synObj) {
                synObj.notifyAll();
            }
            while (!shutdown) {
                genCnt++;
                connected_socket = socket.accept();
                SyncChannelMessage message = SyncChannelMessage
                    .receive(connected_socket.getInputStream());
                group = message.getGroupName();
                id = pool.add(group, connected_socket);
                SyncChannelClientHandler scs_socket = new SyncChannelClientHandler(
                    connected_socket, pool, group, id);
                scs_socket.start();
                message.setCommand(SyncChannelMessage.CONNECTED);
                message.setId(id);
                message.send(connected_socket.getOutputStream());
            }
            socket.close();
        } catch (Exception e) {
            synchronized (synObj) {
                synObj.notifyAll();
            }
            //System.out.println("SyncChannelServer. Unexpected exception : " +
            // e);
            log.add("SyncChannelServer. Unexpected exception: " + e);
        }
    }

    /**
     * Return the port for syncrochannel server (host is localhost)
     * 
     * @return the port number to use as the synchannel
     */
    public int getPort() {
        return port;
    }

    /**
     * Return the count for next accepted connection (eg started from 1)
     * 
     * @return the count
     */
    public int getGenCnt() {
        return genCnt;
    }

    /**
     * Closes Synchronization Channel Server.
     */
    public void close() {
        shutdown = true;
    }

    /**
     * Closes all Synchronization Channel Server side Sockets for all connected
     * clients. (disconnects all connected clients). The counter of connected
     * clients is reset to 0. The generation of the unique Ids for connected
     * clients begins from 1.
     */
    public void disconnectAll() {
        pool.removeAll();
    }
}