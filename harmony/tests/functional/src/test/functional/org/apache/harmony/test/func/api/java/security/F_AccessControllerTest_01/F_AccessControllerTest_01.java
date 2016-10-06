/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.harmony.test.func.api.java.security.F_AccessControllerTest_01;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
//import java.security.AccessControlException;
import org.apache.harmony.test.func.share.ScenarioTest;
import auxiliary.SimpleServerSocket;
import auxiliary.SocketClient;

/**
 * Created on 25.10.2004
 * Modified on 09.11.2004 
 */
public class F_AccessControllerTest_01 extends ScenarioTest
{
    boolean isServerListen = false;
    public int test() 
    {    
        System.setSecurityManager(new SecurityManager());
        ServerSocketThread t = new ServerSocketThread("SimpleServerSocketThread");
        t.start();        
        // trying to connect to the server and to send data until timeout is reached or 
        // data sent successfully, whatever comes first        
        while (t.isAlive())
        {
            if (isServerListen) {
                SocketClient client = new SocketClient(testArgs[0]);
                if (client.Init())
                {                    
                    client.Send(testArgs[1]);
                    return pass("Test passed");
                }
            }
        }
    
        return fail("FAILED");       
    }
    
    public static void main(String[] args) 
    {
        System.exit(new F_AccessControllerTest_01().test(args));
    }
    
    public class ServerSocketThread extends Thread
    {        
        public ServerSocketThread(String name) {
            super(name);
        }        
        
        public void run()
        {            
            try
            {
                SimpleServerSocket socket = new SimpleServerSocket();                
                if (socket.createSocket()) 
                {                
                    isServerListen = true;
                    socket.doListen();    
                    isServerListen = false;
                }            
            }            
            catch (SocketException se)
            {
                se.printStackTrace();
                log.info("SocketException: " + se.getMessage());
            }                       
            catch (SocketTimeoutException ste)
            {
                log.info("Timeout expired.");
            }                    
            catch (IOException ioe)
            {   
                ioe.printStackTrace();
                log.info("IOException: " + ioe.getMessage());
            }
/*            catch (AccessControlException ace)
            {
                ace.printStackTrace();
                log.info("AccessControlException in SimpleServerSocket ctor: " + ace.getMessage());
            }
/**/                                                
        }
    }
}