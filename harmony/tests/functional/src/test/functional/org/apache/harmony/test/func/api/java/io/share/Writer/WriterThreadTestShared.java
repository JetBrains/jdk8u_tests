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
/*
 * Created on 19.11.2004
 *
 */
package org.apache.harmony.test.func.api.java.io.share.Writer;

import java.io.IOException;
import java.io.Writer;

import org.apache.harmony.test.func.api.java.io.share.IOMultiCase;
import org.apache.harmony.test.func.api.java.io.share.MockWriterThreaded;
import org.apache.harmony.test.func.api.java.io.share.MockWriterThreadedNotSynchronized;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.share.Result;

public class WriterThreadTestShared extends IOMultiCase {


    protected Writer getTestedWriter() throws IOException {
        return new MockWriterThreaded();
    }

    private Writer getTestedWriter(Object lock) {
        return new MockWriterThreaded(lock);
    }

    protected int[] getWritten(Writer w) throws IOException {
        return ((MockWriterThreaded) w).getWritten();
    }

    public Result testExceptionAfterClose() throws IOException {
        Object o = new Object();
        final int[] results = new int[2];
        final int[] stage = new int[1];
        final Writer w = getTestedWriter();

        class Thread1Class extends Thread {
            public void run() {
                try {
                    synchronized (stage) {
                        w.write('a');
                        stage[0] = 1;
                        stage.notify();
                        while (stage[0] == 1) {
                            stage.wait();
                        }
                        try {
                            w.write('b');
                            w.flush(); //for caching streams
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
            public void run() {
                try {
                    synchronized (stage) {
                        w.write('a');
                        while (stage[0] == 0) {
                            stage.wait();
                        }
                        w.close();
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

        Thread thr1 = new Thread1Class();
        Thread thr2 = new Thread2Class();
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
            Writer w = getTestedWriter();
            final int thrds = 3;
            final int writes = 10000;

            class Thr extends Thread {
                Writer w;

                int c;

                Thr(Writer w, int c) {
                    this.w = w;
                    this.c = c;
                }

                public void run() {
                    MultiThreadRunner.waitAtBarrier(thrds);
                    try {
                        for (int i = 0; i < writes; ++i) {
                            w.write(c);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            Thr[] thrs = new Thr[thrds];
            for (int i = 0; i < thrds; ++i) {
                thrs[i] = new Thr(w, 'a' + i);
                thrs[i].start();
            }
            for (int i = 0; i < thrds; ++i) {
                try {
                    thrs[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return failed("error in join");
                }
            }
            w.close();

            int[] written = getWritten(w);

            for (int i = 0; i < thrds; ++i) {
                if (written[i + 'a'] != writes) {
                    return failed("expected " + writes + " for " + ('a' + i)
                            + " but got " + written['a' + i]);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return passed();
    }

    public Result testWriteArray() throws IOException {
        try {
            final Writer w = getTestedWriter();
            final int thrds = 3;
            final int writes = 10000;

            class Thr extends Thread {
                char[] c;

                Thr(char[] c) {
                    this.c = c;
                }

                public void run() {
                    MultiThreadRunner.waitAtBarrier(thrds);
                    try {
                        for (int i = 0; i < writes; ++i) {
                            w.write(c);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            Thr[] thrs = new Thr[thrds];
            //these are 3 threads
            thrs[0] = new Thr("abc".toCharArray());
            thrs[1] = new Thr("bcd".toCharArray());
            thrs[2] = new Thr("cde".toCharArray());

            for (int i = 0; i < thrds; ++i) {
                thrs[i].start();
            }
            for (int i = 0; i < thrds; ++i) {
                try {
                    thrs[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return failed("error in join");
                }
            }
            w.close();

            int[] written = getWritten(w);

            for (int i = 0; i < thrds; ++i) {
                int c = i + 'a';
                int expected = writes;
                if (c == 'b' || c == 'd') {
                    expected = writes * 2;
                } else if (c == 'c') {
                    expected = writes * 3;
                }
                if (written[c] != expected) {
                    return failed("expected " + expected + " for " + c
                            + " but got " + written[c]);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return passed();
    }

    public Result testWriteString() throws IOException {
        try {
            final Writer w = getTestedWriter();
            final int thrds = 3;
            final int writes = 10000;

            class Thr extends Thread {
                String s;

                Thr(String s) {
                    this.s = s;
                }

                public void run() {
                    MultiThreadRunner.waitAtBarrier(thrds);
                    try {
                        for (int i = 0; i < writes; ++i) {
                            w.write(s);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            Thr[] thrs = new Thr[thrds];
            //these are 3 threads
            thrs[0] = new Thr("abc");
            thrs[1] = new Thr("bcd");
            thrs[2] = new Thr("cde");

            for (int i = 0; i < thrds; ++i) {
                thrs[i].start();
            }
            for (int i = 0; i < thrds; ++i) {
                try {
                    thrs[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return failed("error in join");
                }
            }
            w.close();

            int[] written = getWritten(w);

            for (int i = 0; i < thrds; ++i) {
                int c = i + 'a';
                int expected = writes;
                if (c == 'b' || c == 'd') {
                    expected = writes * 2;
                } else if (c == 'c') {
                    expected = writes * 3;
                }
                if (written[c] != expected) {
                    return failed("expected " + expected + " for " + c
                            + " but got " + written[c]);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return passed();
    }

    public Result testWriteArraySlice() throws IOException {
        try {
            final Writer w = getTestedWriter();
            final int thrds = 3;
            final int writes = 10000;

            class Thr extends Thread {
                char[] c;

                Thr(char[] c) {
                    this.c = c;
                }

                public void run() {
                    MultiThreadRunner.waitAtBarrier(thrds);
                    try {
                        for (int i = 0; i < writes; ++i) {
                            w.write(c, 2, 3);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            Thr[] thrs = new Thr[thrds];
            //these are 3 threads
            thrs[0] = new Thr("xyabc".toCharArray());
            thrs[1] = new Thr("xybcd".toCharArray());
            thrs[2] = new Thr("yzcde".toCharArray());

            for (int i = 0; i < thrds; ++i) {
                thrs[i].start();
            }
            for (int i = 0; i < thrds; ++i) {
                try {
                    thrs[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return failed("error in join");
                }
            }
            w.close();

            int[] written = getWritten(w);

            for (int i = 0; i < thrds; ++i) {
                int c = i + 'a';
                int expected = writes;
                if (c == 'b' || c == 'd') {
                    expected = writes * 2;
                } else if (c == 'c') {
                    expected = writes * 3;
                }
                if (written[c] != expected) {
                    return failed("expected " + expected + " for " + c
                            + " but got " + written[c]);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return passed();
    }

    public Result testWriteStringSlice() throws IOException {
        try {
            final Writer w = getTestedWriter();
            final int thrds = 3;
            final int writes = 1000;

            class Thr extends Thread {
                String s;

                Thr(String s) {
                    this.s = s;
                }

                public void run() {
                    MultiThreadRunner.waitAtBarrier(thrds);
                    try {
                        for (int i = 0; i < writes; ++i) {
                            w.write(s, 2, 3);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            Thr[] thrs = new Thr[thrds];
            //these are 3 threads
            thrs[0] = new Thr("ggabc");
            thrs[1] = new Thr("rrbcd");
            thrs[2] = new Thr("ttcde");

            for (int i = 0; i < thrds; ++i) {
                thrs[i].start();
            }
            for (int i = 0; i < thrds; ++i) {
                try {
                    thrs[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return failed("error in join");
                }
            }
            w.close();

            int[] written = getWritten(w);

            for (int i = 0; i < thrds; ++i) {
                int c = i + 'a';
                int expected = writes;
                if (c == 'b' || c == 'd') {
                    expected = writes * 2;
                } else if (c == 'c') {
                    expected = writes * 3;
                }
                if (written[c] != expected) {
                    return failed("expected " + expected + " for " + c
                            + " but got " + written[c]);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return passed();
    }

    public Result testLockField() throws IOException {
        try {
            Object lock = new Object();
            final Writer w = getTestedWriter(lock);

            class Thr extends Thread {
                int seed;

                Thr(int seed) {
                    this.seed = seed;
                }

                public void run() {
                    MultiThreadRunner.waitAtBarrier(4);
                    try {
                        switch (seed) {
                        case 0:
                            w.write(new char[] { 'a' });
                            break;
                        case 1:
                            w.write("abcd".toCharArray(), 1, 1);
                            break;
                        case 2:
                            w.write("c");
                            break;
                        case 3:
                            w.write("abcd", 3, 1);
                            break;
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }

            Thr[] thrs = new Thr[4];
            synchronized (lock) {
//                System.err.println("here 0");
                for (int i = 0; i < 4; ++i) {
                    thrs[i] = new Thr(i);
                    thrs[i].start();
                }

//                System.err.println("here 1");
                //do some time-consuming stuff
                Thread.sleep(1000);

                //here written should be 0
//                System.err.println("here 2");
                int[] written = getWritten(w);
                if (written['a'] != 0 || written['b'] != 0 || written['c'] != 0 || written['d'] != 0) {
                    for (int i = 0; i < thrs.length; ++i) {
                        thrs[i].interrupt();
                    }
                    return failed("written is not empty " + written['a'] + written['b'] + written['c'] + written['d']);
                }
//                System.err.println("here 3");
            }

            for (int i = 0; i < thrs.length; ++i) {
                try {
                    thrs[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return failed("error in join");
                }
            }
            w.close();

            int[] written = getWritten(w);
            if (written['a'] != 1 || written['b'] != 1 || written['c'] != 1 || written['d'] != 1) {
                return failed("written is not filled properly " + written['a'] + written['b'] + written['c'] + written['d']);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return passed();
    }

    public Result testLockField0001() throws IOException {
        try {
            Object lock = new Object();
            final Writer w = new MockWriterThreadedNotSynchronized(lock);

            class Thr extends Thread {
                int seed;

                Thr(int seed) {
                    this.seed = seed;
                }

                public void run() {
                    MultiThreadRunner.waitAtBarrier(4);
                    try {
                        switch (seed) {
                        case 0:
                            w.write(new char[] { 'a' });
                            break;
                        case 1:
                            w.write("abcd".toCharArray(), 1, 1);
                            break;
                        case 2:
                            w.write("c");
                            break;
                        case 3:
                            w.write("abcd", 3, 1);
                            break;
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }

            Thr[] thrs = new Thr[4];
            synchronized (lock) {
                for (int i = 0; i < 4; ++i) {
                    thrs[i] = new Thr(i);
                    thrs[i].start();
                }

//                System.err.println("here 1");
                //do some time-consuming stuff
                Thread.sleep(1000);

                //here written should be 0
//                System.err.println("here 2");
                int[] written = getWritten(w);
                if (written['a'] != 1 || written['b'] != 1 || written['c'] != 0 || written['d'] != 0) {
                    for (int i = 0; i < thrs.length; ++i) {
                        thrs[i].interrupt();
                    }
                    return failed("written is not what expected " + written['a'] + written['b'] + written['c'] + written['d']);
                }
//                System.err.println("here 3");
            }

            for (int i = 0; i < thrs.length; ++i) {
                try {
                    thrs[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return failed("error in join");
                }
            }
            w.close();

            int[] written = getWritten(w);
            if (written['a'] != 1 || written['b'] != 1 || written['c'] != 1 || written['d'] != 1) {
                return failed("written is not filled properly " + written['a'] + written['b'] + written['c'] + written['d']);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return passed();
    }
}

