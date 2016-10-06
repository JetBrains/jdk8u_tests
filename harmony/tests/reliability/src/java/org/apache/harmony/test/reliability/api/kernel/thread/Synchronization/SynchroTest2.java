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
 * @version $Revision: 1.2 $
 */
package org.apache.harmony.test.reliability.api.kernel.thread.Synchronization;

import org.apache.harmony.test.reliability.share.Test;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
/**
 *  Goal: check thread synchronization.
 *     Test simulates write to tape. There are 2 types of writer.
 *     Each writer writes series of int values on "tape".
 *     Type1 has right to exclusive write - no one can interrupte it during writing.
 *  The test does:
 *     1. Reads parameter, which is:
 *          param[0] - number of threads
 *          param[1] - length of series
 *     2. Creates and starts param[0] writers/threads (Type1 or Type 2 is selected randomly)
 *     3. Each writer/thread, being started writes on "tape" series of int.
 *     4. After all writers finished checks that the tape has 
 *        uninterrupted series written by type1 writer.
 */


public class SynchroTest2 extends Test {
    static int iteration = 50;
    static volatile int finish;
    static Object finlock = new Object();

    Tape tape = new Tape();
    int thread_number = 20;

    static int serial = 5;

    public static void main(String[] args) {
        System.exit(new SynchroTest2().test(args));
    }

    public int test(String[] params) {
        int l;
        Random rand = new Random();
        int key4type1 = 1000;
        int key4type2 = 0;
        int key = key4type1;

        parseParams(params);
        finish = thread_number;
        for (int i = 0; i < thread_number; i++) {
            l = rand.nextInt(3);
            if (l == 1) {
                new WriterType1(key4type1++, tape).start();
            } else {
                new WriterType2(key4type2++, tape).start();
            }
        }
        while (finish > 0) {} ;

        l = tape.size();
        for (int i = 0; i < l; i++) {
            int k = tape.get(i);
            if (k >= key) {
                for (int j = 1; j < serial; j++) {
                    int m = tape.get(++i);
                    //log.add("#" + m);
                    if (m != k) {
                        return fail("wrong record!");
                    }
                }
            } else {
                //   log.add(" " + tape.get(i));
            }
        }
        return pass("OK");
    }

    public void parseParams(String[] params) {
        if (params.length >= 1) {
            thread_number = Integer.parseInt(params[0]);
            if (params.length >= 2) {
                serial = Integer.parseInt(params[1]);
            }
        }
    }

}

class Lock {
    private int active_writers1;
    private int active_writers2;
    private int waiting_writers2;

    private final LinkedList writerLocks = new LinkedList(); //list of locks
    // for writers

    public synchronized void request_write2() {
        if (active_writers1 == 0 && writerLocks.size() == 0) {
            active_writers2++;
        } else {
            waiting_writers2++;
            try {
                wait();
            } catch (InterruptedException e) {
                //just ignore
            }
        }
    }

    public synchronized void write2_accomplished() {
        if (--active_writers2 == 0)
            notify_writers1();
    }

    public void request_write1() {
        Object lock = new Object();
        synchronized (lock) {
            synchronized (this) {
                boolean okay_to_write = (writerLocks.size() == 0
                    && active_writers2 == 0 && active_writers1 == 0);
                if (okay_to_write) {
                    active_writers1++;
                    return; // the "return" jumps over the "wait" call
                }
                writerLocks.addLast(lock);
            }
            try {
                lock.wait();
            } catch (InterruptedException e) {
            }
        }
    }

    public synchronized void write1_accomplished() {
        active_writers1--;
        if (waiting_writers2 > 0) // priority to waiting writers2
            notify_writers2();
        else
            notify_writers1();
    }

    private void notify_writers2() { // must be accessed from a synchronized
        // method
        active_writers2 += waiting_writers2;
        waiting_writers2 = 0;
        notifyAll();
    }

    private void notify_writers1() { // must be accessed from a synchronized
        // method
        if (writerLocks.size() > 0) {
            Object oldest = writerLocks.removeFirst();
            active_writers1++;
            synchronized (oldest) {
                oldest.notify();
            }
        }
    }
}

class WriterType1 extends Thread {
    private Tape tape;

    private int key;

    public WriterType1(int k, Tape tape) {
        this.tape = tape;
        key = k;
    }

    public void run() {
        //SynchroTest2.log.add("WT1:" + key + "started");
        tape.write1(key);
        synchronized (SynchroTest2.finlock) {
            SynchroTest2.finish--;
            //    SynchroTest2.log.add("WT1:" + key + "finished");
        }
    }
}

class WriterType2 extends Thread {
    private Tape tape;

    private int key;

    public WriterType2(int k, Tape tape) {
        this.tape = tape;
        key = k;
    }

    public void run() {
        //SynchroTest2.log.add("WT2:" + key + "started");
        tape.write2(key);
        synchronized (SynchroTest2.finlock) {
            SynchroTest2.finish--;
            //    SynchroTest2.log.add("WT2:" + key + "finished");
        }
    }
}

class Tape {
    Lock lock = new Lock();
    ArrayList al;

    public Tape() {
        lock = new Lock();
        al = new ArrayList();
    }

    public int size() {
        return al.size();
    }

    public int get(int i) {
        return ((Integer) al.get(i)).intValue();
    }

    public synchronized void add(Object obj) {
        al.add(obj);
    }

    public void write1(int w1) {
        try {
            lock.request_write1();
            try {
                for (int i = 0; i < SynchroTest2.serial; i++) {
                    add(new Integer(w1));
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
            }
        } finally {
            lock.write1_accomplished();
        }
    }

    public void write2(int w2) {
        try {
            lock.request_write2();
            try {
                for (int i = 0; i < SynchroTest2.serial; i++) {
                    add(new Integer(w2));
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
            }
        } finally {
            lock.write2_accomplished();
        }
    }
}


 

