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
package org.apache.harmony.test.func.api.java.io.share.OutputStream;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.harmony.test.func.api.java.io.share.IOMultiCase;
import org.apache.harmony.test.func.api.java.io.share.MockOutputStreamThreaded;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.share.Result;

public class OutputStreamThreadTestShared extends IOMultiCase {
    protected int getThreads() {
        return 3;
    }

    protected int getWrites() {
        return 10000;
    }
    
    protected OutputStream getTestedOutputStream() throws IOException {
        return new MockOutputStreamThreaded();
    }

    protected int[] getWritten(OutputStream os) {
        return ((MockOutputStreamThreaded) os).getWritten();
    }

    public Result testExceptionAfterClose() throws IOException {
        Object o = new Object();
        int[] results = new int[2];
        int[] stage = new int[1];
        OutputStream os = getTestedOutputStream();

        class Thread1Class extends Thread {
            OutputStream os;

            int[] results;

            int[] stage;

            Thread1Class(OutputStream os, int[] stage, int[] results) {
                this.os = os;
                this.stage = stage;
                this.results = results;
            }

            public void run() {
                try {
                    synchronized (stage) {
                        os.write(new byte[] { 'a' });
                        stage[0] = 1;
                        stage.notify();
                        while (stage[0] == 1) {
                            stage.wait();
                        }
                        try {
                            os.write(new byte[] { 'a' });
                            os.flush(); // for BufferedOutputStream
                        } catch (IOException e) {
                            results[0] = Result.PASS;
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    results[0] = fail("got unexpected exception");
                } finally {
                    if (results[0] == 0) {
                        results[0] = fail("unexpected behaviour");
                    }
                }
            }
        }

        class Thread2Class extends Thread {
            OutputStream os;

            int[] stage;

            int[] results;

            Thread2Class(OutputStream os, int[] stage, int[] results) {
                this.os = os;
                this.stage = stage;
                this.results = results;
            }

            public void run() {
                try {
                    synchronized (stage) {
                        os.write(new byte[] { 'a' });
                        while (stage[0] == 0) {
                            stage.wait();
                        }
                        os.close();
                        stage[0] = 2;
                        stage.notify();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    results[1] = fail("got unexpected exception");
                } finally {
                    if (results[1] == 0) {
                        results[1] = Result.PASS;
                    }
                }
            }
        }

        Thread thr1 = new Thread1Class(os, stage, results);
        Thread thr2 = new Thread2Class(os, stage, results);
        thr1.start();
        thr2.start();
        try {
            thr1.join();
            thr2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return failed("exception in join");
        }

        if (results[0] != Result.PASS || results[1] != Result.PASS) {
            return failed("one of threads behaved incorrectly");
        }

        return passed();
    }

    public Result testWrite0001() throws IOException {
        try {
        OutputStream os = getTestedOutputStream();

        class Thr extends Thread {
            OutputStream os;

            int c;

            Thr(OutputStream os, int c) {
                this.os = os;
                this.c = c;
            }

            public void run() {
                MultiThreadRunner.waitAtBarrier(getThreads());
                try {
                    for (int i = 0; i < getWrites(); ++i) {
                        os.write(c);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        Thr[] thrs = new Thr[getThreads()];
        for(int i = 0; i < getThreads(); ++i) {
            thrs[i] = new Thr(os, 'a' + i);
            thrs[i].start();
        }
        for(int i = 0; i < getThreads(); ++i) {
            try {
                thrs[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return failed("error in join");
            }
        }
        os.close();


        int[] written = getWritten(os);
        
        for(int i = 0; i < getThreads(); ++i) {
            if(written[i + 'a'] != getWrites()) {
                return failed("expected " + getWrites() + " for '" + ('a' + i) + " but got " + written['a' + i]); 
             }
        }
        } catch(Throwable e) {
            e.printStackTrace();
            }
        
        return passed();
    }

    public Result testWriteBytes() throws IOException {
        try {
            OutputStream os = getTestedOutputStream();

            class Thr extends Thread {
                OutputStream os;

                byte[] bytes;

                Thr(OutputStream os, byte[] bytes) {
                    this.os = os;
                    this.bytes = bytes;
                }

                public void run() {
                    MultiThreadRunner.waitAtBarrier(getThreads());
                    try {
                        for (int i = 0; i < getWrites(); ++i) {
                            os.write(bytes);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            
            Thr[] thrs = new Thr[getThreads()];
            thrs[0] = new Thr(os, "abc".getBytes());
            thrs[1] = new Thr(os, "bcd".getBytes());
            thrs[2] = new Thr(os, "cde".getBytes());
            
            for(int i = 0; i < getThreads(); ++i) {
                thrs[i].start();
            }
            for(int i = 0; i < getThreads(); ++i) {
                try {
                    thrs[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return failed("error in join");
                }
            }
            os.close();

            int[] written = getWritten(os);
            
            for(int i = 0; i < getThreads(); ++i) {
                int chr = i + 'a';
                int w = getWrites();
                if(chr == 'b' || chr == 'd') {
                    w = getWrites() * 2;
                } else if(chr == 'c') {
                    w = getWrites() * 3;
                }
                if(written[chr] != w ) {
                    return failed("expected " + w + " writes for '" + ('a' + i) + 
                            "' but got " + written['a' + i]); 
                 }
            }
            } catch(Throwable e) {
                e.printStackTrace();
                }
            
            return passed();
    }
    
        public Result testWriteSlice() throws IOException {
            try {
                OutputStream os = getTestedOutputStream();

                class Thr extends Thread {
                    OutputStream os;

                    byte[] bytes;

                    Thr(OutputStream os, byte[] bytes) {
                        this.os = os;
                        this.bytes = bytes;
                    }

                    public void run() {
                        MultiThreadRunner.waitAtBarrier(getThreads());
                        try {
                            for (int i = 0; i < getWrites(); ++i) {
                                os.write(bytes, 1, 2);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                
                Thr[] thrs = new Thr[getThreads()];
                thrs[0] = new Thr(os, "abc".getBytes());
                thrs[1] = new Thr(os, "bcd".getBytes());
                thrs[2] = new Thr(os, "cde".getBytes());
                
                for(int i = 0; i < getThreads(); ++i) {
                    thrs[i].start();
                }
                for(int i = 0; i < getThreads(); ++i) {
                    try {
                        thrs[i].join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return failed("error in join");
                    }
                }
                os.close();

                int[] written = getWritten(os);
                
                for(int i = 0; i < getThreads(); ++i) {
                    int chr = i + 'a';
                    int w = 0;
                    if(chr == 'b' || chr == 'e') {
                        w = getWrites();
                    } else if(chr == 'c' || chr == 'd') {
                        w = getWrites() * 2;
                    }
                    if(written[chr] != w ) {
                        return failed("expected " + w + " writes for '" + ('a' + i) + 
                                "' but got " + written['a' + i]); 
                     }
                }
                } catch(Throwable e) {
                    e.printStackTrace();
                    }
                
                return passed();
        }
}
