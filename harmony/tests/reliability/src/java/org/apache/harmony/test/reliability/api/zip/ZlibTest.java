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

package org.apache.harmony.test.reliability.api.zip;

import org.apache.harmony.test.reliability.share.Test;
import org.apache.harmony.test.reliability.share.Result;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * The test does:
 *    1. Reads parameters, which are:
 *          param[0] - number of deflating/inflating threads to run in parallel
 *          param[1] - directory where input file is located
 *          param[2] - number of bytes to read from the file
 *          param[3] - input file name
 *          param[4] - number of deflating/inflating iterations each thread does
 *
 *     2. Starts and, then, joins all started threads
 *
 *     3. Checks that in each thread all checks PASSed.
 *
 *     4. Each thread, being started:
 *
 *        a. Reads from input file (param[1] + "/" + param[3]) param[2] bytes, closes input stream
 *
 *        b. In each of param[4] iterations:
 *           - Deflates and then inflates read bytes (number of read bytes and the bytes itself are
 *             the same in each thread, on each iteration).
 *           - Compares that deflated/inflated byte array is byte-to-byte equal to the the original 
 *             one (read from input file).
 *           - Calls System.gc().
 */

public class ZlibTest extends Test {

    // Each thread does NUMBER_OF_INFL_DEFL_ITERATIONS inflation/deflation operations
    public int NUMBER_OF_INFL_DEFL_ITERATIONS = 10;

    // Directory where input file is located
    public String path_to_file;

    // Input file name
    public String fileName = "Test.out";

    // How much bytes to read from the input file into byte array
    public int bufferSize = 1024;

    // Number of threads to run in parallel
    public int numThreads = 1;

    public int callSystemGC = 1;

    // Each thread sets PASS/FAIL flag into its corresponding array element
    public int[] statuses;


    public static void main(String[] args) {
        System.exit(new ZlibTest().test(args));
    }


    public int test(String[] params) {
    
        parseParams(params);


        // Start 'numThreads' threads each reading from file, inflating/deflating

        Thread[] t = new Thread[numThreads];

        statuses = new int[t.length];
                                
        for (int i = 0; i < t.length; i++) {
            t[i] = new Thread(new InflaterDeflaterThread(i, this));
            t[i].start();
            //log.add("Thread " + i + " started");
        }
                
        // Correctly wait for all threads to finish

        for (int i = 0; i < t.length; ++i){
            try {
                t[i].join();
                //log.add("Thread " + i + ": joined() ");

            } catch (InterruptedException ie){
                return fail("interruptedException while join() of thread #" + i);
            }
        }

        // For each thread check whether operations/checks PASSed in the thread

        for (int i = 0; i < statuses.length; ++i){
            if (statuses[i] != Result.PASS){
                return fail("thread #" + i + " returned not PASS status");
            }
            //log.add("Status of thread " + i + ": is PASS");
        }

        return pass("OK");
    }


    public void parseParams(String[] params) {

        if (params.length >= 1 && Integer.parseInt(params[0]) > 0) {
            numThreads = Integer.parseInt(params[0]);
        }        

        if (params.length >= 2) {
            path_to_file =  params[1];
        }        

        if (params.length >= 3) {
            bufferSize= Integer.parseInt(params[2]);
        }        

        if (params.length >= 4) {
            fileName = params[3];
        }

        if (params.length >= 5) {
            NUMBER_OF_INFL_DEFL_ITERATIONS = Integer.parseInt(params[4]);
        }        

    }

}


class InflaterDeflaterThread implements Runnable {

    public int id;
    public ZlibTest base;

    public InflaterDeflaterThread(int id, ZlibTest base) {
        this.id = id;
        this.base = base;
    }

    public void run() {
              
        // First, the thread reads 'bufferSize' bytes from the input file

        byte[] buffer = getBytesFromFile(base.path_to_file + File.separator +  base.fileName);

        if (buffer == null) {
            base.statuses[id] = Result.FAIL;
            base.log.add("Thread " + id + ": buffer of bytes is null...");
            return;
        }

        // Second, the thread inflates/deflates read bytes and checks the content of 
        // original byte array is byte-to-byte equal to deflated/inflated byte array
        // in each of NUMBER_OF_INFL_DEFL_ITERATIONS iterations.

        for (int i = 0; i < base.NUMBER_OF_INFL_DEFL_ITERATIONS; ++i) {

            // base.log.add("Thread " + id + ", iteration " + i + ": starting compress/decompress iteration");

            byte[] compressed = InflatorDeflator.zlibDef(buffer, 0, buffer.length);
            byte[] decompressed;

            try {

                decompressed = InflatorDeflator.zlibInf(compressed, 0, compressed.length);

            } catch (DataFormatException e) {
                base.statuses[id] = Result.FAIL;
                base.log.add("Thread " + id + ", iteration " + i + ": DataFormatException while Inflating");
                return;
            }

            if (!InflatorDeflator.compareArrays(decompressed, buffer)){
                base.statuses[id] = Result.FAIL;
                base.log.add("Thread " + id + ", iteration " + i + ": decompressed bytes are not equal to original");
                return;
            }
                        
            if (base.callSystemGC != 0){
                System.gc();
            }
        }

        base.statuses[id] = Result.PASS;
    }



    byte[] getBytesFromFile(String filename) {

        // the method just reads 'bufferSize' bytes from the input file, closes file input stream

        FileInputStream in = null;
        byte[] buffer;
        int read = 0;

        try {

            try {
                in = new FileInputStream(filename);
            } catch (FileNotFoundException e) {
                base.statuses[id] = Result.FAIL;
                base.log.add("Thread " + id + ": FileNotFoundException: file " + filename +" does not exist");
                base.log.add(e);
                return null;
            }

            buffer = new byte[base.bufferSize];

            try {
                read = in.read(buffer, 0, buffer.length);
            } catch (IOException e) {
                base.statuses[id] = Result.FAIL;
                base.log.add("Thread " + id + ": FileInputStream.read: IOException during test execution");
                base.log.add(e);
                return null;
            }

        } finally {
            if (in != null){
                try {
                    in.close();
                    //base.log.add("Thread " + id + ": FileInputStream is closed");
                } catch (IOException ioe) {
                    base.statuses[id] = Result.FAIL;
                    base.log.add("Thread " + id + ": IOException while closing InputStream in thread #" + id);
                }
            }

        }

        //base.log.add("Thread " + id + ": " + read + " bytes read from file");

        return buffer;
    }

}

class InflatorDeflator {

    public static byte[] zlibInf(byte[] data, int offset, int inlen) throws DataFormatException {
        return zlibInf(data, offset, inlen, false);
    }
 
    public static byte[] zlibInf(byte[] data, int offset, int inlen, boolean nowrap) throws DataFormatException {

        // the method inflates 'data' bytes and returns inflated byte array

        int size = 512;
        int inlatedBytesNumber = 0;
        byte[] inflatedArray = null;
        byte[] tmpArray = null;
        Inflater inf = new Inflater(nowrap);

        // if (nowrap){
        //    byte[] add_byte_data = new byte[data.length + 1];
        //    System.arraycopy(data, 0, add_byte_data, 0, data.length);
        //    data = add_byte_data;
        // }

        inf.setInput(data, offset, inlen);

        // we decompress input bytes using dynamic capacity output byte array,
        // since, we do not know beforehand how many bytes will dempressed array be

        do {
            // store current 'array of decompressed bytes' in tmp array
            tmpArray = inflatedArray;

            // increase capacity of 'array of decompressed bytes' by 2x time
            size = size * 2;
            inflatedArray = new byte[size];

            // if not first iteration, copy already decompressed bytes from tmp array 
            // into 'increased capacity array of decompressed bytes'

            if (tmpArray != null) {
                System.arraycopy(tmpArray, 0, inflatedArray, 0, inlatedBytesNumber);
            }

            // continue decompression into 'incresed capacity array of decompressed bytes'
            // starting from last decompressed byte 'inlatedBytesNumber'

            inlatedBytesNumber = inlatedBytesNumber + inf.inflate(inflatedArray, inlatedBytesNumber, size - inlatedBytesNumber);

        } while (!inf.finished());

        // return array of compressed bytes containing only compressed bytes & nothing unrelated

        byte[] b = new byte[inlatedBytesNumber];
        System.arraycopy(inflatedArray, 0, b, 0, b.length);
        return b;
    }


    public static byte[] zlibDef(byte[] data, int offset, int length) {
        return zlibDef(data, offset, length, 0, Deflater.DEFAULT_STRATEGY, false);
    }

    public static byte[] zlibDef(byte[] data, int offset, int length, int level, int strategy, boolean nowrap) {

        // the method deflates 'data' bytes and returns deflated byte array

        int size = 512;
        int deflatedBytesNumber = 0;
        byte[] deflatedArray = null;
        byte[] tmpArray = null;
        Deflater def = new Deflater(level, nowrap);

        def.setStrategy(strategy);

        def.setInput(data, offset, length);
        def.finish();

        // we compress input bytes using dynamic capacity output byte array,
        // since, we do not know beforehand how many bytes will compressed array be

        while (!def.finished()) {

            // store current 'array of compressed bytes' in tmp array

            tmpArray = deflatedArray;

            // increase capacity of 'array of compressed bytes' by 2x time

            size = size * 2;
            deflatedArray = new byte[size];

            // if not first iteration, copy already compressed bytes from tmp array 
            // into 'increased capacity array of compressed bytes'

            if (tmpArray != null) {
                System.arraycopy(tmpArray, 0, deflatedArray, 0, deflatedBytesNumber);
            }

            // continue compression into 'incresed capacity array of compressed bytes'
            // starting from last compressed byte 'deflatedBytesNumber'

            deflatedBytesNumber = deflatedBytesNumber + def.deflate(deflatedArray, deflatedBytesNumber, size - deflatedBytesNumber);
        }
                
        // return array of compressed bytes containing only compressed bytes & nothing unrelated

        byte[] b = new byte[deflatedBytesNumber];
        System.arraycopy(deflatedArray, 0, b, 0, b.length);
        return b;
    }


    public static boolean compareArrays(byte[] b1, byte[] b2){

        // the method just compares that two arrays are byte-to-byte equal

        if (b1.length != b2.length) {
            return false;
        }
        for (int i = 0; i< b1.length; ++i){
            if (b1[i] != b2[i]){
                return false;
            }
        }
        return true;
    }

}

