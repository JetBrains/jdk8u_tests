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
 * @version $Revision: 1.8 $
 */
package org.apache.harmony.harness.synchannel;

import java.io.IOException;
import java.net.SocketException;
import java.io.InputStream;
import java.net.Socket;

/**
 * SyncChannelServerSocket is a Synchronization Channel Server side Socket for
 * connected client. Each SyncChannelServerSocket runs in its own Thread. It
 * permanently listens its Input Stream. If message is received from client
 * side, it resend this message to the appropriate client, using SyncChannelPool
 * and return status message to the client side.
 */
class SyncChannelClientHandler extends Thread {
    private volatile boolean      shutdown = false;
    private Socket                socket;
    private SyncChannelClientPool pool;
    private String                group;
    private int                   id;

    /**
     * Class constructor. Creates Synchronization Channel Server.
     * 
     * @param s Socket returned by ServerSocket accept method.
     * @param p Port number listened by Synchronization Channel Server.
     * @param group_name Group name of the client connected to this Socket.
     * @param client_id Id of the client connected to this Socket.
     */
    protected SyncChannelClientHandler(Socket s, SyncChannelClientPool p,
        String group_name, int client_id) {
        socket = s;
        pool = p;
        group = group_name;
        id = client_id;
    }

    /**
     * Run method for Thread of Synchronization Channel Server Socket.
     */
    public void run() {
        SyncChannelMessage message;
        SyncChannelMessage reply_message;
        InputStream input;
        Socket receiver;
        Socket[] receivers;
        Integer[] receivers_id;
        int temp;

        try {
            while (!shutdown) {
                input = socket.getInputStream();
                message = SyncChannelMessage.receive(input);
                switch (message.getCommand()) {
                case SyncChannelMessage.CLOSE:
                    receiver = pool.remove(message.getGroupName(), new Integer(
                        message.getId()));
                    //					System.out.println("removed Socket = " + receiver + "
                    // Pool size = " + pool.cnGroups());
                    this.close();
                    break;
                case SyncChannelMessage.GROUP_SEND:
                    byte status = SyncChannelMessage.NOT_SUPPLIED;
                    receivers = pool.getClientsInGroup(message.getGroupName());
                    receivers_id = pool.getIdInGroup(message.getGroupName());
                    if (receivers != null) {
                        for (int i = 0; i < receivers.length; i++) {
                            if (receivers_id[i].intValue() != id) {
                                message.send(receivers[i].getOutputStream());
                                status = SyncChannelMessage.SUPPLIED;
                            }
                        }
                        reply_message = new SyncChannelMessage(message
                            .getGroupName(), message.getId(), "");
                        reply_message.setCommand(status);
                        reply_message.send(socket.getOutputStream());
                    } else {
                        reply_message = new SyncChannelMessage(message
                            .getGroupName(), message.getId(), "");
                        reply_message
                            .setCommand(SyncChannelMessage.NOT_SUPPLIED);
                        reply_message.send(socket.getOutputStream());
                    }
                    break;
                case SyncChannelMessage.INDIVIDUAL_SEND:
                    receiver = pool.getClient(message.getGroupName(),
                        new Integer(message.getId()));
                    if (receiver != null) {
                        message.send(receiver.getOutputStream());
                        reply_message = new SyncChannelMessage(message
                            .getGroupName(), message.getId(), "");
                        reply_message.setCommand(SyncChannelMessage.SUPPLIED);
                        reply_message.send(socket.getOutputStream());
                    } else {
                        reply_message = new SyncChannelMessage(message
                            .getGroupName(), message.getId(), "");
                        reply_message
                            .setCommand(SyncChannelMessage.NOT_SUPPLIED);
                        reply_message.send(socket.getOutputStream());
                    }
                    break;
                case SyncChannelMessage.GROUP_CAST:
                    receivers = pool.getClientsInGroup(message.getGroupName());
                    for (int i = 0; i < receivers.length; i++) {
                        if (message.getId() != id)
                            message.send(receivers[i].getOutputStream());
                    }
                    break;
                case SyncChannelMessage.INDIVIDUAL_CAST:
                    receiver = pool.getClient(message.getGroupName(),
                        new Integer(message.getId()));
                    if (receiver != null)
                        message.send(receiver.getOutputStream());
                    break;
                default:
                    break;
                }
            }
            pool.remove(group, new Integer(id));
        } catch (SocketException e) {
            pool.remove(group, new Integer(id));
        } catch (IOException e) {
            pool.remove(group, new Integer(id));
        }

    }

    /**
     * Closes the Synchronization Channel Server side Socket.
     */
    protected void close() {
        shutdown = true;
    }

    /**
     * Gets the Synchronization Channel Server side Socket.
     */
    protected Socket getSocket() {
        return socket;
    }
}