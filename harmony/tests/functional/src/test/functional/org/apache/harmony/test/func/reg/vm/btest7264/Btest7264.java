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
/**
 */
package org.apache.harmony.test.func.reg.vm.btest7264;

import java.util.logging.Logger;
import org.apache.harmony.test.share.reg.RegressionTest;
import java.net.*;
import java.io.*;

/**
 */
public class Btest7264 extends RegressionTest {
    public static void main(String[] args) {
        System.exit(new Btest7264().test(Logger.global, args));
    }

    public int test(Logger logger, String[] args) {
        try {
            System.out.println("Socket test:");
            Accept acc = new Accept();
            Thread th = new Thread(acc);
            th.setDaemon(true);
            th.start();
            Thread th2 = new Exceptional();
            th2.setDaemon(true);
            th2.start();
            Socket s = new Socket("localhost", 1099);
            OutputStream os = s.getOutputStream();
            byte[] barr = "Hello World. This is a very very long message".getBytes();
            for(int i = 0; i < barr.length; ++i) {
                Thread.sleep(200);
                os.write(barr[i]);
            }
            //os.close();
            if (acc.error) {
                return fail();
            } else {
                return pass();
            }
        } catch (Throwable t) {
            return error();
        }
    }
}

class Exceptional extends Thread {
    public void run() {
        try {
            Thread.sleep(1500);
            File f = File.createTempFile("xxxx", "yyyy");
            f.delete();
            new FileInputStream(f);
        } catch(IOException e) {
            System.err.println("got exception - no wonder");
        } catch(Throwable e) {
            e.printStackTrace();
        }
    }
}

class Accept implements Runnable {
    public boolean error = false;
    ServerSocket ss = null;
    public Accept() throws Throwable {
        ss = new ServerSocket(1099);
    }

    public void run() {
        try {
            Socket s = ss.accept();
            while(true) {
                InputStream is = s.getInputStream();
                int c;
                while(true) {
                    try {
                        c = is.read();
                        if (c == -1) return;
                        System.out.print((char)c);
                    } catch (IOException e) {
                        error = true;
                        System.out.println("Exception in read: " + e);
                        e.printStackTrace();
                    }
                }
            }
        }
        catch(Throwable t) {
            error = true;
            System.out.println("Exception in run: "+t);
            t.printStackTrace();
        }
    }
}

