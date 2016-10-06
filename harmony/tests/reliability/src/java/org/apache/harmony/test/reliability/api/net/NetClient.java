/*
 * Copyright 2006 The Apache Software Foundation or its licensors, as applicable
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
 * @author Nikolay V. Bannikov
 * @version $Revision: 1.5 $
 */

package org.apache.harmony.test.reliability.api.net;

import org.apache.harmony.test.reliability.share.Test;


import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.net.InetAddress;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.UnknownHostException;

/**
 * Goal: find errors/failures of Networking API while intensive work
 *       with socket servers/clients.
 *
 * The test does:
 * 
 * 1. There is golden file for reading from a server upon socket client requests.
 * 
 * 2. The socket server is created and run.
 * 3. A lot of socket clients are created in thread and each of them try to 
 *    connect to the server.
 *    
 * 4. After successful connection the client reads the golden file into a file 
 *    created 'on the fly' and writes it in a directory.
 * 
 * 5. Then the file's content is compared with the golden file.
 * 
 * 6. The test passes is comparison is OK.
 * 
 * 7. All created file are deleted.
 * 
 */

public class NetClient  extends Test {

    public int THREAD = 4;
    public static int CNT = 100;
    public static int PORT = 8000;
    public static String path_to_files;
    public final static int SLEEP = 1000; 
    ServerSocket server;

    private Socket socket;
    private BufferedReader bufReader, in;
    private PrintWriter prnWriter;
    private FileInputStream fis;
    private static int clientcounter = 0;
    private final static int NUMBER_OF_TRIES_TO_START_SERVER = 50;

    private static int threadcnt = 0;
    private static int cycle = 0;
    private static String golden = "Test.out";
    private static String golden_lnx = "Test_lnx.out";

    private boolean result = true;
    private String fileName = "";

    private String separator = File.separator;
    private String filedir = System.getProperty("java.io.tmpdir") + separator + "reliability_net";

    public int threadcnt() {
        return threadcnt;
    }

    public int cnt() {
        return clientcounter;
    }

    public boolean result() {
        return result;
    }


    public int test(String[] params) {

        if (params.length >= 1) {
            path_to_files =  params[1];
        }        

        if (params.length >= 2) {
            PORT = Integer.parseInt(params[2]);
        }        

        PORT = PORT + cycle++;

        if (params.length >= 0 && Integer.parseInt(params[0]) > 0) {
            CNT = Integer.parseInt(params[0]);
        }        

        if (params.length >= 3 && Integer.parseInt(params[3]) > 0) {
            THREAD =  Integer.parseInt(params[3]);
        }
        
        if (!path_to_files.endsWith("auxiliary")) {
            log.add("invalid path to the golden files");
            return fail("Failed.");
        }

        if (params.length >= 4) {
            golden = params[4];
        }

        //log.add("tmpdir = " + filedir);

        File temp = new File(filedir);
        temp.mkdir();
        temp.deleteOnExit();

        
        //log.add("PORT = " + PORT++);
        // try to create ServerSocket NUMBER_OF_TRIES_TO_START_SERVER times with 
        // different PORT values as some PORT values could already be taken!
        int count = 0;
        while (count < NUMBER_OF_TRIES_TO_START_SERVER){
            try {    
                server = new ServerSocket(NetClient.PORT);
                log.add("Creates a server socket " + server + ", bound to the" + NetClient.PORT + " port");
            }  catch (Exception e) {
                // try next PORT number
                count++;
                NetClient.PORT++;
                continue;
            }
            break;
        }
         
        if (server == null){
                log.add("Creates a ServerSocket in the range(" + NetClient.PORT + ":" + NetClient.PORT + "): Exception during test execution");
            return fail("Failed.");
        }

        // Listens for a connection to be made to socket and  read file        
        Thread srv = new Thread(
            new Runnable() {
                public void run() {
                    int i = 0;
                    try {
                        while(i != NetClient.CNT) {
                            i++;
                            Socket socket = null;
                            socket = server.accept();                    
                            new SockServer(socket);
                        }
                    }  catch (Exception e) {
            
                        log.add(e);
                        log.add("Creates a SockServer" + i + ": Exception during test execution");

                    } finally {
                        try {
                            server.close();
                            //log.add("server closed");
                        }  catch (IOException e) {
                            log.add("Server closed: Exception during test execution");
                            log.add(e);
                        }
                    }

                }
            }
        );

        srv.start();

        InetAddress ia = null;

        try {    
            ia = InetAddress.getByName("localHost");

        }  catch (UnknownHostException e) {
        
            log.add(e);
            return fail("no IP address for the host");
        }

        //Creates some sockets and connects to the specified port 
        while(true) {

                if(threadcnt() < THREAD) {

                       Client client =  new Client(ia);
                }

                try {    
                    Thread.currentThread().sleep(NetClient.SLEEP);
                }  catch (InterruptedException e) {
                    log.add(e);
                    return fail("another thread has interrupted the current thread");

                 }
                if(cnt() == CNT) {
                    clientcounter = 0;
                    break;
                }
        }


        if (result()) {
                return pass("OK");
        } else {
                return fail("Failed.");
        }
    }


    private class Client extends Thread {

        private int id = clientcounter++;

        public Client(InetAddress ia) {

            threadcnt++;

            try {
                //log.add("Creates a new socket with IP address " + ia + " and connects it to the port " + NetClient.PORT);                                
                socket = new Socket(ia, NetClient.PORT);
            }  catch (Exception e) {

                log.add(e);
                log.add("error occurs when creating the Socket(ia, NetClient.PORT): Exception during test execution");

            } 
            try {

                fileName = filedir + separator + "Test" + id + ".out";
                bufReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                prnWriter = new PrintWriter(new BufferedWriter(new FileWriter(new File(fileName)))); 
                start();

            } catch (Exception e) {

                log.add(e);
                log.add("error occurs when writting to the " + fileName);

                try {
                    socket.close();
                }  catch (Exception ee) {
                    ee.printStackTrace();
                }
            }    
        }
        //two files to be tested for equality
        public boolean checkFiles(String golden, String filename, BufferedReader bufReader, PrintWriter prnWriter, int buf) {

            String str = new String();
            File file = new File(fileName);
            byte a[] = new byte[buf];
            byte b[] = new byte[buf];
            // read lines from BufferedReader and write to the file
            try {
                while((str = bufReader.readLine()) != null) {
                    prnWriter.println(str);
                }
            }  catch (IOException e) {
                log.add(e);
                log.add("error occurs when reading line from file" + fileName);
            } finally {
                try {
                    prnWriter.close();
                    bufReader.close();
                }  catch (IOException e) {
                    log.add(e);
                }
            }
            // write golden file to the array
            try {
                fis = new FileInputStream(golden);
                fis.read(a);
            }  catch (FileNotFoundException e) {
                log.add(e);
                log.add("The file " + golden + "does not exist");
            }  catch (IOException ee) {
                log.add(ee);
                log.add("error occurs when reading byte a[] from  file" + golden);
            } finally {
                try {
                    fis.close();
                }  catch (IOException e) {
                    log.add(e);
                }
            }
            // write a file to the array
            try {
                fis = new FileInputStream(file);
                fis.read(b);
            }  catch (FileNotFoundException e) {
                log.add(e);
                log.add("The file " + fileName + "does not exist");
            }  catch (IOException ee) {
                log.add(ee);
                log.add("error occurs when reading byte b[] from  file" + fileName);
            } finally {
                try {
                    fis.close();
                    socket.close();
                }  catch (IOException e) {
                    log.add(e);
                }
                threadcnt--;
            }

            // arrays to be tested for equality
            if(java.util.Arrays.equals(a, b)) {
                    //log.add("checks "+ filename + " : true");
                    return true;
            } else {
                    log.add("checks "+ filename + " : false");        
                    return false;
            }
        }

        public void run() {

            String pathtogolden = NetClient.path_to_files + separator +  golden;
            if(System.getProperty("os.name").indexOf("Windows") == -1) {
                pathtogolden = NetClient.path_to_files + separator +  golden_lnx;
            }
            if(!checkFiles(pathtogolden, fileName, bufReader, prnWriter, 10000)) {
                result = false;
                log.add("checks the file " + fileName + ": FAILED");
            }
            new File(fileName).delete();
        }  
    }

    private class SockServer extends Thread {

        private Socket socket;
        private BufferedReader bufReader, in;
        private PrintWriter prnWriter;

        public SockServer(Socket s) throws Exception {
            socket = s;
            //log.add("read text from the golden file");
            in =new  BufferedReader(new FileReader(NetClient.path_to_files + File.separator + golden));
            String str1 = new String();
            String str2 = new String();
            while((str1 = in.readLine()) != null) {
                str2 += str1 + "\n";
            }
            //log.add("write text from the golden file to the outputstream");
            bufReader = new BufferedReader(new StringReader(str2));
            prnWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            start();
        }

        public void run() {
            try {
                String str1 = new String();
                while((str1 = bufReader.readLine()) != null) {
                    prnWriter.println(str1);
                }
                prnWriter.close();
                bufReader.close();
                in.close();
            }  catch (Exception e) {
                    log.add(e);
                    log.add("error occurs when closing the streams");
            } finally {
                try {
                    socket.close();
                }  catch (Exception e) {
                    log.add(e);
                    log.add("error occurs when closing the Socket");
                }
            }

        } 
    }

    public static void main(String[] args) {
        System.exit(new NetClient().test(args));
    }

}
