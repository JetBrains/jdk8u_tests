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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.net.InetAddress;

import org.apache.harmony.test.reliability.share.Test;

/*
 * Goal: Test datagram packets send/receive operations 
 *  passed parameters:
 *  parameter[0] - number of threads to start to send packets
 *  
 *  The test Thread does the following:
 *   parameter[1] number of receiver threads is started, every thread:
 *   * starts receiving data by DatagramSocket

 *   parameter[0] number of sender threads is started, every thread:
 *   * starts sending datagram packets to localhost with predefined port numbers
 *  
 *  The test is stopped after TIME_FOR_ONE_ITERATION_TO_WORK timeout
 *  
 *  No hangs, crashes or fails are expected
 *  */

public class DatagramTest extends Test{
    static volatile boolean failed = false;
    static final int NUMBER_OF_THREADS = 30;
    static int numberOfThreads = NUMBER_OF_THREADS;
    volatile static boolean stopThreads = false;  
    final static int STARTING_PORT_NUMBER = 8100;
    final static int NUMBER_OF_TRIES_TO_CREATE_SOCKET = 1000;
    final static int NUMBER_OF_RECEIVERS = 10;
    final static int TIME_FOR_ONE_ITERATION_TO_WORK = 1; //minutes
    int[] portNumbers = null;

    public static void main(String[] params){
        System.exit(new DatagramTest().test(params));
    }
    
    public int test(String[] params){
        parseParams(params);
        stopThreads = false;
        Thread[] rthrds = new Thread[NUMBER_OF_RECEIVERS];
        Thread[] sthrds = new Thread[numberOfThreads];
        portNumbers = new int[NUMBER_OF_RECEIVERS];
        for (int i = 0; i<NUMBER_OF_RECEIVERS; i++){
            receiveRunner rr = null;;
            rthrds[i] = rr = new receiveRunner(STARTING_PORT_NUMBER + i, this);
            portNumbers[i] = rr.initialize();
            if (portNumbers[i] == -1 || failed == true){
                return fail("Failed to create DatagramSocket with port number " + (STARTING_PORT_NUMBER + i));
            }
            rthrds[i].start();
        }
        
        for (int i = 0; i<numberOfThreads; i++){
            sthrds[i] = new sendRunner(this);
            if (failed == true){
                return fail("FAILED");
            }
            sthrds[i].start();
        }

        try {
            Thread.currentThread().sleep(TIME_FOR_ONE_ITERATION_TO_WORK * 60 * 1000);
        } catch (InterruptedException e1) {
            return fail("Main thread was interrupted");
        }
        stopThreads = true;
        
        for (int i = 0; i<NUMBER_OF_RECEIVERS; i++){
            try {
                rthrds[i].join();
            } catch (InterruptedException e) {
                return fail("FAILED");
            }
        }
        for (int i = 0; i<numberOfThreads; i++){
            try {
                sthrds[i].join();
            } catch (InterruptedException e) {
                return fail("FAILED");
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

class receiveRunner extends Thread{
    Random rm = new Random();
    int port = 0;
    DatagramSocket ds = null;
    static final int NUMBER_BYTES_TO_RECEIVE = 100;
    DatagramTest base = null;
    
    receiveRunner(int p, DatagramTest b){
        base = b;
        port = p;
        initialize();
    }
    
    int initialize(){
        int counter = 0;
        while (counter < DatagramTest.NUMBER_OF_TRIES_TO_CREATE_SOCKET){
            try {
                ds = new DatagramSocket(port + counter);
            } catch (SocketException e) {
                //DatagramTest.log.add("Failed to create DatagramSocket on port " + port);
                counter++;
                continue;
            }
            break;
        }
        if (ds != null){
            return (port + counter);
        }else{
            return -1;
        }
    }
    
    public void run (){
        while (!DatagramTest.stopThreads){
            byte buf[] = new byte[NUMBER_BYTES_TO_RECEIVE];
            DatagramPacket dp = new DatagramPacket(buf, buf.length);
            try {
                ds.receive(dp);
            } catch (IOException e) {
                // possible
            }
            byte[] data = dp.getData();
            dp.getLength();
            String msg = new String(data);
            //System.out.println(msg);
            dp.getOffset();
            dp.getPort();
        }
        ds.close();
    }
    
}
    
class sendRunner extends Thread{
    Random rm = new Random();
    DatagramSocket ds = null;
    DatagramTest base = null;
    
    sendRunner(DatagramTest b){
        base = b;
        try {
            ds = new DatagramSocket();
        } catch (SocketException e) {
            DatagramTest.stopThreads = true;
            DatagramTest.log.add("Failed to create DatagramSocket in " + getId() + " thread");
            DatagramTest.failed = true;
        }
    }
    
    public void run (){
        while (!DatagramTest.stopThreads){
            String msg = "Thread " + getId() + " sends you Hello"; 
            DatagramPacket dp;
            int port = base.portNumbers [rm.nextInt(DatagramTest.NUMBER_OF_RECEIVERS)];
            try {
                dp = new DatagramPacket(msg.getBytes(), msg.getBytes().length ,InetAddress.getLocalHost(), port);
            } catch (UnknownHostException e) {
                DatagramTest.stopThreads = true;
                DatagramTest.log.add("Failed to create DatagramPacket in " + getId() + " thread, localhost, port " + port);
                DatagramTest.failed = true;
                return;
            }
            
            try {
                ds.send(dp);
            } catch (IOException e) {
                DatagramTest.stopThreads = true;
                DatagramTest.log.add("Failed to send packet in " + getId() + " thread, localhost, port " + port);
                DatagramTest.failed = true;
            }
        }
        ds.close();
    }
    
}
    
