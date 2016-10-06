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
 * @author Oleg V. Oleinik
 * @version $Revision: 1.1 $
 */

package org.apache.harmony.test.reliability.api.nio.channels.filechannel;

import org.apache.harmony.test.reliability.share.Test;

import java.util.Random;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.ByteBuffer;


/**
 * Goal: check for intermittent errors operating with a file sucessively via FileInputStream and 
 *       associated channel, FileOutputStream and associated channel.
 *
 * Idea: Write into a file via FOS, then channel, then, FOS, then, channel, etc. an array of bytes.
 *       Then, read from the file via FIS, channel, FIS, channel, etc. Finally, check that read byte
 *       array has the same content as the original one, thus proving that working via FIS/FOS and
 *       channels shares input/output state and causes no errors.
 *
 * The test does:
 *
 *     1. Parses parameters:
 *             param[0] - a path to an exiting directory where the test can store its files.
 *
 *     2. Creates a file with the name param[0]/fileName.
 *    
 *     3. Creates FileInputStream, FileOutputStream and associated channels.
 *
 *     4. Creates an array of random size (<= SIZE) and fills-in it with random bytes.
 *
 *     5. Writes the bytes into the file:
 *
 *         a. Splits the byte array into N sub-arrays.
 *         b. Writes subarrays sequentially via FOS, channel, FOS, channel, etc.
 *        
 *     6. Reads bytes from the file by N portions via FIS, channel, FIS, channel, etc.
 *
 *     7. Compares initial array and read from the file, expecting they have the same content.
 *
 *     8. Truncates created file from 'file size' to 0 successively during N iterations.
 * 
 */

public class ChannelFIOSTest extends Test {

    // name of file to write/read byte to/from:

    static final String fileName = "ChannelFIOSTest.file";

    // maximum number of bytes totally to be written into file:

    static final int SIZE = 1000000;

    int actualSize = 0; // will be actual size of the file, a random value

    // number of subarrays to be written / read successively via FOS, channel / FIS, channel:

    static final int N = 50;

    String outputDir = "";

    String workFileName = "";

    FileOutputStream fos = null;
    FileChannel outChannel = null;

    FileInputStream fis = null;
    FileChannel inChannel = null;


    public static void main(String[] args) {
        System.exit(new ChannelFIOSTest().test(args));
    }


    public int test(String[] params) {

        File f = null;

        try {

            parseParams(params);
  
            f = Utils.createWorkFile(workFileName);

            // Open FIS, FOS and corresponding channels:

            openFISFOSChannels(f);
                 
            // Create a byte array of random size which we will play with

            byte[] bytes_to_write = Utils.createRndBytes(getSize());

            // First, we split bytes_to_write array into N equal parts and 
            // write parts sequntially into the file via FOS, channel, FOS, channel, etc.

            writeBytes(bytes_to_write, f);

            // Second, we read from the file via FIS, channel, FIS, channel, etc.

            byte[] read_bytes = readBytes(f);

            // Third, compare wrote and read bytes.
  
            if (!compare(bytes_to_write, read_bytes)) {
                return fail("Written and read arrays differ...");
            }

            // Finally, truncate the created file from 'file size' to 0
            // during N iterations.

            truncateFile();


        } catch (Throwable t) {
            t.printStackTrace();
            return fail("Exception thrown: " + t);

        } finally {

            try {

                if (fos != null){
                    fos.close();
                }

                if (outChannel != null){
                    outChannel.close();
                }

                if (fis != null){
                    fis.close();
                }
                if (inChannel != null){
                    inChannel.close();
                }

            } catch (Throwable t){
            }

            if (f != null){
                f.delete();
            }
        }

        return pass("OK");
    }


    // the method truncates the created file from 'file size'  
    // up to 0 sucessively during N iterations

    void truncateFile() throws Exception {

        long fileSize = outChannel.position(0).size();

        long step = fileSize / N;

        try {

            for (int i = 0; i <= N + 1; ++i) {

                outChannel.truncate(fileSize - step * i);

            }

        } catch (IllegalArgumentException iae) {

            // System.out.println("IAE");

            // this is just in case to check nothing but exception happens 
            // in case of negative size
        }

    }

    boolean compare(byte[] b1, byte[] b2){

        if (b1.length != b2.length) {
            log.add("byte arrays comparison: lengths of arrays are different: " + b1.length + ", " + b2.length);
            return false;
        }

        boolean passed = true;

        for (int i = 0; i < b1.length; ++i){
            if (b1[i] != b2[i]) {
                log.add("Bytes are different at position " + i + ": " + Byte.toString(b1[i]) + 
                    " vs. " + Byte.toString(b2[i]));
                passed = false;
                break;
            }
        }

        return passed;
    }


    // The key method of the test: it reads bytes via FileInputStream, channel,
    // FileInputStream and channel, etc. expecting that FIS and channel share state,
    // i.e. reading from channel affects correspondingly FIS and vise versa.

    byte[] readBytes(File f) throws Exception {

        byte[] b_total = new byte[actualSize];
        int step = actualSize / N;

        byte[] b = new byte[step];
        ByteBuffer bb = ByteBuffer.allocate(step);

        for (int i = 0; i < N; ++i) {
                 
            // READ NEXT PORTION OF BYTES VIA FIS:

            fis.read(b);
            System.arraycopy(b, 0, b_total, i * step, b.length);

            ++i;
          
            if (i < N) {

                bb.clear(); // needed to prepare ByteBuffer for channel read / put operations 

                // READ NEXT PORTION OF BYTES VIA CHANNEL:

                inChannel.read(bb);
                byte[] arr = bb.array();
                System.arraycopy(arr, 0, b_total, i * step, arr.length);
            }
        }

        return b_total;
    }


    // The key method of the test: it writes bytes via FileOutputStream, channel,
    // FileOutputStream and channel, etc. expecting that FOS and channel share state,
    // i.e. writing via channel affects correspondingly FOS and vise versa.

    void writeBytes(byte[] b, File f) throws Exception {
             
        ByteBuffer[] bb = splitByteArray(b);

        for (int i = 0; i < bb.length; ++i) {

            // WRITE NEXT PORTION OF BYTES VIA FOS:

            fos.write(bb[i].array()); 

            ++i;

            // WRITE NEXT PORTION OF BYTES VIA CHANNEL:

            if (i < bb.length) {
                FileLock fl = outChannel.lock();
                outChannel.write(bb[i]);
                outChannel.force(false);
                fl.release();
            }
        }
    }

    // Helpful method which just splits byte array "b" into N equal parts
    // each part is copied into ByteBuffer (non-direct) and prepared for writing.

    ByteBuffer[] splitByteArray(byte[] b){

        int step = b.length / N;

        ByteBuffer[] bb = new ByteBuffer[N];

        for (int i = 0; i < bb.length; ++i) {
            bb[i] = ByteBuffer.allocate(step);
            bb[i].put(b, i * step, step);
            bb[i].flip(); // needed to prepare ByteBuffer for channel write / get operations
        }

        return bb;
    }


    // The method just creates FIS, FOS and associated channels

    void openFISFOSChannels(File f) throws Throwable {

        try {

            fos = new FileOutputStream(f);
            outChannel = fos.getChannel();

            fis = new FileInputStream(f);
            inChannel = fis.getChannel();

        } catch (Throwable t){
            log.add(t.toString() +
                " exception was thrown while creating FileOutputStream or FileInputStream" +
                " or associated channels for " + f.getPath());
            throw t;
        }
    }


    public void parseParams(String[] params) throws Exception {

        // NOTE: outputDir must exist prior to running this test

        if (params.length >= 1) {
            outputDir =  params[0];
        } else {
            throw new Exception("output directory is not specified. Usage: Test <existing output dir name>");
        }
        workFileName = outputDir + File.separator + fileName;
    }

    int getSize() {

        actualSize = (Utils.r.nextInt(SIZE) / N) * N;
            
        // log.add("Bytes: " + actualSize);

        return actualSize;
    }

}
