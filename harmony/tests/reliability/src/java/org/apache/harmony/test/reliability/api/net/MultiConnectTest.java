/*
 * Copyright 2007 The Apache Software Foundation or its licensors, as applicable
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Aleksey Ignatenko
 * @version $Revision: 1.0 $
 */

package org.apache.harmony.test.reliability.api.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.net.InetAddress;

import org.apache.harmony.test.reliability.share.Test;

/*
 * Goal: Test making connections to many ports on localHost
 *  passed parameters:
 *  parameter[0] - number of scanner threads to start
 *  parameter[1] - number of ports opened simultaneously
 *  
 *  The test Thread does the following:
 *   parameter[1] number of server threads are started, every thread:
 *   * has own unique port number to process requests
 *   * starts receiving connections by ServerSocket.accept() method in a cycle
 *   * cycle is numberOfAcceptedRequests times

 *   parameter[0] number of sender threads is started, every thread:
 *   * starts creating socket conections to localHost by every reserved port number
 *  
 *  No hangs, crashes or fails are expected
 *  
 *  Note: choose input parameters values properly to avoid "max allowed connections exceeded" error to appear
 *  */

public class MultiConnectTest extends Test{
    static volatile boolean failed = false;
    static final int NUMBER_OF_THREADS = 10;
    static int numberOfThreads = NUMBER_OF_THREADS;
    final static int STARTING_PORT_NUMBER = 8100;
    static int numberOfPortsToOpen = 2000;
    static int numberOfAcceptedRequests = 1000;
    static ServerRunner[] sPorts = null;

    public static void main(String[] params){
        System.exit(new MultiConnectTest().test(params));
    }
    
    public int test(String[] params){
        parseParams(params);

        sPorts = new ServerRunner[numberOfPortsToOpen];
        for (int i=0; i<numberOfPortsToOpen; i++){
            sPorts[i] = new ServerRunner(STARTING_PORT_NUMBER + i);
        }

        Thread[] thrds = new Thread[numberOfThreads];
        for (int i=0; i< numberOfThreads; i++){
            thrds[i] = new portScannerRunner();
            thrds[i].start();
        }

        for (int i=0; i< numberOfThreads; i++){
            try {
                thrds[i].join();
            } catch (InterruptedException e) {
                log.add("Failed to join thread " + thrds[i].getId());
                failed = true;
            }
        }
        
        if (failed == true){
            return fail("FAILED");
        }

        return pass("OK");
    }
    
    public void parseParams(String[] params) {

        if (params.length >= 1) {
            numberOfThreads = Integer.parseInt(params[0]);
        }
        if (params.length >= 2) {
            numberOfPortsToOpen = Integer.parseInt(params[1]);
        }
        
    }
}

class ServerRunner extends Thread{
    int port = 0;
    int counter = 0;
    ServerRunner(int p){
        port = p;
        start();
    }
    public void run (){
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
        } catch (IOException e1) {
            // Ignore - the port could already be used
            return;
        }        
        while (counter < MultiConnectTest.numberOfAcceptedRequests){
            try {
                ss.accept();
            } catch (IOException e) {
                MultiConnectTest.log.add("Server thread " + getId() + " could not accept request: " + e.getMessage());
                MultiConnectTest.failed = true;
                return;
            }
            counter++;
        }
        try {
            ss.close();
        } catch (IOException e) {
            MultiConnectTest.log.add("Failed to close ServerSocket on port " + port);
            MultiConnectTest.failed = true;
            return;
        }
        
    }
    
}


class portScannerRunner extends Thread{
    
    public void run (){
        
        ArrayList<Socket> connections = new ArrayList<Socket>(); 
        for (int i=0; i<MultiConnectTest.numberOfPortsToOpen; i++){
            Socket sc = null;
            try {
                sc = new Socket(InetAddress.getLocalHost(), MultiConnectTest.STARTING_PORT_NUMBER + i);
                connections.add(sc);
            } catch (UnknownHostException e) {
                MultiConnectTest.log.add("Failed to locate localhost.");
                MultiConnectTest.failed = true;
                return;
            } catch (IOException e) {
                // Ignore - server could already be down
                return;
            }
            
            try {
                sc.close();
            } catch (IOException e) {
                MultiConnectTest.log.add("Failed to close socket to localhost by port " + (MultiConnectTest.STARTING_PORT_NUMBER + i) + " : " + e.getMessage());
                MultiConnectTest.failed = true;
            }
        }
        
    }
    
}
    
