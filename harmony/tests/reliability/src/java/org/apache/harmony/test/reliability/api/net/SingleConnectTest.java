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
 * Goal: Test making connections to one port on localHost
 *  passed parameters:
 *  parameter[0] - number of threads to start to send packets
 *  
 *   parameter[0] number of sender threads are started, every thread:
 *   * starts creating socket conections to localHost by reserved port number
 *  
 *  No hangs, crashes or fails are expected
 *  
 *  */

public class SingleConnectTest extends Test{
    static volatile boolean failed = false;
    static final int NUMBER_OF_THREADS = 100;
    static int numberOfThreads = NUMBER_OF_THREADS;
    final static int STARTING_PORT_NUMBER = 8100;
    static int numberOfAcceptedRequests = 1000;
    static int numberOfTriesToOpenPort = 2000;
    static int portStarted = 0;

    public static void main(String[] params){
        System.exit(new SingleConnectTest().test(params));
    }
    
    public int test(String[] params){
        parseParams(params);

        ServerRunner1 sPort = null;
        for (int i=0; i<numberOfTriesToOpenPort; i++){
            sPort = new ServerRunner1(STARTING_PORT_NUMBER + i);
            if (sPort.started){
                break;
            }
        }
        if (!sPort.isAlive()){
            return fail("Failed to start any server");
        }
        portStarted = sPort.port;
        
        Thread[] thrds = new Thread[numberOfThreads];
        for (int i=0; i< numberOfThreads; i++){
            thrds[i] = new portScannerRunner1();
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
        
    }
}

class ServerRunner1 extends Thread{
    int port = 0;
    int counter = 0;
    boolean started = false;
    ServerRunner1(int p){
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
        started = true;
        while (counter < SingleConnectTest.numberOfAcceptedRequests){
            try {
                ss.accept();
            } catch (IOException e) {
                SingleConnectTest.log.add("Server thread " + getId() + " could not accept request: " + e.getMessage());
                SingleConnectTest.failed = true;
                return;
            }
            counter++;
        }
        try {
            ss.close();
        } catch (IOException e) {
            SingleConnectTest.log.add("Failed to close ServerSocket on port " + port);
            SingleConnectTest.failed = true;
            return;
        }
        
    }
    
}


class portScannerRunner1 extends Thread{
    
    public void run (){
        
        ArrayList<Socket> connections = new ArrayList<Socket>(); 
        while (true){
            Socket sc = null;
            try {
                sc = new Socket(InetAddress.getLocalHost(), SingleConnectTest.portStarted);
                connections.add(sc);
            } catch (UnknownHostException e) {
                SingleConnectTest.log.add("Failed to locate localhost.");
                SingleConnectTest.failed = true;
                return;
            } catch (IOException e) {
                // Ignore - server could already down or max allowed connections number is reached
                break;
                
            }
            
            try {
                sc.close();
            } catch (IOException e) {
                SingleConnectTest.log.add("Failed to close socket to localhost by port " + (SingleConnectTest.portStarted) + " : " + e.getMessage());
                SingleConnectTest.failed = true;
            }
        }
        
    }
    
}
    
