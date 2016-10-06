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
package org.apache.harmony.test.func.api.java.io.share.InputStream;

import java.io.IOException;
import java.io.InputStream;

import org.apache.harmony.test.func.api.java.io.share.IOMultiCase;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.test.func.api.java.io.share.Utils;
import org.apache.harmony.share.Result;

public class InputStreamThreadTestShared extends IOMultiCase {
    protected static final int READS = 10000;
    protected static int THREADS;

    public InputStream getTestedInputStream() throws IOException {
        return new InputStream() {
            int i = 'a' - 1;

            public synchronized int read() {
                i++;
                if (i >= 'a' + THREADS) {
                    i = 'a';
                }
                return i;
            }
        };
    }
    
    public void parseArgs(String[] params) {
        super.parseArgs(params);
        THREADS = (Utils.THREADS == 1) ? 3 : Utils.THREADS;
    }
    
    public Result testReadByte() throws IOException {
        final InputStream is = getTestedInputStream();
        final int[] read = new int['z'];

        try {
            class Thr extends Thread {
                public void run() {
                    MultiThreadRunner.waitAtBarrier(THREADS);
                    int c;
                    int[] read_local = new int['z'];
                    try {
                        for (int i = 0; i < READS; ++i) {
                          ++read_local[is.read()];
                        }
                        synchronized(read) {
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
            is.close();

            for (int i = 0; i < THREADS; ++i) {
                if (read[i + 'a'] != READS) {
                    return failed("expected " + READS + " for " + ('a' + i)
                            + " but got " + read['a' + i]);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return passed();
    }

    public Result testReadBytes() throws IOException {
        final InputStream is = getTestedInputStream();
        final int[] read = new int['z'];

        try {
            class Thr extends Thread {
                public void run() {
                    MultiThreadRunner.waitAtBarrier(THREADS);
                    byte[] buf = new byte[THREADS];
                    int[] read_local = new int['z']; //to avoid synchronization overhead in the cycle
                    try {
                        for (int i = 0; i < READS; ++i) {
                            is.read(buf);
                                for (int j = 0; j < buf.length; ++j) {
                                    ++read_local[buf[j]];
                                }
                        }
                        synchronized(read) {
                        for(int i = 0; i < read_local.length; ++i) {
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
            is.close();

            for (int i = 0; i < THREADS; ++i) {
                if (read[i + 'a'] != THREADS * READS ) {
                    return failed("expected " + THREADS * READS + " for "
                            + ('a' + i) + " but got " + read['a' + i]);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return passed();
    }

    public Result testReadBytesSlice() throws IOException {
        final InputStream is = getTestedInputStream();
        final int[] read = new int['z'];

        try {
            class Thr extends Thread {
                public void run() {
                    MultiThreadRunner.waitAtBarrier(THREADS);
                    byte[] buf = new byte[THREADS + 5];
                    int[] read_local = new int['z']; //to avoid synchronization overhead in the cycle
                    try {
                        for (int i = 0; i < READS; ++i) {
                            is.read(buf, 3, THREADS);
                                for (int j = 3; j < 3 + THREADS; ++j) {
                                    ++read_local[buf[j]];
                                }
                        }
                        synchronized(read) {
                        for(int i = 0; i < read_local.length; ++i) {
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
            is.close();

            for (int i = 0; i < THREADS; ++i) {
                if (read[i + 'a'] != THREADS * READS) {
                    return failed("expected " + THREADS * READS + " for "
                            + ('a' + i) + " but got " + read['a' + i]);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return passed();
    }
}
