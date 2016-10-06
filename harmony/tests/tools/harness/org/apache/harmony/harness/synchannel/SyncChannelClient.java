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
 * @version $Revision: 1.7 $
 */
package org.apache.harmony.harness.synchannel;

import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;

/**
 * SyncChannelClient. Client is created with specified Group name. Group name
 * may be not unique. All clients with the same Group name may be used for the
 * broadcast of the messages.
 */
public class SyncChannelClient {
    private int    client_id         = 0;
    private Socket client            = null;
    private String client_group_name;
    private String host;
    private int    port;

    ArrayList      received_messages = new ArrayList();

    /**
     * Class constructor. Creates SyncChannelClient. It is not connected to
     * SyncChannelServer. To connect created client to the server method
     * connect() should be invoked.
     * 
     * @param argc Array of strings to initialize Synchronization Channel
     *        Server. At the current implementation argc[0] should specify Group
     *        name of this client for registration on the SyncChannelServer.
     *        argc[1] should specify the remote host name, where
     *        SyncChannelServer is started. argc[2] should specify the remote
     *        port number, which is listened by the SyncChannelServer.
     */
    public SyncChannelClient(String[] argc) {
        client_group_name = argc[0];
        host = argc[1];
        port = (new Integer(argc[2])).intValue();
    }

    /**
     * Sends String to the group of the clients, connected to the
     * SyncChannelServer.
     * 
     * @param group_name The Group name of the receivers of the sent message.
     *        If multiple clients are in this group, all clients will receive
     *        the message. If sender is also in this group, all clients will
     *        receive the message, except of the sender. If group consist of
     *        only sender, false will be returned ( message cannot be sent by
     *        the client to itself).
     * @param text The String to send/receive.
     * @return true If text was received by all receivers in the Group.
     * @throws IOException
     */
    public boolean send(String group_name, String text) throws IOException {
        SyncChannelMessage message = new SyncChannelMessage(group_name, 0, text);
        message.setCommand(SyncChannelMessage.GROUP_SEND);
        message.send(client.getOutputStream());
        while (true) {
            message = SyncChannelMessage.receive(client.getInputStream());
            if (message.getCommand() == SyncChannelMessage.SUPPLIED)
                return true;
            else {
                if (message.getCommand() == SyncChannelMessage.NOT_SUPPLIED) {
                    return false;
                } else {
                    received_messages.add(message);
                }
            }
        }
    }

    /**
     * Sends String to the client, connected to the SyncChannelServer. Message
     * cannot be sent by the client to itself. In this case false will be
     * returned.
     * 
     * @param group_name The Group name of the receiver of the sent message.
     * @param id The unique id of the receiver of the sent message.
     * @param text The String to send/receive.
     * @return true If text was received by receiver.
     * @throws IOException
     */
    public boolean send(String group_name, int id, String text)
        throws IOException {
        SyncChannelMessage message = new SyncChannelMessage(group_name, id,
            text);
        message.setCommand(SyncChannelMessage.INDIVIDUAL_SEND);
        message.send(client.getOutputStream());
        while (true) {
            message = SyncChannelMessage.receive(client.getInputStream());
            if (message.getCommand() == SyncChannelMessage.SUPPLIED) {
                return true;
            } else {
                if (message.getCommand() == SyncChannelMessage.NOT_SUPPLIED) {
                    return false;
                } else {
                    received_messages.add(message);
                }
            }
        }
    }

    /**
     * Sends String to the client, connected to the SyncChannelServer. Message
     * cannot be sent by the client to itself. Actually, the result of the
     * sending is undefined.
     * 
     * @param group_name The Group name of the receivers of the sent message.
     *        If multiple clients are in this group, all clients will receive
     *        the message. If sender is also in this group, all clients will
     *        receive the message, except of the sender.
     * @param text The String to send/receive.
     * @throws IOException
     */
    public void cast(String group_name, String text) throws IOException {
        SyncChannelMessage message = new SyncChannelMessage(group_name, 0, text);
        message.setCommand(SyncChannelMessage.GROUP_CAST);
        message.send(client.getOutputStream());
    }

    /**
     * Sends String to the client, connected to the SyncChannelServer. Message
     * cannot be sent by the client to itself. Actually, the result of the
     * sending is undefined.
     * 
     * @param group_name The Group name of the receiver of the sent message.
     * @param id The unique id of the receiver of the sent message.
     * @param text The String to send/receive.
     * @throws IOException
     */
    public void cast(String group_name, int id, String text) throws IOException {
        SyncChannelMessage message = new SyncChannelMessage(group_name, id,
            text);
        message.setCommand(SyncChannelMessage.INDIVIDUAL_CAST);
        message.send(client.getOutputStream());
    }

    /**
     * Receives String from some client, connected to the SyncChannelServer.
     * Message cannot be received by the client from itself.
     * 
     * @return Received String.
     * @throws IOException
     */
    public String receive() throws IOException {
        SyncChannelMessage message;
        if (received_messages.isEmpty()) {
            message = SyncChannelMessage.receive(client.getInputStream());
        } else {
            message = (SyncChannelMessage)received_messages.get(0);
            received_messages.remove(0);
        }
        return message.getText();
    }

    /**
     * Connects client to the SyncChannelServer.
     * 
     * @return Unique Id of the client across all the connected clients.
     * @throws IOException
     */
    public int connect() throws IOException {
        SyncChannelMessage message = new SyncChannelMessage(client_group_name,
            client_id, "");
        client = new Socket(host, port);
        message.send(client.getOutputStream());
        while (true) {
            message = SyncChannelMessage.receive(client.getInputStream());
            if (message.getCommand() == SyncChannelMessage.MESSAGE) {
                received_messages.add(message);
            } else {
                client_id = message.getId();
                return client_id;
            }
        }
    }

    /**
     * Disconnects this client from the SyncChannelServer.
     * 
     * @throws IOException
     */
    public void disconnect() throws IOException {
        SyncChannelMessage message = new SyncChannelMessage(client_group_name,
            client_id, "");
        message.setCommand(SyncChannelMessage.CLOSE);
        message.send(client.getOutputStream());
        client.close();
    }

    /**
     * Gets the Id of the client.
     * 
     * @return The Id of the client.
     */
    public int getId() {
        return client_id;
    }

    /**
     * Gets the Group name of the client.
     * 
     * @return The Group name of the client.
     */
    public String getGroup() {
        return client_group_name;
    }
}