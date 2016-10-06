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

import java.util.HashMap;
import java.io.IOException;
import java.net.Socket;

/**
 * SyncChannelPool is used for registration of connected clients to the
 * Synchronization Channel Server. Is implemented as HashMap of HashMap's. At
 * the first level HashMap keys are Group names of the clients, values are
 * HashMap's, where keys are unique Ids of the clients and values are Sockets of
 * the connected clients.
 */
class SyncChannelClientPool {

    /**
     * HashMap keys - Group names, values - Hash map, where keys - unique
     * client's Ids in this Group, values - Client's Socket
     */
    private HashMap client_pool = new HashMap();

    private int     unique_id   = 0;

    /**
     * Adds the client to the Synchronization Channel Pool. If Id of the client
     * is not unique, it will not be added to the Pool, false will be returned.
     * 
     * @param name The Group name of the connected client.
     * @param socket The Synchronization Channel Server side Socket for this
     *        client.
     * @return The unique Id for the connected client.
     */
    protected int add(String name, Socket socket) {
        synchronized (client_pool) {
            HashMap client;
            unique_id++;
            if (client_pool.containsKey(name)) {
                client = (HashMap)client_pool.get(name);
            } else {
                client = new HashMap();
            }
            client.put(new Integer(unique_id), socket);
            client_pool.put(name, client);
            return unique_id;
        }
    }

    /**
     * Gets the Synchronization Channel Server side Socket for client.
     * 
     * @param name The Group name of the connected client.
     * @param internal_id The Id of the client.
     * @return The Synchronization Channel Server side Socket of the client with
     *         given Group name and Id or null, if client with given Id is not
     *         connected to the Server.
     */
    protected Socket getClient(String name, Integer internal_id) {
        synchronized (client_pool) {
            HashMap client;
            if (client_pool.containsKey(name)) {
                client = (HashMap)client_pool.get(name);
                if (client.containsKey(internal_id)) {
                    return (Socket)client.get(internal_id);
                }
            }
            return null;
        }
    }

    /**
     * Gets the array of Synchronization Channel Server side Sockets for group
     * of clients.
     * 
     * @param name The Group name of the connected clients.
     * @return The array of the Synchronization Channel Server side Sockets of
     *         the clients with given Group name. or null, if there is no any
     *         connected clients with given Group name.
     */
    protected Socket[] getClientsInGroup(String name) {
        synchronized (client_pool) {
            HashMap client;
            if (client_pool.containsKey(name)) {
                client = (HashMap)client_pool.get(name);
                return (Socket[])client.values().toArray(new Socket[0]);
            }
            return null;
        }
    }

    /**
     * Gets the array of Ids for group of clients.
     * 
     * @param name The Group name of the connected clients.
     * @return The array of the Ids of the clients with given Group name. or
     *         null, if there is no any connected clients with given Group name.
     */
    protected Integer[] getIdInGroup(String name) {
        synchronized (client_pool) {
            HashMap client;
            if (client_pool.containsKey(name)) {
                client = (HashMap)client_pool.get(name);
                return (Integer[])client.keySet().toArray(new Integer[0]);
            }
            return null;
        }
    }

    /**
     * Gets the array of the Group names of connected clients.
     * 
     * @return The array of the Group names of connected clients.
     */
    protected String[] getGroups() {
        synchronized (client_pool) {
            return (String[])client_pool.keySet().toArray(new String[0]);
        }
    }

    /**
     * Count of Groups in the Synchronization Channel Pool.
     * 
     * @return The count of Groups in the Synchronization Channel Pool.
     */
    protected int cnGroups() {
        synchronized (client_pool) {
            return client_pool.size();
        }
    }

    /**
     * Count of clients in the Group.
     * 
     * @param name The Group name of the connected clients.
     * @return The count of clients in the Group.
     */
    protected int cnClientsInGroup(String name) {
        synchronized (client_pool) {
            if (client_pool.containsKey(name)) {
                return ((HashMap)client_pool.get(name)).size();
            } else {
                return 0;
            }
        }
    }

    /**
     * Removes the client from the Synchronization Channel Pool.
     * 
     * @param name The Group name of the connected clients.
     * @param internal_id The Id of the client.
     * @return The Synchronization Channel Server side Socket of the client with
     *         given Group name and Id or null, if client with given Group name
     *         and Id is not connected to the Server.
     */
    protected Socket remove(String name, Integer internal_id) {
        synchronized (client_pool) {
            HashMap client;
            Socket res;
            if (client_pool.containsKey(name)) {
                client = (HashMap)client_pool.get(name);
                if (client.containsKey(internal_id)) {
                    res = (Socket)client.remove(internal_id);
                    try {
                        res.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (client.isEmpty()) {
                        client_pool.remove(name);
                    } else {
                        client_pool.put(name, client);
                    }
                    return res;
                } else
                    return null;
            } else
                return null;
        }
    }

    /**
     * Removes (disconnects) all clients from the Synchronization Channel Pool.
     */
    protected void removeAll() {
        synchronized (client_pool) {
            Integer[] keys = (Integer[])client_pool.keySet().toArray();
            int size = keys.length;
            unique_id = 0;
            try {
                for (int i = 0; i < size; i++) {
                    ((Socket)client_pool.get(keys[i])).close();
                    client_pool.remove(keys[i]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}