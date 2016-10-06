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

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;


/**
 * Goal: check FileChannel write operation for thread safety.
 *
 * Idea: Several threads write into a FileChannel equal size chunks of bytes each starting 
 *       and ending with MARK byte. If write operations are thread unsafe, then chunks 
 *       will be mixed.
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
 *     4. Starts N_OF_THREADS threads, each thread:
 *
 *          a. Creates N_OF_BUFFERS_PER_WRITE byte arrays of (CHUNK_SIZE + 2) size,
 *            each wrapped into direct ByteBuffer.
 *
 *          b. Writes the chunks.
 *
 *          c. Repeates the opration N_OF_WRITES_PER_THREAD times.
 *        
 *     6. Checks the content of the file: reads bytes from the file by (CHUNK_SIZE + 2) 
 *        chunks, checks that first and last bytes of each read chunk is MARK.
 *
 *     7. Closes channels.
 * 
 */

public class FileChannelThrSafetyTest extends Test {


    // each written chunk has has size (CHUNK_SIZE + 2) 
    // and content {MARK, <random bytes>, MARK}

    static final int CHUNK_SIZE = 2000;


    // Each thread repeats writing into a channel N_OF_WRITES_PER_THREAD times

    static final int N_OF_WRITES_PER_THREAD = 20;


    // Each write operation is called with N_OF_BUFFERS_PER_WRITE ByteBuffers

    static final int N_OF_BUFFERS_PER_WRITE = 20;


    // Mark-up byte value

    static final byte MARK = 0x22;


    // Number of byte-writing threads to run
 
    static final int N_OF_THREADS = 10;


    boolean passed = true;

    String workFileName = "";
  
    String fileName = "FileChannelThrSafetyTest.file";

    String outputDir = "";

    FileChannel outChannel = null, inChannel = null;


    public static void main(String[] args) {
        System.exit(new FileChannelThrSafetyTest().test(args));
    }


    public int test(String[] params) {

        File f = null;

        passed = true;

        try {

            parseParams(params);

            f = Utils.createWorkFile(workFileName);

            outChannel = new FileOutputStream(f).getChannel();

            inChannel = new FileInputStream(f).getChannel();

            runThreads();

            if (!passed || !checkWrittenContent()) {
                return fail("Failed");
            }

        } catch (Throwable t) {

            t.printStackTrace();

            return fail("Exception thrown: " + t);

        } finally {

            try {

                if (outChannel != null){
                    outChannel.close();
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


    // Reads from inChannel by (CHUNK_SIZE + 2) chunks, checks the first and last 
    // bytes are MARK bytes.

    boolean checkWrittenContent() throws Exception {

        MappedByteBuffer mp = null;

        int steps = N_OF_WRITES_PER_THREAD * N_OF_THREADS * N_OF_BUFFERS_PER_WRITE;

        int size = (CHUNK_SIZE + 2);

        ByteBuffer bb = ByteBuffer.allocate(size); 

        for (int i = 0; i < steps; ++i) {

            bb.clear();

            inChannel.read(bb);

            byte first = bb.get(0);

            byte last = bb.get(bb.capacity() - 1);

            if (first != MARK || last != MARK) {

                log.add("Channel writing is thread-unsafe? - Chunk # " + i + ": first byte is " +
                    first + ", last byte is " + last + ", While " + MARK + 
                    " byte is expected for each.");

                return false;
            }

        }

        return true;
    }

 
    void runThreads() throws Exception {

        Thread[] t = new Thread[N_OF_THREADS];

        for (int i = 0; i < t.length; ++i) {
            t[i] = new ChunkWriter(this); 
            t[i].start();
        }

        for (int i = 0; i < t.length; ++i) {
            t[i].join();
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

}




class ChunkWriter extends Thread {

    FileChannel outChannel = null;

    FileChannelThrSafetyTest base = null;


    ChunkWriter(FileChannelThrSafetyTest base) {

        this.outChannel = base.outChannel;

        this.base = base;
    }


    public void run () {

        try {

            for (int i = 0; i < FileChannelThrSafetyTest.N_OF_WRITES_PER_THREAD; ++i) {

                long written = outChannel.write(createByteChunks());

                // base.log.add("Bytes written " + written);

                Thread.yield();

                Thread.sleep(10);
            }

            outChannel.force(false);

        } catch (Exception e){
            base.log.add("Thread " + Thread.currentThread().getId() + ": Exception \"" + e + 
                "\", while writing ByteBuffer[] into channel");
            base.passed = false;
        }
    }


    ByteBuffer[] createByteChunks() {

        ByteBuffer[] bb_arr = new ByteBuffer[FileChannelThrSafetyTest.N_OF_BUFFERS_PER_WRITE];

        for (int i = 0; i < bb_arr.length; ++i) {
            bb_arr[i] = createByteChunk();
        }
       
        return bb_arr;
    }


    ByteBuffer createByteChunk() {

        byte[] exclusion_bytes = new byte[] {FileChannelThrSafetyTest.MARK, FileChannelThrSafetyTest.MARK};

        byte[] b = Utils.createRndBytes(FileChannelThrSafetyTest.CHUNK_SIZE + 2, exclusion_bytes);

        b[0] = FileChannelThrSafetyTest.MARK;
  
        b[b.length - 1] = b[0];

        ByteBuffer bb = ByteBuffer.allocateDirect(b.length).wrap(b);

        return bb;
    }

}

