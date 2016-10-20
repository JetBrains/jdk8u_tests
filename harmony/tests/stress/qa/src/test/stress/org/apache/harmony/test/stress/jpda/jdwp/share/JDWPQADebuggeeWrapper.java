/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */    


package org.apache.harmony.test.stress.jpda.jdwp.share;

import java.io.IOException;

import org.apache.harmony.share.framework.jpda.LogWriter;
import org.apache.harmony.share.framework.jpda.StreamRedirector;
import org.apache.harmony.share.framework.jpda.TestErrorException;
import org.apache.harmony.share.framework.jpda.TestOptions;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPDebuggeeWrapper;
import org.apache.harmony.share.framework.jpda.jdwp.TransportWrapper;


/**
 * Created on 15.04.2005 
 * @author Alexei S.Vaskin
 * @version $Revision: 1.4 $
 */
public class JDWPQADebuggeeWrapper extends JDWPDebuggeeWrapper {

    //public final static String DEFAULT_ATTACHING_ADDRESS = "localhost:9898";
    
    private boolean server;
    Process process;
    StreamRedirector err;
    StreamRedirector out;
    TransportWrapper transport;

    /**
     * @param settings
     * @param logWriter
     * @param connectorType
     */
    public JDWPQADebuggeeWrapper(TestOptions settings, LogWriter logWriter, boolean server) {
        super(settings, logWriter);
        this.server = server;
    }

    /**
     * Launches VM and sets connection with it  
     */
    public void start() {
        transport = createTransportWrapper();

    	if (server) {
    	    listen(); 
    	} else {
    	    attach();
    	}
    }
    
    private void listen(){
        try {
            String address = settings.getTransportAddress();
            address = transport.startListening(address);
            logWriter.println("Launching as a server listening on: " + address);
            launchVM(address, true);
            logWriter.println("Accepting connection");
            transport.accept(settings.getTimeout(), settings.getTimeout());
            setConnection(transport);
            logWriter.println("Established connection");
        } catch (IOException e) {
            logWriter.printError(e);
            throw new TestErrorException(e);
        } finally {
            try {
                transport.stopListening();
            } catch (IOException e) {
                logWriter.printError("An exception is thrown during a call to stopListening", e);
            }
        }
    }
    
    private void attach() {
        String address = settings.getTransportAddress();
        if (address == null) {
            address = TestOptions.DEFAULT_ATTACHING_ADDRESS;
        }
        try {
            logWriter.println("Launching debuggee VM in debug mode as a server on address: " + address);
            launchVM(address, false);            
            logWriter.println("Attaching to debuggee on address: " + address);
            transport.attach(address, settings.getTimeout(), settings.getTimeout());
            setConnection(transport);
            logWriter.println("Established connection");
        } catch (IOException e) {
            logWriter.printError(e);
            throw new TestErrorException(e);
        }
    }
    
    protected void launchVM(String address, boolean bServer){
        
        String cmdLine = settings.getDebuggeeJavaPath()
        	+ " -cp \"" + settings.getDebuggeeClassPath()
        	+ "\" -agentlib:" + settings.getDebuggeeAgentName() + "="
        	+ settings.getDebuggeeAgentOptions(address, bServer) + " "
        	+ settings.getDebuggeeVMExtraOptions() + " "
        	+ settings.getDebuggeeClassName();        	
        	
        logWriter.println("Launching: " + cmdLine);
        try {
            process = launchProcess(cmdLine); 
            if (process != null) {
                logWriter.println("Start redirectors");
                
                out = new StreamRedirector(process.getInputStream(),
                        logWriter, "STDOUT");
                out.start();
                err = new StreamRedirector(process.getErrorStream(),
                        logWriter, "STDERR");
                err.start();
            }
        } catch (IOException e) {
            logWriter.printError(e);
            throw new TestErrorException(e);
        }
                
    }     
    
    /**
     * Launches process with given command line.
     * 
     * @param cmdLine
     *            command line
     * @return associated Process object or null if not available
     * @throws IOException
     *             if error occured in launching process
     */
    protected Process launchProcess(String cmdLine) throws IOException {
        process = Runtime.getRuntime().exec(cmdLine);
        return process;
    }

    /* (non-Javadoc)
     * @see org.apache.harmony.share.test.framework.jpda.DebuggeeWrapper#stop()
     */
    public void stop() {
    	closeConnection();

    	if (process != null) {
            ProcessWaiter thrd = new ProcessWaiter();
            thrd.start();
            try {
                thrd.join(settings.getTimeout());
            } catch (InterruptedException e) {
                throw new TestErrorException(e);
            }

            if (thrd.isAlive()) {
                thrd.interrupt();
            }

            try {
                int exitCode = process.exitValue();
                logWriter.println("Finished debuggee: " + exitCode);
            } catch (IllegalThreadStateException e) {
                logWriter.printError("Enforced debuggee termination");
            }
            process.destroy();
        }

        if (out != null)
            out.exit();
        if (err != null)
            err.exit(); 
    }

    private void closeConnection() {
        try {
            vmMirror.closeConnection();
        } catch(IOException e) {
            logWriter.printError(e);
        }       
    }
    
    public class ProcessWaiter extends Thread {
        public void run() {
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                logWriter.println("ProcessWaiter thread interrupted: " + e);
            }
        }
    }
}

