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
 * @version $Revision: 1.1 $
 */

package org.apache.harmony.test.reliability.api.nio.channels.filechannel;

import org.apache.harmony.test.reliability.share.Test;
import org.apache.harmony.test.reliability.share.Result;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.MappedByteBuffer;

/**
 * Goal: find resource leaks or intermittent failures caused by MappedByteBuffer.force() / FileChannel.force() operations.
 * The test does: 
 * 1. Reads parameters, which are: 
 *        param[0] - number of threads to run in parallel 
 *        param[1] - directory where input file is located 
 *        param[2] - input file name 
 *        param[3] - number of iterations each thread does
 * 
 * 2. Starts and, then, joins all started threads
 * 
 * 3. Checks that in each thread all checks PASSed.
 * 
 * 4. Each thread, being started:
 * 
 * a. Reads content from input of RandomAccess file (param[1] + "/" + param[3])
 *    to a file channel for mapping into memory
 * 
 * b. In each of param[4] iterations: 
 *        - maps region of this channel's file
 *        - directly into memory
 *         - writes random memory buffers to tmpfile
 *        - Forces any changes made to this buffer's content 
 *        - Forces any updates to this channel's file  
 */

public class MappedByteBufferForceTest extends Test {

    // Each thread does NUMBER_OF_ITERATIONS mapping operations
    public int NUMBER_OF_ITERATIONS = 1000;

    // Directory where input file is located
    public String path_to_file;

    // Input file name
    public String fileName = "Test.out";

    // Number of threads to run in parallel
    public int numThreads = 1;

    public int callSystemGC = 1;

    // Each thread sets PASS/FAIL flag into its corresponding array element
    public int[] statuses;

    public static void main(String[] args) {
        System.exit(new MappedByteBufferForceTest().test(args));
    }

    public int test(String[] params) {

        parseParams(params);

        // Start 'numThreads' threads each reading from file

        Thread[] t = new Thread[numThreads];

        statuses = new int[t.length];

        for (int i = 0; i < t.length; i++) {
            t[i] = new Thread(new MappedByteBufferForceTestThread(i, this));
            t[i].start();
            // log.add("Thread " + i + " started");
        }

        // Correctly wait for all threads to finish

        for (int i = 0; i < t.length; ++i) {
            try {
                t[i].join();
                // log.add("Thread " + i + ": joined() ");

            } catch (InterruptedException ie) {
                return fail("interruptedException while join() of thread #" + i);
            }
        }

        // For each thread check whether operations/checks PASSed in the thread

        for (int i = 0; i < statuses.length; ++i) {
            if (statuses[i] != Result.PASS) {
                return fail("thread #" + i + " returned not PASS status");
            }
            // log.add("Status of thread " + i + ": is PASS");
        }

        return pass("OK");
    }

    public void parseParams(String[] params) {

        if (params.length >= 1 && Integer.parseInt(params[0]) > 0) {
            numThreads = Integer.parseInt(params[0]);
        }

        if (params.length >= 2) {
            path_to_file = params[1];
        }

        if (params.length >= 3) {
            fileName = params[2];
        }

        if (params.length >= 4) {
            NUMBER_OF_ITERATIONS = Integer.parseInt(params[3]);
        }

    }

}

class MappedByteBufferForceTestThread implements Runnable {

    public int id;

    public MappedByteBufferForceTest base;

    public MappedByteBufferForceTestThread(int id,
        MappedByteBufferForceTest base) {
        this.id = id;
        this.base = base;
    }

    FileChannel getFileChannelFromFile(String fileName) {
        File f = null;
        FileChannel fc = null;
        try {
            f = new File(fileName);
            fc = new RandomAccessFile(f, "rw").getChannel();
        } catch (IOException e) {
            base.statuses[id] = Result.FAIL;
            base.log.add("Thread " + id
                + ": IOException on RandomAccessFile creation");
        }
        return fc;
    }

    MappedByteBuffer getMappedByteBuffer(FileChannel fc) {

        File tmpfile = null;
        try {
            tmpfile = File.createTempFile("tmpfile", null);
        } catch (IOException e) {
            base.statuses[id] = Result.FAIL;
            base.log.add("Thread " + id + ": IOException on TempFile creation");
        }

        tmpfile.deleteOnExit();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(tmpfile);
        } catch (FileNotFoundException e) {
            base.statuses[id] = Result.FAIL;
            base.log.add("Thread " + id
                + ": FileNotFoundException on TempFile creation");
        }
        MappedByteBuffer mb = null;
        int size = 0;
        try {
            base.statuses[id] = Result.FAIL;
            mb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            base.statuses[id] = Result.PASS;
            size = (int) fc.size();
        } catch (IOException e) {
            base.statuses[id] = Result.FAIL;
            base.log.add("Thread " + id
                + ": IOException while getting size of channel's file");
        }
        byte[] buf = new byte[size];

        int len = 0;
        //for (int i = 0; i < iteration; ++i) {
        mb.position(0);
        while ((len = Math.min(buf.length, mb.remaining())) > 0) {
            mb.get(buf, 0, len);
            try {
                fos.write(buf);
            } catch (IOException e) {
                base.statuses[id] = Result.FAIL;
                base.log.add("Thread " + id
                    + ": IOException while writing buffer");
            }

        }
        try {
            fos.flush();
            fos.close();
        } catch (IOException e) {
            base.statuses[id] = Result.FAIL;
            base.log.add("Thread " + id
                + ": IOException while closing FileOutputStream");
        }

        //}
        return mb;
    }

    public void run() {

        FileChannel.MapMode mode = FileChannel.MapMode.READ_ONLY;
        FileChannel fc = null;
        MappedByteBuffer mb = null;

        long s = 0;
        base.statuses[id] = Result.PASS;

        for (int i = 0; i < base.NUMBER_OF_ITERATIONS; i++) {
            fc = getFileChannelFromFile(base.path_to_file + File.separator
                + base.fileName);
            mb = getMappedByteBuffer(fc);
            mb.force();
            try {
                fc.force(true);
            } catch (IOException e) {
                base.statuses[id] = Result.FAIL;
                base.log.add("Thread " + id
                    + ": IOException while forcing FileChannel");
            }
            try {
                fc.close();
            } catch (IOException e) {
                base.statuses[id] = Result.FAIL;
                base.log.add("Thread " + id
                    + ": IOException while closing FileChannel");
            }

            if (base.callSystemGC != 0) {
                System.gc();
            }

        }
    }
}