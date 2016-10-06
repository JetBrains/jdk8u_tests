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
/**
 * Created on 20.12.2004
 *
 */
package org.apache.harmony.test.func.api.java.io.share.Reader;

import java.io.IOException;
import java.io.Reader;

import org.apache.harmony.test.func.api.java.io.share.IOMultiCase;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.test.func.api.java.io.share.Utils;
import org.apache.harmony.share.Result;

class ReaderImplThread extends Reader {
    char c = 'a' - 1;

    public ReaderImplThread() {
        super();
    }

    public ReaderImplThread(Object lock) {
        super(lock);
    }

    public void close() throws IOException {
    }

    public int read(char[] dest, int start, int length) throws IOException {
        synchronized (lock) {
            for (int i = start; i < start + length; ++i) {
                ++c;
                if (c >= 'a' + ReaderThreadTestShared.THREADS) {
                    c = 'a';
                }

                dest[i] = c;
            }
        }
        return length;
    }

}

public class ReaderThreadTestShared extends IOMultiCase {
    protected static int THREADS = 1;
    
    protected int getReads() {
        return 10000;
    }

    public void parseArgs(String[] params) {
        super.parseArgs(params);
        THREADS = (Utils.THREADS == 1) ? 3 : Utils.THREADS;
    }
    
    protected Reader getTestedReader() throws IOException {
        return new ReaderImplThread();
    }

    protected Reader getTestedReader(Object lock) throws IOException {
        return new ReaderImplThread(lock);
    }

    public Result testReadInt() throws IOException {
        final Reader r = getTestedReader();
        final int[] read = new int['z'];

        try {
            class Thr extends Thread {
                public void run() {
                    MultiThreadRunner.waitAtBarrier(THREADS);
                    int c;
                    int[] read_local = new int['z'];
                    try {
                        for (int i = 0; i < getReads(); ++i) {
                            ++read_local[r.read()];
                        }
                        synchronized (read) {
                            for (int i = 0; i < read_local.length; ++i) {
                                read[i] += read_local[i];
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            Thr[] thrs = new Thr[THREADS];
            for (int i = 0; i < THREADS; ++i) {
                thrs[i] = new Thr();
                thrs[i].start();
            }
            for (int i = 0; i < THREADS; ++i) {
                try {
                    thrs[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return failed("error in join");
                }
            }
            r.close();

            for (int i = 0; i < THREADS; ++i) {
                if (read[i + 'a'] != getReads()) {
                    return failed("expected " + getReads() + " for " + ('a' + i)
                            + " but got " + read['a' + i]);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return passed();
    }

    public Result testReadChars0001() throws IOException {
        final Reader r = getTestedReader();
        final int[] read = new int['z'];

        try {
            class Thr extends Thread {
                public void run() {
                    MultiThreadRunner.waitAtBarrier(THREADS);
                    char[] buf = new char[THREADS];
                    int[] read_local = new int['z']; //to avoid synchronization
                                                     // overhead in the cycle
                    try {
                        for (int i = 0; i < getReads(); ++i) {
                            r.read(buf);
                            for (int j = 0; j < buf.length; ++j) {
                                ++read_local[buf[j]];
                            }
                        }
                        synchronized (read) {
                            for (int i = 0; i < read_local.length; ++i) {
                                read[i] += read_local[i];
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            Thr[] thrs = new Thr[THREADS];
            for (int i = 0; i < THREADS; ++i) {
                thrs[i] = new Thr();
                thrs[i].start();
            }
            for (int i = 0; i < THREADS; ++i) {
                try {
                    thrs[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return failed("error in join");
                }
            }
            r.close();

            for (int i = 0; i < THREADS; ++i) {
                if (read[i + 'a'] != getReads() * THREADS) {
                    return failed("expected " + getReads() * THREADS + " for "
                            + ('a' + i) + " but got " + read['a' + i]);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return passed();
    }

    public Result testReadChars0002() throws IOException {
        final Reader r = getTestedReader();
        final int[] read = new int['z'];

        try {
            class Thr extends Thread {
                public void run() {
                    MultiThreadRunner.waitAtBarrier(THREADS);
                    char[] buf = new char[3 + THREADS];
                    int[] read_local = new int['z']; //to avoid synchronization
                                                     // overhead in the cycle
                    try {
                        for (int i = 0; i < getReads(); ++i) {
                            r.read(buf, 2, THREADS);
                            for (int j = 0; j < buf.length; ++j) {
                                ++read_local[buf[j]];
                            }
                        }
                        synchronized (read) {
                            for (int i = 0; i < read_local.length; ++i) {
                                read[i] += read_local[i];
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            Thr[] thrs = new Thr[THREADS];
            for (int i = 0; i < THREADS; ++i) {
                thrs[i] = new Thr();
                thrs[i].start();
            }
            for (int i = 0; i < THREADS; ++i) {
                try {
                    thrs[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return failed("error in join");
                }
            }
            r.close();

            for (int i = 0; i < THREADS; ++i) {
                if (read[i + 'a'] != getReads() * THREADS) {
                    return failed("expected " + getReads() * THREADS + " for "
                            + ('a' + i) + " but got " + read['a' + i]);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return passed();
    }
    

    public Result testSkip() throws IOException {
        final Reader r = getTestedReader();

        try {
            class Thr extends Thread {
                public void run() {
                    MultiThreadRunner.waitAtBarrier(THREADS);
                    try {
                        for (int i = 0; i < getReads() - 1; ++i) {
                            r.skip(1);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            Thr[] thrs = new Thr[THREADS];
            for (int i = 0; i < THREADS; ++i) {
                thrs[i] = new Thr();
                thrs[i].start();
            }
            for (int i = 0; i < THREADS; ++i) {
                try {
                    thrs[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return failed("error in join");
                }
            }
            
            if(r.read() != 'a') {
                return failed("skip seems to be not thread-safe");
            }
            
            r.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return passed();
    }
}
