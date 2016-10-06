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
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * The test does:
 *    1. Reads parameters, which are:
 *          param[0] - number of threads to run in parallel
 *          param[1] - allocated size of buffers
 *          param[2] - number of iterations
 *          param[3] - size of the byte buffer array
 *
 *     2. Starts and, then, joins all started threads
 *
 *     3. Checks that in each thread all checks PASSed.
 *
 *     4. Each thread, being started:
 *
 *         In each of param[2] iterations:
 *           - creates FileChannel object associated with file output stream
 *           - allocates some param[3] byte buffers.
 *           - writes a sequence of bytes to this channel from the given buffers.
 *           - Calls System.gc()
 */


public class ChannelWriteTest extends Test {

    public int NUMBER_OF_ITERATIONS = 1000;

    // size of the byte buffer array
    public int NUMBER_OF_BYTE_BUFFERS = 1;

    // How much bytes to read from the input file into byte array
    public int bufferSize = 1024;

    // Number of threads to run in parallel
    public int numThreads = 1;

    public int callSystemGC = 1;

    // Each thread sets PASS/FAIL flag into its corresponding array element
    public int[] statuses;

    // Number bytes to write into the buffer
    public int write_byte =50;


    public static void main(String[] args) {
        System.exit(new ChannelWriteTest().test(args));
    }


    public int test(String[] params) {
    
        parseParams(params);


        // Start 'numThreads' threads each reading from file, inflating/deflating

        Thread[] t = new Thread[numThreads];

        statuses = new int[t.length];
                                
        for (int i = 0; i < t.length; i++) {
            t[i] = new Thread(new ChannelWriteThread(i, this));
            t[i].start();
            //log.add("Thread " + i + " started");
        }
                
        // Correctly wait for all threads to finish

        for (int i = 0; i < t.length; ++i){
            try {
                t[i].join();
                // log.add("Thread " + i + ": joined() ");

            } catch (InterruptedException ie){
                return fail("interruptedException while join() of thread #" + i);
            }
        }

        // For each thread check whether operations/checks PASSed in the thread

        for (int i = 0; i < statuses.length; ++i){
            if (statuses[i] != Result.PASS){
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
            bufferSize= Integer.parseInt(params[1]);
        }        

        if (params.length >= 3) {
            NUMBER_OF_ITERATIONS = Integer.parseInt(params[2]);
        }        

        if (params.length >= 4) {
            NUMBER_OF_BYTE_BUFFERS = Integer.parseInt(params[3]);
        }        

    }

}


class ChannelWriteThread implements Runnable {

    public int id;
    public ChannelWriteTest base;

    public ChannelWriteThread(int id, ChannelWriteTest base) {
        this.id = id;
        this.base = base;
    }

    public void run() {
        FileOutputStream fos = null;
        FileChannel channel = null;

        try {
            File file = File.createTempFile("test", null);
            file.deleteOnExit();
            fos = new FileOutputStream(file);
            channel = fos.getChannel();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        base.statuses[id] = Result.PASS;
        ByteBuffer[] buffer = new ByteBuffer[base.NUMBER_OF_BYTE_BUFFERS];

        for (int i = 0; i < base.NUMBER_OF_ITERATIONS; ++i) {
            for(int j = 0; j < base.NUMBER_OF_BYTE_BUFFERS; j++) {
                buffer[j] = ByteBuffer.allocate(2*base.bufferSize); 
                putByte(buffer[j], base.write_byte);
                buffer[j].flip(); 
            }

            try {
                channel.write(buffer);  
            } catch (IOException e) {
                base.statuses[id] = Result.FAIL;
                base.log.add("Thread " + id + " : IOException while buffer writing");
            }

            for(int j = 0; j < base.NUMBER_OF_BYTE_BUFFERS; j++) {
                buffer[j].clear();
            }

            if (base.callSystemGC != 0){
                System.gc();
            }
        }
        try {
            channel.close();
            fos.close();
        } catch (IOException e) {
            base.statuses[id] = Result.FAIL;
            base.log.add("Thread " + id + " : IOException while closing");
        }
    }

    public void putByte(ByteBuffer buf, int cnt) {
        for (int i=0; i < cnt; i++ ){
            buf.put( (byte) i);
            buf.put( (byte) '\n');
        }
    }


}

