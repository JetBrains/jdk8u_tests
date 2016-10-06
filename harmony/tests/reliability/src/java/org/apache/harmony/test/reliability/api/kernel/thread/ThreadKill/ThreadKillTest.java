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
 * @author Igor A. Pyankov
 * @version $Revision: 1.1 $
 */

package org.apache.harmony.test.reliability.api.kernel.thread.ThreadKill;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import org.apache.harmony.test.reliability.share.Test;

/**
 *  Goal: check VM thread subsystem/GC cooperations 
 *        The test does:
 *        1. Reads parameters, which are:
 *           param[0] - number of threads
 *           param[1] - how times repeat test
 *        2. Creates param[0]-sized array of threads and starts them
 *           There are 2 kind of threads - math (does some calculation)
 *           and input/output (imitates output)
 *        3. randomly interrupts threads
 *        4. interrupts remaining threads
 *        5. call .join() for each thread to sure that
 *           all threads have been killed
 *        6. repeat 2-5  param[1] times
 * 
 *        No exceptions must be trown.
 *    
 */

public class ThreadKillTest extends Test {
    public static Random rand = new Random();
    public static Object lock = new Object();
    public static int THREADS_COUNT = 10;
    public static int CYCLES_COUNT = 10;
    public static int[] table;   
    public static volatile int started = 0;

    public static void main(String[] args) {
        System.exit(new ThreadKillTest().test(args));
    }

    public int test(String[] params) {
        parseParams(params);
        //table = new int[THREADS_COUNT];
        started = 0;
        for (int i = 0; i < CYCLES_COUNT; i++) {
            Thread t[] = new Thread[THREADS_COUNT];            
            for (int k = 0; k < THREADS_COUNT;) {
                t[k] = new MathThread(k);                    
                k++;
                t[k] = new IOThread(k);       
                k++;
            } 
            try {                    
                for (int k = 0; k < THREADS_COUNT; k++) {                 
                    t[k].start();
                }                
                // just wait for all threads started                
                while (started < THREADS_COUNT) {
                    Thread.yield();    
                }
                // random murder
                for (int k = 0; k < THREADS_COUNT*5; k++) {
                    int j = rand.nextInt(THREADS_COUNT);
                    t[j].interrupt();
                }                               
                
                //to kill survived thread 
                for (int k = 0; k < THREADS_COUNT; k++) {
                    if (!t[k].isInterrupted()) {
                        t[k].interrupt();
                    }
                }
                
                // wait for till all threads will be killed
                int l = 1;
                while (l > 0) {
                    l = 0;
                    for (int k = 0; k < THREADS_COUNT; k++) {
                        if (t[k].isAlive()) {
                            l++;
                        }
                        Thread.yield();
                    }                    
                }
                // to be sure all started treads are killed     
                for (int k = 0; k < THREADS_COUNT; k++) {
                    t[k].interrupt();
                    t[k].join();
                }
            } catch (InterruptedException ie) {
                return fail("interruptedException while join of threads");
            }
        }
        return pass("OK");
    }

    public void parseParams(String[] params) {
        if (params.length >= 1) {
            THREADS_COUNT = Integer.parseInt(params[0]);
            if (params.length >= 2) {
                CYCLES_COUNT = Integer.parseInt(params[1]);
            }
        }
    }

}


class MathThread extends Thread {
    private int index;

    public MathThread(int index) {
        this.index = index;
    }

    public void run() {        
        // just verify sin^2+cos^2==1
        double alfa;
        double sinalfa;
        double cosalfa;
        double beta ;
        double gamma = 1.0D;
        double delta = 1.0E-17;
        int num_errors = 0;
        ThreadKillTest.started++;

        //ThreadKillTest.log.add("Theard " + index + " started");
        while (true) {
            alfa = ThreadKillTest.rand.nextDouble() * 2.D * java.lang.Math.PI;
            cosalfa = java.lang.Math.cos(alfa);
            sinalfa = java.lang.Math.sin(alfa);
            beta = sinalfa * sinalfa + cosalfa * cosalfa;

            if (java.lang.Math.abs(beta - gamma) > delta) {
                // ThreadKillTest.log.add(index + " error " + beta);
                num_errors++;
            } else {
                //ThreadKillTest.log.add (index + " OK " + beta);
            }
            
            // place to kill thread 
            synchronized (ThreadKillTest.lock) {
                try {
                    ThreadKillTest.lock.wait(500);
                } catch (InterruptedException ie) {
                    //ThreadKillTest.log.add("MathTheard " + index + " killed");
                    return;
                }
            }
        }
    }
}

class IOThread extends Thread {
    private int index;
    private int buflen = 256;
    private byte b[];
    public IOThread(int index) {
        this.index = index;
    }

    public void run() {
        int k = 1;
        FakeInputStream fis = new FakeInputStream ();
        FakeOutputStream fos = new FakeOutputStream ();    
        ThreadKillTest.started++;

        //ThreadKillTest.log.add("Theard " + index + " started");
        while (k > 0) {

            b = new byte[buflen];
            try {
                k = fis.read(b);
            } catch (IOException ie1) {
                k = -10;
            }
            if (k >= 0) {
                for (int i = 0; i < k; i++) {
                    b[i] = (byte) (b[i] + 12);
                }

                try {
                    fos.write(b, 0, k - 1);
                } catch (IOException ie2) {
                    k = -20;
                }
            }
            // place to kill thread
            synchronized (ThreadKillTest.lock) {
                try {
                    ThreadKillTest.lock.wait(100);
                } catch (InterruptedException ie) {
                    //ThreadKillTest.log.add("IOThread " + index + " killed");
                    return;
                }
            }
        }
    }
}

class FakeOutputStream extends OutputStream {
    private byte b[];
    private int pointer;
    private int buflen = 128;
    
    public FakeOutputStream () {
        b = new byte [buflen];
        pointer = 0;
    }
 
    public void write(int arg) throws IOException {
        // fills buffer and does nothing
        b[pointer++]=(byte) arg;
        if (pointer == buflen) {
            pointer = 0;
        }
    }
}

class FakeInputStream extends InputStream {
    private byte b[];
    private int pointer;
    private int buflen = 128;

    public FakeInputStream() {
        b = new byte[buflen];
        ThreadKillTest.rand.nextBytes(b);
        pointer = 0;
    }

    public int read() throws IOException {
        if (pointer == buflen) {
            pointer = 0;
            ThreadKillTest.rand.nextBytes(b);
        }
        return (int) b[pointer++];
    }
}